package Model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Factory {

    public ArrayList<Role> chooseActions (int count) {
        ArrayList <Role> list = new ArrayList<>();
        switch (count) {
            case 2:
                Pane pane = new Pane();
                pane.getStyleClass().add("btn3");
                list.add(new Role(3,12 , false, true, false, pane));
                Pane pane2 = new Pane();
                pane2.getStyleClass().add("btn4");
                list.add(new Role(4,34 , true, false, true, pane2));
                break;
            case 3:
                Pane pane3 = new Pane();
                pane3.getStyleClass().add("btn2");
                list.add(new Role(2,12 , false, false, false, pane3));
                Pane pane4 = new Pane();
                pane4.getStyleClass().add("btn3");
                list.add(new Role(3,3, true, false, true, pane4));
                Pane pane5 = new Pane();
                pane5.getStyleClass().add("btn2");
                list.add(new Role(2,4, false, true, false, pane5));
                break;
            case 4:
                Pane pane6 = new Pane();
                pane6.getStyleClass().add("btn1");
                pane6.setLayoutY(50);
                pane6.setLayoutX(10);
                pane6.setPrefWidth(80);
                pane6.setPrefHeight(80);
                list.add(new Role(1,1, false, false, false, pane6));
                Pane pane7 = new Pane();
                pane6.setLayoutY(160);
                pane6.setLayoutX(10);
                pane7.setPrefWidth(80);
                pane7.setPrefHeight(133);
                pane7.getStyleClass().add("btn2");
                list.add(new Role(2,2, false, false, true, pane7));
                Pane pane8 = new Pane();
                pane8.setPrefWidth(80);
                pane8.setPrefHeight(133);
                pane8.getStyleClass().add("btn2");
                list.add(new Role(2,3, false, true, false, pane8));
                Pane pane9 = new Pane();
                pane9.setPrefWidth(80);
                pane9.setPrefHeight(133);
                pane9.getStyleClass().add("btn2");
                list.add(new Role(2,4, true, false, false, pane9));
                break;
        }
        for (int i = 0; i < list.size(); i ++) {
            list.get(i).setPane(changePane(list.get(i)));
        }
        double x = 10, y = 30;
        list.get(0).getPane().setLayoutX(x);
        list.get(0).getPane().setLayoutY(y);
        for (int i = 1; i < list.size(); i ++) {
            list.get(i).getPane().setLayoutX(x);
            double g = list.get(i - 1).getPane().getLayoutY() + list.get(i - 1).getPane().getHeight();
            System.out.println("l " + g);
            list.get(i).getPane().setLayoutY(list.get(i - 1).getPane().getLayoutY() + list.get(i - 1).getPane().getPrefHeight() + 10);
            list.get(i).getPane().setLayoutX(x);
        }
        return list;
    }

    private Pane changePane (Role role) {
        Pane pane = role.getPane();
        if (role.getArrow() > 0) {
            int length = String.valueOf(role.getArrow()).length();
            ImageView imageView = new ImageView("res/pic/window/1.png");
            int h = 0;
            int h2 = 20;
            for (int i = 0; i < length; i ++) {
                int n = Integer.parseInt(String.valueOf(String.valueOf(role.getArrow()).charAt(i)));
                pane.getChildren().add(imageView);
                imageView = setParams(imageView, h);
                imageView.setRotate(90*n);
                imageView.setLayoutY(i * imageView.getFitHeight());
                h = i;
            }
            if (role.isGlass()) {
                ImageView imageView2 = new ImageView("res/pic/window/2.png");
                pane.getChildren().add(setParams(imageView2, h));
                h ++;
            }
            if (role.isPortal()) {
                ImageView imageView3 = new ImageView("res/pic/window/3.png");
                pane.getChildren().add(setParams(imageView3, h));
                h ++;
            }
            if (role.isBridge()) {
                ImageView imageView4 = new ImageView("res/pic/window/4.png");
                pane.getChildren().add(setParams(imageView4, h));
            }
        }
        return pane;
    }

    public ImageView setParams (ImageView imageView, int h) {
        ImageView image = imageView;
        image.setFitWidth(80);
        image.setFitHeight(80);
        image.setLayoutX(0);
        image.setLayoutY(h * image.getFitHeight() + image.getFitHeight() - image.getFitHeight()/3);
        return image;
    }
}
