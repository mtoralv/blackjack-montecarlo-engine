package simulation;

import java.util.Locale;

public final class Report {

    private Report() {
    }

    public static void printHeader(SimulationConfig base) {
        System.out.printf(Locale.ROOT, "Rules: %s | %,d sessions x %,d hands | seed %d%n",
                base.rules().describe(), base.simulations(), base.handsPerSimulation(), base.seed());
        System.out.printf(Locale.ROOT, "%-14s %-12s %8s %8s %18s %18s %8s %10s%n",
                "Playing", "Betting", "Win %", "Push %", "House edge", "Player return", "Ruin %", "Hands/s");
        System.out.println("-".repeat(104));
    }

    public static void printRow(SimulationResult r) {
        Stats s = r.stats();
        System.out.printf(Locale.ROOT, "%-14s %-12s %7.2f%% %7.2f%% %9.3f%% +-%5.3f%% %9.3f%% +-%5.3f%% %7.1f%% %10s%n",
                r.playingName(), r.bettingName(),
                100 * s.rate(s.wins), 100 * s.rate(s.pushes),
                100 * s.houseEdge(), 196 * s.houseEdgeStdError(),
                100 * s.returnOnAction(), 196 * s.returnOnActionStdError(),
                100 * r.ruinRate(),
                humanRate(s.rounds() / Math.max(1e-9, r.elapsedSeconds())));
    }

    private static String humanRate(double perSecond) {
        if (perSecond >= 1e6) return String.format(Locale.ROOT, "%.1fM", perSecond / 1e6);
        if (perSecond >= 1e3) return String.format(Locale.ROOT, "%.1fk", perSecond / 1e3);
        return String.format(Locale.ROOT, "%.0f", perSecond);
    }
}
