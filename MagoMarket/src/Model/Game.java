package Model;

import Controller.GameWindow;
import javafx.scene.input.MouseEvent;

class Game {

    private boolean isFloorIsEmpty (Point point, Chip chip, MouseEvent event, GameWindow gameWindow) {
        boolean f = true;
        for (int i = 0; i < 4; i++) {
            if ((gameWindow.getChips().get(i).getPosition().x == point.x)&&(gameWindow.getChips().get(i).getPosition().y == point.y)&&(gameWindow.getChips().get(i).getCardId() == chip.getCardId(event))) {
                f = false;
            }
        }
        return f;
    }

    public boolean isChipCanBeMoved (MouseEvent event, Point start, Point end, Chip chip, GameWindow gameWindow) {
        boolean f = false;
        if (isFloorIsEmpty(end, chip, event, gameWindow)) {
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
    void mouseMoved(MouseEvent event, GameWindow gameWindow){
        double x = 0, y = 0;
        gameWindow.setClosestLoupeId(-1);
        findClosestLoupe(event, gameWindow);
        String pos = String.valueOf((int)gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getPosition().x) + String.valueOf((int)gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getPosition().y);
        switch (pos) {
            case "31":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                rotateCard(gameWindow, 6, 2);
                break;
            case "43":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                rotateCard(gameWindow, 2, 0);
                break;
            case "24":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                rotateCard(gameWindow, 0, 4);
                break;
            case "12":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestLoupeId()).getCardId()).getImage().getWidth()/4;
                rotateCard(gameWindow, 4, 6);
                break;
        }
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutX(x);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutY(y);
    }


    private void rotateCard (GameWindow gameWindow, int x, int y) {
        while (gameWindow.getCards().get(gameWindow.getMoveCard()).getMap()[x][y] != 20) {
            gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
        }
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

    private  void findClosestLoupe (MouseEvent event, GameWindow gameWindow) {
        double min = gameWindow.getPane().getPrefWidth();
        double x, y;
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
    }
}
