package Model;

import Controller.GameWindow;

import java.util.ArrayList;

public class GameRules {

    private boolean isFloorIsEmpty(Point event, Point point, Chip chip, GameWindow gameWindow) {
        boolean f = true;
        for (int i = 0; i < 4; i++) {
            if ((gameWindow.getChips().get(i).getPosition().x == point.x) && (gameWindow.getChips().get(i).getPosition().y == point.y) && (gameWindow.getChips().get(i).getCardId() == event.getCardId(chip))) {
                f = false;
            }
        }
        return f;
    }

    private void glassToGlass(Card card, ArrayList<Card> cards) { //проверяем после того, как поставили карту
        for (int i = 0; i < card.getMap().length; i++) {
            for (int j = 0; j < card.getMap().length; j++) {
                if ((card.getMap()[i][j] >= 21) && (card.getMap()[i][j] <= 24)) {
                    for (int k = 0; k < cards.size(); k++) {//карты
                        System.out.println();
                        for (int z = 0; z < card.getMap().length; z++) {
                            for (int t = 0; t < card.getMap().length; t++) {
                                if ((cards.get(k).getMap()[z][t] >= 21) && (cards.get(k).getMap()[z][t] <= 24) && (i != z) && (j != t)) {
                                    int w = (int) Math.abs(cards.get(k).layoutXY(t / 2 + 1, z / 2 + 1).x - card.layoutXY(j / 2 + 1, i / 2 + 1).x);
                                    int h = (int) Math.abs(cards.get(k).layoutXY(t / 2 + 1, z / 2 + 1).y - card.layoutXY(j / 2 + 1, i / 2 + 1).y);
                                    if ((w <= card.getImage().getWidth() / 4) && (h <= card.getImage().getHeight() / 4)) {
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

    private boolean isChipOnBridge(Point event, Chip chip, GameWindow gameWindow) {
        boolean f = false;
        int bridges = gameWindow.getCards().get(chip.getCardId()).getBridges();
        String s = String.valueOf(bridges).substring(1);
        if (bridges != 0) {
            int count = Integer.parseInt(String.valueOf(s.length())) / 4;
            for (int i = 0; i < count; i++) {
                int x1 = Integer.parseInt(String.valueOf(s.charAt(i))) + 1;
                int y1 = Integer.parseInt(String.valueOf(s.charAt(i + 1))) + 1;
                int x2 = Integer.parseInt(String.valueOf(s.charAt(i + 2))) + 1;
                int y2 = Integer.parseInt(String.valueOf(s.charAt(i + 3))) + 1;
                System.out.println(chip.getPosition().y + " " + chip.getPosition().x);
                if ((chip.getPosition().y == x1) && (chip.getPosition().x == y1) && (event.getPosition(true, chip).y == x2) && (event.getPosition(true, chip).x == y2) ||
                        ((chip.getPosition().y == x2) && (chip.getPosition().x == y2) && (event.getPosition(true, chip).y == x1) && (event.getPosition(true, chip).x == y1))) {
                    f = true;
                }
            }
        }
        return f;
    }

    public Point calcD(Point start, Point end) {
        double dx = (Math.abs(end.x - start.x));
        double dy = (Math.abs(end.y - start.y));
        return new Point(dx, dy);
    }

    private boolean isOnLine(Point event, Point dOld, Point start, Point end, Chip chip, GameWindow gameWindow) {
        if ((calcD(start, end).x == dOld.x) && (calcD(start, end).y == dOld.y)) {
            if (isFloorIsEmpty(event, end, chip, gameWindow))
                if (event.isOnLine(start, end, chip, gameWindow, this))
                    return true;
        }
        return false;
    }

    private boolean isChipMovable(Point event, Point start, Point end, Chip chip, GameWindow gameWindow) {
        if (isFloorIsEmpty(event, end, chip, gameWindow)) {
            Point minPoint = start, maxPoint = end;
            if ((end.x < start.x) || (end.y < start.y)) {
                minPoint = end;
                maxPoint = start;
            }
            if ((minPoint.x != -1) && (minPoint.y != -1)) {
                if (event.isOnLine(start, end, chip, gameWindow, this)) {
                    return true;
                } else if (event.isOnPortal(end, chip, gameWindow)) {
                    return true;
                } else if (event.isOnArrow(start, end, chip, gameWindow)) {
                    return true;
                } else if (isChipOnBridge(event, chip, gameWindow)) {
                    return true;
                }
                //транспортные задачи, зажачи потоков
                else {
                    Point d = calcD(new Point(chip.getLayoutX(), chip.getLayoutY()).getPosition(false, chip), (event.getPosition(false, chip)));
                    //System.out.println("d " + d.x + " y " + d.y);
                    if ((d.x <= 2.5) && (d.y > 1)) {
                        int c = (int)Math.round(d.y / 72.5);
                        System.out.println("c "+ c);
                        Point point = new Point(chip.getLayoutX(), chip.getLayoutY());
                        for (int i = 0; i < c; i++) {
                            Point pointCard = point.getPosition(true, chip);
                            int cardId = point.getCardId(chip);
                            System.out.println("cardId " + cardId);
                            if (gameWindow.getCards().get(cardId).getMap()[(int) pointCard.y][(int) pointCard.x] != 0) {
                                if (isFloorIsEmpty(event, point, chip, gameWindow)) {
                                    return true;
                                }
                            }
                            //if (pointCard.isOnLine())
                            point.x += 72.5;
                            point = point.getPosition(false, chip);


                        }
//                        if (event.isOnLine(minPoint, new Point(minPoint.x, minPoint.y + 1), chip, gameWindow, this))
//                            if (isFloorIsEmpty(event, new Point(minPoint.x, minPoint.y + 1), chip, gameWindow)) {
//                            for (int i = (int) minPoint.y; i < (int) maxPoint.y - 2; i++) {
//                                Point old = new Point(calcD(minPoint, new Point(minPoint.x, i + 1)).x, calcD(minPoint, new Point(minPoint.x, i + 1)).y);
//                                Point s = new Point(minPoint.x, minPoint.y + i);
//                                Point e = new Point(minPoint.x, minPoint.y + i + 1);
//                                    if (!isOnLine(event, old, s, e, chip, gameWindow)) {
//                                    return false;
//                                }
//                            }
//                            return true;
//                        }
                    } else if ((d.x > 1) && (d.y == 0)) {
                            if (event.isOnLine(minPoint, new Point(minPoint.x + 1, minPoint.y), chip, gameWindow, this))
                                if (isFloorIsEmpty(event, new Point(minPoint.x + 1, minPoint.y), chip, gameWindow)) {
                                    for (int i = (int) minPoint.x; i < (int) maxPoint.x - 2; i++) {
                                        Point old = new Point(calcD(minPoint, new Point(i + 1, minPoint.y)).x, calcD(minPoint, new Point(i + 1, minPoint.y)).y);
                                        Point s = new Point(minPoint.x + i, minPoint.y );
                                        Point e = new Point(minPoint.x + i + 1, minPoint.y);
                                        if (!isOnLine(event, old, s, e, chip, gameWindow)) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                        }
                }
        }
        }
        return false;
    }

    public void mouseMoved(Point event, GameWindow gameWindow) {
        if (!gameWindow.getCards().get(gameWindow.getMoveCard()).isVisible()) {
            gameWindow.getCards().get(gameWindow.getMoveCard()).setVisible(true);
        }
        double x = 0, y = 0;
        gameWindow.setClosestFindGlassId(-1);
        findClosestGlass(event, gameWindow);
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

    public void mouseReleased(Point event, GameWindow gameWindow) {
        boolean f = false;
        for (int i = 0; i < 4; i++) {
            Chip chip = gameWindow.getChips().get(i);
            if (chip.isSelected) {
                f = true;
                Point pointCard = event.getPosition(true, chip);
                Point point = event.getPosition(false, chip);
                if (isChipMovable(event, chip.getPosition(), event.getPosition(true, chip), chip, gameWindow)) {
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
                if ((event.x > chip.getLayoutX()) && (event.x < chip.getLayoutX() + chip.getImage().getWidth()) && (event.y > chip.getLayoutY()) && (event.y < chip.getLayoutY() + chip.getImage().getHeight())) {
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

    private void findClosestGlass(Point event, GameWindow gameWindow) {
        double min = gameWindow.getPane().getPrefWidth();
        double x, y;
        for (int i = 0; i < gameWindow.getFindGlasses().size(); i++) {
            int chipId = gameWindow.getFindGlasses().get(i);
            x = gameWindow.getChips().get(chipId).getLayoutX();
            y = gameWindow.getChips().get(chipId).getLayoutY();
            double length = Math.sqrt(Math.pow((event.x - x), 2) + Math.pow((event.y - y), 2));
            if (length < min) {
                min = length;
                gameWindow.setClosestFindGlassId(chipId);
            }
        }
    }
}
