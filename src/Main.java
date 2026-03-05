/*
import java.util.Scanner;
//import game.Player;
import game.Game;
*/
import simulation.MonteCarlo;
import strategy.playingStrategy.BasicStrategy;
import strategy.playingStrategy.HiLoStrategy;
import strategy.playingStrategy.OnlyHit;
import strategy.playingStrategy.OnlyStand;
import strategy.playingStrategy.RandomStrategy;
import strategy.playingStrategy.PlayingStrategy;
import strategy.playingStrategy.ThresholdStrategy;

public class Main {

    private static final int numSimulations = 1000;
    private static final int handsPerSimulation = 100000;
    private static final int betSize = 10;
    private static final int startingBalance = betSize*handsPerSimulation*10;
    private static final int numDecks = 6;
    
    public static void main(String[] args) {
        

        // card counting strategies need the numdecks parameter; might fix later with a config file
        testStrategy(new HiLoStrategy(numDecks));


        testStrategy(new RandomStrategy());


        testStrategy(new ThresholdStrategy());


        testStrategy(new OnlyHit());


        testStrategy(new OnlyStand());


        testStrategy(new BasicStrategy());
        

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

    public static void testStrategy(PlayingStrategy strategy)
    {
        String name = strategy.getName();
        System.out.println(name + ": ");
        MonteCarlo strat = new MonteCarlo(numSimulations, handsPerSimulation, startingBalance, betSize, numDecks , strategy);
        strat.simulate();
        strat.printResults();
        strat.exportCSV();
        System.out.println();
    }
}
