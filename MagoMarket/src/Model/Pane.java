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
                    gameWindow.setMoveCard(false);
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
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutX(event.getX() - gameWindow.getCards().get(0).getImage().getWidth()/2);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutY(event.getY() - gameWindow.getCards().get(0).getImage().getHeight()/2);
    }
    private void mouseReleased (MouseEvent event) {
        boolean f = false;
        for (int i = 0; i < 4; i ++) {
            Chip chip = gameWindow.getChips().get(i);
            if(chip.isSelected){
                f = true;
                Point point = getPosition(event);
                if ((point.y >= 0) && (point.x >= 0)) {
                    gameWindow.getChips().get(i).setLayoutX(point.x);
                    gameWindow.getChips().get(i).setLayoutY(point.y);
                    gameWindow.getChips().get(i).toFront();
                }
                chip.isSelected = false;
            }
        }
        if (!f) {
            for (int i = 0; i < 4; i ++) {
                Chip chip = gameWindow.getChips().get(i);
                if((event.getX() > chip.getLayoutX()) && (event.getX() < chip.getLayoutX() + chip.getImage().getWidth()) && (event.getY() > chip.getLayoutY()) && (event.getY() < chip.getLayoutY() + chip.getImage().getHeight())) {
                    chip.isSelected = true;
                    System.out.println("true");
                }
            }
        }
    }
    private Point getPosition (MouseEvent event) {
       double x = -1, y = -1;
       int n = getCardId(event);
       if (n >= 0) {
           Card card = gameWindow.getCards().get(n);
           double width = card.getImage().getWidth()/4;
           System.out.println("w " + width/4);
           x = Math.ceil((event.getX() - card.getLayoutX())/(width));
           y = Math.ceil((event.getY() - card.getLayoutY())/(width));
           System.out.println(gameWindow.getChips().get(0).getImage().getWidth());
           x = card.getLayoutX() + x*width - width + (Math.floor(width/2) - Math.floor(gameWindow.getChips().get(0).getImage().getWidth()/2));
           y = card.getLayoutY() + y*width - width + (Math.floor(width/2) - Math.floor(gameWindow.getChips().get(0).getImage().getWidth()/2));

       }
       return new Point(x, y);
    }

    private int getCardId (MouseEvent event) {
        int n = -1;
        for(int i = 0; i < gameWindow.getCards().size(); i ++) {
            Card card = gameWindow.getCards().get(i);
            if ((event.getX() > card.getLayoutX()) && (event.getX() < card.getLayoutX() + card.getImage().getWidth()) && (event.getY() > card.getLayoutY()) && (event.getY() < card.getLayoutY() + card.getImage().getHeight())) {
                n = i;
            }
        }
        return n;
    }

}
