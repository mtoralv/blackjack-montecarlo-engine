package strategy;

import java.util.List;
import java.util.Locale;
import java.util.SplittableRandom;

import game.Rules;
import strategy.betting.BettingStrategy;
import strategy.betting.FlatBet;
import strategy.betting.HiLoStrategy;
import strategy.betting.Martingale;
import strategy.betting.OneThreeTwoSix;
import strategy.betting.OscarsGrind;
import strategy.playing.BasicStrategy;
import strategy.playing.OnlyHit;
import strategy.playing.OnlyStand;
import strategy.playing.PlayingStrategy;
import strategy.playing.RandomStrategy;
import strategy.playing.ThresholdStrategy;

public final class Strategies {

    public static final List<String> PLAYING = List.of("basic", "threshold", "random", "onlyhit", "onlystand");
    public static final List<String> BETTING = List.of("flat", "hilo", "martingale", "oscar", "1326");

    private Strategies() {
    }

    public static PlayingStrategy playing(String key, Rules rules, SplittableRandom rng) {
        return switch (key.toLowerCase(Locale.ROOT)) {
            case "basic" -> new BasicStrategy(rules);
            case "threshold" -> new ThresholdStrategy();
            case "random" -> new RandomStrategy(rng);
            case "onlyhit" -> new OnlyHit();
            case "onlystand" -> new OnlyStand();
            default -> throw new IllegalArgumentException("Unknown playing strategy '" + key + "'. Options: " + PLAYING);
        };
    }

    public static BettingStrategy betting(String key, int spread) {
        return switch (key.toLowerCase(Locale.ROOT)) {
            case "flat" -> new FlatBet();
            case "hilo" -> new HiLoStrategy(spread);
            case "martingale" -> new Martingale();
            case "oscar" -> new OscarsGrind();
            case "1326" -> new OneThreeTwoSix();
            default -> throw new IllegalArgumentException("Unknown betting strategy '" + key + "'. Options: " + BETTING);
        };
    }
}
