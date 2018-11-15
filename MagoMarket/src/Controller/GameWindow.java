package Controller;

import Model.Card;
import Model.Chip;
import Model.Pane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GameWindow {

    @FXML
    AnchorPane pane;
    @FXML
    Button newCard;

    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Chip> chips = new ArrayList<>();
    private boolean isMoveCard = false;
    private int moveCardId;

    @FXML
    void initialize () throws FileNotFoundException {
        createCards(1);
        createChip();
        create();
        pane.addEventFilter(MouseEvent.MOUSE_RELEASED, new Pane(this));
        pane.addEventFilter(MouseEvent.MOUSE_MOVED, new Pane(this));
        newCard.setOnMouseClicked(this::addNewCard);
    }

    private void addNewCard(MouseEvent event){
        isMoveCard = true;
        int i = 0;
        int j = 0;
        while ((cards.get(i).isUsed()) || (j != cards.size())) {
            if (i + 1  != cards.size())
                i++;
            j++;
        }
        if (i != cards.size()) {
            moveCardId = i;
            getCards().get(getMoveCard()).setLayoutX(event.getSceneX() - getCards().get(0).getImage().getWidth()/2);
            getCards().get(getMoveCard()).setLayoutY(event.getSceneY() - getCards().get(0).getImage().getHeight()/2);
            pane.getChildren().add(cards.get(i));
        }

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
            Card card = new Card(mas, "res/pic/cards/" + String.valueOf(i) + ".png", false);
            card.setImage(new Image(card.getUrl(), 300, 300, true, false));
            card.setLayoutX(pane.getPrefWidth()/2 - 150);
            card.setLayoutY(pane.getPrefHeight()/2 - 150);
            System.out.println(card.getFitWidth()/2);
            cards.add(card);
        }
    }

    private void create() {
        pane.getChildren().add(cards.get(0));
        cards.get(0).setUsed(true);
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

    public boolean isMoveCard() {
        return isMoveCard;
    }

    public void setMoveCard(boolean moveCard) {
        this.isMoveCard = moveCard;
    }

    public int getMoveCard(){
        return moveCardId;
    }


}
