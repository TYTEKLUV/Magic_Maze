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
    private ClientStarter clientStarter;
    private GameWindow gameWindow;
    private GameMenu gameMenu;
    private MainMenu mainMenu;
    private RoomMenu roomMenu;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameWindow = (GameWindow) loadFXML(primaryStage, "../View/FieldEditor.fxml", "Magic Maze", 1280, 720, false);
        mainMenu = (MainMenu) loadFXML(new Stage(), "../View/MainMenu.fxml", "Magic Maze", 1280, 720, true);
        gameMenu = (GameMenu) loadFXML(new Stage(), "../View/GameMenu.fxml", "Magic Maze", 1280, 720, false);
        roomMenu = (RoomMenu) loadFXML(new Stage(), "../View/RoomMenu.fxml", "Magic Maze", 1280, 720, false);
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
        clientStarter = new ClientStarter(gameWindow);
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

    public ClientStarter getClientStarter() {
        return clientStarter;
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }
}
