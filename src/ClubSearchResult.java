import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Objects;

public class ClubSearchResult {
    public static String clubName;
    public static int selectedRadioButton;
    public Label title;
    public TableView<Player> playersTableView;
    public TableColumn<Player, String> nameColumn;
    public TableColumn<Player, String> countryColumn;
    public TableColumn<Player, String> ageColumn;
    public TableColumn<Player, String> heightColumn;
    public TableColumn<Player, String> clubColumn;
    public TableColumn<Player, String> positionColumn;
    public TableColumn<Player, String> jerseyColumn;
    public TableColumn<Player, String> salaryColumn;
    
    public void initialize () {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        jerseyColumn.setCellValueFactory(new PropertyValueFactory<>("jerseyNumber"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        
        nameColumn.setSortable(true); // Allow the column to be sorted manually
        nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        countryColumn.setSortable(true);
        countryColumn.setSortType(TableColumn.SortType.ASCENDING);
        ageColumn.setSortable(true);
        ageColumn.setSortType(TableColumn.SortType.ASCENDING);
        heightColumn.setSortable(true);
        heightColumn.setSortType(TableColumn.SortType.ASCENDING);
        clubColumn.setSortable(true);
        clubColumn.setSortType(TableColumn.SortType.ASCENDING);
        positionColumn.setSortable(true);
        positionColumn.setSortType(TableColumn.SortType.ASCENDING);
        jerseyColumn.setSortable(false);
//        jerseyColumn.setSortType(TableColumn.SortType.ASCENDING);
        salaryColumn.setSortable(true);
        salaryColumn.setSortType(TableColumn.SortType.ASCENDING);
        
        switch (selectedRadioButton) {
            case 0:
                title.setText(clubName+" ক্লাবের সর্বোচ্চ বেতনের খেলোয়াড়দের তালিকা");
                loadPlayers(Player.MaxPaidPlayersInClub(clubName));
                break;
            case 1:
                title.setText(clubName+" ক্লাবের সবচেয়ে বয়স্ক খেলোয়াড়দের তালিকা");
                loadPlayers(Player.OldestPlayersInClub(clubName));
                break;
            case 2:
                title.setText(clubName+" ক্লাবের সর্বোচ্চ উচ্চতার খেলোয়াড়দের তালিকা");
                loadPlayers(Player.TallestPlayersInClub(clubName));
                break;
            default:
                break;
        }
        
        playersTableView.setOnMouseClicked(event->{
            if (event.getClickCount() == 2) {
                Player selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    showPlayerDetails(selectedPlayer);
                }
            }
        });
        
        playersTableView.setOnKeyPressed(event->{
            if (event.getCode() == KeyCode.ENTER) {
                Player selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    showPlayerDetails(selectedPlayer);
                }
            }
        });
    }
    
    public void handleBackButton (ActionEvent ignoredActionEvent) {MenuFX.showSearchClubsMenu();}
    
    private void loadPlayers (List<Player> players) {
        ObservableList<Player> playersData = FXCollections.observableArrayList(players);
        
        playersTableView.setItems(playersData);
        
        playersTableView.getSortOrder().add(nameColumn);
        playersTableView.sort();
    }
    
    private void showPlayerDetails (Player selectedPlayer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        ButtonType customButton = new ButtonType("ঠিক আছে");
        alert.getButtonTypes().setAll(customButton);
        
        alert.setTitle("Player Details");
        alert.setHeaderText(selectedPlayer.getName()+" এর তথ্য");
        alert.setContentText("নাম: "+selectedPlayer.getName()+"\n"+"দেশ: "+selectedPlayer.getCountry()+"\n"+"বয়স: "+selectedPlayer.getAge()+"\n"+"উচ্চতা: "+selectedPlayer.getHeight()+" মিটার\n"+"ক্লাব: "+selectedPlayer.getClub()+"\n"+"অবস্থান: "+selectedPlayer.getPosition()+"\n"+(selectedPlayer.getJerseyNumber().isEmpty() ? "" : ("জার্সি নম্বর: "+selectedPlayer.getJerseyNumber()+"\n"))+"সাপ্তাহিক বেতন: $"+selectedPlayer.getSalary());
        
        DialogPane dialogPane = alert.getDialogPane();
//        dialogPane.setPrefSize(400, 200);
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("alert_style.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        
        alert.showAndWait();
    }
}
