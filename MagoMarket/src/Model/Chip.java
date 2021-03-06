package Model;

import Controller.GameWindow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Chip extends ImageView {

    boolean isSelected = false;
    public boolean isBusy = false;
    public boolean isOnFindGlass = false;
    public boolean isOnExit = false;
    public boolean isOnWeapon = false;
    GameWindow gameWindow;
    public String urlBusy;
    public String urlSelected;
    public String url;

    public Chip(String url, String urlSelected, String urlBusy, GameWindow gameWindow) {
        this.urlBusy = urlBusy;
        this.gameWindow = gameWindow;
        this.url = url;
        this.urlSelected = urlSelected;
    }

    void setClicked() {
        isSelected = true;
        setImage(new Image(urlSelected, 45, 45, true, true));
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
        if (busy)
             setImage(new Image(urlBusy, 45, 45, true, true));
        else
            setDefault();
    }

    void setDefault() {
        isSelected = false;
        setImage(new Image(url, 45, 45, true, true));
    }

    public void delete() {
        setVisible(false);
//        setLayoutX(gameWindow.getStage().getWidth() - 10);
//        setLayoutY(gameWindow.getStage().getHeight()/2 - 200);
        gameWindow.getChips().remove(this);
        gameWindow.getPane().getChildren().remove(this);
    }

    int getCardId() {
        int n = -1;
        for (int i = 0; i < gameWindow.getCards().size(); i++) {
            Card card = gameWindow.getCards().get(i);
            if (card.isUsed()) {
                final double layoutX = card.getLayoutX();
                final double layoutY = card.getLayoutY();
                final double width = card.getImage().getWidth();
                if ((getLayoutX() > layoutX) && (getLayoutX() < layoutX + width) && (getLayoutY() > layoutY) && (getLayoutY() < layoutY + width)) {
                    n = i;
                }
            }
        }
        return n;
    }

    Point getPosition() { //тек положение чипа
        double x, y;
        double width = (gameWindow.getCards().get(0).getImage().getWidth()) / 4;
        final double layoutX = gameWindow.getCards().get(getCardId()).getLayoutX();
        final double layoutY = gameWindow.getCards().get(getCardId()).getLayoutY();
        x = Math.ceil((getLayoutX() - layoutX) / (width));
        y = Math.ceil((getLayoutY() - layoutY) / (width));
        return new Point(x, y);
    }

}
