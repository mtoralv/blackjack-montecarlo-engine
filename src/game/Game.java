package game;

import java.util.Scanner;
import strategy.*;
import util.Colors; 

public class Game {
    
    private static final int DEALER_STAND = 17;

    private Scanner scanner = new Scanner(System.in);

    private boolean silent = false;

    private Deck deck;
    private Player player;
    private Hand dealer;
    private int bet;
    private Strategy strategy;

    public Game( Player player)
    {
        this.player = player;
        this.deck = new Deck();
        this.dealer= new Hand();
    }

    public void round()
    {
        if(!silent)
        { 
            clearScreen();
            header(false);
            do 
            {
                System.out.print("Set a bet (0-" + player.getBalance() + "): ");
                try {
                    setBet(scanner.nextInt());
                    scanner.nextLine();
                }
                catch (Exception e) {
                    clearScreen();
                    header(true);
                    scanner.nextLine();
                    setBet(0);
                }
            } 
            while(this.bet < 1 || this.bet > player.getBalance());
        }

        initialDeal();

        if(!silent)
        {
            System.out.println("Dealing cards...");
            currentState(true);
        }

        
        boolean resolved = playerTurn();
        if(!resolved)
        {
            dealerTurn();
            determineWinner();
        }

        if(!silent)
        {
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
        }

        player.resetHand();
        dealer.resetHand();
    }

    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void header(boolean start)
    {
        System.out.println(Colors.YELLOW + " |--------------------------| " );
        System.out.println( " |          CASINO          | " );
        System.out.println(" |--------------------------| " + Colors.RESET);
        System.out.println();
        if(start)
        {
            System.out.println("Chips: " + Colors.GREEN + player.getBalance() + Colors.RESET + "        Bet: " + Colors.YELLOW + this.bet + Colors.RESET);
            System.out.println();
        }
    }

    public boolean askAnother()
    {
        while(true)
        {
            System.out.print("Play another round? (y/n): ");

            String ans = scanner.nextLine().toLowerCase();

            if(ans.equals("y")) return true;
            else if(ans.equals("n")) return false;

            else System.out.println("Invalid input. Enter y or n.");
        }
    }

    public void setSilent(boolean silent)
    {
        this.silent = silent;   
    }

    public boolean getSilent()
    {
        return silent;
    }

    private void initialDeal()
    {
        deck.shuffle();
        for(int i=0;i<2;i++)
        {
            dealer.addCard(deck.deal());
        }
        for(int i=0;i<2;i++)
        {
            player.addCard(deck.deal());
        }
    }

    private boolean playerTurn()
    {
        if(player.getHand().isBlackjack())
        {
            dealerTurn();
            determineWinner();
            return true;
        }
        boolean playerStand=false;
        while(!player.getHand().isBust() && !playerStand)
        {
            String input;
            if(!silent)
            {
                System.out.print("(h)it or (s)tand: ");
                input = scanner.nextLine();
            }
            else
            {
                input = strategy.decide(player.getHand(), dealer.getFirstCard());
            }
            
            if(input.toLowerCase().equals("s"))
            {
                playerStand=true;
            }
            else if(input.toLowerCase().equals("h"))
            {
                player.addCard(deck.deal());
                if(!silent) currentState(true);
                if(player.getHand().getTotal()==21)
                {
                    playerStand = true;
                }
            }
            else
            {
                if(!silent) currentState(true);
            }
            
        }
        return false;
    }

    private void dealerTurn()
    {
        while(dealer.getTotal()<DEALER_STAND)
        {
            dealer.addCard(deck.deal());
            if(!silent)currentState(false);
        }
    }

    private void determineWinner()
    {
        if(player.getHand().isBlackjack() && !dealer.isBlackjack())
        {
            player.changeBalance(bet * 3 / 2);
            if(!silent)System.out.println("Blackjack! ( +"+ bet*3/2 + "$ )");
        }
        else if(player.getHand().isBust())
        {
            player.changeBalance(-bet);
            if(!silent)System.out.println("House wins! ( -"+ bet + "$ )");
        }
        else if(dealer.isBust())
        {
            player.changeBalance(+bet);
            if(!silent)System.out.println("Player wins! ( +" + bet + "$ )");
        }
        else if(player.getHand().getTotal()==dealer.getTotal())
        {
            player.changeBalance(0);
            if(!silent)System.out.println("Tie! ( Bet returned )");
        }
        else if(player.getHand().getTotal()>dealer.getTotal())
        {
            player.changeBalance(+bet);
            if(!silent)System.out.println("Player wins! ( +" + bet + "$ )");  
        }
        else
        {
            player.changeBalance(-bet);
            if(!silent)System.out.println("House wins! ( -"+ bet + "$ )");    
        }
    }

    public void setBet(int bet)
    {
        this.bet = bet;
    }

    private void currentState(boolean hideDealer)
    {
        clearScreen();
        header(true);
        System.out.print( Colors.YELLOW + "DEALER" + Colors.RESET );
        if(hideDealer)
        {
            System.out.println( "     " + dealer.getFirstCard().toString() + Colors.MAGENTA + "[??]" + Colors.RESET + "  |  " + Colors.RED + "??" + Colors.RESET);
        }
        else
        {
            System.out.println(dealer.toString() );
        }
        System.out.println(Colors.WHITE + " ---------------------------- " + Colors.RESET);
        System.out.println("Your hand:");
        System.out.println(player.getHand().toString());
        System.out.println();
    }


}
