package Game.Controller;

import Game.Model.*;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameWindow extends ControllerFXML {

    @FXML
    public AnchorPane root;
    @FXML
    public Pane newCard;

    private Pane pane;
    private int cardsCount = 9;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Chip> chips = new ArrayList<>();
    private ArrayList<Role> roles = new ArrayList<>();
    private PlayerList players = new PlayerList();
    private ArrayList<Integer> findGlasses = new ArrayList<>();
    private boolean isMoveCard = false;
    private int moveCardId;
    private int closestFindGlassId;
    private int currentPlayer = -1;
    private GameRules gameRules = new GameRules();
    private boolean weaponsReceived = false;

    @FXML
    void initialize() {
        Scale scale = new Scale(root, 1280, 720);
        root.getChildren().add(scale);
        scale.toBack();
        pane = scale.getScalePane();
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public boolean isWeaponsReceived() {
        return weaponsReceived;
    }

    public void setWeaponsReceived(boolean weaponsReceived) {
        this.weaponsReceived = weaponsReceived;
    }

    public void clientChips(ArrayList<Integer> chipsOrder) {
        createChip(chipsOrder);
    }

    public void cardsRotate(int angle) {
    }

    public void create(PlayerList players) throws FileNotFoundException {
        Factory factory = new Factory();
        roles = factory.chooseActions(players.size());
        this.players = players;
        createCards(1);
    }

    public int getNotUsedCard() {
        int i = 0;
        int j = 0;
        while ((cards.get(i).isUsed()) && (j != cards.size())) {
            if ((i + 1) != cards.size())
                i++;
            j++;
        }
        if (j == cards.size()) {
            i = -1;
        }
        return i;
    }

    public void sendCard(int id, Point point, int angle) {
        cards.get(id).setLayoutX(point.x);
        cards.get(id).setLayoutY(point.y);
        cards.get(id).setUsed(true);
        cards.get(id).rotate(angle);
        pane.getChildren().add(cards.get(id));
        cards.get(id).setVisible(true);
        new GameRules().moveReleased(this, point);
    }

    private void createChip(ArrayList<Integer> chipsOrder) {
        for (int i = 1; i <= 4; i++) {
            Chip chip = new Chip("res/pic/mag" + i + ".png", "res/pic/mag" + i + i + ".png", this);
            chip.setLayoutX(cards.get(0).getLayoutX() + 10);
            chip.setLayoutY(cards.get(0).getLayoutY() + 10);
            chips.add(chip);
        }
        int k = 0;
        for (int i = 2; i < 4; i++) {
            for (int j = 2; j < 4; j++) {
                Point point = cards.get(0).layoutXY(j, i).getPosition(false, this);
                chips.get(chipsOrder.get(k)).setLayoutX(point.x);
                chips.get(chipsOrder.get(k)).setLayoutY(point.y);
                k++;
            }
        }
        addStartObjects();
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
            card.setLayoutX(((Scale) pane.getParent()).WIDTH / 2f - 150);
            card.setLayoutY(((Scale) pane.getParent()).HEIGHT / 2f - 150);
            cards.add(card);
        }
        cards.get(0).setUsed(true);
    }

    private void addStartObjects() {
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

    public ArrayList<Integer> getFindGlasses() {
        return findGlasses;
    }

    public void setMoveCard(boolean moveCard) {
        this.isMoveCard = moveCard;
    }

    public Pane getPane() {
        return pane;
    }

    public int getClosestFindGlassId() {
        return closestFindGlassId;
    }

    public void setClosestFindGlassId(int closestFindGlassId) {
        this.closestFindGlassId = closestFindGlassId;
    }
}