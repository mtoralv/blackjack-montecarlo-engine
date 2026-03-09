package strategy.bettingStrategy;

public class Martingale implements BettingStrategy {

    boolean lastRoundWin=true;
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

    public void roundResult(boolean win) 
    {
        this.lastRoundWin=win;
    }


    public String getName()
    {
        return "Martingale";
    }
    
}
