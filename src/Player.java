public class Player {

    private int balance;
    private Hand hand = new Hand();

    public Player(int initialBalance)
    {
        balance = initialBalance;
    }

    public int getBalance()
    {
        return this.balance;
    }

    public void changeBalance(int change)
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
