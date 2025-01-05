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
    
    private void handleReceivedObject(Object receivedObj) {
        if (receivedObj instanceof LoginDTO loginDTO) {
            handleLoginDTO(loginDTO);
        } else if (receivedObj instanceof String receivedStr) {
            handleStringRequest(receivedStr);
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
    
    private void handleStringRequest(String receivedStr) {
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
                        for (Player player : playerList) {
                            System.out.println(player.getClub());
                        }
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error: Could not read player list from socket. " + e.getMessage());
            }
        }
    }
    
    
}