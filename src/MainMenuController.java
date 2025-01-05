import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MainMenuController {
    @FXML
    public void addPlayersButtonClicked () throws IOException {MenuFX.showAddPlayersMenu();}
    
    public void searchPlayersButtonClicked () throws IOException {MenuFX.showSearchPlayersMenu();}
    
    public void handleClubManagement() throws IOException {
        ClubManagementClient clubManagementClient = new ClubManagementClient();
        clubManagementClient.begin();
    }
    
    @FXML
    public void exitButtonClicked () {
        Platform.exit();
    }
    
    public void clubSearchMenuButtonClicked (MouseEvent ignoredMouseEvent) {
        MenuFX.showSearchClubsMenu();
    }
}
