import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import util.SocketWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ClubManagementClient {
    private SocketWrapper socketWrapper;
    private Stage stage;
    ReadThread readThread;
    private List<Player> allPlayers;
    private ClubManagementHomepage clubManagementHomepage;
    
    public ClubManagementHomepage getClubManagementHomepage() {
        return clubManagementHomepage;
    }
    
    public void setAllPlayers(List<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }
    
    public List<Player> getAllPlayers() {
        return allPlayers;
    }
    
    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }
    
    //Connecting to a server
    public void connect(String host, int port) throws IOException {
        socketWrapper = new SocketWrapper(host, port);
        readThread = new ReadThread(this);
        socketWrapper.write("REQUEST_ALL_PLAYERS_LIST");
    }
    
    public void sendLoginData(String username, String password) throws IOException {
        LoginDTO loginDTO = new LoginDTO(username, password);
        socketWrapper.write(loginDTO);
    }
    
    public Object receiveResponse() throws IOException, ClassNotFoundException {
        return socketWrapper.read();
    }
    
    public void disconnect() throws IOException {
        socketWrapper.write("DISCONNECT");
        socketWrapper.closeConnection();
        readThread.stopThread();
    }
    
    public void begin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("club_management_login.fxml"));
        Parent root = loader.load();
        
        ClubManagementLogin controller = loader.getController();
        
        controller.setClient(this);
        this.connect("localhost", 44444);
        
        socketWrapper.write("REQUEST_ALL_PLAYERS_LIST");
        
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("Club Management");
        stage.setScene(scene);
        
        stage.setOnCloseRequest(event -> {
            try {
                disconnect();
            } catch (IOException e) {
                System.err.println("Error: Failed to disconnect from server. " + e.getMessage());
            }
        });
        
        stage.show();
    }
    
    public void sendLoginData(LoginDTO loginDTO) throws IOException {
        socketWrapper.write(loginDTO);
    }
    
    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        ButtonType customButton = new ButtonType("ঠিক আছে");
        alert.getButtonTypes().setAll(customButton);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("লগইন ব্যর্থ হয়েছে");
        alert.setContentText("ক্লাবের নাম অথবা পাসওয়ার্ড লিখতে ভুল হয়েছে।");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("nord-dark.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        alert.showAndWait();
    }
    
    public void showHomePage(String clubName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("club_management_homepage.fxml"));
        Parent root = loader.load();
        
        clubManagementHomepage = loader.getController();
        clubManagementHomepage.setClubManagementClient(this);
        clubManagementHomepage.setClubName(clubName);
        clubManagementHomepage.init(clubName);
        
        stage.setScene(new Scene(root));
        stage.setTitle("Club Management Home");
        stage.show();
    }
    
    public void refreshTables() throws IOException {
        clubManagementHomepage.tableRefresh();
    }
    
}
