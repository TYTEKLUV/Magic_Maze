package Model;

import Controller.GameWindow;
import javafx.scene.image.Image;

public class Point {
    double x, y;
    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    Point getPosition(boolean isInCard, Chip chip) { //в карте
        double x = -1, y = -1;
        int n = getCardId(chip);
        if (n >= 0) {
            Card card = chip.gameWindow.getCards().get(n);
            final double layoutX = card.getLayoutX();
            final double layoutY = card.getLayoutY();
            double width = (card.getImage().getWidth())/4;
            x = Math.ceil((this.x - layoutX)/(width));
            y = Math.ceil((this.y - layoutY)/(width));
            if (!isInCard) {
                width = (card.getImage().getWidth() - 10)/4;
                final Image image = chip.gameWindow.getChips().get(0).getImage();
                x = layoutX + 5 + (x - 1) * (width) + (Math.floor((width)/ 2) - Math.floor((image.getWidth()) / 2));
                y = layoutY + 5 + (y - 1) * (width) + (Math.floor((width)/ 2) - Math.floor((image.getWidth()) / 2));
            }
        }
        return new Point(x, y);
    }

    boolean isOnArrow(Point start, Point end, Chip chip, GameWindow gameWindow) {
        final int x = (int) (start.y - 1) * 2;
        final int y = (int) (start.x - 1) * 2;
        final Card cardEvent = gameWindow.getCards().get(getCardId(chip));
        final Card card = gameWindow.getCards().get(chip.getCardId());
        if ((cardEvent.getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == 20)) {
            int n = Integer.parseInt("20" + getCardId(chip));
            return card.getMap()[x][y] == n;
        } else {
            int n = Integer.parseInt("20" + chip.getCardId());
            if (cardEvent.getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == n) {
                return card.getMap()[x][y] == 20;
            }
        }
        return false;
    }

    boolean isOnPortal(Point end, Chip chip, GameWindow gameWindow) {
        int ind = Integer.parseInt("3" + String.valueOf(gameWindow.getChips().indexOf(chip) + 1));
        final Card card = gameWindow.getCards().get(getCardId(chip));
        System.out.println(ind);
        System.out.println(card.getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2]);
        return card.getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == ind;
    }

    public boolean isOnLine(Point start, Point end, Chip chip, GameWindow gameWindow, GameRules gameRules) {
        double dx = gameRules.calcD(start, end).x;
        double dy = gameRules.calcD(start, end).y;
        System.out.println("start " + start.x + " " + start.y);
        Point minPoint = start;
        if ((end.x < start.x) || (end.y < start.y)) {
            minPoint = end;
        }
        final int cardId = getCardId(chip);
        if ((dx == 0) && (chip.getCardId() == cardId)) {
            if (dy == 1) {
                if (gameWindow.getCards().get(cardId).getMap()[(int) (minPoint.y - 1) * 2 + 1][(int) (minPoint.x - 1) * 2] != 0) {
                    return true;
                }
            }
        } else if ((dx == 1) && (chip.getCardId() == cardId)) {
            if (dy == 0) {
                if (gameWindow.getCards().get(cardId).getMap()[(int) (minPoint.y - 1) * 2][(int) (minPoint.x - 1) * 2 + 1] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    int getCardId(Chip chip) {
        int n = -1;
        for(int i = 0; i < chip.gameWindow.getCards().size(); i ++) {
            Card card = chip.gameWindow.getCards().get(i);
            if(card.isUsed()) {
                final double layoutX = card.getLayoutX();
                final double layoutY = card.getLayoutY();
                final double width = card.getImage().getWidth();
                if ((this.x > layoutX) && (this.x < layoutX + width) && (this.y > layoutY) && (this.y < layoutY + width)) {
                    n = i;
                }
            }
        }
        return n;
    }
}
