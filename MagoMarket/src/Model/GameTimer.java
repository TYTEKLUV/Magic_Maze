package Model;

import Controller.GameWindow;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class GameTimer {
    Label message;

    Label time;
    int currentTime;
    int maxTime = 180;
    private GameWindow gameWindow;
    Alert alert;

    public GameTimer(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        Platform.runLater(() -> alert = new Alert(Alert.AlertType.INFORMATION));
    }

    public void reset() {
        currentTime = maxTime;
    }

    public void start() {
        currentTime = maxTime;
        Platform.runLater(this::showInLabel);
        new Thread(() -> {
            while (currentTime > 0) {
                Platform.runLater(() -> time.setText("Time " + currentTime));
//                System.out.println("Time " + currentTime);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentTime--;
            }
            Platform.runLater(() -> {
                time.setText("");
                showMessage("Time ZACONCHILOS'! YOU LOSE!");
            });
            
//            System.out.println("Time down");
        }).start();
    }

    public void swap() {
        currentTime = maxTime - currentTime;
    }

    private void showInLabel() {
        time = new Label();
        time.setLayoutX(20);
        time.setLayoutY(20);
        time.setFont(Font.font("Consolas", 20));
        gameWindow.getRoot().getChildren().add(time);
    }

    public void showMessage(String text) {

        if (!text.equals(" VYCHODY OTKRYTY")) {
            alert.setTitle("Окончание игры!");
            alert.setHeaderText(null);
            alert.setContentText(text);
            Platform.runLater(() -> alert.showAndWait());
            currentTime=0;
            time.setText("");
        } else {
            if (message == null) {
                message = new Label();
                message.setLayoutX(20);
                message.setLayoutY(60);
                message.setFont(Font.font("Consolas", 22));
                gameWindow.getRoot().getChildren().add(message);
            }
            message.setText(text);
        }
    }
}
