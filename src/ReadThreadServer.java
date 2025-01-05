import util.SocketWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ReadThreadServer implements Runnable {
    private final SocketWrapper socketWrapper;
    private final HashMap<String, String> clubsMap;
    private final List<Player> availablePlayers;
    private final List<Player> allPlayersList;
    private volatile boolean running = true;
    
    public ReadThreadServer(HashMap<String, String> map, List<Player> availablePlayers, SocketWrapper socketWrapper, List<Player> allPlayersList) {
        this.clubsMap = map;
        this.availablePlayers = availablePlayers;
        this.socketWrapper = socketWrapper;
        this.allPlayersList = allPlayersList;
        Thread thread = new Thread(this);
        thread.start();
    }
    
    public void stopThread() {
        running = false;
        try {
            socketWrapper.closeConnection(); // Ensure socket is closed when stopping the thread
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                Object receivedObject = socketWrapper.read();
                if (receivedObject != null) {
                    handleReceivedObject(receivedObject);
                }
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    System.err.println("Error: Could not read from socket. " + e.getMessage());
                }
                stopThread(); // Stop thread on error
            }
        }
    }
    
    private void handleReceivedObject(Object receivedObject) {
        switch (receivedObject) {
            case LoginDTO loginDTO -> handleLoginRequest(loginDTO);
            case String requestStr -> {
                try {
                    handleStringRequest(requestStr);
                } catch (IOException e) {
                    System.err.println("Error handling string request: " + e.getMessage());
                }
            }
            case Player player -> handlePlayerRequest(player);
            default ->
                    System.err.println("Error: Unhandled object type received: " + receivedObject.getClass().getName());
        }
    }
    
    private void handleLoginRequest(LoginDTO loginDTO) {
        String password = clubsMap.get(loginDTO.getUsername());
        loginDTO.setStatus(password != null && password.equals(loginDTO.getPassword()));
        try {
            socketWrapper.write(loginDTO);
        } catch (IOException e) {
            System.err.println("Error writing LoginDTO to socket: " + e.getMessage());
        }
    }
    
    private void handleStringRequest(String requestStr) throws IOException {
        switch (requestStr) {
            case "DISCONNECT" -> {
                System.out.println("Disconnected: " + socketWrapper.getSocket().getInetAddress());
                stopThread(); // Stop thread on disconnect
            }
            case "REQUEST_ALL_PLAYERS_LIST" -> {
                try {
                    socketWrapper.write("SEND_ALL_PLAYERS_LIST");
                    socketWrapper.write(allPlayersList);
                } catch (IOException e) {
                    System.err.println("Error sending player list: " + e.getMessage());
                }
            }
            default -> handlePlayerMarketplace(requestStr);
        }
    }
    
    private void handlePlayerMarketplace(String requestStr) throws IOException {
        if (requestStr.startsWith("SELL_PLAYER, ")) {
            String playerName = requestStr.split(", ")[1];
            boolean playerUpdated = false;
            synchronized (allPlayersList) {
                for (Player player : allPlayersList) {
                    if (player.getName().trim().equalsIgnoreCase(playerName.trim()) && !"MARKETPLACE".equals(player.getClub())) {
                        player.setClub("MARKETPLACE");
                        playerUpdated = true;
                        break;
                    }
                }
            }
            if (playerUpdated) {
                socketWrapper.write("SEND_ALL_PLAYERS_LIST");
                socketWrapper.write(allPlayersList);
                for (Player player : allPlayersList) {
                    System.out.println(player.getClub());
                }
//                socketWrapper.write("SELL_PLAYER_SUCCESS");
            } else {
                socketWrapper.write("PLAYER_ALREADY_IN_MARKETPLACE");
            }
        }
    }
    
    private void handlePlayerRequest(Player player) {
        synchronized (availablePlayers) {
            if (!availablePlayers.contains(player)) {
                availablePlayers.add(player);
                try {
                    socketWrapper.write("PLAYER_ADDITION_SUCCEEDED");
                } catch (IOException e) {
                    System.err.println("Error writing player addition success: " + e.getMessage());
                }
                System.out.println("Player " + player.getName() + " is now available to be bought.");
            } else {
                try {
                    socketWrapper.write("PLAYER_ADDITION_FAILED");
                } catch (IOException e) {
                    System.err.println("Error writing player addition failure: " + e.getMessage());
                }
                System.out.println("Player " + player.getName() + " is already available to be bought.");
            }
        }
    }
}
