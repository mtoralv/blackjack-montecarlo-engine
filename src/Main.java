/*
import java.util.Scanner;
//import game.Player;
import game.Game;
*/
import simulation.MonteCarlo;
import strategy.ThresholdStrategy;
import strategy.RandomStrategy;
import strategy.Strategy;
import strategy.BasicStrategy;
import strategy.HiLoStrategy;
import strategy.OnlyHit;
import strategy.OnlyStand;

public class Main {
    public static void main(String[] args) {
        

        testStrategy(new HiLoStrategy(6)); // this is hardcoded; must change when possible


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

    public static void testStrategy(Strategy strategy)
    {
        int numSimulations = 100;
        int handsPerSimulation = 100000;
        int betSize = 10;
        int startingBalance = betSize*handsPerSimulation*10;
        int numDecks = 6;

        String name = strategy.getName();
        System.out.println(name + ": ");
        MonteCarlo strat = new MonteCarlo(numSimulations, handsPerSimulation, startingBalance, betSize, numDecks , strategy);
        strat.simulate();
        strat.printResults();
        strat.exportCSV();
        System.out.println();
    }
}
