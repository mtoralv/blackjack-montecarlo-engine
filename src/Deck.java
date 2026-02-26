import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    
    private ArrayList<Card> deck = new ArrayList<>();

    public Deck()
    {
        newDeck();
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
        return deck.removeFirst();
    }

    public void shuffle()
    {
        Collections.shuffle(deck);
    }

    private void newDeck()
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
}
