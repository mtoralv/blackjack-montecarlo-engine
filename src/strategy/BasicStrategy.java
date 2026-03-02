package strategy;

import game.Hand;
import game.Card;

public class BasicStrategy implements Strategy {
        
    private static final String[][] BasicStratTable = {
        {"h","h","h","h","h","h","h","h","h","h"}, 
        {"h","h","h","h","h","h","h","h","h","h"}, 
        {"h","d","d","d","d","h","h","h","h","h"}, 
        {"d","d","d","d","d","d","d","d","h","h"}, 
        {"d","d","d","d","d","d","d","d","d","h"}, 
        {"h","s","s","s","h","h","h","h","h","h"}, 
        {"s","s","s","s","s","h","h","h","h","h"}, 
        {"s","s","s","s","s","h","h","h","h","h"}, 
        {"s","s","s","s","s","h","h","h","r","h"}, 
        {"s","s","s","s","s","h","h","r","r","r"}, 
        {"s","s","s","s","s","s","s","s","s","s"}, // softhand from here 
        {"h","h","h","d","d","h","h","h","h","h"}, 
        {"h","h","h","d","d","h","h","h","h","h"}, 
        {"h","h","d","d","d","h","h","h","h","h"}, 
        {"h","h","d","d","d","h","h","h","h","h"}, 
        {"h","d","d","d","d","h","h","h","h","h"}, 
        {"s","ds","ds","ds","ds","s","s","h","h","h"}, 
        {"s","s","s","s","s","s","s","s","s","s"}, 
        {"s","s","s","s","s","s","s","s","s","s"}, 
    
    };

    public String decide(Hand playerHand, Card dealerUpcard) {
    
        int col = (dealerUpcard.getValue()-2);
        int row;

        if(playerHand.isSoft())
        {
            row = playerHand.getTotal() - 2;
        }
        else
        {
            if(playerHand.getTotal() <= 7)
            {
                row = 0;
            }
            else if(playerHand.getTotal() > 17)
            {
                return "s";
            }
            else
            {
                row = playerHand.getTotal() - 7;
            }

            
        }

        /*
        DISCLAIMER; This is a temporal fix due to main.java not supporting d, ds and r at the moment;
        conversion table shown -->
        DS → "s"         
        */

        if(BasicStratTable[row][col].equals("ds"))
        {
            return "s";
        }
        else
        {
            return BasicStratTable[row][col];
        }

        // end of disclaimer part

    }
}