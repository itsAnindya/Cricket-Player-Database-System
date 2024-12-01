import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws Exception {
        Player.readPlayerListFromFile("data/players.txt"); //Loads the file into the list.
        Menu.MainMenu();
        Player.writePlayerListToFile("data/players.txt");
        exit(0);
    }
}
