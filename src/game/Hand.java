package game;

import java.util.ArrayList;
import util.Colors;

public class Hand {
    
    private ArrayList<Card> hand = new ArrayList<>();

    public Hand()
    {
    }

    public void addCard(Card card)
    {
        hand.add(card);
    }

    public int getSize()
    {
        return hand.size();
    }

    public int getTotal()
    {
        int totalVal = 0;
        int ace = 0;
        for( int i=0; i<hand.size(); i++)
        {
            if(hand.get(i).getValue()==11)
            {
                ace++;
            }
            totalVal+=hand.get(i).getValue();
        }
        while(ace>0 && totalVal>21)
        {
            ace--;
            totalVal-=10;
        }
        return totalVal;
    }

    public boolean isBust()
    {
        return (getTotal()>21);
    }

    public String toString()
    {
        String mainString = "     ";

        if(hand.isEmpty())
        {
            mainString = "Empty Hand!";
        }
        else{         
            for(int i = 0; i<hand.size(); i++)
            {
                mainString+= hand.get(i).toString();
            }
            mainString+= "  |  " + Colors.RED + getTotal() + Colors.RESET;
        }
        
        return mainString;
    }

    public boolean isSoft()
    {
        int totalVal = 0;
        int ace = 0;
        for( int i=0; i<hand.size(); i++)
        {
            if(hand.get(i).getValue()==11)
            {
                ace++;
            }
            totalVal+=hand.get(i).getValue();
        }
        while(ace>0 && totalVal>21)
        {
            ace--;
            totalVal-=10;
        }
        return (ace > 0);
    }

    public void resetHand()
    {
        hand.clear();
    }

    public boolean isBlackjack()
    {
        return(hand.size()==2 && this.getTotal()==21);
    }
    
    public Card getFirstCard()
    {
        return hand.getFirst();
    }

    public Card getCard(int i)
    {
        return (hand.get(i));
    }


}
