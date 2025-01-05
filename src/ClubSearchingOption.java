import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//Controller for the club_searching_option.fxml file.
public class ClubSearchingOption {
    public Button nextButton;
    public RadioButton maxSalary;
    public RadioButton maxAge;
    public RadioButton maxHeight;
    public RadioButton totalYearlyIncome;
    public ComboBox<String> clubsComboBox;
    ToggleGroup group;
    
    public void initialize () {
        nextButton.setDisable(true);
        
        group = new ToggleGroup();
        maxSalary.setToggleGroup(group);
        maxAge.setToggleGroup(group);
        maxHeight.setToggleGroup(group);
        totalYearlyIncome.setToggleGroup(group);
        
        populateClubComboBox();
        
        group.selectedToggleProperty().addListener((_, _, _) -> checkConditions());
        clubsComboBox.valueProperty().addListener((_, _, _) -> checkConditions());
    }
    
    private void checkConditions () {
        boolean isRadioSelected = group.getSelectedToggle() != null;
        boolean isClubSelected = clubsComboBox.getValue() != null;
        
        nextButton.setDisable(!isRadioSelected || !isClubSelected);
    }
    
    private void populateClubComboBox() {
        List<String> clubs = Player.PlayerList.stream().map(Player::getClub).distinct().sorted().collect(Collectors.toList());
        ObservableList<String> options = FXCollections.observableList(clubs);
        clubsComboBox.setItems(options);
    }
    
    public void backButtonClicked (MouseEvent ignoredMouseEvent) {
        try {
            MenuFX.showMainMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void nextPageButtonClicked (ActionEvent ignoredActionEvent) throws IOException {
        loadNextPage((RadioButton) group.getSelectedToggle(), clubsComboBox.getValue());
    }
    
    //Next page loading depends on which club is selected and which radio button is selected.
    private void loadNextPage (RadioButton selectedRadioButton, String selectedClub) throws IOException {
        ClubSearchResult.clubName = selectedClub;
        if(selectedRadioButton == maxSalary) {
            ClubSearchResult.selectedRadioButton = 0;
        }
        else if(selectedRadioButton == maxAge) {
            ClubSearchResult.selectedRadioButton = 1;
        }
        else if(selectedRadioButton == maxHeight) {
            ClubSearchResult.selectedRadioButton = 2;
        }
        else if(selectedRadioButton == totalYearlyIncome) {
            showTotalYearlySalary(selectedClub);
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("club_search_result.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) nextButton.getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Club Search Result");
        stage.show();
    }
    
    private void showTotalYearlySalary (String selectedClub) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        ButtonType okButton = new ButtonType("ঠিক আছে");
        alert.getButtonTypes().setAll(okButton);
        alert.setTitle("Total yearly salary: "+selectedClub);
        alert.setHeaderText(selectedClub+" ক্লাবের মোট বার্ষিক বেতন");
        alert.setContentText("মোট বার্ষিক বেতন: $"+Player.TotalSalary(selectedClub));
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("alert_style.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        alert.showAndWait();
    }
}
