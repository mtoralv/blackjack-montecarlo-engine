package strategy.bettingStrategy;

public class Martingale implements BettingStrategy {

    int lastRoundResult;
    int lastRoundBet;

public int getBet(int baseBet) 
{
    int bet;

    if(this.lastRoundWin)
    { 
        bet = baseBet;
    }
    else
    {
        bet = this.lastRoundBet*2;
    }
    this.lastRoundBet = bet;

    return bet;
}

default void roundResult(int result) 
    {
        this.lastRoundResult=result;
    }


    public String getName()
    {
        return "Martingale";
    }
    
}
