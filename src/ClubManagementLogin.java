import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


//Controller for the club management window
public class ClubManagementLogin {
    
    public TextField clubUsername;
    public PasswordField passwordField;
    public Button exitButton;
    private ClubManagementClient client = new ClubManagementClient();
    
    public void handleLogin (ActionEvent actionEvent) throws IOException {
        LoginDTO loginDTO = new LoginDTO(clubUsername.getText(), passwordField.getText());
        client.sendLoginData(loginDTO);
    }
    
    public void handleReset () {
        passwordField.setText(null);
        clubUsername.setText(null);
    }
    
    public void handleExit (ActionEvent ignoredActionEvent) throws IOException {
        client.disconnect();
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    public void setClient (ClubManagementClient client) {
        this.client = client;
    }
    
    public ClubManagementClient getClient () {
        return client;
    }
}
