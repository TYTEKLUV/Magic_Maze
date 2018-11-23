package Model;

import Controller.GameWindow;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Pane implements EventHandler<MouseEvent> {

    private GameWindow gameWindow;
    private Game game = new Game();

    public Pane(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }
    @Override
    public void handle(MouseEvent event) {
        switch (String.valueOf(event.getEventType())) {
            case "MOUSE_CLICKED":
                if(gameWindow.isMoveCard()) {
                    moveReleased();
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
        game.mouseMoved(event, gameWindow);
    }

    private void  moveReleased () {
        Point point = gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getPosition();
        gameWindow.getChips().get(gameWindow.getClosestLoupeId()).isOnLoupe = false;
        int n = Integer.parseInt("20" + String.valueOf(gameWindow.getMoveCard()));
        gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getMap()[((int)point.y - 1)*2][((int)point.x - 1)*2] = n;
        gameWindow.setMoveCard(false);
    }

    private void mouseReleased (MouseEvent event) {
        boolean f = false;
        for (int i = 0; i < 4; i ++) {
            Chip chip = gameWindow.getChips().get(i);
            if(chip.isSelected){
                f = true;
                Point point = chip.getPosition(event, false);
                if (game.isChipCanBeMoved(event, chip.getPosition(), chip.getPosition(event, true), chip, gameWindow))
                    if ((point.y >= 0) && (point.x >= 0)) {
                        gameWindow.getChips().get(i).setLayoutX(point.x);
                        gameWindow.getChips().get(i).setLayoutY(point.y);
                        gameWindow.getChips().get(i).toFront();
                        gameWindow.getChips().get(i).whereAreUNow(event);
                    }
                    chip.isSelected = false;
                    chip.setImage(new Image(chip.url, 45, 45, true, true));
            }
        }
        if (!f) {
            for (int i = 0; i < 4; i ++) {
                Chip chip = gameWindow.getChips().get(i);
                if((event.getX() > chip.getLayoutX()) && (event.getX() < chip.getLayoutX() + chip.getImage().getWidth()) && (event.getY() > chip.getLayoutY()) && (event.getY() < chip.getLayoutY() + chip.getImage().getHeight())) {
                    chip.isSelected = true;
                    chip.setImage(new Image(chip.urlSelected, 45, 45, true, true));
                }
            }
        }
    }



}
