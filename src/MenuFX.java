import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuFX extends Application {
    public static void showSearchPlayersMenu () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuFX.class.getResource("search_players_menu.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(Objects.requireNonNull(MenuFX.class.getResource("tab_style.css")).toExternalForm());
        stage.setTitle("Search Players");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void showSearchClubsMenu () {
        FXMLLoader loader = new FXMLLoader(MenuFX.class.getResource("club_searching_option.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        stage.setTitle("Search Clubs");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private static Stage stage;
    
    public static void showMainMenu () throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MenuFX.class.getResource("mainmenu.fxml")));
        
        stage.setTitle("Main Menu");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        
        stage.show();
    }
    
    public static void showAddPlayersMenu () throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MenuFX.class.getResource("addplayers.fxml")));
        stage.setTitle("Add Players");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    
    
    @Override
    public void init () throws Exception {
        Player.readPlayerListFromFile("src/resources/players.txt");
    }
    
    @Override
    public void start (Stage primaryStage) throws IOException {
        stage = primaryStage;
        showMainMenu();
    }
    
    @Override
    public void stop () {
        stage.close();
        try {
            Player.writePlayerListToFile("src/resources/players.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main (String[] args) {
        launch(args);
    }
}
