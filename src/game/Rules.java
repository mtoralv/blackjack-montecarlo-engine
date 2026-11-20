package game;

public record Rules(
        int decks,
        double penetration,
        boolean dealerHitsSoft17,
        double blackjackPayout,
        boolean doubleAfterSplit,
        boolean lateSurrender,
        int maxHands,
        boolean resplitAces,
        boolean dealerPeeks) {

    public Rules {
        if (decks < 1) throw new IllegalArgumentException("decks must be >= 1");
        if (penetration <= 0 || penetration > 1) throw new IllegalArgumentException("penetration must be in (0, 1]");
        if (maxHands < 1) throw new IllegalArgumentException("maxHands must be >= 1");
    }

    // typical 6 deck Vegas game: S17, 3:2, DAS, late surrender, reshuffle at 75%
    public static Rules standard() {
        return new Rules(6, 0.75, false, 1.5, true, true, 4, false, true);
    }

    public Rules withDecks(int decks) {
        return new Rules(decks, penetration, dealerHitsSoft17, blackjackPayout, doubleAfterSplit, lateSurrender, maxHands, resplitAces, dealerPeeks);
    }

    public Rules withPenetration(double penetration) {
        return new Rules(decks, penetration, dealerHitsSoft17, blackjackPayout, doubleAfterSplit, lateSurrender, maxHands, resplitAces, dealerPeeks);
    }

    public Rules withDealerHitsSoft17(boolean h17) {
        return new Rules(decks, penetration, h17, blackjackPayout, doubleAfterSplit, lateSurrender, maxHands, resplitAces, dealerPeeks);
    }

    public Rules withBlackjackPayout(double payout) {
        return new Rules(decks, penetration, dealerHitsSoft17, payout, doubleAfterSplit, lateSurrender, maxHands, resplitAces, dealerPeeks);
    }

    public Rules withDoubleAfterSplit(boolean das) {
        return new Rules(decks, penetration, dealerHitsSoft17, blackjackPayout, das, lateSurrender, maxHands, resplitAces, dealerPeeks);
    }

    public Rules withLateSurrender(boolean surrender) {
        return new Rules(decks, penetration, dealerHitsSoft17, blackjackPayout, doubleAfterSplit, surrender, maxHands, resplitAces, dealerPeeks);
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append(decks).append("D ").append(dealerHitsSoft17 ? "H17" : "S17");
        if (doubleAfterSplit) sb.append(" DAS");
        if (lateSurrender) sb.append(" LS");
        sb.append(blackjackPayout == 1.5 ? " 3:2" : blackjackPayout == 1.2 ? " 6:5" : " BJ x" + blackjackPayout);
        sb.append(" pen ").append(Math.round(penetration * 100)).append('%');
        return sb.toString();
    }
}
