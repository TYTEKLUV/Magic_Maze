package Game.Model;

import Game.Controller.GameWindow;

import java.io.IOException;
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
        return gameWindow.getCards().get(event.getCardId(gameWindow)).getMap()[(int) point.y][(int) point.x] != 0;
    }

    private void glassRefactor(Point glass, GameWindow gameWindow) {
        glass = glass.getPosition(false, gameWindow);
        double width = gameWindow.getCards().get(0).getWidth() / 4 - 2.5;
        Point d = new Point(glass.x + width, glass.y).getPosition(false, gameWindow);
        changeGlass(d, glass, gameWindow);
        d = new Point(glass.x, glass.y + width).getPosition(false, gameWindow);
        changeGlass(d, glass, gameWindow);
        d = new Point(glass.x - width, glass.y).getPosition(false, gameWindow);
        changeGlass(d, glass, gameWindow);
        d = new Point(glass.x, glass.y - width).getPosition(false, gameWindow);
        changeGlass(d, glass, gameWindow);
    }

    private void changeGlass(Point d, Point glass, GameWindow gameWindow) {
        glass = glass.getPosition(false, gameWindow);
        if ((d.getCardId(gameWindow) != -1) && (d.getCardId(gameWindow) != glass.getCardId(gameWindow)) && (glass.getCardId(gameWindow) != -1)) {
            final double x = d.getPosition(true, gameWindow).x;
            final double y = d.getPosition(true, gameWindow).y;
            final double x2 = glass.getPosition(true, gameWindow).x;
            final double y2 = glass.getPosition(true, gameWindow).y;
            Point p = new Point(x, y).localToMap();
            Point p2 = new Point(x2, y2).localToMap();
            Card card = gameWindow.getCards().get(d.getCardId(gameWindow));
            Card card2 = gameWindow.getCards().get(glass.getCardId(gameWindow));
            if ((card.getMap()[(int) p.y][(int) p.x] >= 21) && (card.getMap()[(int) p.y][(int) p.x] <= 24)) {
                card.getMap()[(int) p.y][(int) p.x] = 20;
                card2.getMap()[(int) p2.y][(int) p2.x] = 20;
            } else if (card.getMap()[(int) p.y][(int) p.x] != 20) {
                glass = glass.getPosition(true, gameWindow);
                p = new Point(glass.x, glass.y).localToMap();
                card2.getMap()[(int) p.y][(int) p.x] = 10;
            }
        }
    }

    public void glassToGlass(ArrayList<Card> cards, GameWindow gameWindow) { //проверяем после того, как поставили карту
        for (int k = 0; k < cards.size(); k++) {
            for (int i = 0; i < cards.get(k).getMap().length; i++) {
                for (int j = 0; j < cards.get(k).getMap().length; j++) {
                    if ((cards.get(k).getMap()[i][j] >= 21) && (cards.get(k).getMap()[i][j] <= 24)) {
                        glassRefactor(cards.get(k).layoutXY(j / 2 + 1, i / 2 + 1), gameWindow);
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
            if (event.isOnPortal(end, chip, gameWindow)) {
                return true;
            }
            if ((d.x <= 2.5) && (d.y > 1)) {
                int c = (int) Math.round(d.y / 72.5);
                Point point = new Point(chip.getLayoutX(), chip.getLayoutY()).getPosition(false, gameWindow);
                double r = -gameWindow.getCards().get(0).getWidth() / 4;
                if (chip.getLayoutY() < event.y) {
                    r *= -1;
                }
                for (int i = 0; i < c; i++) {
                    Point pEnd = new Point(point.x, point.y + r).getPosition(false, gameWindow);
                    if (point.getCardId(gameWindow) != pEnd.getCardId(gameWindow)) {
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
                double r = -gameWindow.getCards().get(0).getWidth() / 4;
                if (chip.getLayoutX() < event.x) {
                    r *= -1;
                }
                for (int i = 0; i < c; i++) {
                    Point pEnd = new Point(point.x + r, point.y).getPosition(false, gameWindow);
                    if (point.getCardId(gameWindow) != pEnd.getCardId(gameWindow)) {
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
            return isChipOnBridge(event, chip, gameWindow);
        }
        return false;
    }

    public void mouseMoved(Point event, GameWindow gameWindow) {
        if (!gameWindow.getCards().get(gameWindow.getMoveCard()).isVisible()) {
            gameWindow.getCards().get(gameWindow.getMoveCard()).setVisible(true);
        }
        double x = 0, y = 0;
        findClosestGlass(event, gameWindow);
        String pos = String.valueOf((int) gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getPosition().x) + (int) gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getPosition().y;
        switch (pos) {
            case "31":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth() / 4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth();
                rotateCard(gameWindow, 6, 2);
                break;
            case "43":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth() / 4;
                rotateCard(gameWindow, 2, 0);
                break;
            case "24":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth() / 4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth();
                rotateCard(gameWindow, 0, 4);
                break;
            case "12":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getWidth() / 4;
                rotateCard(gameWindow, 4, 6);
                break;
        }
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutX(x);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutY(y);
    }

    public void moveReleased(GameWindow gameWindow, Point event) throws IOException {
        findClosestGlass(new Point(event.x + 150, event.y + 150), gameWindow);
        final int closestGlassId = gameWindow.getClosestFindGlassId();
        System.out.println("closestGlassId = " + closestGlassId);
        Point point = gameWindow.getChips().get(closestGlassId).getPosition();
        gameWindow.getChips().get(closestGlassId).isOnFindGlass = false;
        final int cardId = gameWindow.getChips().get(closestGlassId).getCardId();
        gameWindow.getCards().get(cardId).getMap()[((int) point.y - 1) * 2][((int) point.x - 1) * 2] = 20;
        glassToGlass(gameWindow.getCards(), gameWindow);
        for (int i = 0; i < gameWindow.getChips().size(); i++) {
            gameWindow.getChips().get(i).whereAreUNow();
        }
        gameWindow.setMoveCard(false);
        if (gameWindow.getNotUsedCard() != -1) {
            gameWindow.newCard.setDisable(false);
            gameWindow.root.requestFocus();
        }
    }

    public void mouseReleased(Point event, GameWindow gameWindow) throws IOException {
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
                        gameWindow.getChips().get(i).whereAreUNow();
                        gameWindow.getMain().sendAll("GAME MOVE " + i + " " + (int)point.x + " " + (int)point.y);
                    }
                }
                chip.setDefault();
            }
        }
        if (!f) {
            for (int i = 0; i < 4; i++) {
                Chip chip = gameWindow.getChips().get(i);
                if ((event.x > chip.getLayoutX()) && (event.x < chip.getLayoutX() + chip.getWidth()) && (event.y > chip.getLayoutY()) && (event.y < chip.getLayoutY() + chip.getWidth())) {
                    chip.setClicked();
                    gameWindow.getMain().sendAll("GAME SELECT " + i);
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

    public void findClosestGlass(Point event, GameWindow gameWindow) {
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
