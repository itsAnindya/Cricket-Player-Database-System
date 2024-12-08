import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws Exception {
        Player.readPlayerListFromFile("src/resources/players.txt"); //Loads the file into the list.
        Menu.MainMenu();
        Player.writePlayerListToFile("src/resources/players.txt");
        exit(0);
    }
}