
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Player {

    String Name, Country, Club, Position;
    int age, jerseyNumber;
    double height;
    long salary;

    public Player() {
    }

    public Player(String Name, String Country, int age, double height, String Club, String Position, int jerseyNumber, long salary) {
        this.Name = Name;
        this.Country = Country;
        this.age = age;
        this.height = height;
        this.Club = Club;
        this.Position = Position;
        this.jerseyNumber = jerseyNumber;
        this.salary = salary;
    }

    public String getName() {
        return Name;
    }

    public String getCountry() {
        return Country;
    }

    public String getClub() {
        return Club;
    }

    public String getPosition() {
        return Position;
    }

    public int getAge() {
        return age;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public double getHeight() {
        return height;
    }

    public double getSalary() {
        return salary;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public void setClub(String Club) {
        this.Club = Club;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}

public class PlayerList {

    List<Player> playerList = new ArrayList<>();

    final void getPlayerListFromFile() throws Exception {
        final String INPUT_FILE_NAME = "resource/players.txt";
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
                p.setJerseyNumber(Integer.parseInt(playerInfos[6]));
                p.setSalary(Long.parseLong(playerInfos[7]));

                playerList.add(p);
            }
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

    public Player getPlayer(int i) {
        return playerList.get(i);
    }
}
