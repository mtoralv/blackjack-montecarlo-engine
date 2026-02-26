import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player = new Player(500);

        Scanner scanner = new Scanner(System.in);
        boolean continueGame=true;

        Game game = new Game(player);

        while(player.getBalance()>0 && continueGame)
        { 
            game.round();

            System.out.println("Do you want to play another round? (y/n): ");
            String ans = scanner.nextLine();
            if(ans.toLowerCase().equals("y"))
            {
                continueGame=true;
            }
            else if(ans.toLowerCase().equals("n"))
            {
                continueGame=false;
            }
        }
        scanner.close();
        if(player.getBalance() <= 0)
        {
            System.out.println("You're out of money! Game over.");
        }
        else
        {
            game.clearScreen(false);
            System.out.println("Thanks for playing!");
        }
    }   
}
