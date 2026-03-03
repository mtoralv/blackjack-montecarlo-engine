package game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    
    private boolean reshuffled = false;
    private int numDecks=6;
    private ArrayList<Card> deck = new ArrayList<>();

    public Deck()
    {
        newDeck(numDecks);
    }

    public Card deal()
    {
        if(deck.isEmpty())
        {
            reshuffled=true;
            newDeck(numDecks);
            shuffle();
        }
        return deck.removeFirst();
    }

    public boolean isReshuffled()
    {
        boolean reset=reshuffled;
        reshuffled=false;
        return reset;
    }

    public void shuffle()
    {
        Collections.shuffle(deck);
    }

    private void newDeck(int numDecks)
    {
        deck.clear();
        for(int i=0 ; i < numDecks ; i++)
        {
            for(Suit suit : Suit.values())
            {
                for(Rank rank : Rank.values())
                {
                    deck.add(new Card(rank,suit));
                }
            }
        }

    }
}
