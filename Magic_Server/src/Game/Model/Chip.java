package Game.Model;

import Game.Controller.GameWindow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Chip extends ImageView {

    boolean isSelected = false;
    public boolean isOnFindGlass = false;
    public boolean isOnExit = false;
    public boolean isOnWeapon = false;
    GameWindow gameWindow;
    public String urlSelected;
    public String url;
    private double width = 45;

    public Chip(String url, String urlSelected, GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.url = url;
        this.urlSelected = urlSelected;
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    void setClicked() {
        isSelected = true;
        setImage(new Image(urlSelected, 45, 45, true, true));
    }

    void setDefault() {
        isSelected = false;
        setImage(new Image(url, 45, 45, true, true));
    }

    void whereAreUNow() {
        Point point = new Point(getLayoutX(), getLayoutY()).getPosition(false, gameWindow);
        isOnExit = false;
        isOnWeapon = false;
        isOnFindGlass = false;
        double x = -1, y = -1;
        Point point2 = point.getPosition(true, gameWindow);
        x = (point2.x - 1) * 2;
        y = (point2.y - 1) * 2;
        int z = gameWindow.getCards().get(point.getCardId(gameWindow)).getMap()[(int) y][(int) x];
        if (String.valueOf(z).equals(2 + String.valueOf(gameWindow.getChips().indexOf(this) + 1))) {
            isOnFindGlass = true;
        } else {
            if ((z >= 51) && (z <= 54)) {
                isOnWeapon = true;
            } else {
                if ((z >= 61) && (z <= 64)) {
                    isOnExit = true;
                }
            }
        }
    }

    int getCardId() {
        int n = -1;
        for (int i = 0; i < gameWindow.getCards().size(); i++) {
            Card card = gameWindow.getCards().get(i);
            if (card.isUsed()) {
                final double layoutX = card.getLayoutX();
                final double layoutY = card.getLayoutY();
                final double width = card.getWidth();
                if ((getLayoutX() > layoutX) && (getLayoutX() < layoutX + width) && (getLayoutY() > layoutY) && (getLayoutY() < layoutY + width)) {
                    n = i;
                }
            }
        }
        return n;
    }

    Point getPosition() { //тек положение чипа
        double x, y;
        double width = (gameWindow.getCards().get(0).getWidth()) / 4;
        final double layoutX = gameWindow.getCards().get(getCardId()).getLayoutX();
        final double layoutY = gameWindow.getCards().get(getCardId()).getLayoutY();
        x = Math.ceil((getLayoutX() - layoutX) / (width));
        y = Math.ceil((getLayoutY() - layoutY) / (width));
        return new Point(x, y);
    }

}
