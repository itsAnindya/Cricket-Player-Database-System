
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class PlayerList {

    protected static List<Player> playerList = new ArrayList<>();

    public static void getPlayerListFromFile() throws Exception {
        final String INPUT_FILE_NAME = "players.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                Player p = new Player();
                String[] playerInfos = line.split(",");
                p.setName(playerInfos[0]);
                p.setCountry(playerInfos[1]);
                p.setAge(Integer.parseInt(playerInfos[2]));
                p.setHeight(Double.parseDouble(playerInfos[3]));
                p.setClub(playerInfos[4]);
                p.setPosition(playerInfos[5]);
                if (!playerInfos[6].isEmpty()) {
                    p.setJerseyNumber(Integer.parseInt(playerInfos[6]));
                }
                else{
                    p.setJerseyNumber(-1);
                }
                p.setSalary(Long.parseLong(playerInfos[7]));

                playerList.add(p);
            }
        }
    }

    public static void putPlayerListToFile() throws Exception {
        final String OUTPUT_FILE_NAME = "players.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {
            for (Player p : playerList) {}
        }
    }

    public List<Player> searchByName(String searchStr) {
        List<Player> searchResults = new ArrayList<>();
        for (Player p : playerList) {
            if (p.Name.trim().toLowerCase().contains(searchStr.trim().toLowerCase())) {
                searchResults.add(p);
            }
        }

        return searchResults;
    }

    public PlayerList() throws Exception {
        getPlayerListFromFile();
    }
    public static List<Player> getPlayerList() {
        return playerList;
    }

    public Player getPlayer(int i) {
        return playerList.get(i);
    }
}
