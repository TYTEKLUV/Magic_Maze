package Model;

import Controller.GameWindow;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class Game {



    private boolean isFloorIsEmpty(Point point, Chip chip, MouseEvent event, GameWindow gameWindow) {
        boolean f = true;
        for (int i = 0; i < 4; i++) {
            if ((gameWindow.getChips().get(i).getPosition().x == point.x) && (gameWindow.getChips().get(i).getPosition().y == point.y) && (gameWindow.getChips().get(i).getCardId() == chip.getCardId(event))) {
                f = false;
            }
        }
        return f;
    }

    private void glassToGlass(Card card, ArrayList<Card> cards) { //проверяем после того, как поставили карту
        for (int i = 0; i <  card.getMap().length; i++) {
            for (int j = 0; j <  card.getMap().length; j++) {
                if ((card.getMap()[i][j] >= 21) && (card.getMap()[i][j] <= 24)) {
                    for (int k = 0; k < cards.size(); k++) {//карты
                        System.out.println();
                        for (int z = 0; z <  card.getMap().length; z++) {
                            for (int t = 0; t <  card.getMap().length; t++) {
                                if ((cards.get(k).getMap()[z][t] >= 21) && (cards.get(k).getMap()[z][t] <= 24) && (i != z) && (j != t)) {
                                    int w = (int) Math.abs(cards.get(k).layoutXY(t/2 + 1, z/2 + 1).x - card.layoutXY(j/2 + 1, i/2 + 1).x);
                                    int h = (int) Math.abs(cards.get(k).layoutXY(t/2 + 1, z/2 + 1).y - card.layoutXY(j/2 + 1, i/2 + 1).y);
                                    if ((w <= card.getImage().getWidth()/4)&&(h <= card.getImage().getHeight()/4)) {
                                        card.getMap()[i][j] = 20;
                                        cards.get(k).getMap()[z][t] = Integer.parseInt("20" + String.valueOf(cards.indexOf(card)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isChipOnBridge (MouseEvent event, Chip chip, GameWindow gameWindow) {
        boolean f = false;
        int bridges = gameWindow.getCards().get(chip.getCardId()).getBridges();
        String s = String.valueOf(bridges).substring(1);
        if (bridges!= 0) {
            int count = Integer.parseInt(String.valueOf(s.length()))/4;
            for (int i = 0; i < count; i++) {
                int x1 = Integer.parseInt(String.valueOf(s.charAt(i))) + 1;
                int y1 = Integer.parseInt(String.valueOf(s.charAt(i + 1))) + 1;
                int x2 = Integer.parseInt(String.valueOf(s.charAt(i + 2))) + 1;
                int y2 = Integer.parseInt(String.valueOf(s.charAt(i + 3))) + 1;
                System.out.println(chip.getPosition().y + " " + chip.getPosition().x);
                if ((chip.getPosition().y == x1)&&(chip.getPosition().x == y1)&&(chip.getPosition(event, true).y == x2)&&(chip.getPosition(event, true).x == y2)||
                ((chip.getPosition().y == x2)&&(chip.getPosition().x == y2)&&(chip.getPosition(event, true).y == x1)&&(chip.getPosition(event, true).x == y1))) {
                    f = true;
                }
            }
        }
        return f;
    }

    private boolean isChipOnLine (MouseEvent event, Point start, Point end, Chip chip, GameWindow gameWindow) {
        boolean f = false;
        double dx = (Math.abs(end.x - start.x));
        double dy = (Math.abs(end.y - start.y));
        Point minPoint = start;
        if ((end.x < start.x) || (end.y < start.y)) {
            minPoint = end;
        }
        final int cardId = chip.getCardId(event);
        if ((dx == 0) && (chip.getCardId() == cardId)) {
            if (dy == 1) {
                if (gameWindow.getCards().get(cardId).getMap()[(int) (minPoint.y - 1) * 2 + 1][(int) (minPoint.x - 1) * 2] != 0) {
                    f = true;
                }
            }
        } else if ((dx == 1) && (chip.getCardId() == cardId)) {
            if (dy == 0) {
                if (gameWindow.getCards().get(cardId).getMap()[(int) (minPoint.y - 1) * 2][(int) (minPoint.x - 1) * 2 + 1] != 0) {
                    f = true;
                }
            }
        }
        return f;
    }

    private boolean isChipOnPortal (MouseEvent event, Point end, Chip chip, GameWindow gameWindow) {
        boolean f = false;
        int ind = Integer.parseInt("3" + String.valueOf(gameWindow.getChips().indexOf(chip) + 1));
        if (gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == ind) {
            f = true;
        }
        return  f;
    }

    private boolean isChipOnArrow (MouseEvent event, Point start, Point end, Chip chip, GameWindow gameWindow) {
        boolean f = false;
        if ((gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == 20)) {
            int n = Integer.parseInt("20" + String.valueOf(chip.getCardId(event)));
            if (gameWindow.getCards().get(chip.getCardId()).getMap()[(int) (start.y - 1) * 2][(int) (start.x - 1) * 2] == n) {
                f = true;
            }
        } else {
            int n = Integer.parseInt("20" + String.valueOf(chip.getCardId()));
            if (gameWindow.getCards().get(chip.getCardId(event)).getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == n) {
                if (gameWindow.getCards().get(chip.getCardId()).getMap()[(int) (start.y - 1) * 2][(int) (start.x - 1) * 2] == 20) {
                    f = true;
                }
            }
        }
        return f;
    }

    private boolean isChipCanBeMoved(MouseEvent event, Point start, Point end, Chip chip, GameWindow gameWindow) {
        boolean f = false;
        if (isFloorIsEmpty(end, chip, event, gameWindow)) {
            Point minPoint = start;
            if ((end.x < start.x) || (end.y < start.y)) {
                minPoint = end;
            }
            if ((minPoint.x != -1) && (minPoint.y != -1)) {
                if (isChipOnLine(event, start, end, chip, gameWindow)) {
                        f = true;
                }  else if (isChipOnPortal(event, end, chip, gameWindow)) {
                        f = true;
                    } else if (isChipOnArrow(event, start, end, chip, gameWindow)) {
                        f = true;
                    }
                    else if (isChipOnBridge(event, chip, gameWindow)) {
                        f = true;
                    }

            }
        }
        return f;
    }

    public void mouseMoved(MouseEvent event, GameWindow gameWindow) {
        if (!gameWindow.getCards().get(gameWindow.getMoveCard()).isVisible()) {
            gameWindow.getCards().get(gameWindow.getMoveCard()).setVisible(true);
        }
        double x = 0, y = 0;
        gameWindow.setClosestFindGlassId(-1);
        findClosestFindGlass(event, gameWindow);
        String pos = String.valueOf((int) gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getPosition().x) + String.valueOf((int) gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getPosition().y);
        switch (pos) {
            case "31":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                rotateCard(gameWindow, 6, 2);
                break;
            case "43":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                rotateCard(gameWindow, 2, 0);
                break;
            case "24":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                rotateCard(gameWindow, 0, 4);
                break;
            case "12":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                rotateCard(gameWindow, 4, 6);
                break;
        }
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutX(x);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutY(y);
    }

    public void moveReleased(GameWindow gameWindow) {
        Point point = gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getPosition();
        gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).isOnFindGlass = false;
        int n = Integer.parseInt("20" + String.valueOf(gameWindow.getMoveCard()));
        gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getMap()[((int) point.y - 1) * 2][((int) point.x - 1) * 2] = n;
        glassToGlass(gameWindow.getCards().get(gameWindow.getMoveCard()), gameWindow.getCards());
        gameWindow.setMoveCard(false);
        if (gameWindow.getNotUsedCard() != -1) {
            gameWindow.newCard.setDisable(false);
            gameWindow.root.requestFocus();
        }
    }

    public void mouseReleased(MouseEvent event, GameWindow gameWindow) {
        boolean f = false;
        for (int i = 0; i < 4; i++) {
            Chip chip = gameWindow.getChips().get(i);
            if (chip.isSelected) {
                f = true;
                Point pointCard = chip.getPosition(event, true);
                Point point = chip.getPosition(event, false);
                if (isChipCanBeMoved(event, chip.getPosition(), chip.getPosition(event, true), chip, gameWindow)) {
                    if ((pointCard.y != -1) && (pointCard.x != -1)) {
                        gameWindow.getChips().get(i).setLayoutX(point.x);
                        gameWindow.getChips().get(i).setLayoutY(point.y);
                        gameWindow.getChips().get(i).toFront();
                        gameWindow.getChips().get(i).whereAreUNow(event);
                    }
                }
                chip.setDefault();
            }
        }
        if (!f) {
            for (int i = 0; i < 4; i++) {
                Chip chip = gameWindow.getChips().get(i);
                if ((event.getX() > chip.getLayoutX()) && (event.getX() < chip.getLayoutX() + chip.getImage().getWidth()) && (event.getY() > chip.getLayoutY()) && (event.getY() < chip.getLayoutY() + chip.getImage().getHeight())) {
                    chip.setClicked();
                }
            }
        }
    }

    private void rotateCard(GameWindow gameWindow, int x, int y) {
        while (gameWindow.getCards().get(gameWindow.getMoveCard()).getMap()[x][y] != 20) {
            gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
        }
    }

    private Card rotateMap(Card card) {
        int[][] map = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                map[i][j] = card.getMap()[6 - j][i];
            }
        }
        // ---------------------- ТУТ МОСТЫ
        int b = card.bridges;
        if (card.bridges != 0) {
            int bridge = b;
            b = 0;
            while (bridge != 0) {
                int a = bridge % 10000;
                int a1 = a % 100;
                a = a / 100;
                a = 10000 + 1000 * (a % 10) + 100 * (3 - a / 10) + 10 * (a1 % 10) + (3 - a1 / 10);
                b = b * 100000 + a;
                bridge = bridge / 100000;
            }
        }
        card.bridges = b;
        // --------------------- ТУТ МОСТЫ
        card.setMap(map);
        card.setRotate(card.getRotate() + 90);
        return card;
    }

    private void findClosestFindGlass(MouseEvent event, GameWindow gameWindow) {
        double min = gameWindow.getPane().getPrefWidth();
        double x, y;
        for (int i = 0; i < gameWindow.getFindGlasses().size(); i++) {
            int chipId = gameWindow.getFindGlasses().get(i);
            x = gameWindow.getChips().get(chipId).getLayoutX();
            y = gameWindow.getChips().get(chipId).getLayoutY();
            double length = Math.sqrt(Math.pow((event.getX() - x), 2) + Math.pow((event.getY() - y), 2));
            if (length < min) {
                min = length;
                gameWindow.setClosestFindGlassId(chipId);
            }
        }
    }
}
