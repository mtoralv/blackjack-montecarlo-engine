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

    public void playerTurn()
    {
        boolean playerStand=false;
        while(!player.getHand().isBust() && !playerStand)
        {
            System.out.println("Hit (h) or Stand (s)");
            String input = scanner.nextLine();

            if(input.toLowerCase().equals("s"))
            {
                playerStand=true;
            }
            else
            {
                player.addCard(deck.deal());
            }
        }
    }

    public void dealerTurn()
    {
        while(dealer.getTotal()<DEALER_STAND)
        {
            dealer.addCard(deck.deal());
        }
    }

    public void determineWinner()
    {
        if(player.getHand().isBlackjack())
        {
            player.changeBalance(bet*1.5);
            System.out.println("Blackjac! (+"+ bet*1.5 + "$)");
        }
        else if(player.getHand().isBust())
        {
            player.changeBalance(-bet);
            System.out.println("House wins! (-"+ bet + "$)");
        }
        else if(dealer.isBust())
        {
            player.changeBalance(+bet);
            System.out.println("Player wins! (+" + bet + "$)");
        }
        else if(player.getHand().getTotal()==dealer.getTotal())
        {
            player.changeBalance(0);
            System.out.println("Tie! (Bet returned)");
        }
        else if(player.getHand().getTotal()>dealer.getTotal())
        {
            player.changeBalance(+bet);
            System.out.println("Player wins! (+" + bet + "$)");  
        }
        else
        {
            player.changeBalance(-bet);
            System.out.println("House wins! (-"+ bet + "$)");    
        }
    }

    public void setBet(double bet)
    {
        this.bet = bet;
    }

    public void round()
    {
        System.out.println("Set a bet: ");
        double bet = scanner.nextDouble();
        if(bet<1 || bet>player.getBalance())
        {
            System.out.println("Select a valid bet!!!");
            round();
        }
        initialDeal();
        System.out.println("Dealing cards...");
        System.out.println("Dealer:");
        System.out.println(dealer.getFirstCard().toString());
        System.out.println("Your hand:");
        System.out.println(player.getHand().toString());
        playerTurn();
        dealerTurn();
        determineWinner();
    }
}
