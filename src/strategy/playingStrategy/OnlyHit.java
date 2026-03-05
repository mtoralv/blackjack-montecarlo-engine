package strategy.playingStrategy;
import game.Hand;
import game.Card;
public class OnlyHit implements PlayingStrategy {

    public String decide(Hand playerHand, Card dealerUpcard) {

        return "h";
    }

    public String getName()
    {
        return "OnlyHitStrategy";
    }
    
}
