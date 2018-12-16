package Model;

import Controller.GameWindow;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class GameTimer {
    Label time;
    int currentTime;
    int maxTime = 180;
    private GameWindow gameWindow;

    public GameTimer(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public void reset() {
        currentTime = maxTime;
    }

    public void start() {
        currentTime = maxTime;
        Platform.runLater(this::showInLabel);
        new Thread(() -> {
            while (currentTime != 0) {
                Platform.runLater(() -> time.setText("Time " + currentTime));
//                System.out.println("Time " + currentTime);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentTime--;
            }
            Platform.runLater(() -> time.setText("Time ZACONCHILOS'"));
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
}
