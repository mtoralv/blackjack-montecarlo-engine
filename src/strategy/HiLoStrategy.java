package strategy;
import game.Hand;
import game.Card;
public class HiLoStrategy implements Strategy {

    public String decide(Hand playerHand, Card dealerUpcard) {

        
        return "s";
    }

    public String getName()
    {
        return "HiLoStrategy";
    }

    
}
