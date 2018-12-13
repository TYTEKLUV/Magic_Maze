package Game.Controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Scale extends AnchorPane {
    private AnchorPane root;
    private Pane scalePane = new Pane();
    private double scale = 0;
    private double scaleAmount = 0.1;
    public final double WIDTH, HEIGHT;

    public Scale(AnchorPane root, int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.root = root;
        createField();
    }

    private void createField() {
        setPrefHeight(HEIGHT);
        setPrefWidth(WIDTH);

        scalePane.setPrefWidth(WIDTH);
        scalePane.setPrefHeight(HEIGHT);
        setTopAnchor(this, (double) 0);
        setBottomAnchor(this, (double) 0);
        setLeftAnchor(this, (double) 0);
        setRightAnchor(this, (double) 0);
        getChildren().add(scalePane);

        setStyle("-fx-background-color: #9a977c"); //Синий #4b6d90, Фиолетовый #413843

        moving();
        scaling();
    }

    private void moving() {
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    scalePane.setLayoutY(scalePane.getLayoutY() + 10);
                    break;
                case S:
                    scalePane.setLayoutY(scalePane.getLayoutY() - 10);
                    break;
                case A:
                    scalePane.setLayoutX(scalePane.getLayoutX() + 10);
                    break;
                case D:
                    scalePane.setLayoutX(scalePane.getLayoutX() - 10);
                    break;
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
