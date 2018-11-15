package Controller;

import Model.Card;
import Model.Chip;
import Model.Click;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameWindow {

    @FXML
    Button cardMaker;
    @FXML
    AnchorPane pane;

    ArrayList<Card> cards = new ArrayList<>();
    ArrayList<Chip> chips = new ArrayList<>();

    @FXML
    void initialize () throws FileNotFoundException {
        createCards(1);
        createChip();
        create();
        pane.addEventFilter(MouseEvent.MOUSE_RELEASED, new Click(this));
    }

    private void createChip () {
        for (int i = 1; i <= 4; i ++) {
            Chip chip = new Chip(false);
            chip.setImage(new Image("res/pic/mag" + i +".png", 45, 45, true, true));
            chips.add(chip);
        }
        double step = cards.get(0).getImage().getWidth()/4;
        chips.get(0).setLayoutX(cards.get(0).getLayoutX() + step*2 + step/2 - chips.get(0).getImage().getWidth()/2);
        chips.get(0).setLayoutY(cards.get(0).getLayoutY() + step*2 + step/2 - chips.get(0).getImage().getHeight()/2);
        chips.get(1).setLayoutX(cards.get(0).getLayoutX() + step*2 + step/2 - chips.get(0).getImage().getWidth()/2);
        chips.get(1).setLayoutY(cards.get(0).getLayoutY() + step*1 + step/2 - chips.get(0).getImage().getHeight()/2);
        chips.get(2).setLayoutX(cards.get(0).getLayoutX() + step*1 + step/2 - chips.get(0).getImage().getWidth()/2);
        chips.get(2).setLayoutY(cards.get(0).getLayoutY() + step*1 + step/2 - chips.get(0).getImage().getHeight()/2);
        chips.get(3).setLayoutX(cards.get(0).getLayoutX() + step*1 + step/2 - chips.get(0).getImage().getWidth()/2);
        chips.get(3).setLayoutY(cards.get(0).getLayoutY() + step*2 + step/2 - chips.get(0).getImage().getHeight()/2);
    }

    private void createCards (int level) throws FileNotFoundException {
        Scanner input = new Scanner(new FileReader("src/res/cards.txt"));
        boolean f = false;
        int mas[][] = new int[4][4];
        for (int i = 1; i <= 2; i ++) {
            for (int j = 0; j < 4; j ++) {
                for (int k = 0; k < 4; k ++) {
                    mas[j][k] = input.nextInt();
                }
            }
            Card card = new Card(mas, "res/pic/cards/" + String.valueOf(i) + ".png");
            card.setImage(new Image(card.getUrl(), 300, 300, true, false));
            card.setLayoutX(pane.getPrefWidth()/2 - 150);
            card.setLayoutY(pane.getPrefHeight()/2 - 150);
            System.out.println(card.getFitWidth()/2);
            cards.add(card);
        }
    }

    private void create() {
        pane.getChildren().add(cards.get(0));
        for (int i = 0; i < 4; i ++) {
            pane.getChildren().add(chips.get(i));
        }
    }
    @FXML
    void click(MouseEvent event) {
        System.out.println(event.getX() + " " + event.getY());
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/CardEditor.fxml"));
//        Parent root1 = null;
//        try {
//            root1 = fxmlLoader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setTitle("Другая форма");
//        stage.setScene(new Scene(root1));
//        stage.show();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<Chip> getChips() {
        return chips;
    }
}
