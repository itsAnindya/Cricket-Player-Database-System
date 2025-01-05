import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;

public class AddPlayersController {
    
    @FXML
    private TextField nameField, countryField, ageField, heightField, clubField, positionField, jerseyField, salaryField;
    
    @FXML
    private Button saveButton;
    
    public void initialize() {
        // Initially disable the Save button
        saveButton.setDisable(true);
        
        // Add listeners to required text fields
        nameField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        countryField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        ageField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        heightField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        clubField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        positionField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        salaryField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
    }
    
    private void checkFields () {
        boolean isNameEmpty = nameField.getText().trim().isEmpty();
        boolean isCountryEmpty = countryField.getText().trim().isEmpty();
        boolean isAgeInteger = isInteger(ageField.getText().trim());
        boolean isHeightDouble = isDouble(heightField.getText().trim());
        boolean isClubEmpty = clubField.getText().trim().isEmpty();
        boolean isPositionEmpty = positionField.getText().trim().isEmpty();
        boolean isSalaryLong = isLong(salaryField.getText().trim());
        
        saveButton.setDisable(isNameEmpty || isClubEmpty || !isAgeInteger || !isHeightDouble || isPositionEmpty || !isSalaryLong || isCountryEmpty);
    }
    
    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str); // Try parsing the string as an integer
            return true;           // Parsing succeeded
        } catch (NumberFormatException e) {
            return false;          // Parsing failed
        }
    }
    
    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str); // Try parsing the string as a double
            return true;           // Parsing succeeded
        } catch (NumberFormatException e) {
            return false;          // Parsing failed
        }
    }
    
    public boolean isLong(String str) {
        try {
            Long.parseLong(str); // Try parsing the string as a long
            return true;           // Parsing succeeded
        } catch (NumberFormatException e) {
            return false;          // Parsing failed
        }
    }
    
    private void handleDuplicate (Player player) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Player already exists");
        alert.setHeaderText("ডেটাবেইজ হালনাগাদ ব্যর্থ হয়েছে");
        alert.setContentText(player.getName() + " নামের একজন প্লেয়ার ইতোমধ্যে ডেটাবেইজে যুক্ত রয়েছে।\nঅনুগ্রহ করে ভিন্ন কোনো নাম টাইপ করুন।");
        changeButtonType(alert);
    }
    
    private void changeButtonType (Alert alert) {
        ButtonType customButton = new ButtonType("ঠিক আছে");
        alert.getButtonTypes().setAll(customButton);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(400, 200);
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        
        alert.showAndWait();
    }
    
    public void saveButtonClicked () {
        Player newPlayer = getNewPlayer();

        for (Player p : Player.PlayerList) {
            if (p.getName().trim().equalsIgnoreCase(newPlayer.getName().trim())) {
                handleDuplicate(p);
                return;
            }
        }
        
        Player.PlayerList.add(newPlayer);
        
        showAlert(newPlayer);
        
        clearFields();
        
    }
    
    private void showAlert(Player newPlayer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Player Added Successfully");
        alert.setHeaderText("ডেটাবেইজ হালনাগাদ হয়েছে");
        alert.setContentText(newPlayer.getName() + " - কে সফলভাবে ডেটাবেইজে যুক্ত করা হয়েছে।");
        changeButtonType(alert);
    }
    
    private Player getNewPlayer () {
        Player newPlayer = new Player();
        
        newPlayer.setName(nameField.getText().trim());
        newPlayer.setCountry(countryField.getText().trim());
        newPlayer.setAge(Integer.parseInt(ageField.getText()));
        newPlayer.setHeight(Double.parseDouble(heightField.getText()));
        newPlayer.setClub(clubField.getText().trim());
        newPlayer.setPosition(positionField.getText().trim());
        if (!jerseyField.getText().isEmpty()) {
            newPlayer.setJerseyNumber(Integer.parseInt(jerseyField.getText()));
        } else {
            newPlayer.setJerseyNumber(-1);
        }
        newPlayer.setSalary(Long.parseLong(salaryField.getText()));
        return newPlayer;
    }
    
    
    public void backButtonClicked () throws IOException {
        MenuFX.showMainMenu();
    }
    
    private void clearFields () {
        nameField.clear();
        countryField.clear();
        ageField.clear();
        heightField.clear();
        clubField.clear();
        positionField.clear();
        jerseyField.clear();
        salaryField.clear();
    }
    
}
