/*
import java.util.Scanner;
//import game.Player;
import game.Game;
*/
import simulation.MonteCarlo;
import strategy.ThresholdStrategy;
import strategy.RandomStrategy;
import strategy.BasicStrategy;
import strategy.HiLoStrategy;

public class Main {
    public static void main(String[] args) {

        int numSimulations = 10;
        int handsPerSimulation = 1000000;
        int betSize = 10;
        int startingBalance = betSize*handsPerSimulation + 1000000;

        System.out.println("Random strat: ");
        MonteCarlo random = new MonteCarlo(numSimulations, handsPerSimulation, startingBalance, betSize, new RandomStrategy());
        random.simulate();
        random.printResults();


        System.out.println();


        System.out.println("Theshold strat: ");
        MonteCarlo threshold = new MonteCarlo(numSimulations, handsPerSimulation, startingBalance, betSize, new ThresholdStrategy());
        threshold.simulate();
        threshold.printResults();

        
        System.out.println();


        System.out.println("Basic strat: ");
        MonteCarlo basic = new MonteCarlo(numSimulations, handsPerSimulation, startingBalance, betSize, new BasicStrategy());
        basic.simulate();
        basic.printResults();

        
        System.out.println();

/*
        System.out.println("HiLo strat: ");
        MonteCarlo HiLo = new MonteCarlo(numSimulations, handsPerSimulation, startingBalance, betSize, new HiLoStrategy());
        HiLo.simulate();
        HiLo.printResults();
*/




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
}
