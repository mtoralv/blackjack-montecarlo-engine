package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.IntStream;

import game.Card;
import game.CardListener;
import game.RoundResult;
import game.Rules;
import game.Shoe;
import game.Table;
import strategy.Strategies;
import strategy.betting.BettingStrategy;
import strategy.betting.HiLoStrategy;
import strategy.playing.PlayingStrategy;

public final class MonteCarlo {

    private MonteCarlo() {
    }

    public static SimulationResult run(SimulationConfig cfg) {
        long start = System.nanoTime();
        // one seed per session, so results are the same no matter how many threads run
        long[] seeds = new SplittableRandom(cfg.seed()).longs(cfg.simulations()).toArray();

        Stats[] perSession = new Stats[cfg.simulations()];
        SessionResult[] sessions = new SessionResult[cfg.simulations()];
        IntStream.range(0, cfg.simulations()).parallel().forEach(i -> {
            Stats stats = new Stats();
            sessions[i] = runSession(cfg, seeds[i], stats);
            perSession[i] = stats;
        });

        Stats total = new Stats();
        for (Stats s : perSession) total.merge(s); // fixed order to keep sums identical

        Rules rules = cfg.rules();
        String playingName = Strategies.playing(cfg.playing(), rules, new SplittableRandom(0)).name();
        String bettingName = Strategies.betting(cfg.betting(), cfg.spread()).name();
        double elapsed = (System.nanoTime() - start) / 1e9;
        return new SimulationResult(cfg, playingName, bettingName, total, List.of(sessions), elapsed);
    }

    static SessionResult runSession(SimulationConfig cfg, long seed, Stats stats) {
        Rules rules = cfg.rules();
        SplittableRandom rng = new SplittableRandom(seed);
        Table table = new Table(rules, new Shoe(rules.decks(), rules.penetration(), rng.split()));
        PlayingStrategy player = Strategies.playing(cfg.playing(), rules, rng.split());
        BettingStrategy betting = Strategies.betting(cfg.betting(), cfg.spread());
        betting.reset(rules);

        // extra counter just to log the true count of each hand for the stats
        HiLoStrategy counter = new HiLoStrategy(1);
        counter.reset(rules);
        CardListener listeners = new CardListener() {
            @Override
            public void onCard(Card card) {
                betting.onCard(card);
                counter.onCard(card);
            }

            @Override
            public void onShuffle() {
                betting.onShuffle();
                counter.onShuffle();
            }
        };

        double unit = cfg.unit();
        double tableMax = cfg.tableMaxUnits() * unit;
        double balance = cfg.bankroll();
        double net = 0;
        double peak = 0;
        double maxDrawdown = 0;
        boolean ruined = false;
        int played = 0;
        float[] trajectory = new float[cfg.handsPerSimulation() / cfg.sampleEvery() + 1];

        for (int h = 0; h < cfg.handsPerSimulation(); h++) {
            if (balance < unit) {
                ruined = true;
                break;
            }
            table.shuffleIfNeeded(listeners); // shuffle before betting, like at a real table
            double trueCount = counter.trueCount();
            double bet = Math.min(Math.max(betting.nextBet(unit), unit), tableMax);
            bet = Math.min(bet, balance);

            RoundResult result = table.playRound(bet, balance, player, listeners);
            betting.onRoundEnd(bet, result.net());
            stats.record(result, trueCount);

            balance += result.net();
            net += result.net();
            played++;
            peak = Math.max(peak, net);
            maxDrawdown = Math.max(maxDrawdown, peak - net);
            if (played % cfg.sampleEvery() == 0) {
                trajectory[played / cfg.sampleEvery()] = (float) net;
            }
        }
        if (ruined || played < cfg.handsPerSimulation()) {
            int from = played / cfg.sampleEvery() + 1;
            if (from < trajectory.length) Arrays.fill(trajectory, from, trajectory.length, (float) net);
        }
        return new SessionResult(net, played, ruined, maxDrawdown, trajectory);
    }

    public static List<SimulationResult> runAll(List<SimulationConfig> configs, boolean verbose) {
        List<SimulationResult> results = new ArrayList<>();
        for (SimulationConfig cfg : configs) {
            SimulationResult r = run(cfg);
            if (verbose) Report.printRow(r);
            results.add(r);
        }
        return results;
    }
}
