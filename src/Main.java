/*
import java.util.Scanner;
//import game.Player;
import game.Game;
*/
import simulation.MonteCarlo;
import strategy.playingStrategy.*;
import strategy.bettingStrategy.*;;

public class Main {

    private static final int numSimulations = 10;
    private static final int handsPerSimulation = 100000;
    private static final int betSize = 10;
    private static final int startingBalance = betSize*handsPerSimulation*10;
    private static final int numDecks = 6;
    
    public static void main(String[] args) {
        

        // card counting strategies need the numdecks parameter; might fix later with a config file
        testStrategy(new BasicStrategy(), new HiLoStrategy(6));

        testStrategy(new BasicStrategy(), new BaseBet());

        testStrategy(new RandomStrategy(), new BaseBet());


        testStrategy(new ThresholdStrategy(), new BaseBet());


        testStrategy(new OnlyHit(), new BaseBet());


        testStrategy(new OnlyStand(), new BaseBet());

        

        /* 
        Player player = new Player(500);

        Scanner scanner = new Scanner(System.in);
        boolean continueGame=true;

        Game game = new Game(player);

        while(player.getBalance()>0 && continueGame)
        { 
            game.round();
            if(player.getBalance()>0)
            {
                continueGame=game.askAnother();
            }
        }
        scanner.close();
        if(player.getBalance() <= 0)
        {
            System.out.println("You're out of money! Game over.");
        }
        else
        {
            game.clearScreen();
            game.header(false);
            if(!game.getSilent()) System.out.println("Thanks for playing!");
        }
*/


    }   

    public static void testStrategy(PlayingStrategy playingStrategy, BettingStrategy bettingStrategy)
    {
        String playingName = playingStrategy.getName();
        String bettingName = bettingStrategy.getName();
        System.out.println(playingName + " " + bettingName + ": ");
        MonteCarlo strat = new MonteCarlo(numSimulations, handsPerSimulation, startingBalance, betSize, numDecks , playingStrategy , bettingStrategy);
        strat.simulate();
        strat.printResults();
        strat.exportCSV();
        System.out.println();
    }
}
