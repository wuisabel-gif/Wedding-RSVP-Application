package app;

import controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.FontLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        FontLoader.loadFonts();

        MainController controller = new MainController(primaryStage);
        Scene scene = new Scene(controller.getView(), 1200, 900);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

        primaryStage.setTitle("Wedding RSVP Guest Management System");
        primaryStage.setMinWidth(1040);
        primaryStage.setMinHeight(740);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
