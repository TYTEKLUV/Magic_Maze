package Model;

import Controller.GameWindow;

import java.util.ArrayList;

public class GameRules {


    private boolean isFloorIsEmpty(Point event, GameWindow gameWindow) {
        Point point = event.getPosition(true, gameWindow);
        for (int i = 0; i < 4; i++) {
            final Chip chip = gameWindow.getChips().get(i);
            if ((chip.getPosition().x == point.x) && (chip.getPosition().y == point.y) && (chip.getCardId() == event.getCardId(gameWindow))) {
                return false;
            }
        }
        point = point.localToMap();
        if (gameWindow.getCards().get(event.getCardId(gameWindow)).getMap()[(int) point.y][(int) point.x] == 0) {
            return false;
        }
        return true;
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
                                        cards.get(k).getMap()[z][t] = 20;
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
        int bridges = gameWindow.getCards().get(chip.getCardId()).getBridges();
        String s = String.valueOf(bridges).substring(1);
        if (bridges != 0) {
            int count = Integer.parseInt(String.valueOf(s.length())) / 4;
            for (int i = 0; i < count; i++) {
                int x1 = Integer.parseInt(String.valueOf(s.charAt(i))) + 1;
                int y1 = Integer.parseInt(String.valueOf(s.charAt(i + 1))) + 1;
                int x2 = Integer.parseInt(String.valueOf(s.charAt(i + 2))) + 1;
                int y2 = Integer.parseInt(String.valueOf(s.charAt(i + 3))) + 1;
                if ((chip.getPosition().y == x1) && (chip.getPosition().x == y1) && (event.getPosition(true, gameWindow).y == x2) && (event.getPosition(true, gameWindow).x == y2) ||
                        ((chip.getPosition().y == x2) && (chip.getPosition().x == y2) && (event.getPosition(true, gameWindow).y == x1) && (event.getPosition(true, gameWindow).x == y1))) {
                    return true;
                }
            }
        }
        return false;
    }

    Point calcD(Point start, Point end) {
        double dx = (Math.abs(end.x - start.x));
        double dy = (Math.abs(end.y - start.y));
        return new Point(dx, dy);
    }

    private boolean isChipMovable(Point event, Point start, Point end, Chip chip, GameWindow gameWindow) {
        Point minPoint = start;
        if ((end.x < start.x) || (end.y < start.y)) {
            minPoint = end;
        }
        Point d = calcD(new Point(chip.getLayoutX(), chip.getLayoutY()).getPosition(false, gameWindow), (event.getPosition(false, gameWindow)));
        if ((minPoint.x != -1) && (minPoint.y != -1) && (isFloorIsEmpty(event, gameWindow))) {
            if ((d.x <= 2.5) && (d.y > 1)) {
                int c = (int) Math.round(d.y / 72.5);
                Point point = new Point(chip.getLayoutX(), chip.getLayoutY()).getPosition(false, gameWindow);
                double r = - gameWindow.getCards().get(0).getImage().getWidth()/4;
                if (chip.getLayoutY() < event.y) {
                    r *= -1;
                }
                for (int i = 0; i < c; i++) {
                    Point pEnd = new Point(point.x, point.y + r).getPosition(false, gameWindow);
                    if (point.getCardId(gameWindow) != pEnd.getCardId(gameWindow)){
                        pEnd = new Point(point.x + 2.5, point.y + r + 10).getPosition(false, gameWindow);
                    }
                    if (!(isFloorIsEmpty(pEnd, gameWindow) && (point.isOnLine(pEnd, gameWindow, this)))) {
                        return false;
                    }
                    point.y += r;
                    point = point.getPosition(false, gameWindow);
                }
                return true;
            } else if (((d.x > 1) && (d.y <= 2.5))) {
                int c = (int) Math.round(d.x / 72.5);
                Point point = new Point(chip.getLayoutX(), chip.getLayoutY()).getPosition(false, gameWindow);
                double r = - gameWindow.getCards().get(0).getImage().getWidth()/4;
                if (chip.getLayoutX() < event.x) {
                    r *= -1;
                }
                for (int i = 0; i < c; i++) {
                    Point pEnd = new Point(point.x + r, point.y).getPosition(false, gameWindow);
                    if (point.getCardId(gameWindow) != pEnd.getCardId(gameWindow)){
                        pEnd = new Point(point.x + r + 10, point.y + 2.5).getPosition(false, gameWindow);
                    }
                    if (!(isFloorIsEmpty(pEnd, gameWindow) && (point.isOnLine(pEnd, gameWindow, this)))) {
                        return false;
                    }
                    point.x += r;
                    point = point.getPosition(false, gameWindow);
                }
                return true;
            }
            if (event.isOnPortal(end, chip, gameWindow)) {
                return true;
            } else if (isChipOnBridge(event, chip, gameWindow)) {
                return true;
            }
//                //транспортные задачи, зажачи потоков
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
        final int closestGlassId = gameWindow.getClosestFindGlassId();
        Point point = gameWindow.getChips().get(closestGlassId).getPosition();
        gameWindow.getChips().get(closestGlassId).isOnFindGlass = false;
        final int cardId = gameWindow.getChips().get(closestGlassId).getCardId();
        gameWindow.getCards().get(cardId).getMap()[((int) point.y - 1) * 2][((int) point.x - 1) * 2] = 20;
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
                Point pointCard = event.getPosition(true, gameWindow);
                Point point = event.getPosition(false, gameWindow);
                if (isChipMovable(event, chip.getPosition(), event.getPosition(true, gameWindow), chip, gameWindow)) {
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
