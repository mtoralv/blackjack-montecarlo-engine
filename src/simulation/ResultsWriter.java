package simulation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class ResultsWriter {

    private ResultsWriter() {
    }

    public static void write(Path dir, List<SimulationResult> results) throws IOException {
        Files.createDirectories(dir.resolve("sessions"));
        Files.createDirectories(dir.resolve("trajectories"));
        writeSummary(dir.resolve("summary.csv"), results);
        writeOutcomes(dir.resolve("outcomes.csv"), results);
        writeCountEdge(dir.resolve("count_edge.csv"), results);
        for (SimulationResult r : results) {
            writeSessions(dir.resolve("sessions").resolve(r.id() + ".csv"), r);
            writeTrajectories(dir.resolve("trajectories").resolve(r.id() + ".csv"), r);
        }
    }

    private static void writeSummary(Path file, List<SimulationResult> results) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            w.write("playing,betting,rules,decks,penetration,simulations,hands_per_sim,rounds,"
                    + "win_rate,loss_rate,push_rate,player_blackjack_rate,double_rate,split_rate,surrender_rate,"
                    + "house_edge,house_edge_se,player_return,player_return_se,mean_bet_units,"
                    + "ev_per_round_units,sd_per_round_units,ruin_rate,mean_final_units,mean_max_drawdown_units,"
                    + "bankroll_units,seed,elapsed_s\n");
            for (SimulationResult r : results) {
                SimulationConfig c = r.config();
                Stats s = r.stats();
                double unit = c.unit();
                double meanFinal = r.sessions().stream().mapToDouble(SessionResult::finalNet).average().orElse(0) / unit;
                double meanDd = r.sessions().stream().mapToDouble(SessionResult::maxDrawdown).average().orElse(0) / unit;
                w.write(String.join(",",
                        r.playingName(), r.bettingName(), c.rules().describe(),
                        Integer.toString(c.rules().decks()), num(c.rules().penetration()),
                        Integer.toString(c.simulations()), Integer.toString(c.handsPerSimulation()),
                        Long.toString(s.rounds()),
                        num(s.rate(s.wins)), num(s.rate(s.losses)), num(s.rate(s.pushes)),
                        num(s.rate(s.playerBlackjacks)), num(s.rate(s.doubles)), num(s.rate(s.splits)),
                        num(s.rate(s.surrenders)),
                        num(s.houseEdge()), num(s.houseEdgeStdError()),
                        num(s.returnOnAction()), num(s.returnOnActionStdError()),
                        num(s.meanBet() / unit), num(s.meanNet() / unit), num(s.stdDevNet() / unit),
                        num(r.ruinRate()), num(meanFinal), num(meanDd),
                        Double.isInfinite(c.bankroll()) ? "inf" : num(c.bankroll() / unit),
                        Long.toString(c.seed()), num(r.elapsedSeconds())));
                w.write('\n');
            }
        }
    }

    private static void writeOutcomes(Path file, List<SimulationResult> results) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            w.write("playing,betting,net_per_bet,rounds\n");
            for (SimulationResult r : results) {
                long[] o = r.stats().outcomes;
                for (int i = 0; i < o.length; i++) {
                    if (o[i] == 0) continue;
                    double ratio = (i - Stats.OUTCOME_OFFSET) / 2.0;
                    w.write(r.playingName() + "," + r.bettingName() + "," + num(ratio) + "," + o[i] + "\n");
                }
            }
        }
    }

    private static void writeCountEdge(Path file, List<SimulationResult> results) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            w.write("playing,betting,true_count,rounds,player_return,player_return_se\n");
            for (SimulationResult r : results) {
                Stats s = r.stats();
                for (int i = 0; i < s.tcRounds.length; i++) {
                    long n = s.tcRounds[i];
                    if (n == 0) continue;
                    double mean = s.tcSumR[i] / n;
                    double se = Math.sqrt(Math.max(0, s.tcSumR2[i] / n - mean * mean) / n);
                    w.write(r.playingName() + "," + r.bettingName() + "," + (i + Stats.TC_MIN) + ","
                            + n + "," + num(mean) + "," + num(se) + "\n");
                }
            }
        }
    }

    private static void writeSessions(Path file, SimulationResult r) throws IOException {
        double unit = r.config().unit();
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            w.write("session,final_units,hands_played,ruined,max_drawdown_units\n");
            List<SessionResult> sessions = r.sessions();
            for (int i = 0; i < sessions.size(); i++) {
                SessionResult s = sessions.get(i);
                w.write(i + "," + num(s.finalNet() / unit) + "," + s.handsPlayed() + ","
                        + (s.ruined() ? 1 : 0) + "," + num(s.maxDrawdown() / unit) + "\n");
            }
        }
    }

    private static void writeTrajectories(Path file, SimulationResult r) throws IOException {
        int every = r.config().sampleEvery();
        double unit = r.config().unit();
        List<SessionResult> sessions = r.sessions();
        int points = sessions.get(0).trajectory().length;
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            StringBuilder header = new StringBuilder("session");
            for (int p = 0; p < points; p++) header.append(',').append((long) p * every);
            w.write(header.append('\n').toString());
            for (int i = 0; i < sessions.size(); i++) {
                StringBuilder line = new StringBuilder().append(i);
                for (float v : sessions.get(i).trajectory()) line.append(',').append(num(v / unit));
                w.write(line.append('\n').toString());
            }
        }
    }

    static String num(double v) {
        if (v == Math.rint(v) && Math.abs(v) < 1e15) return Long.toString((long) v);
        String s = String.format(Locale.ROOT, "%.8g", v);
        if (s.contains("e") || s.contains("E")) return s;
        if (s.contains(".")) s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        return s;
    }
}
