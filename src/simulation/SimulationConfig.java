package simulation;

import game.Rules;

public record SimulationConfig(
        Rules rules,
        String playing,
        String betting,
        int spread,
        int simulations,
        int handsPerSimulation,
        double unit,
        double bankroll,
        double tableMaxUnits,
        long seed,
        int sampleEvery) {

    public SimulationConfig {
        if (simulations < 1) throw new IllegalArgumentException("simulations must be >= 1");
        if (handsPerSimulation < 1) throw new IllegalArgumentException("hands must be >= 1");
        if (unit <= 0) throw new IllegalArgumentException("unit must be > 0");
        if (sampleEvery < 1) throw new IllegalArgumentException("sampleEvery must be >= 1");
    }

    public SimulationConfig with(String playing, String betting) {
        return new SimulationConfig(rules, playing, betting, spread, simulations, handsPerSimulation, unit, bankroll, tableMaxUnits, seed, sampleEvery);
    }
}
