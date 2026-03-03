package strategy;
import game.Hand;
import game.Card;
public class HiLoStrategy implements Strategy {

    private int runningCount;
    private int numDecks;
    private int cardsDealt;
    private Strategy baseStrategy; // MUST CHANGE THIS when i implement betting strategies appart from playing ones [IMPORTANT TO-DO]

    public HiLoStrategy(int numDecks) 
    {
        this.numDecks = numDecks;
        this.baseStrategy = new BasicStrategy();
    }

    public String decide(Hand playerHand, Card dealerUpcard) {

        return baseStrategy.decide(playerHand, dealerUpcard);

    }

    public void seeCard(Card card)
    {
        if(card.getValue()<=6)
        {
            runningCount++;
        }
        else if (card.getValue() >= 10) 
        {
            runningCount--;   
        }
        
        cardsDealt++;
    
    }

    public void resetCount() 
    {
        runningCount = 0;
        cardsDealt = 0;
    }

    public int getBet(int baseBet) 
    {

        double trueCount = (double) (runningCount / (numDecks - (double) cardsDealt / 52));

        if(trueCount>1)
        {
            int calculatedBet = (int) (baseBet * trueCount);
            int maxBet = baseBet * 12; 
            return Math.min(calculatedBet, maxBet);
        }
        else
        {
            return baseBet ;
        }
    }

    public String getName()
    {
        return "HiLoStrategy";
    }

    
}
