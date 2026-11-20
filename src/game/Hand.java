package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Hand {

    private final List<Card> cards = new ArrayList<>(6);
    private int hardTotal; // aces counted as 1, total() turns one into 11 if it fits
    private int aces;
    private boolean fromSplit;

    public Hand() {
    }

    static Hand splitFrom(Card card) {
        Hand hand = new Hand();
        hand.fromSplit = true;
        hand.add(card);
        return hand;
    }

    public void add(Card card) {
        cards.add(card);
        if (card.isAce()) {
            aces++;
            hardTotal += 1;
        } else {
            hardTotal += card.value();
        }
    }

    Card removeSecond() {
        Card card = cards.remove(1);
        if (card.isAce()) {
            aces--;
            hardTotal -= 1;
        } else {
            hardTotal -= card.value();
        }
        fromSplit = true;
        return card;
    }

    public int total() {
        return isSoft() ? hardTotal + 10 : hardTotal;
    }

    public boolean isSoft() {
        return aces > 0 && hardTotal + 10 <= 21;
    }

    public boolean isBust() {
        return hardTotal > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && !fromSplit && total() == 21;
    }

    public boolean isPair() {
        return cards.size() == 2 && cards.get(0).value() == cards.get(1).value();
    }

    public boolean isFromSplit() {
        return fromSplit;
    }

    public int size() {
        return cards.size();
    }

    public Card card(int index) {
        return cards.get(index);
    }

    public List<Card> cards() {
        return Collections.unmodifiableList(cards);
    }

    @Override
    public String toString() {
        return cards + " (" + (isSoft() ? "soft " : "") + total() + ")";
    }
}
