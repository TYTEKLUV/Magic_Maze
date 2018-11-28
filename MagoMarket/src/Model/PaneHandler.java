package Model;

import Controller.GameWindow;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class PaneHandler implements EventHandler<MouseEvent> {

    private GameWindow gameWindow;
    private Game game = new Game();

    public PaneHandler(GameWindow gameWindow) {
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
        game.mouseReleased(event, gameWindow);
    }

    private void mouseMoved (MouseEvent event) {
        game.mouseMoved(event, gameWindow);
    }


}
