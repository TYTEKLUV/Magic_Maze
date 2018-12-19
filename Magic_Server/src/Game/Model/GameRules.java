package Game.Model;

import Game.Controller.GameWindow;

import java.io.IOException;
import java.util.ArrayList;

public class GameRules {

    private boolean isEveryoneLeft(GameWindow gameWindow) {
//        for(int i = 0; i < gameWindow.getChips().size(); i ++) {
//            if (!gameWindow.getChips().get(i).isOnExit){
//                return false;
//            }
//        }
        return gameWindow.getChips().isEmpty();
    }

    private boolean isWeaponsReceived(GameWindow gameWindow) {
        for(int i = 0; i < gameWindow.getChips().size(); i ++) {
            if (!gameWindow.getChips().get(i).isOnWeapon){
                return false;
            }
        }
        return true;
    }

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

    public boolean rulesCheck(int id, Point event, GameWindow gameWindow, int role) {
        if (portalRule(id, event, gameWindow, role)&&(!gameWindow.isWeaponsReceived())){
            System.out.println("POOORTAL POWER");
            return true;
        }
        if (bridgeRule(id, event, gameWindow, role)) {
            System.out.println("BRIIIDGE POWER");
            return true;
        }
        if (getArrowRotate(id, event, gameWindow, role)) {
            System.out.println("ARROW POWER");
            return true;
        }
        return false;
    }

    private boolean timeRule(int id, Point event, GameWindow gameWindow) {
        int c = gameWindow.getChips().get(id).getCardId();
        return gameWindow.getCards().get(c).getMap()[(int) event.getPosition(true, gameWindow).localToMap().y][(int) event.getPosition(true, gameWindow).localToMap().x] == 40;
    }

    private boolean portalRule(int id, Point event, GameWindow gameWindow, int role) {
        Chip chip = gameWindow.getChips().get(id);
        if (gameWindow.getRoles().get(role).isPortal()) {
//            if (event.isOnPortal(chip, gameWindow)) {
                return true;
//            }
        }
        return false;
    }


    private boolean bridgeRule(int id, Point event, GameWindow gameWindow, int role) {
        Chip chip = gameWindow.getChips().get(id);
        if (gameWindow.getRoles().get(role).isBridge()) {
            if (event.isOnBridge(chip, gameWindow)) {
                return true;
            }
        }
        return false;
    }

    private boolean getArrowRotate(int id, Point event, GameWindow gameWindow, int role) {
        Chip chip = gameWindow.getChips().get(id);
        Point p = new Point(chip.getLayoutX(), chip.getLayoutY()).getPosition(false, gameWindow);
        Point e = new Point(event.x, event.y).getPosition(false, gameWindow);
        Point d = new Point(p.x - e.x, p.y - e.y);
        System.out.println("d " + d.x + " " + d.y);
        System.out.println("arr " + gameWindow.getRoles().get(role).getArrow());
        String arrow = String.valueOf(gameWindow.getRoles().get(role).getArrow());
        int length = String.valueOf(arrow).length();
        double n = 2.5;
        for (int i = 0; i < length; i ++) {
            if ((Math.abs(d.x) <= n)&&(d.y > 0)) {
                if (Integer.parseInt(String.valueOf(arrow.charAt(i))) == 1) {
                    return true;
                }
            }
            else
            if ((d.x < 0)&&(Math.abs(d.y) <= n)) {
                if (Integer.parseInt(String.valueOf(arrow.charAt(i))) == 2) {
                    return true;
                }
            }
            else
            if ((Math.abs(d.x) <= n)&&(d.y < 0)) {
                if (Integer.parseInt(String.valueOf(arrow.charAt(i))) == 3) {
                    return true;
                }
            }
            else
            if ((d.x > 0)&&(Math.abs(d.y) <= n)) {
                if (Integer.parseInt(String.valueOf(arrow.charAt(i))) == 4) {
                    return true;
                }
            }
        }
        return false;
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
            if (event.isOnPortal(chip, gameWindow)) {
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
            if (event.isOnBridge(chip, gameWindow)) {
                System.out.println("on bridge");
                return true;
            }

        }
        return false;
    }

    public void moveReleased(GameWindow gameWindow, Point event) {
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

    public void chipMove(int id, Point event, GameWindow gameWindow, int role) throws IOException {
        gameWindow.getChips().get(id).isSelected = true;
        for (int i = 0; i < 4; i++) {
            Chip chip = gameWindow.getChips().get(i);
            if (chip.isSelected) {
                Point pointCard = event.getPosition(true, gameWindow);
                Point point = event.getPosition(false, gameWindow);
                Point pointChip = new Point(chip.getLayoutX(), chip.getLayoutY()).getPosition(false, gameWindow);
                if (pointChip.x == point.x && pointChip.y == point.y) {
                    gameWindow.getMain().sendAll("GAME MOVE " + i + " " + (int) point.x + " " + (int) point.y);
                } else {
                    if ((isChipMovable(event, chip.getPosition(), event.getPosition(true, gameWindow), chip, gameWindow))&&(rulesCheck(id, event, gameWindow, role))) {
                        if ((pointCard.y != -1) && (pointCard.x != -1)) {
                            gameWindow.getChips().get(i).setLayoutX(point.x);
                            gameWindow.getChips().get(i).setLayoutY(point.y);
                            gameWindow.getChips().get(i).toFront();
                            gameWindow.getChips().get(i).whereAreUNow();
                            gameWindow.getMain().sendAll("GAME MOVE " + i + " " + (int) point.x + " " + (int) point.y);
                            if (timeRule(id, event, gameWindow))
                                gameWindow.getMain().swapTimer();
                        }
                    }
                    if (isWeaponsReceived(gameWindow) && !gameWindow.isWeaponsReceived()) {
                        System.out.println("TEKAEM PATZANI!");
                        gameWindow.setWeaponsReceived(true);
                        //запретить телепорт, разрешить выход
                    }
                    else if (chip.isOnExit && isWeaponsReceived(gameWindow)) {
                        chip.delete();
                        gameWindow.getMain().sendAll("GAME DISAPPEAR " + gameWindow.getChips().indexOf(chip));
                    }
                    else
                    if (isEveryoneLeft(gameWindow) && gameWindow.isWeaponsReceived()) {
                        System.out.println("ETO WIN!");
                    }
                    chip.setDefault();
                }
            }
        }
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
