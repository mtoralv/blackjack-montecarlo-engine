
public class Card {

    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit)
    {
        this.rank=rank;
        this.suit=suit;
    }

    public int getValue()
    {
        return rank.getValue();
    }

    public String toString()
    {
        String cardString = "";
        if(suit.getSuit().equals("H") || suit.getSuit().equals("D"))
        {
            cardString = Colors.MAGENTA + "[" + Colors.RED + suit.getSuit() + rank.getValue() + suit.getSuit() + Colors.MAGENTA + "]" + Colors.RESET;
        }
        else if(suit.getSuit().equals("C") || suit.getSuit().equals("S"))
        {
            cardString = Colors.MAGENTA + "[" + Colors.BLACK + suit.getSuit() + rank.getValue() + suit.getSuit() + Colors.MAGENTA + "]" + Colors.RESET;
        }
        return cardString ;
    }
}

    

