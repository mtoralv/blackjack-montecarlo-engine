/*
import java.util.Scanner;
//import game.Player;
import game.Game;
*/
import simulation.MonteCarlo;
import strategy.BasicStrategy;
import strategy.RandomStrategy;

public class Main {
    public static void main(String[] args) {

        System.out.println("Random strat: ");
        MonteCarlo mc = new MonteCarlo(100, 1000000, 5000, 10, new RandomStrategy());
        mc.simulate();
        mc.printResults();
        System.out.println();
        System.out.println("Basic strat: ");
        MonteCarlo basic = new MonteCarlo(100, 1000000, 5000, 10, new BasicStrategy());
        basic.simulate();
        basic.printResults();
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
