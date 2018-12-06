package Controller;

import Model.GameRules;
import Model.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class PaneHandler implements EventHandler<MouseEvent> {

    private GameWindow gameWindow;
    private GameRules game = new GameRules();

    PaneHandler(GameWindow gameWindow) {
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
                if (gameWindow.isMoveCard()) {
                    mouseMoved(event);
                }
                break;
        }
    }

    private void moveReleased () {
        game.moveReleased(gameWindow);
    }

    private void mouseReleased (MouseEvent event) {
        game.mouseReleased(new Point(event.getX(), event.getY()), gameWindow);
    }

    private void mouseMoved (MouseEvent event) {
        game.mouseMoved(new Point(event.getX(), event.getY()), gameWindow);
    }


}
