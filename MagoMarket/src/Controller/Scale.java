package Controller;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Scale extends AnchorPane {
    private AnchorPane root;
    private Pane scalePane = new Pane();
    private double scale = 0;
    private double scaleAmount = 0.1;
    private GameWindow gameWindow;
    public final double WIDTH;
    public final double HEIGHT;
    private double xOffset, yOffset;

    public Scale(GameWindow gameWindow, int WIDTH, int HEIGHT) {
        this.gameWindow = gameWindow;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.root = gameWindow.getRoot();
        createField();
    }

    private void createField() {
        setPrefHeight(HEIGHT);
        setPrefWidth(WIDTH);
        
        scalePane.setPrefWidth(WIDTH);
        scalePane.setPrefHeight(HEIGHT);
        setTopAnchor(this, (double)0);
        setBottomAnchor(this, (double)0);
        setLeftAnchor(this, (double)0);
        setRightAnchor(this, (double)0);
        getChildren().add(scalePane);

        setStyle("-fx-background-color: #9a977c"); //Синий #4b6d90, Фиолетовый #413843

        mouseDragMoving();
        scaling();
    }

    private void mouseDragMoving() {
        root.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                xOffset = event.getX();
                yOffset = event.getY();
            }
        });

        root.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                scalePane.setLayoutX(scalePane.getLayoutX() + event.getX() - xOffset);
                xOffset = event.getX();
                scalePane.setLayoutY(scalePane.getLayoutY() + event.getY() - yOffset);
                yOffset = event.getY();
            }
        });
    }

    private void scaling() {
        root.setOnScroll(event -> {
            scale = event.getDeltaY() > 0 ? scale + scaleAmount : scale - scaleAmount;
            scale = scale > 0 ? 0 : scale;
            scalePane.setScaleX(Math.exp(scale));
            scalePane.setScaleY(Math.exp(scale));
        });
    }

    public Pane getScalePane() {
        return scalePane;
    }
}
