import java.util.Scanner;

public class SearchPlayersMenu {
    public static void displayMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("""
                Player Searching Options:
                (1) By Player Name
                (2) By Club and Country
                (3) By Position
                (4) By Salary Range
                (5) Country-wise player count
                (6) Back to Main Menu
                
                Select an option (1-6):\s""");
        int option = input.nextInt();
        switch (option) {
            case 1:

        }
    }
}
