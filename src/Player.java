import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Player {

    String Name, Country, Club, Position;
    int age, jerseyNumber;
    double height;
    long salary;

    public static List<Player> PlayerList = new ArrayList<>();

    public static void readPlayerListFromFile(String filePath) throws Exception {
        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            String[] playerInfos;
            while ((line = br.readLine()) != null) {
                playerInfos = line.split(",");
                Player p = new Player();
                p.setName(playerInfos[0]);
                p.setCountry(playerInfos[1]);
                p.setAge(Integer.parseInt(playerInfos[2]));
                p.setHeight(Double.parseDouble(playerInfos[3]));
                p.setClub(playerInfos[4]);
                p.setPosition(playerInfos[5]);
                p.setJerseyNumber(playerInfos[6].isEmpty() ? -1 : Integer.parseInt(playerInfos[6]));
                p.setSalary(Long.parseLong(playerInfos[7]));

                PlayerList.add(p);
            }
        }
    }

    public static void writePlayerListToFile(String filePath) throws Exception {
        File outputFile = new File(filePath); // File object for "resources/players.txt"
        if (!outputFile.exists()) {
            outputFile.createNewFile(); // Create the file if it doesn't exist
        }

        DecimalFormat df = new DecimalFormat("#");
        String line;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (Player p : PlayerList) {
                line = p.getName() + "," +
                        p.getCountry() + "," +
                        p.getAge() + "," +
                        p.getHeight() + "," +
                        p.getClub() + "," +
                        p.getPosition() + "," +
                        p.getJerseyNumber() + "," +
                        df.format(p.getSalary());
                bw.write(line);
                bw.newLine();
            }
        }
    }

    @org.jetbrains.annotations.NotNull
    public static List<Player> SearchByName(String name) {
        List<Player> searchResults = new ArrayList<>();
        for (Player p : PlayerList) {
            if (p.getName().trim().toLowerCase().contains(name.trim().toLowerCase())) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }

    public static @NotNull List<Player> SearchByCountryAndClub(String country, String club) {
        List<Player> searchResults = new ArrayList<>();
        for (Player p : PlayerList) {
            if (p.getCountry().trim().equalsIgnoreCase(country.trim()) && (p.getClub().trim().equalsIgnoreCase(club.trim()) || club.equalsIgnoreCase("ANY"))) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }

    public static @NotNull List<Player> SearchByPosition(String position) {
        List<Player> searchResults = new ArrayList<>();
        for (Player p : Player.PlayerList) {
            if (p.getPosition().trim().equalsIgnoreCase(position)) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }

    public static @NotNull List<Player> SearchBySalaryRange(long lower, long upper) {
        List<Player> searchResults = new ArrayList<>();
        for (Player p : Player.PlayerList) {
            if (p.getSalary() >= lower && p.getSalary() <= upper) {
                searchResults.add(p);
            }
        }
        return searchResults;
    }

    public static @NotNull Map<String, Integer> CountryWisePlayerCount() {
        Map<String, Integer> countryPlayerCountMap = new TreeMap<>();
        for (Player p : PlayerList) {
            countryPlayerCountMap.put(p.getCountry(), countryPlayerCountMap.getOrDefault(p.getCountry(), 0) + 1);
        }

        return countryPlayerCountMap;
    }

    public static @NotNull List<Player> MaxPaidPlayersInClub(String club) {
        List<Player> maxPaidPlayers = new ArrayList<>();
        long maxWeeklySalary = -1;

        for (Player p : PlayerList) {
            if (p.getSalary() > maxWeeklySalary && p.getClub().trim().equalsIgnoreCase(club.trim())) {
                maxPaidPlayers.clear();
                maxPaidPlayers.add(p);
                maxWeeklySalary = p.getSalary();
            } else if (p.getSalary() == maxWeeklySalary && p.getClub().trim().equalsIgnoreCase(club.trim())) {
                maxPaidPlayers.add(p);
            }
        }
        return maxPaidPlayers;
    }

    public static @NotNull List<Player> OldestPlayersInClub(String club) {
        List<Player> oldestPlayers = new ArrayList<>();
        int maxAge = -1;

        for (Player p : PlayerList) {
            if (p.getAge() > maxAge && p.getClub().trim().equalsIgnoreCase(club.trim())) {
                oldestPlayers.clear();
                oldestPlayers.add(p);
                maxAge = p.getAge();
            } else if (p.getAge() == maxAge && p.getClub().trim().equalsIgnoreCase(club.trim())) {
                oldestPlayers.add(p);
            }
        }
        return oldestPlayers;
    }

    public static @NotNull List<Player> TallestPlayersInClub(String club) {
        List<Player> tallestPlayers = new ArrayList<>();
        double maxHeight = -1;

        for (Player p : PlayerList) {
            if (p.getHeight() > maxHeight && p.getClub().trim().equalsIgnoreCase(club.trim())) {
                tallestPlayers.clear();
                tallestPlayers.add(p);
                maxHeight = p.getHeight();
            } else if (p.getHeight() == maxHeight && p.getClub().trim().equalsIgnoreCase(club.trim())) {
                tallestPlayers.add(p);
            }
        }
        return tallestPlayers;
    }

    public static long TotalSalary(String club) {
        long totalSalary = -1;
        for (Player p : PlayerList) {
            if (p.getClub().trim().equalsIgnoreCase(club.trim())) {
                if (totalSalary == -1) {
                    totalSalary = p.getSalary();
                } else {
                    totalSalary += p.getSalary();
                }
            }
        }
        return totalSalary * 52;
    }

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

    //Console-based function
    public void display() {
        System.out.println("Name: " + Name);
        System.out.println("Country: " + Country);
        System.out.println("Age: " + age + " years");
        System.out.println("Height: " + height + " meters");
        System.out.println("Club: " + Club);
        System.out.println("Position: " + Position);
        if (jerseyNumber != -1) {
            System.out.println("Jersey Number: " + jerseyNumber);
        }
        System.out.println("Salary: " + salary + "\n");
    }

    //Console-based function
    public static void displaySearchResults(@NotNull List<Player> playerList) {
        int i = 0;
        for (Player p : playerList) {
            System.out.printf("Result %02d out of %02d:\n", ++i, playerList.size());
            p.display();
        }
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

    public long getSalary() {
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
