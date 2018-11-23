package Model;

import Controller.GameWindow;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
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
                while (gameWindow.getCards().get(gameWindow.getMoveCard()).getMap()[6][2] != 20) {
                    gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
                }
                break;
            case "43":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                while (gameWindow.getCards().get(gameWindow.getMoveCard()).getMap()[2][0] != 20) {
                    gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
                }
                break;
            case "24":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                while (gameWindow.getCards().get(gameWindow.getMoveCard()).getMap()[0][4] != 20) {
                    gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
                }
                break;
            case "12":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                while (gameWindow.getCards().get(gameWindow.getMoveCard()).getMap()[4][6] != 20) {
                    gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
                }
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
        gameWindow.getChips().get(gameWindow.getClosestLoupeId()).isOnLoupe = false;
        int n = Integer.parseInt("20" + String.valueOf(gameWindow.getMoveCard()));
        gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getMap()[((int)point.y - 1)*2][((int)point.x - 1)*2] = n;
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
//                System.out.println(isFloorIsEmpty(chip.getPosition()));
                Point point = chip.getPosition(event, false);
                //if (!isFloorIsEmpty(chip.getPosition(event, true)))
                if (isChipCanBeMoved(event, chip.getPosition(), chip.getPosition(event, true), chip))
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

    private boolean isFloorIsEmpty (Point point, Chip chip, MouseEvent event) {
        boolean f = true;
        for (int i = 0; i < 4; i++) {
            if ((gameWindow.getChips().get(i).getPosition().x == point.x)&&(gameWindow.getChips().get(i).getPosition().y == point.y)&&(gameWindow.getChips().get(i).getCardId() == chip.getCardId(event))) {
                f = false;
            }
        }
        return f;
    }

    private boolean isChipCanBeMoved (MouseEvent event, Point start, Point end, Chip chip) {
        boolean f = false;
        if (isFloorIsEmpty(end, chip, event)) {
            double dx = (Math.abs(end.x - start.x));
            double dy = (Math.abs(end.y - start.y));
            Point minPoint = start;
            if ((end.x < start.x)||(end.y < start.y)){
                minPoint = end;
            }
            if ((dx == 0)&&(dy == 1)&&(chip.getCardId() == chip.getCardId(event))) {
                if (gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int)(minPoint.y - 1) * 2 + 1][(int)(minPoint.x  - 1) * 2] != 0) {
                    f = true;
                }
            }
            else
            if ((dx == 1)&&(dy == 0)&&(chip.getCardId() == chip.getCardId(event))) {
                if (gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int)(minPoint.y - 1) * 2][(int)(minPoint.x  - 1) * 2 + 1] != 0) {
                    f = true;
                }
            }
            else {
                int ind = Integer.parseInt("3" + String.valueOf(gameWindow.getChips().indexOf(chip) + 1));
                if (gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int)(end.y - 1) * 2][(int)(end.x - 1) * 2] == ind) {
                    f = true;
                }
                else
                if ((gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int)(end.y - 1) * 2][(int)(end.x - 1) * 2] == 20)) {
                    int n = Integer.parseInt("20" + String.valueOf(chip.getCardId(event)));
                    System.out.println("n " + n);
                    if (gameWindow.getCards().get(chip.getCardId()).getMap()[(int)(start.y - 1) * 2][(int)(start.x - 1) * 2] == n) {
                        f = true;
                    }
                }
                else {
                    int n = Integer.parseInt("20" + String.valueOf(chip.getCardId()));
                    if (gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == n) {
                        if (gameWindow.getCards().get(chip.getCardId()).getMap()[(int)(start.y - 1) * 2][(int)(start.x - 1) * 2] == 20) {
                            f = true;
                        }
                    }
                }
                System.out.println();
                System.out.println("place" + gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int)(end.y - 1) * 2][(int)(end.x - 1) * 2]);
            }
        }
        return f;
    }

}
