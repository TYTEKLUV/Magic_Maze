package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private GameWindow gameWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameWindow = (GameWindow) loadFXML(primaryStage, "../View/FieldEditor.fxml", "Magic Maze", 1280, 720, false);
        createFieldController();
        clientStarter();
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
        stage.setOnCloseRequest(event -> System.exit(0));
        if (show) stage.show();
        return controller;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    private void clientStarter() {
        ClientStarter clientStarter = new ClientStarter(gameWindow);
        clientStarter.start();
    }

    private void createFieldController() {
        gameWindow.getStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W:
                    gameWindow.getPane().setLayoutY(gameWindow.getPane().getLayoutY() + 10);
                    break;
                case S:
                    gameWindow.getPane().setLayoutY(gameWindow.getPane().getLayoutY() - 10);
                    break;
                case A:
                    gameWindow.getPane().setLayoutX(gameWindow.getPane().getLayoutX() + 10);
                    break;
                case D:
                    gameWindow.getPane().setLayoutX(gameWindow.getPane().getLayoutX() - 10);
                    break;
            }
        });
    }
}
