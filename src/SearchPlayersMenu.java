import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchPlayersMenu {
    public TextField searchByPositionTextField;
    public TextField upperSalaryRange;
    public TextField lowerSalaryRange;
    @FXML
    private TableView<Player> playersTableView;
    
    @FXML
    private TableColumn<Player, String> nameColumn;
    
    @FXML
    private TableColumn<Player, Integer> ageColumn;
    
    @FXML
    private TableColumn<Player, Double> heightColumn;
    
    @FXML
    private TableColumn<Player, String> countryColumn;
    
    @FXML
    private TableColumn<Player, String> clubColumn;
    
    @FXML
    private TableColumn<Player, String> positionColumn;
    
    @FXML
    private TableColumn<Player, String> jerseyColumn;
    
    @FXML
    private TableColumn<Player, Long> salaryColumn;
    
    @FXML
    private Button searchButton_CnC;
    
    @FXML
    private TextField searchByNameTextField;
    
    @FXML
    private ComboBox<String> countryDropdown, clubDropdown;
    
    @FXML
    public void initialize () {
        searchButton_CnC.setDisable(true);
        
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
        
        loadPlayers(Player.PlayerList);
        countryDropdown.setOnAction(_->populateClubDropdown());
        countryDropdown.valueProperty().addListener((_, _, _)->{
            searchButton_CnC.setDisable(false);
            clubDropdown.setValue("ANY");
            populateClubDropdown();
        });
        populateCountryDropdown();
        
        // "\\d*" is a regular expression that matches one or more digits.
        lowerSalaryRange.textProperty().addListener((_, oldValue, newValue)->{
            if (!newValue.matches("\\d*")) {
                lowerSalaryRange.setText(oldValue);
            }
        });
        
        upperSalaryRange.textProperty().addListener((_, oldValue, newValue)->{
            if (!newValue.matches("\\d*")) {
                upperSalaryRange.setText(oldValue);
            }
        });
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
    
    private void loadPlayers (List<Player> players) {
        ObservableList<Player> playersData = FXCollections.observableArrayList(players);

        playersTableView.setItems(playersData);
        
        playersTableView.getSortOrder().add(nameColumn);
        playersTableView.sort();
    }
    
    public void searchByName_ButtonClicked (MouseEvent ignoredMouseEvent) {
        String searchName = searchByNameTextField.getText().toLowerCase().trim();
        loadPlayers(Player.SearchByName(searchName));
    }
    
    public void searchByName (ActionEvent ignoredEvent) {
        String searchName = searchByNameTextField.getText().toLowerCase().trim();
        loadPlayers(Player.SearchByName(searchName));
    }
    
    public void backButtonClicked () throws IOException {
        MenuFX.showMainMenu();
    }
    
    private void populateCountryDropdown () {
        List<String> countries = Player.PlayerList.stream().map(Player::getCountry).distinct().sorted().collect(Collectors.toList());
        
        countries.addFirst("ANY");
        ObservableList<String> countryList = FXCollections.observableArrayList(countries);
        this.countryDropdown.setItems(countryList);
        
        countryDropdown.setPromptText("দেশ নির্বাচন করুন");
    }
    
    //Club Dropdown list depends on the country that is selected.
    private void populateClubDropdown () {
        String selectedCountry = countryDropdown.getValue();
        
        List<String> clubs = Player.PlayerList.stream().filter(player->"ANY".equals(selectedCountry) || player.getCountry().equals(selectedCountry)).map(Player::getClub).distinct().sorted().collect(Collectors.toList());
        clubs.addFirst("ANY");
        
        ObservableList<String> clubList = FXCollections.observableList(clubs);
        clubDropdown.setItems(clubList);
        
        clubDropdown.setPromptText("ক্লাব নির্বাচন করুন");
    }
    
    //"CnC" stands for "Country and Club"
    public void searchByCnC (MouseEvent ignoredMouseEvent) {
        loadPlayers(Player.SearchByCountryAndClub(countryDropdown.getValue(), clubDropdown.getValue()));
    }
    
    public void searchByPosition_ButtonClicked (MouseEvent ignoredMouseEvent) {
        String searchPosition = searchByPositionTextField.getText().toLowerCase().trim();
        loadPlayers(Player.SearchByPosition(searchPosition));
    }
    
    public void searchByPosition (ActionEvent ignoredActionEvent) {
        String searchPosition = searchByPositionTextField.getText().toLowerCase().trim();
        loadPlayers(Player.SearchByPosition(searchPosition));
    }
    
    public void searchBySalaryRange (ActionEvent ignoredActionEvent) {
        salaryRangeSearch();
    }
    
    private void salaryRangeSearch () {
        long upperRange, lowerRange;
        try {lowerRange = Long.parseLong(lowerSalaryRange.getText());} catch (NumberFormatException e) {lowerRange = 0;}
        try {upperRange = Long.parseLong(upperSalaryRange.getText());} catch (NumberFormatException e) {
            upperRange = Long.MAX_VALUE;
        }
        loadPlayers(Player.SearchBySalaryRange(lowerRange, upperRange));
    }
    
    public void searchBySalaryRange_ButtonClicked (MouseEvent ignoredMouseEvent) {
        salaryRangeSearch();
    }
    
    public void countryWisePlayerCount_ButtonClicked (MouseEvent ignoredMouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("country_wise_player_count.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Stage stage = new Stage();
        stage.setTitle("Country-wise player count");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
