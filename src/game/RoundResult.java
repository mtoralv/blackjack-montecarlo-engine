package game;

import java.util.List;

public record RoundResult(
        double initialBet,
        double totalWagered,
        double net,
        List<Hand> playerHands,
        List<Double> handNets,
        Hand dealerHand,
        boolean playerBlackjack,
        boolean dealerBlackjack,
        int doubles,
        int splits,
        boolean surrendered) {
}
