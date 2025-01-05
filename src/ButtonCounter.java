import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonCounter extends Application implements ActionListener {
    private AtomicInteger counter = new AtomicInteger(0);
    
    private void buttonAction (Button button) {
        button.setText("Button - " + counter.incrementAndGet());
    }
    @Override
    public void start (Stage primaryStage) throws Exception {
        primaryStage.setTitle("UI Control");
        primaryStage.setResizable(false);
        Button button = new Button("Button - " + 0);
        button.setOnAction(e -> {buttonAction(button);});
//        button.setText("Button");
        StackPane layout = new StackPane();
        layout.getChildren().add(button);
        
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
//        if (e == buttonAction();)
        
    }
}
