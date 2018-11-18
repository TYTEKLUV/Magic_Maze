package Model;

import Controller.GameWindow;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Pane implements EventHandler<MouseEvent> {

    private GameWindow gameWindow;

    public Pane(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }
    @Override
    public void handle(MouseEvent event) {
        switch (String.valueOf(event.getEventType())) {
            case "MOUSE_RELEASED":
                if(gameWindow.isMoveCard()){
                    moveReleased(event);
                }
                else mouseReleased(event);
                break;
            case "MOUSE_MOVED":
                if (gameWindow.isMoveCard())
                   mouseMoved(event);
                break;
        }
    }

    private void  mouseMoved(MouseEvent event) {
        double min = gameWindow.getPane().getPrefWidth();
        double x = 0, y = 0;
        gameWindow.setClosestLoupeId(-1);
        for (int i = 0; i < gameWindow.getLoupes().size(); i ++) {
            int chipId = gameWindow.getLoupes().get(i);
            x = gameWindow.getChips().get(chipId).getLayoutX();
            y = gameWindow.getChips().get(chipId).getLayoutY();
            double length = Math.sqrt(Math.pow((event.getX() - x), 2) + Math.pow((event.getY() - y), 2));
            if (length < min) {
                min = length;
                gameWindow.setClosestLoupeId(chipId);
            }
        }
        String pos = String.valueOf((int)gameWindow.getChips().get(     gameWindow.getClosestLoupeId()).getPosition().x) + String.valueOf((int)gameWindow.getChips().get(               gameWindow.getClosestLoupeId()).getPosition().y);
        switch (pos) {
            case "31":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                while() {
                    gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
                }
                break;
            case "43":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                break;
            case "24":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                break;
            case "12":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                break;
        }
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutX(event.getX() - gameWindow.getCards().get(0).getImage().getWidth()/2);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutX(x);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutY(event.getY() - gameWindow.getCards().get(0).getImage().getHeight()/2);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutY(y);

    }

    private Card rotateMap (Card card) {
        int[][] map = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                map[i][j] = card.getMap()[6-j][i];
            }
        }
        card.setMap(map);
        card.setRotate(card.getRotate()+90);
        return card;
    }
    private void  moveReleased (MouseEvent event) {
        Point point = gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getPosition();
        System.out.println("cl " + gameWindow.getClosestLoupeId());
        System.out.println(((int)(point.y - 1)*2) + " " + ((int)(point.x - 1)*2));
        gameWindow.getChips().get(gameWindow.getClosestLoupeId()).isOnLoupe = false;
        gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getMap()[((int)point.y - 1)*2][((int)point.x - 1)*2] = 10;
        gameWindow.setMoveCard(false);
    }
    private void mouseReleased (MouseEvent event) {
        boolean f = false;
        if (gameWindow.isMoveCard()) {

        }
        for (int i = 0; i < 4; i ++) {
            Chip chip = gameWindow.getChips().get(i);
            if(chip.isSelected){
                f = true;
                Point point = chip.getPosition(event, false);
                if ((point.y >= 0) && (point.x >= 0)) {
                    gameWindow.getChips().get(i).setLayoutX(point.x);
                    gameWindow.getChips().get(i).setLayoutY(point.y);
                    gameWindow.getChips().get(i).toFront();
                    gameWindow.getChips().get(i).whereAreUNow(event);

                }
                gameWindow.getChips().get(i).isSelected = false;
            }
        }
        if (!f) {
            for (int i = 0; i < 4; i ++) {
                Chip chip = gameWindow.getChips().get(i);
                if((event.getX() > chip.getLayoutX()) && (event.getX() < chip.getLayoutX() + chip.getImage().getWidth()) && (event.getY() > chip.getLayoutY()) && (event.getY() < chip.getLayoutY() + chip.getImage().getHeight())) {
                    chip.isSelected = true;
                }
            }
        }
    }

}
