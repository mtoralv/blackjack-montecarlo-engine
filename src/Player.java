public class Player {

    private int balance;
    private Hand hand;

    public Player()
    {
        balance = 100;
    }

    public int getBalance()
    {
        return this.balance;
    }

    public void changeBalance(int change)
    {
        balance+=change;
    }
    
}
