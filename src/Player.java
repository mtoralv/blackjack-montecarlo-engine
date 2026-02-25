public class Player {

    private double balance;
    private Hand hand = new Hand();

    public Player(int initialBalance)
    {
        balance = initialBalance;
    }

    public double getBalance()
    {
        return this.balance;
    }

    public void changeBalance(double change)
    {
        balance+=change;
    }

    public Hand getHand()
    {
        return hand;
    }

    public void resetHand()
    {
        hand.resetHand();
    }

    public void addCard(Card card)
    {
        hand.addCard(card);;
    }  
}
