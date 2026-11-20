package game;

public interface CardListener {

    CardListener NONE = new CardListener() { };

    default void onCard(Card card) {
    }

    default void onShuffle() {
    }
}
