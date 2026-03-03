package strategy;
import game.Hand;
import game.Card;
public class OnlyStand implements Strategy {

    public String decide(Hand playerHand, Card dealerUpcard) {

        return "s";
    }

    public String getName()
    {
        return "OnlyStandStrategy";
    }

    
}
