package Game.Model;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Factory {

    private final int width = 80;
    private int h = 10;
    private String[] styles = {"btn1", "btn2", "btn3", "btn4"};

    public ArrayList<Role> chooseActions(int count) {
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

    private Pane changePane(Pane pane, String style, int h) {
        pane.getStyleClass().add(style);
        pane.setPrefWidth(width);
        pane.setPrefHeight(h);
        return pane;
    }

}
