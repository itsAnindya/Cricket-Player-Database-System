import util.SocketWrapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private final ServerSocket serverSocket;
    private final HashMap<String, String> clubsMap;
    private static List<Player> availablePlayers;
    private List<Player> allPlayersList;
    
    private List<Player> readPlayerListFromFile(String filePath) throws Exception {
        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        List<Player> playerList = new ArrayList<>();
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
            
            playerList.add(p);
        }
        return playerList;
    }
    
    public void writePlayerListToFile(String filePath) throws Exception {
        File outputFile = new File(filePath); // File object for "resources/players.txt"
        if (!outputFile.exists()) {
            outputFile.createNewFile(); // Create the file if it doesn't exist
        }
        
        DecimalFormat df = new DecimalFormat("#");
        String line;
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (Player p : allPlayersList) {
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
    
    public HashMap<String, String> readCredentials(String filepath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
        HashMap<String, String> clubMap = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            clubMap.put(line.split(", ")[0].trim(), line.split(", ")[1]);
        }
        bufferedReader.close();
        return clubMap;
    }
    
    public void writeCredentials(String filepath) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
        for (HashMap.Entry<String, String> entry : clubsMap.entrySet()) {
            bufferedWriter.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }
        bufferedWriter.close();
    }
    
    public void synchronizePlayerLists(String filepath) throws IOException {
        Player.PlayerList = allPlayersList;
        writeCredentials(filepath);
    }
    
    Server(int port, String clubCredentialFilePath, String playerListFilePath) throws Exception {
        serverSocket = new ServerSocket(port);
        clubsMap = readCredentials(clubCredentialFilePath);
        allPlayersList = readPlayerListFromFile(playerListFilePath);
        
        System.out.println("Server started, listening on port " + port);
        if (clubsMap != null) {
            System.out.println("Club hashmap loaded.");
        }
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            serve(clientSocket);
        }
    }
    
    private void serve(Socket clientSocket) throws IOException {
        SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
        System.out.println("New client connected: " + clientSocket.getInetAddress());
        new ReadThreadServer(clubsMap, availablePlayers, socketWrapper, allPlayersList);
    }
    
    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {
            new Server(44444, "src/resources/clubs.csv", "src/resources/players.txt");
        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
    }
    
    public HashMap<String, String> getClubsMap() {
        return clubsMap;
    }
}
