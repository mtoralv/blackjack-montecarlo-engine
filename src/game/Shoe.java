package game;

import java.util.SplittableRandom;

public final class Shoe {

    private final Card[] cards;
    private final SplittableRandom rng;
    private final int cutCard;
    private int next;

    public Shoe(int decks, double penetration, SplittableRandom rng) {
        this.cards = new Card[decks * 52];
        int i = 0;
        for (int d = 0; d < decks; d++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards[i++] = Card.of(rank, suit);
                }
            }
        }
        this.rng = rng;
        this.cutCard = Math.max(1, (int) Math.round(cards.length * penetration));
        shuffle();
    }

    private Shoe(Card[] stacked) {
        this.cards = stacked;
        this.rng = null;
        this.cutCard = stacked.length;
        this.next = 0;
    }

    public static Shoe stacked(Card... order) {
        return new Shoe(order.clone());
    }

    public boolean needsShuffle() {
        return next >= cutCard;
    }

    public boolean isEmpty() {
        return next >= cards.length;
    }

    public void shuffle() {
        next = 0;
        if (rng == null) return; // stacked shoe (tests)
        for (int i = cards.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1); // fisher-yates
            Card tmp = cards[i];
            cards[i] = cards[j];
            cards[j] = tmp;
        }
    }

    public Card draw() {
        if (isEmpty()) {
            throw new IllegalStateException("Shoe is empty");
        }
        return cards[next++];
    }

    public int size() {
        return cards.length;
    }

    public int remaining() {
        return cards.length - next;
    }
}
