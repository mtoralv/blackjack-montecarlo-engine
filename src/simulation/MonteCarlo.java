package simulation;

import java.util.ArrayList;

import game.Game;
import game.Player;
import strategy.Strategy;

public class MonteCarlo {

    private int numSimulations;
    private int handsPerSimulation;
    private int startingBalance;
    private int betSize;
    private Strategy strategy;
    private ArrayList<ArrayList<Integer>> balanceHistory;
    private int totalWins;
    private int totalLosses;
    private int totalTies;
    private int totalBlackjacks;
    private int totalHands;
    private int totalWagered;
    private int netProfit;

    public MonteCarlo(int numSimulations, int handsPerSimulation, int startingBalance, int betSize, Strategy strategy)
    {
        this.numSimulations = numSimulations;
        this.handsPerSimulation = handsPerSimulation;
        this.startingBalance = startingBalance;
        this.betSize = betSize;
        this.strategy = strategy;
        this.balanceHistory = new ArrayList<>();
    }

    public void simulate()
    {
        for(int i=0 ; i < numSimulations ; i++)
        {

            ArrayList<Integer> simulation = new ArrayList<>();
            simulation.add(startingBalance);
            Player player = new Player(startingBalance);
            Game game = new Game(player);
            game.setSilent(true);
            game.setStrategy(strategy);
            
            for(int j=0; j < handsPerSimulation ; j++)
            {
            
                int newBetSize = Math.min(betSize, player.getBalance());
                game.setBet(newBetSize);
                
                if(player.getBalance()==0)
                {
                    break;
                }
                else
                {
                    int prevBalance = player.getBalance();
                    game.round(); 

                    netProfit += player.getBalance() - prevBalance;

                    totalHands++;
                    totalWagered += newBetSize;
                    if(prevBalance == player.getBalance())
                    {
                        totalTies++;
                    }
                    else if(player.getBalance() - prevBalance == newBetSize * 3 / 2)
                    {
                        totalBlackjacks++;
                        totalWins++;
                    }
                    else if(prevBalance < player.getBalance())
                    {
                        totalWins++;
                    }
                    else if( prevBalance > player.getBalance())
                    {
                        totalLosses++;
                    }
                }
                    simulation.add(player.getBalance());
            }
            balanceHistory.add(simulation);
        }
    }

    public ArrayList<ArrayList<Integer>> getBalanceHistory()
    {
        return balanceHistory;
    }
    
    public void printResults()
    {
        double houseEdge = -(double) netProfit / totalWagered * 100;
        double winRate = (double) totalWins / totalHands * 100;

        System.out.println("Total hands: " + this.totalHands);
        System.out.println("Total wins: " + this.totalWins);
        System.out.println("Total looses: " + this.totalLosses);
        System.out.println("Total ties: " + this.totalTies);
        System.out.println("Total blackjacks: " + this.totalBlackjacks);
        System.out.printf("Win rate: %.3f%%%n", winRate);
        System.out.printf("House edge: %.3f%%%n", houseEdge);

    }
    
    public void exportCSV()
    {

    }
    
}
