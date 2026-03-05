package strategy.bettingStrategy;

import game.Card;

public class HiLoStrategy implements BettingStrategy {

    private int runningCount;
    private int numDecks;
    private int cardsDealt;

    public HiLoStrategy(int numDecks) 
    {
        this.numDecks = numDecks;
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
