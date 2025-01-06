import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClubManagementHomepage {
    public TableColumn<Player, String> nameColumn_sell;
    public TableColumn<Player, String> heightColumn_sell;
    public TableColumn<Player, String> ageColumn_sell;
    public TableColumn<Player, String> countryColumn_sell;
    public TableColumn<Player, String> positionColumn_sell;
    public TableView<Player> sellTable;
    public Button logOutButton;
    public Button buyPlayerButton;
    public Button sellPlayerButton;
    public TableView<Player> buyTable;
    public TableColumn<Player, String> nameColumn_sold;
    public TableColumn<Player, String> heightColumn_sold;
    public TableColumn<Player, String> ageColumn_sold;
    public TableColumn<Player, String> countryColumn_sold;
    public TableColumn<Player, String> positionColumn_sold;
    private ClubManagementClient clubManagementClient;
    private String clubName;
    
    public void setClubManagementClient(ClubManagementClient clubManagementClient) {
        this.clubManagementClient = clubManagementClient;
    }
    
    public void init(String clubName) throws IOException {
        SetCellValueProperty(nameColumn_sell, heightColumn_sell, ageColumn_sell, countryColumn_sell, positionColumn_sell);
        SetCellValueProperty(nameColumn_sold, heightColumn_sold, ageColumn_sold, countryColumn_sold, positionColumn_sold);
        
        //Setting the columns as sortable
        nameColumn_sell.setSortable(true);
        heightColumn_sell.setSortable(true);
        ageColumn_sell.setSortable(true);
        countryColumn_sell.setSortable(true);
        positionColumn_sell.setSortable(true);
        nameColumn_sold.setSortable(true);
        heightColumn_sold.setSortable(true);
        ageColumn_sold.setSortable(true);
        countryColumn_sold.setSortable(true);
        positionColumn_sold.setSortable(true);
        
        loadClubPlayers(clubName);
        loadSoldPlayers();
        
        sellPlayerButton.setDisable(true);
        buyPlayerButton.setDisable(true);
        
        sellTable.getSelectionModel().selectedItemProperty().addListener((_, oldSelection, newSelection) -> {
            sellPlayerButton.setDisable(newSelection == null);
        });
        
        buyTable.getSelectionModel().selectedItemProperty().addListener((_, oldSelection, newSelection) -> {
            buyPlayerButton.setDisable(newSelection == null);
        });
    }
    
    private void SetCellValueProperty(TableColumn<Player, String> nameColumnSell, TableColumn<Player, String> heightColumnSell, TableColumn<Player, String> ageColumnSell, TableColumn<Player, String> countryColumnSell, TableColumn<Player, String> positionColumnSell) {
        nameColumnSell.setCellValueFactory(new PropertyValueFactory<>("name"));
        heightColumnSell.setCellValueFactory(new PropertyValueFactory<>("height"));
        ageColumnSell.setCellValueFactory(new PropertyValueFactory<>("age"));
        countryColumnSell.setCellValueFactory(new PropertyValueFactory<>("country"));
        positionColumnSell.setCellValueFactory(new PropertyValueFactory<>("position"));
    }
    
    public void tableRefresh() throws IOException {
        Platform.runLater(() -> {
            try {
                loadClubPlayers(clubName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                loadSoldPlayers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sellTable.refresh();
            buyTable.refresh();
        });
    }
    
    public void failedOperationAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        ButtonType customButton = new ButtonType("ঠিক আছে");
        alert.getButtonTypes().setAll(customButton);
        alert.setTitle("Requested Operation Failed");
        alert.setHeaderText("এই মুহূর্তে অনুরোধটি বাস্তবায়ন সম্ভব নয়");
        alert.setContentText("অনুগ্রহ করে পেজটি রিফ্রেশ করে পুনারায় চেষ্টা করুন।");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("nord-dark.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        alert.showAndWait();
    }
    
    @FXML
    private void sellPlayer() {
//        System.out.println("sellPlayer");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Sale");
        alert.setHeaderText("বিক্রয় নিশ্চিত করুন");
        alert.setContentText("আপনি কি " + sellTable.getSelectionModel().getSelectedItem().getName() + " কে বিক্রয় করতে চান?");
        
        ButtonType confirmButton = new ButtonType("হ্যাঁ, বিক্রয় করুন");
        ButtonType cancelButton = new ButtonType("না, বাতিল করুন");
        alert.getButtonTypes().setAll(confirmButton, cancelButton);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("nord-dark.css")).toExternalForm());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                Player player = sellTable.getSelectionModel().getSelectedItem();
                try {
                    clubManagementClient.getSocketWrapper().write("SELL_PLAYER, " + player.getName());
                    System.out.println("SELL PLAYER, " + player.getName() + " MESSAGE SENT"); //Debug message
                    Platform.runLater(() -> {
                        try {
                            loadClubPlayers(this.clubName);
                            loadSoldPlayers();
                        } catch (IOException e) {
                            System.err.println("Error: Could not refresh tables. " + e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Error: Could not write to socket. " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void buyPlayer(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Purchase");
        alert.setHeaderText("ক্রয় নিশ্চিত করুন");
        alert.setContentText("আপনি কি " + buyTable.getSelectionModel().getSelectedItem().getName() + " কে ক্রয় করতে চান?");
        
        ButtonType confirmButton = new ButtonType("হ্যাঁ, ক্রয় করুন");
        ButtonType cancelButton = new ButtonType("না, বাতিল করুন");
        alert.getButtonTypes().setAll(confirmButton, cancelButton);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("nord-dark.css")).toExternalForm());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                Player player = buyTable.getSelectionModel().getSelectedItem();
                try {
                    clubManagementClient.getSocketWrapper().write("BUY_PLAYER, " + player.getName() + ", " + clubName);
                    System.out.println("BUY PLAYER, " + player.getName() + ", " + clubName + " MESSAGE SENT"); //Debug message
                    Platform.runLater(() -> {
                        try {
                            loadClubPlayers(this.clubName);
                            loadSoldPlayers();
                        } catch (IOException e) {
                            System.err.println("Error: Could not refresh tables. " + e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Error: Could not write to socket. " + e.getMessage());
                }
            }
        });
    }
    
    private void loadSoldPlayers() throws IOException {
        List<Player> soldPlayers = new ArrayList<>();
        for (Player p : clubManagementClient.getAllPlayers()) {
            if (p.getClub().equals("MARKETPLACE")) {
                soldPlayers.add(p);
            }
        }
        
        ObservableList<Player> observableList = FXCollections.observableList(soldPlayers);
        buyTable.setItems(observableList);
        buyTable.refresh();
        buyTable.getSortOrder().add(nameColumn_sold);
        buyTable.sort();
    }
    
    private void loadClubPlayers(String clubName) throws IOException {
        List<Player> players = new ArrayList<>();
        for (Player player : clubManagementClient.getAllPlayers()) {
            if (player.getClub().trim().equals(clubName)) {
                players.add(player);
            }
        }
        
        ObservableList<Player> observableList = FXCollections.observableList(players);
        sellTable.setItems(observableList);
        sellTable.refresh();
        sellTable.getSortOrder().add(nameColumn_sell);
        sellTable.sort();
    }
    
    public ClubManagementClient getClubManagementClient() {
        return clubManagementClient;
    }
    
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
    
    public String getClubName() {
        return clubName;
    }
    
    @FXML
    private void logOutAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("club_management_login.fxml"));
        Parent root = fxmlLoader.load();
        ClubManagementLogin controller = fxmlLoader.getController();
        controller.setClient(clubManagementClient);
        stage.setTitle("Club Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    public void handleRefresh(ActionEvent actionEvent) throws IOException {
        clubManagementClient.getSocketWrapper().write("CLIENT: REQUESTED_REFRESH");
    }
}
