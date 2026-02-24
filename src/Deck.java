import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    
    private ArrayList<Card> deck = new ArrayList<>();

    public Deck()
    {
        newDeck();
    }

    public void newDeck()
    {
        deck.clear();
        for(Suit suit : Suit.values())
        {
            for(Rank rank : Rank.values())
            {
                deck.add(new Card(rank,suit));
            }
        }
    }

    public int size()
    {
        return deck.size();
    }

    public Card deal()
    {
        if(deck.isEmpty())
        {
            System.out.println("ERROR, empty Deck");
            System.out.println("Getting new Deck and reshuffling...");
            newDeck();
            shuffle();
        }
        Card dealtCard = deck.getFirst();
        deck.removeFirst();
        return dealtCard;
    }

    public void shuffle()
    {
        Collections.shuffle(deck);
    }
}
