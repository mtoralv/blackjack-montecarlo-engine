package game;

public enum Suit {
    HEARTS("H", true), DIAMONDS("D", true), CLUBS("C", false), SPADES("S", false);

    private final String symbol;
    private final boolean red;

    Suit(String symbol, boolean red) {
        this.symbol = symbol;
        this.red = red;
    }

    public String symbol() {
        return symbol;
    }

    public boolean isRed() {
        return red;
    }
}
