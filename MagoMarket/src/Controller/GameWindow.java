package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GameWindow {

    @FXML
    public AnchorPane root;
    @FXML
    public Pane newCard;

    private Pane pane;
    private int cardsCount = 6;
    private ArrayList <Card> cards = new ArrayList<>();
    private ArrayList <Chip> chips = new ArrayList<>();
    private ArrayList <Player> players = new ArrayList<>();
    private ArrayList<Integer> loupes = new ArrayList<>();
    private boolean isMoveCard = false;
    private int moveCardId;
    private int closestLoupeId;

    @FXML
    void initialize() throws FileNotFoundException {
        root.setMinSize(1280, 720);
        Scale scale = new Scale(root, 1280, 720);
        root.getChildren().add(scale);
        scale.toBack();

        pane = scale.getScalePane();

        createPlayers();
        createCards(1);
        createChip();
        create();
        pane.addEventFilter(MouseEvent.MOUSE_CLICKED, new PaneHandler(this));
        pane.addEventFilter(MouseEvent.MOUSE_MOVED, new PaneHandler(this));
        newCard.setOnMouseClicked(this::addNewCard);
    }

    private void createPlayers () {
        int count = 4;
        players.clear();
        for (int i = 0; i < count; i ++) {
            Factory factory = new Factory();
            players = factory.chooseActions(count);
        }

    }



    public int getNotUsedCard () {
        int i = 0;
        int j = 0;
        while ((cards.get(i).isUsed()) && (j != cards.size())) {
            if ((i + 1) != cards.size())
                i++;
            j++;
        }
        //i = j == cards.size() ? -1 : i;
        if (j == cards.size()) { i = -1; }
        return i;
    }

    private void addNewCard(MouseEvent event) {
        int n = 0;
        loupes.clear();
        for (int i = 0; i < 4; i++) {
           if (chips.get(i).isOnLoupe) {
               loupes.add(i);
               n ++;
           }
        }
        if (n > 0) {
            int i = getNotUsedCard();
            if (i != -1) {
                moveCardId = i;
                isMoveCard = true;
                cards.get(i).setUsed(true);
                getCards().get(getMoveCard()).setLayoutX(event.getSceneX() - getCards().get(0).getImage().getWidth() / 2);
                getCards().get(getMoveCard()).setLayoutY(event.getSceneY() - getCards().get(0).getImage().getHeight() / 2);
                pane.getChildren().add(cards.get(i));
                newCard.setDisable(true);
            }
        }

    }

    private void createChip() {
        for (int i = 1; i <= 4; i++) {
            Chip chip = new Chip("res/pic/mag" + i + ".png","res/pic/mag"+ i + i +".png", this);
            chip.setImage(new Image(chip.url, 45, 45, true, true));
            chips.add(chip);
        }
        double step = cards.get(0).getImage().getWidth() / 4;
        chips.get(0).setLayoutX(cards.get(0).getLayoutX() + step * 2 + step / 2 - chips.get(0).getImage().getWidth() / 2);
        chips.get(0).setLayoutY(cards.get(0).getLayoutY() + step * 2 + step / 2 - chips.get(0).getImage().getHeight() / 2);
        chips.get(1).setLayoutX(cards.get(0).getLayoutX() + step * 2 + step / 2 - chips.get(0).getImage().getWidth() / 2);
        chips.get(1).setLayoutY(cards.get(0).getLayoutY() + step * 1 + step / 2 - chips.get(0).getImage().getHeight() / 2);
        chips.get(2).setLayoutX(cards.get(0).getLayoutX() + step * 1 + step / 2 - chips.get(0).getImage().getWidth() / 2);
        chips.get(2).setLayoutY(cards.get(0).getLayoutY() + step * 1 + step / 2 - chips.get(0).getImage().getHeight() / 2);
        chips.get(3).setLayoutX(cards.get(0).getLayoutX() + step * 1 + step / 2 - chips.get(0).getImage().getWidth() / 2);
        chips.get(3).setLayoutY(cards.get(0).getLayoutY() + step * 2 + step / 2 - chips.get(0).getImage().getHeight() / 2);
    }

    private void createCards(int level) throws FileNotFoundException {
        Scanner input = new Scanner(new FileReader("src/res/cards.txt"));
        for (int i = 1; i <= cardsCount; i++) {
            int mas[][] = new int[7][7];
            for (int j = 0; j < 7; j++) {
                for (int k = 0; k < 7; k++) {
                    mas[j][k] = input.nextInt();
                }
            }
            int bridges = input.nextInt();
            Card card = new Card(mas, "res/pic/cards/" + String.valueOf(i) + ".png", bridges);
            card.setImage(new Image(card.getUrl(), 300, 300, true, false));
            card.setLayoutX(((Scale) pane.getParent()).WIDTH/2f - 150);
            card.setLayoutY(((Scale) pane.getParent()).HEIGHT/2f - 150);
            cards.add(card);
        }
    }

    private void create() {
        pane.getChildren().add(cards.get(0));
        cards.get(0).setUsed(true);
        for (int i = 0; i < 4; i++) {
            pane.getChildren().add(chips.get(i));
        }
    }

    @FXML
    void click(MouseEvent event) {
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

    public ArrayList<Integer> getLoupes() {
        return loupes;
    }

    public boolean isMoveCard() {
        return isMoveCard;
    }

    public void setMoveCard(boolean moveCard) {
        this.isMoveCard = moveCard;
    }

    public int getMoveCard() {
        return moveCardId;
    }

    public Pane getPane() {
        return pane;
    }

    public int getClosestLoupeId() {
        return closestLoupeId;
    }

    public void setClosestLoupeId(int closestLoupeId) {
        this.closestLoupeId = closestLoupeId;
    }
}
