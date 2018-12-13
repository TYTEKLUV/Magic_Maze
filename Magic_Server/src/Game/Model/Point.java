package Game.Model;

import Game.Controller.GameWindow;
import javafx.scene.image.Image;

public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point getPosition(boolean isInCard, GameWindow gameWindow) { //в карте
        double x = -1, y = -1;
        int n = getCardId(gameWindow);
        if (n >= 0) {
            Card card = gameWindow.getCards().get(n);
            final double layoutX = card.getLayoutX();
            final double layoutY = card.getLayoutY();
            double width = (card.getWidth()) / 4;
            x = Math.ceil((this.x - layoutX) / (width));
            y = Math.ceil((this.y - layoutY) / (width));
            if (!isInCard) {
                width = (card.getWidth() - 10) / 4;
                final Chip chip = gameWindow.getChips().get(0);
                x = layoutX + 5 + (x - 1) * (width) + (Math.floor((width) / 2) - Math.floor((chip.getWidth()) / 2));
                y = layoutY + 5 + (y - 1) * (width) + (Math.floor((width) / 2) - Math.floor((chip.getWidth()) / 2));
            }
        }
        return new Point(x, y);
    }

    boolean isOnPortal(Point end, Chip chip, GameWindow gameWindow) {
        int ind = Integer.parseInt("3" + String.valueOf(gameWindow.getChips().indexOf(chip) + 1));
        final Card card = gameWindow.getCards().get(getCardId(gameWindow));
        return card.getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2] == ind;
    }

    public boolean isOnLine(Point end, GameWindow gameWindow, GameRules gameRules) {
        getPosition(false, gameWindow);
        end = end.getPosition(false, gameWindow);
        double dx = gameRules.calcD(this, end).x;
        double dy = gameRules.calcD(this, end).y;
        Point minPoint = this;
        if ((end.x < this.x) || (end.y < this.y)) {
            minPoint = end;
        }
        final int cardId = end.getCardId(gameWindow);
        minPoint = minPoint.getPosition(true, gameWindow);
        end = end.getPosition(true, gameWindow);
        if ((dx <= 2.5)) {
            if ((dy == 72.5)) {
                if (gameWindow.getCards().get(cardId).getMap()[(int) (minPoint.y - 1) * 2 + 1][(int) (minPoint.x - 1) * 2] != 0) {
                    return true;
                }
            } else if (dy == 82.5) {
                int place = gameWindow.getCards().get(cardId).getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2];
                if (place == 20) {
                    return true;
                }
            }
        } else if ((dy <= 2.5)) {
            if (dx == 72.5) {
                if (gameWindow.getCards().get(cardId).getMap()[(int) (minPoint.y - 1) * 2][(int) (minPoint.x - 1) * 2 + 1] != 0) {
                    return true;
                }
            } else if (dx == 82.5) {
                int place = gameWindow.getCards().get(cardId).getMap()[(int) (end.y - 1) * 2][(int) (end.x - 1) * 2];
                if (place == 20) {
                    return true;
                }
            }
        }
        return false;
    }

    int getCardId(GameWindow gameWindow) {
        int n = -1;
        for (int i = 0; i < gameWindow.getCards().size(); i++) {
            Card card = gameWindow.getCards().get(i);
            if (card.isUsed()) {
                final double layoutX = card.getLayoutX();
                final double layoutY = card.getLayoutY();
                final double width = card.getWidth();
                if ((x > layoutX) && (x < layoutX + width) && (y > layoutY) && (y < layoutY + width)) {
                    n = i;
                }
            }
        }
        return n;
    }

    Point localToMap() {
        x = (x - 1) * 2;
        y = (y - 1) * 2;
        return this;
    }
}
