package Model;

import Controller.GameWindow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Chip extends ImageView {

    boolean isSelected  = false;
    public boolean isOnFindGlass = false;
    public boolean  isOnExit   = false;
    public  boolean  isOnWeapon = false;
    GameWindow gameWindow;
    public String urlSelected;
    public String url;

    public Chip(String url, String urlSelected, GameWindow gameWindow) {
       this.gameWindow = gameWindow;
       this.url = url;
       this.urlSelected = urlSelected;
    }
    public void setClicked () {
        isSelected = true;
        setImage(new Image(urlSelected, 45, 45, true, true));
    }

    public void setDefault() {
        isSelected = false;
        setImage(new Image(url, 45, 45, true, true));
    }
    
    void whereAreUNow(MouseEvent event) {
        isOnExit   = false;
        isOnWeapon = false;
        isOnFindGlass = false;
        double x = -1, y = -1;
        Point point = getPosition(event, true);
        x = (point.x - 1) * 2;
        y = (point.y - 1) * 2;
        int z = gameWindow.getCards().get(getCardId(event)).getMap()[(int)y][(int)x];
        if (String.valueOf(z).equals(String.valueOf(2) + String.valueOf(gameWindow.getChips().indexOf(this) + 1))){
            isOnFindGlass = true;
        } else {
            if ((z >= 51)&&(z <= 54)) {
                isOnWeapon = true;
            } else {
                if ((z >= 61)&&(z <= 64)) {
                    isOnExit = true;
                }
            }
        }
    }

    int getCardId(MouseEvent event) {
        int n = -1;
        for(int i = 0; i < gameWindow.getCards().size(); i ++) {
            Card card = gameWindow.getCards().get(i);
            if(card.isUsed()) {
                if ((event.getX() > card.getLayoutX()) && (event.getX() < card.getLayoutX() + card.getImage().getWidth()) && (event.getY() > card.getLayoutY()) && (event.getY() < card.getLayoutY() + card.getImage().getHeight())) {
                    n = i;
                }
            }
        }
        return n;
    }

    int getCardId() {
        int n = -1;
        for(int i = 0; i < gameWindow.getCards().size(); i ++) {
            Card card = gameWindow.getCards().get(i);
            if(card.isUsed()) {
                if ((getLayoutX() > card.getLayoutX()) && (getLayoutX() < card.getLayoutX() + card.getImage().getWidth()) && (getLayoutY() > card.getLayoutY()) && (getLayoutY() < card.getLayoutY() + card.getImage().getHeight())) {
                    n = i;
                }
            }
        }
        return n;
    }

    Point getPosition() { //тек положение чипа
        double x, y;
        double width = gameWindow.getCards().get(0).getImage().getWidth()/4;
        double r = width/2 - getImage().getWidth()/2;
        x = Math.ceil((getLayoutX() - r + width - gameWindow.getCards().get(getCardId()).getLayoutX()) / width);
        y = Math.ceil((getLayoutY() - r + width - gameWindow.getCards().get(getCardId()).getLayoutY()) / width);
        return new Point(x, y);
    }

    Point getPosition(MouseEvent event, boolean isInCard) { //в карте
        double x = -1, y = -1;
        int n = getCardId(event);
        if (n >= 0) {
            Card card = gameWindow.getCards().get(n);
            double width = card.getImage().getWidth()/4;
            x = Math.ceil((event.getX() - card.getLayoutX())/(width));
            y = Math.ceil((event.getY() - card.getLayoutY())/(width));
            if (!isInCard) {
                x = card.getLayoutX() + x * width - width + (Math.floor(width / 2) - Math.floor(gameWindow.getChips().get(0).getImage().getWidth() / 2));
                y = card.getLayoutY() + y * width - width + (Math.floor(width / 2) - Math.floor(gameWindow.getChips().get(0).getImage().getWidth() / 2));
            }

        }
        return new Point(x, y);
    }

}
