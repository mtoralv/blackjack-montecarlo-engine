import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import console.ConsoleGame;
import game.Rules;
import simulation.MonteCarlo;
import simulation.Report;
import simulation.ResultsWriter;
import simulation.SimulationConfig;
import simulation.SimulationResult;
import strategy.Strategies;

public final class Main {

    private static final String USAGE = """
            Blackjack Monte Carlo Engine

            Usage:
              java -cp bin Main [suite|simulate|play|help] [options]

            Commands:
              suite       every playing strategy with flat betting, plus basic strategy with every betting system (default)
              simulate    only the combinations given by --play and --bet
              play        play Blackjack interactively in the terminal

            Simulation options:
              --play LIST          playing strategies: basic, threshold, random, onlyhit, onlystand   [basic]
              --bet LIST           betting strategies: flat, hilo, martingale, oscar, 1326            [flat]
              --sims N             independent player sessions                                         [1000]
              --hands N            hands per session                                                   [10000]
              --seed N             master random seed                                                  [42]
              --bankroll UNITS     starting bankroll per session, in base units (0 = unlimited)         [0]
              --table-max UNITS    table maximum, in base units                                        [500]
              --spread N           maximum bet spread for Hi-Lo, in units                              [12]
              --sample-every N     record cumulative profit every N hands                              [100]
              --out DIR            output directory for the CSV files                                  [results]

            Rule options (simulate, suite and play):
              --decks N            decks in the shoe                                                   [6]
              --penetration F      fraction of the shoe dealt before reshuffling                       [0.75]
              --h17                dealer hits soft 17 (default: stands)
              --six-five           blackjack pays 6:5 instead of 3:2
              --no-das             no double after split
              --no-surrender       no late surrender

            Examples:
              java -cp bin Main simulate --play basic --bet flat,hilo --sims 2000 --hands 20000
              java -cp bin Main simulate --play basic --bet martingale,oscar --bankroll 200 --out results/ruin
              java -cp bin Main play --bankroll 100
            """;

    public static void main(String[] args) throws IOException {
        String command = args.length > 0 && !args[0].startsWith("--") ? args[0].toLowerCase(Locale.ROOT) : "suite";
        String[] rest = args.length > 0 && !args[0].startsWith("--") ? Arrays.copyOfRange(args, 1, args.length) : args;

        Map<String, String> opts;
        try {
            opts = parse(rest);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("Run with 'help' for usage.");
            System.exit(2);
            return;
        }

        try {
            switch (command) {
                case "suite" -> simulate(opts, true);
                case "simulate" -> simulate(opts, false);
                case "play" -> play(opts);
                case "help", "-h" -> System.out.print(USAGE);
                default -> {
                    System.err.println("Unknown command '" + command + "'.");
                    System.out.print(USAGE);
                    System.exit(2);
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }

    private static void simulate(Map<String, String> o, boolean suite) throws IOException {
        Rules rules = rules(o);
        double bankroll = Double.parseDouble(o.getOrDefault("bankroll", "0"));
        SimulationConfig base = new SimulationConfig(
                rules, "basic", "flat",
                Integer.parseInt(o.getOrDefault("spread", "12")),
                Integer.parseInt(o.getOrDefault("sims", "1000")),
                Integer.parseInt(o.getOrDefault("hands", "10000")),
                1.0,
                bankroll <= 0 ? Double.POSITIVE_INFINITY : bankroll,
                Double.parseDouble(o.getOrDefault("table-max", "500")),
                Long.parseLong(o.getOrDefault("seed", "42")),
                Integer.parseInt(o.getOrDefault("sample-every", "100")));

        List<SimulationConfig> configs = new ArrayList<>();
        if (suite) {
            for (String p : Strategies.PLAYING) configs.add(base.with(p, "flat"));
            for (String b : Strategies.BETTING) {
                if (!b.equals("flat")) configs.add(base.with("basic", b));
            }
        } else {
            for (String p : o.getOrDefault("play", "basic").split(",")) {
                for (String b : o.getOrDefault("bet", "flat").split(",")) {
                    configs.add(base.with(p.trim(), b.trim()));
                }
            }
        }
        // check the names before simulating anything
        for (SimulationConfig c : configs) {
            Strategies.playing(c.playing(), rules, null);
            Strategies.betting(c.betting(), c.spread());
        }

        Report.printHeader(base);
        List<SimulationResult> results = MonteCarlo.runAll(configs, true);
        Path out = Path.of(o.getOrDefault("out", "results"));
        ResultsWriter.write(out, results);
        System.out.println();
        System.out.println("House edge = -E[net / initial bet]; player return = total net / total bet. Intervals are 95 %.");
        System.out.println("CSV files written to " + out + "/");
    }

    private static void play(Map<String, String> o) {
        double bankroll = Double.parseDouble(o.getOrDefault("bankroll", "100"));
        long seed = o.containsKey("seed") ? Long.parseLong(o.get("seed")) : System.nanoTime();
        new ConsoleGame(rules(o), bankroll > 0 ? bankroll : 100, seed).run();
    }

    private static Rules rules(Map<String, String> o) {
        Rules rules = Rules.standard()
                .withDecks(Integer.parseInt(o.getOrDefault("decks", "6")))
                .withPenetration(Double.parseDouble(o.getOrDefault("penetration", "0.75")));
        if (o.containsKey("h17")) rules = rules.withDealerHitsSoft17(true);
        if (o.containsKey("six-five")) rules = rules.withBlackjackPayout(1.2);
        if (o.containsKey("no-das")) rules = rules.withDoubleAfterSplit(false);
        if (o.containsKey("no-surrender")) rules = rules.withLateSurrender(false);
        return rules;
    }

    private static final List<String> FLAGS = List.of("h17", "six-five", "no-das", "no-surrender");
    private static final List<String> VALUED = List.of("play", "bet", "sims", "hands", "seed", "bankroll",
            "table-max", "spread", "sample-every", "out", "decks", "penetration");

    private static Map<String, String> parse(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("--")) throw new IllegalArgumentException("Unexpected argument: " + args[i]);
            String key = args[i].substring(2);
            if (FLAGS.contains(key)) {
                map.put(key, "true");
            } else if (VALUED.contains(key)) {
                if (i + 1 >= args.length) throw new IllegalArgumentException("Missing value for --" + key);
                map.put(key, args[++i]);
            } else {
                throw new IllegalArgumentException("Unknown option: --" + key);
            }
        }
        return map;
    }
}
