import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadThread implements Runnable {
    private final ClubManagementClient client;
    private final Thread thread;
    private boolean running = true;
    
    public ReadThread(ClubManagementClient client) {
        this.client = client;
        thread = new Thread(this);
        thread.start();
    }
    
    public void stopThread() {
        running = false;
        thread.interrupt();
    }
    
    public void run() {
        while (running) {
            try {
                Object receivedObj = client.getSocketWrapper().read();
                handleReceivedObject(receivedObj);
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    System.err.println("Error: Could not read from socket. " + e.getMessage());
                } else {
                    System.out.println("Read thread stopped.");
                }
                break;
            }
        }
    }
    
    private void handleReceivedObject(Object receivedObj) throws IOException, ClassNotFoundException {
        if (receivedObj instanceof LoginDTO loginDTO) {
            handleLoginDTO(loginDTO);
        } else if (receivedObj instanceof String receivedStr) {
            handleStringRequest(receivedStr);
        } else if (receivedObj instanceof Object[] receivedData) {
            if (receivedData.length == 2) {
                if ("SERVER: REQUESTED_REFRESH_ACCEPTED".equals(receivedData[0])) {
                    Platform.runLater(() -> {
                        client.setAllPlayers((List<Player>) receivedData[1]);
                        try {
                            client.refreshTables();
                        } catch (IOException e) {
                            System.err.println("Error: Could not refresh tables. " + e.getMessage());
                        }
                    });
                }
            }
        }
    }
    
    private void handleLoginDTO(LoginDTO loginDTO) {
        Platform.runLater(() -> {
            if (loginDTO.isStatus()) {
                System.out.println("Logged in as " + loginDTO.getUsername());
                try {
                    client.showHomePage(loginDTO.getUsername());
                } catch (IOException e) {
                    System.err.println("Error: Could not show home page. " + e.getMessage());
                }
            } else {
                System.out.println(loginDTO.getUsername() + " is not logged in");
                client.showAlert();
            }
        });
    }
    
    private void handleStringRequest(String receivedStr) throws IOException, ClassNotFoundException {
        if ("SEND_ALL_PLAYERS_LIST".equals(receivedStr)) {
            try {
                Object receivedObj2 = client.getSocketWrapper().read();
                if (receivedObj2 instanceof List<?> rawList) {
                    List<Player> playerList = new ArrayList<>();
                    for (Object playerObj : rawList) {
                        if (playerObj instanceof Player p) {
                            playerList.add(p);
                        }
                    }
                    Platform.runLater(() -> {
                        client.setAllPlayers(playerList);
                        for (Player player : playerList) {//Debug message
                            System.out.println(player.getName() + ", " + player.getClub());
                        }
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error: Could not read player list from socket. " + e.getMessage());
            }
        } else if ("PLAYER_SOLD".equals(receivedStr)) {
            Object soldPlayerObj = client.getSocketWrapper().read();
            if (soldPlayerObj instanceof Player soldPlayer) {
                System.out.println("soldPlayerObj instanceof Player soldPlayer");
                for (Player p : client.getAllPlayers()) {
                    if (p.getName().trim().equals(soldPlayer.getName().trim())) {
                        p.setClub("MARKETPLACE");
                        System.out.println(p.getName() + ", " + p.getClub() + " SOLD");
                        client.refreshTables();
                    }
                }
            } else {
                System.out.println("soldPlayerObj is not an instanceof Player soldPlayer");
            }
        } else if (receivedStr.startsWith("SERVER: PLAYER_BOUGHT, ")) {
            String playerName = receivedStr.split(", ")[1];
            String clubName = receivedStr.split(", ")[2];
            for (Player p : client.getAllPlayers()) {
                if (p.getName().trim().equals(playerName)) {
                    p.setClub(clubName);
                    System.out.println(p.getName() + ", " + p.getClub() + " BOUGHT BY THE CLIENT");
                    client.refreshTables();
                    break;
                }
            }
        } else if ("PLAYER COULD NOT BE UPDATED IN SERVER".equals(receivedStr)) {
            client.getClubManagementHomepage().failedOperationAlert();
        }
    }
    
    
}