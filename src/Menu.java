import java.util.*;

public class Menu {
    public static void MainMenu() {
        Scanner input = new Scanner(System.in);
        int option;
        do {
            System.out.println("""
                    Main Menu:
                    (1) Search Players
                    (2) Search Clubs
                    (3) Add Player
                    (4) Exit System\s""");
            System.out.print("\nSelect an option (1-4): ");
            option = input.nextInt();
            input.nextLine();
            switch (option) {
                case 1: {
                    SearchPlayersMenu();
                }
                break;
                case 2: {
                    SearchClubsMenu();
                }
                break;
                case 3: {
                    AddPlayerMenu();
                }
                break;
                case 4: {
                    System.out.println("Exiting Program...");
                }
                break;
                default: {
                    System.out.println("Error: Invalid option! Please choose a valid option.\n");
                }
                break;
            }
        } while (option != 4);
    }

    private static void SearchPlayersMenu() {
        Scanner input = new Scanner(System.in);
        int option;
        do {
            System.out.println("""
                    
                    Player Searching Options:
                    (1) By Player Name
                    (2) By Club and Country
                    (3) By Position
                    (4) By Salary Range
                    (5) Country-wise player count
                    (6) Back to Main Menu\s""");
            System.out.print("\nSelect an option (1-6): ");
            option = input.nextInt();
            input.nextLine();
            switch (option) {
                case 1: {
                    //Search by player name.
                    List<Player> searchResults;
                    System.out.print("Enter the player's name: ");
                    String name = input.nextLine();
                    searchResults = Player.SearchByName(name);
                    if (searchResults.isEmpty()) { //If no player is found
                        System.out.println("\nPlayer not found!\n");
                    } else {
                        System.out.println("Search Results:\n");
                        Player.displaySearchResults(searchResults);
                        System.out.println();
                    }
                }
                return;
                case 2: {
                    //If the user chooses option (2), you should ask the user to input a country first and then ask the
                    //user to input a club. The user can input a club name (e.g., “Royal Challengers Bangalore”) or
                    //“ANY.” If “ANY” is inputted, the program should display all players of this country from the
                    //database. Display all information of players with this country and this club (or all clubs if “ANY”
                    //is inputted) if found, or display “No such player with this country and club” if not found.

                    List<Player> searchResults;

                    System.out.print("Enter the country's name: ");
                    String country = input.nextLine().trim();

                    System.out.print("Enter the club's name (Enter \"ANY\" to display all players of this country): ");
                    String club = input.nextLine().trim();

                    searchResults = Player.SearchByCountryAndClub(country, club);

                    if (searchResults.isEmpty()) { //If no player is found
                        System.out.println("\nSorry! No player found!\n");
                    } else {
                        System.out.println("Search Results:\n");
                        Player.displaySearchResults(searchResults);
                        System.out.println();
                    }
                }
                break;
                case 3: {
                    //If the user chooses option (3), you should ask the user to input a position and then search the
                    //database for any player with the specific position. If found, display all the players or display “No
                    //such player with this position” if not found.

                    List<Player> searchResults;

                    System.out.print("Enter the player's position: ");

                    searchResults = Player.SearchByPosition(input.nextLine().trim());

                    if (searchResults.isEmpty()) { //If no player is found
                        System.out.println("\nSorry! No player found!\n");
                    } else {
                        System.out.println("Search Results:\n");
                        Player.displaySearchResults(searchResults);
                        System.out.println();
                    }
                }
                break;
                case 4: {
                    //If the user chooses option (4), you should ask the user to input two numbers as a range and
                    //then search the database for any player with a weekly salary in this range. If found, display all
                    //the players or display “No such player with this weekly salary range” if not found.

                    List<Player> searchResults;
                    long start, end;

                    System.out.println("Enter two numbers as salary range:");

                    System.out.print("Enter the lower bound: ");
                    start = input.nextLong();
                    input.nextLine();

                    System.out.print("Enter the upper bound: ");
                    end = input.nextLong();
                    input.nextLine();

                    if (start > end) {
                        System.out.println("\nError: The lower bound cannot be greater than upper bound!\n");
                        break;
                    }

                    searchResults = Player.SearchBySalaryRange(start, end);

                    if (searchResults.isEmpty()) {
                        System.out.println("\nNo such player with this weekly salary range.\n");
                    } else {
                        System.out.println("Search Results:\n");
                        Player.displaySearchResults(searchResults);
                        System.out.println();
                    }
                }
                break;
                case 5: {
                    //If the user chooses option (5), display the country-wise count of players. You don’t need to take
                    //any input from the users.

                    Map<String, Integer> countryPlayerCountHashMap = Player.CountryWisePlayerCount();

                    System.out.println("Showing country-wise player counts: ");
                    for (Map.Entry<String, Integer> entry : countryPlayerCountHashMap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }

                    System.out.println();
                }
                break;
                case 6: {
                    System.out.println("Going back to Main Menu...\n");
                    break;
                }
                default:
                    break;
            }
        } while (option != 6);
    }

    private static void SearchClubsMenu() {
        Scanner input = new Scanner(System.in);
        int option;
        do {
            System.out.println("""
                    
                    Club Searching Options:
                    (1) Player(s) with the maximum salary of a club
                    (2) Player(s) with the maximum age of a club
                    (3) Player(s) with the maximum height of a club
                    (4) Total yearly salary of a club
                    (5) Back to Main Menu\s""");
            System.out.print("\nSelect an option (1-5): ");
            option = input.nextInt();
            input.nextLine();
            switch (option) {
                case 1: {
                    //If the user chooses option (1), you should ask the user to input a club name and then search for
                    //the players with the maximum weekly salary. Display all information about these players if the
                    //club is found, or display “No such club with this name” if not found.

                    System.out.print("Enter the club's name: ");
                    String clubName = input.nextLine();

                    List<Player> maxWeeklySalaryInClub = Player.MaxWeeklySalaryInClub(clubName);

                    if(maxWeeklySalaryInClub.isEmpty()) {
                        System.out.println("\nNo such club with this name!\n");
                    }
                    else {
                        System.out.println("\nShowing the players with the maximum salary of the club \"" + clubName + "\":\n");
                        Player.displaySearchResults(maxWeeklySalaryInClub);
                        System.out.println();
                    }
                }
                break;
                case 2: {
                    //If the user chooses option (2), you should ask the user to input a club name and then search for
                    //the players with the maximum age. Display all information about these players if the club is
                    //found, or display “No such club with this name” if not found.

                    System.out.print("Enter the club's name: ");
                    String clubName = input.nextLine();

                    List<Player> oldestPlayersInClub = Player.OldestPlayersInClub(clubName);

                    if (oldestPlayersInClub.isEmpty()) {
                        System.out.println("\nNo such club with this name!\n");
                    } else {
                        System.out.println("\nShowing the oldest players of the club \"" + clubName + "\":\n");
                        Player.displaySearchResults(oldestPlayersInClub);
                        System.out.println();
                    }
                    break;
                }
                case 3:{
                    //If the user chooses option (3), you should ask the user to input a club name and then search for
                    //the players with the maximum height. Display all information about these players if the club is
                    //found, or display “No such club with this name” if not found.

                    System.out.println("Enter the club's name: ");
                    String clubName = input.nextLine();

                    List<Player> tallestPlayersInClub = Player.TallestPlayersInClub(clubName);

                    if (tallestPlayersInClub.isEmpty()) {
                        System.out.println("\nNo such club with this name!\n");
                    }
                    else {
                        System.out.println("\nShowing the tallest players of the club \"" + clubName + "\":\n");
                        Player.displaySearchResults(tallestPlayersInClub);
                        System.out.println();
                    }
                }
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;
            }
        } while (option != 5);
    }

    private static void AddPlayerMenu() {
        System.out.println("Add a new player:");
        Scanner input = new Scanner(System.in);

        System.out.print("Enter player's name: ");
        String name = input.nextLine().trim();
        //Checking for duplicate names.
        for (Player p : Player.PlayerList) {
            if (p.getName().trim().equalsIgnoreCase(name)) {
                System.out.println("Player with this name already exists! Please choose another Name.");
                return;
            }
        }
        Player p = new Player();
        p.setName(name);

        System.out.print("Enter the player's country: ");
        p.setCountry(input.nextLine().trim());

        System.out.print("Enter the player's age in years: ");
        p.setAge(input.nextInt());
        input.nextLine();

        System.out.print("Enter the player's height in meters: ");
        p.setHeight(input.nextDouble());
        input.nextLine();

        System.out.print("Enter the club's name: ");
        p.setClub(input.nextLine().trim());

        System.out.print("Enter the player's position: ");
        p.setPosition(input.nextLine().trim());

        System.out.print("Enter the player's jersey number: ");
        p.setJerseyNumber(input.nextInt());
        input.nextLine();

        System.out.print("Enter the player's weekly salary: ");
        p.setSalary(input.nextLong());
        input.nextLine();

        Player.PlayerList.add(p);
        System.out.println("\nPlayer added successfully!\n");
    }
}
