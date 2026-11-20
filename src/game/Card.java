package game;

public final class Card {

    private static final Card[][] CACHE = new Card[Rank.values().length][Suit.values().length];

    static {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                CACHE[rank.ordinal()][suit.ordinal()] = new Card(rank, suit);
            }
        }
    }

    private final Rank rank;
    private final Suit suit;

    private Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public static Card of(Rank rank, Suit suit) {
        return CACHE[rank.ordinal()][suit.ordinal()];
    }

    public static Card of(Rank rank) {
        return of(rank, Suit.SPADES);
    }

    public Rank rank() {
        return rank;
    }

    public Suit suit() {
        return suit;
    }

    public int value() {
        return rank.value();
    }

    public boolean isAce() {
        return rank == Rank.ACE;
    }

    public int hiLo() {
        int v = rank.value();
        if (v <= 6) return 1;
        if (v >= 10) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return rank.symbol() + suit.symbol();
    }
}
