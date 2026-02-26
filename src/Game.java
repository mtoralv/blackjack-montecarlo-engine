import java.util.Scanner;

public class Game {
    
    private static final int DEALER_STAND = 17;

    Scanner scanner = new Scanner(System.in);

    private Deck deck;
    private Player player;
    private Hand dealer;
    private double bet;

    public Game( Player player)
    {
        this.player = player;
        this.bet = player.getBalance()/10;
        this.deck = new Deck();
        this.dealer= new Hand();
    }

    public void initialDeal()
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

    public boolean playerTurn()
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
            System.out.print("(h)it or (s)tand: ");
            String input = scanner.nextLine();

            if(input.toLowerCase().equals("s"))
            {
                playerStand=true;
            }
            else if(input.toLowerCase().equals("h"))
            {
                player.addCard(deck.deal());
                currentState(true);
            }
            else
            {
                currentState(true);
            }
        }
        return false;
    }

    public void dealerTurn()
    {
        while(dealer.getTotal()<DEALER_STAND)
        {
            dealer.addCard(deck.deal());
            currentState(false);
        }
    }

    public void determineWinner()
    {
        if(player.getHand().isBlackjack() && !dealer.isBlackjack())
        {
            player.changeBalance(bet*1.5);
            System.out.println("Blackjack! ( +"+ bet*1.5 + "$ )");
        }
        else if(player.getHand().isBust())
        {
            player.changeBalance(-bet);
            System.out.println("House wins! ( -"+ bet + "$ )");
        }
        else if(dealer.isBust())
        {
            player.changeBalance(+bet);
            System.out.println("Player wins! ( +" + bet + "$ )");
        }
        else if(player.getHand().getTotal()==dealer.getTotal())
        {
            player.changeBalance(0);
            System.out.println("Tie! ( Bet returned )");
        }
        else if(player.getHand().getTotal()>dealer.getTotal())
        {
            player.changeBalance(+bet);
            System.out.println("Player wins! ( +" + bet + "$ )");  
        }
        else
        {
            player.changeBalance(-bet);
            System.out.println("House wins! ( -"+ bet + "$ )");    
        }
    }

    public void setBet(double bet)
    {
        this.bet = bet;
    }

    public void clearScreen(boolean start)
{
        System.out.print("\033[H\033[2J");
        System.out.flush();
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

    public void round()
    {
        clearScreen(false);
        do 
        {
            System.out.print("Set a bet (0-" + player.getBalance() + "): ");
            try {
                setBet(scanner.nextDouble());
                scanner.nextLine();
            }
            catch (Exception e) {
                clearScreen(true);
                scanner.nextLine();
                setBet(0);
            }
        } 
        while(this.bet < 1 || this.bet > player.getBalance());

        initialDeal();
        System.out.println("Dealing cards...");
        currentState(true);
        
        boolean resolved = playerTurn();
        if(!resolved)
        {
            dealerTurn();
            determineWinner();
        }
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        player.resetHand();
        dealer.resetHand();
    }

    public void currentState(boolean hideDealer)
    {
        clearScreen(true);
        System.out.print( Colors.YELLOW + "DEALER     " + Colors.RESET );
        if(hideDealer)
        {
            System.out.println(dealer.getFirstCard().toString() + " | ??");
        }
        else
        {
            System.out.println(dealer.toString() );
        }
        System.out.println(Colors.WHITE + " ---------------------------- " + Colors.RESET);
        System.out.println("Your hand:");
        System.out.println(player.getHand().toString());
    }
}
