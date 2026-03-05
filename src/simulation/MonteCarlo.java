package simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import game.Game;
import game.Player;
import strategy.bettingStrategy.BettingStrategy;
import strategy.playingStrategy.PlayingStrategy;

public class MonteCarlo {

    private int numSimulations;
    private int handsPerSimulation;
    private int startingBalance;
    private int betSize;
    private PlayingStrategy playingStrategy;
    private BettingStrategy bettingStrategy;
    private ArrayList<ArrayList<Integer>> netProfitHistory;
    private int totalWins;
    private int totalLosses;
    private int totalTies;
    private int totalBlackjacks;
    private int totalHands;
    private int totalWagered;
    private int netProfit;
    private int totalSurrender;
    private int totalDoubleDown;
    private int numDecks;

    public MonteCarlo(int numSimulations, int handsPerSimulation, int startingBalance, int betSize, int numDecks, PlayingStrategy playingStrategy, BettingStrategy bettingStrategy)
    {
        this.numSimulations = numSimulations;
        this.handsPerSimulation = handsPerSimulation;
        this.startingBalance = startingBalance;
        this.betSize = betSize;
        this.numDecks = numDecks;
        this.playingStrategy = playingStrategy;
        this.bettingStrategy = bettingStrategy;
        this.netProfitHistory = new ArrayList<>();
    }

    public void simulate()
    {
        for(int i=0 ; i < numSimulations ; i++)
        {

            ArrayList<Integer> simulation = new ArrayList<>();
            Player player = new Player(startingBalance);
            Game game = new Game(player, numDecks );
            game.setSilent(true);
            game.setPlayingStrategy(playingStrategy);
            game.setBettingStrategy(bettingStrategy);
            int simNetProfit = 0;
            simulation.add(0);
            
            for(int j=0; j < handsPerSimulation ; j++)
            {
            
                int newBetSize = Math.min(bettingStrategy.getBet(betSize), player.getBalance());
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
                    else if(player.getBalance() == prevBalance - newBetSize/2)
                    {
                        totalSurrender++;
                        totalLosses++;
                    }
                    else if(player.getBalance() == prevBalance + newBetSize*2 || player.getBalance() == prevBalance - newBetSize*2)
                    {
                        totalWagered += newBetSize;
                        totalDoubleDown++;
                        if(prevBalance < player.getBalance())
                        {
                            totalWins++;
                        }
                        else if( prevBalance > player.getBalance())
                        {
                            totalLosses++;
                        }
                    }
                    else if(prevBalance < player.getBalance())
                    {
                        totalWins++;
                    }
                    else if( prevBalance > player.getBalance())
                    {
                        totalLosses++;
                    }

                    simNetProfit += player.getBalance() - prevBalance;
                    simulation.add(simNetProfit);
                }    
            }
            netProfitHistory.add(simulation);
        }
    }

    public ArrayList<ArrayList<Integer>> getnetProfitHistory()
    {
        return netProfitHistory;
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
        System.out.println("Total surrender: " + this.totalSurrender);
        System.out.println("Total double down: " + this.totalDoubleDown);
        System.out.printf("Win rate: %.3f%%%n", winRate);
        System.out.printf("House edge: %.3f%%%n", houseEdge);

    }

    public int getNumDecks()
    {
        return numDecks;
    }
    

    public void exportCSV() {
    try {
        FileWriter writer = new FileWriter("CSVresults/" + playingStrategy.getName() + "+" + bettingStrategy.getName() + "_exportedCSV.csv");
        
        for (int i = 0; i < getnetProfitHistory().size(); i++) {
            for (int j = 0; j < getnetProfitHistory().get(i).size(); j++) {
                writer.write(getnetProfitHistory().get(i).get(j) + ",");
            }
            writer.write("\n");
        }

        writer.close();
        System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}

}
