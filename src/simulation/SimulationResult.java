package simulation;

import java.util.List;

public record SimulationResult(SimulationConfig config, String playingName, String bettingName,
                               Stats stats, List<SessionResult> sessions, double elapsedSeconds) {

    public String id() {
        return (playingName + "__" + bettingName).replaceAll("[^A-Za-z0-9_\\-]", "");
    }

    public double ruinRate() {
        return sessions.stream().filter(SessionResult::ruined).count() / (double) sessions.size();
    }
}
