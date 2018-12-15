package Game.Model;

import Game.Controller.GameWindow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Factory {

    private final int width = 80;
    private int h = 10;
    private String[] styles = {"btn1", "btn2", "btn3", "btn4"};
    private GameWindow gameWindow;

    public Factory(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public ArrayList<Role> chooseActions(int count, GameWindow gameWindow) {
        final int height2 = 133;
        final int height3 = 190;
        final int height4 = 248;
        ArrayList<Role> list = new ArrayList<>();
        switch (count) {
            case 2:
                list.add(new Role(3, 12, false, true, false, changePane(new Pane(), styles[2], height3)));
                list.add(new Role(4, 34, true, false, true, changePane(new Pane(), styles[3], height4)));
                break;
            case 3:
                list.add(new Role(2, 12, false, false, false, changePane(new Pane(), styles[1], height2)));
                list.add(new Role(3, 3, true, false, true, changePane(new Pane(), styles[2], height3)));
                list.add(new Role(2, 4, false, true, false, changePane(new Pane(), styles[1], height2)));
                break;
            case 4:
                list.add(new Role(1, 1, false, false, false, changePane(new Pane(), styles[0], width)));
                list.add(new Role(2, 2, false, false, true, changePane(new Pane(), styles[1], height2)));
                list.add(new Role(2, 3, false, true, false, changePane(new Pane(), styles[1], height2)));
                list.add(new Role(2, 4, true, false, false, changePane(new Pane(), styles[1], height2)));
                break;
        }

        return list;
    }


    private Role myRole(Role role) {
        double center;
        if (role.isGlass()) {
            center = (role.getPane().getPrefHeight() + width + h) / 2;
        } else {
            center = (role.getPane().getPrefHeight() + h) / 2;
            gameWindow.newCard.setVisible(false);
        }
        role.getPane().setLayoutY(gameWindow.root.getPrefHeight() / 2 - center);
        gameWindow.newCard.setLayoutY(role.getPane().getLayoutY() + role.getPane().getPrefHeight() + h);
        return role;
    }

    private Pane changePane(Pane pane, String style, int h) {
        pane.getStyleClass().add(style);
        pane.setPrefWidth(width);
        pane.setPrefHeight(h);
        return pane;
    }

    private Pane addPicOnPane(Role role) {
        Pane pane = role.getPane();
        if (role.getArrow() > 0) {
            int length = String.valueOf(role.getArrow()).length();
            int h = 0;
            ImageView imageView;
            for (int i = 0; i < length; i++) {
                imageView = new ImageView(/*"res/pic/window/1.png"*/);
                int n = Integer.parseInt(String.valueOf(String.valueOf(role.getArrow()).charAt(i)));
                pane.getChildren().add(imageView);
                imageView = setParams(imageView, h);
                imageView.setRotate(90 * n);
                h++;
            }
            if (role.isGlass()) {
                imageView = new ImageView(/*"res/pic/window/2.png"*/);
                pane.getChildren().add(setParams(imageView, h));
                h++;
            }
            if (role.isPortal()) {
                imageView = new ImageView(/*"res/pic/window/3.png"*/);
                pane.getChildren().add(setParams(imageView, h));
                h++;
            }
            if (role.isBridge()) {
                imageView = new ImageView(/*"res/pic/window/4.png"*/);
                pane.getChildren().add(setParams(imageView, h));
            }
        }
        return pane;
    }

    private ImageView setParams(ImageView imageView, int h) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(width);
        imageView.setLayoutX(0);
        imageView.setLayoutY(h * (imageView.getFitHeight() / 1.4));
        return imageView;
    }
}
