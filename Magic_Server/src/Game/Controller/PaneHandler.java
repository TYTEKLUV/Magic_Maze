//package Game.Controller;
//
//import Game.Model.GameRules;
//import Game.Model.Point;
//import javafx.event.EventHandler;
//import javafx.scene.input.MouseEvent;
//
//import java.io.IOException;
//
//public class PaneHandler implements EventHandler<MouseEvent> {
//
//    private GameWindow gameWindow;
//    private GameRules game = new GameRules();
//
//    PaneHandler(GameWindow gameWindow) {
//        this.gameWindow = gameWindow;
//    }
//
//    @Override
//    public void handle(MouseEvent event) {
//        switch (String.valueOf(event.getEventType())) {
//            case "MOUSE_CLICKED":
//                if (gameWindow.isMoveCard()) {
//                    try {
//                        moveReleased(event);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else mouseReleased(event);
//                break;
//            case "MOUSE_MOVED":
//                if (gameWindow.isMoveCard()) {
//                    mouseMoved(event);
//                }
//                break;
//        }
//    }
//
//    private void moveReleased(MouseEvent event) throws IOException {
//        game.moveReleased(new Point(event.getX(), event.getY()), gameWindow);
//    }
//
//    private void mouseReleased(MouseEvent event) throws IOException {
//        game.mouseReleased(new Point(event.getX(), event.getY()), gameWindow);
//    }
//
//    private void mouseMoved(MouseEvent event) {
//        game.mouseMoved(new Point(event.getX(), event.getY()), gameWindow);
//    }
//
//
//}
