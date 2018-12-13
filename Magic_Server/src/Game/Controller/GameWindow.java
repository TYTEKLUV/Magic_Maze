package Game.Controller;

import Game.Client.Client;
import Game.Model.*;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
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
    private Client client;

    @FXML
    void initialize() {
        Scale scale = new Scale(root, 1280, 720);
        root.getChildren().add(scale);
        scale.toBack();
        pane = scale.getScalePane();
    }

    public void clientChips(ArrayList<Integer> chipsOrder) {
        createChip(chipsOrder);
    }

    public void cardsRotate(int angle) {
    }

    public void create(PlayerList players) throws FileNotFoundException {
        this.players = players;
        createCards(1);
    }

//    private void createRoles() {
//        int count = 4;
//        int h = 10;
//        ArrayList<Integer> list = new ArrayList<>();
//        for (int i = 0; i < count; i++) { list.add(i); }
//        Collections.shuffle(list);
//        list = new ArrayList<>(Arrays.asList(3, 1, 0, 2));
//        roles.clear();
//        Factory factory = new Factory(this);
//        for (Integer aList : list) { players.add(new Player(aList)); }
//        currentPlayer = 0;
//        roles = factory.chooseActions(count, this);
//        for (Role role : roles) { root.getChildren().add(role.getPane()); }
//        AnchorPane.setRightAnchor(roles.get(players.get(currentPlayer).getRole()).getPane(), (double) h);
//        //newCard.setLayoutY(roles.get(roles.size() - 1).getPane().getLayoutY() + roles.get(roles.size() - 1).getPane().getPrefHeight() + h);
//    }

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
    }

    private void addNewCard(MouseEvent event) {
        int n = 0;
        findGlasses.clear();
        for (int i = 0; i < 4; i++) {
            if (chips.get(i).isOnFindGlass) {
                findGlasses.add(i);
                n++;
            }
        }
        if (n > 0) {
            int i = getNotUsedCard();
            if (i != -1) {
                moveCardId = i;
                isMoveCard = true;
                cards.get(i).setUsed(true);
                cards.get(i).setVisible(false);
                getCards().get(getMoveCard()).setLayoutX(event.getSceneX() - getCards().get(0).getWidth() / 2);
                getCards().get(getMoveCard()).setLayoutY(event.getSceneY() - getCards().get(0).getWidth() / 2);
                pane.getChildren().add(cards.get(i));
                newCard.setDisable(true);
            }
        }
    }

    private void createChip(ArrayList<Integer> chipsOrder) {
        for (int i = 1; i <= 4; i++) {
            Chip chip = new Chip("res/pic/mag" + i + ".png", "res/pic/mag" + i + i + ".png", this);
//            chip.setImage(new Image(chip.url, 45, 45, true, true));
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
//            card.setImage(new Image(card.getUrl(), 300, 300, true, false));
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

    public PlayerList getPlayers() {
        return players;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
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

    public int getClosestFindGlassId() {
        return closestFindGlassId;
    }

    public void setClosestFindGlassId(int closestFindGlassId) {
        this.closestFindGlassId = closestFindGlassId;
    }

    public Client getClient() {
        return client;
    }
}
