package simulation;

public record SessionResult(double finalNet, int handsPlayed, boolean ruined, double maxDrawdown, float[] trajectory) {
}
