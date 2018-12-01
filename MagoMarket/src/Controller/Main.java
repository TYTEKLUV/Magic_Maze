package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    private ControllerFXML gameWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameWindow = loadFXML(primaryStage, "../View/FieldEditor.fxml", "Magic Maze", 1280, 720, true);
//        Parent root = FXMLLoader.load(getClass().getResource("../View/FieldEditor.fxml"));
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        Scene scene = new Scene(root, 1280, 720);
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(true);
//        primaryStage.setMinWidth(1280);
//        primaryStage.setMinHeight(720);
//        primaryStage.show();
    }

    private ControllerFXML loadFXML(Stage stage, String file, String title, double width, double height, boolean show) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        Parent root = loader.load();
        ControllerFXML controller = loader.getController();
        controller.setMain(this);
        controller.setStage(stage);
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.getScene().setCursor(Cursor.DEFAULT);
        if (show) stage.show();
        return controller;
    }

    public ControllerFXML getGameWindow() {
        return gameWindow;
    }
}
