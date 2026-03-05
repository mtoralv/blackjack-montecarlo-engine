package strategy.playingStrategy;
import game.Hand;
import game.Card;
public class OnlyStand implements PlayingStrategy {

    public String decide(Hand playerHand, Card dealerUpcard) {

        return "s";
    }

    public String getName()
    {
        return "OnlyStandStrategy";
    }

    
}
