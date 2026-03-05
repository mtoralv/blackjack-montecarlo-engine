package strategy;
import game.Hand;
import game.Card;
public class OnlyHit implements Strategy {

    public String decide(Hand playerHand, Card dealerUpcard) {

        return "h";
    }

    public String getName()
    {
        return "OnlyHitStrategy";
    }
    
}
