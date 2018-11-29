package Model;

import javafx.scene.image.ImageView;

public class Card extends ImageView {
    int map[][];
    int bridges;
    String url;
    boolean isUsed;

    public Card(int map[][], String url, int bridges) {
        super();
        this.map = map;
        this.url = url;
        this.bridges = bridges;
    }

    public Point layoutXY(int x, int y) {
        x = (int)(getLayoutX() + getImage().getWidth()/4*x);
        y = (int)(getLayoutY() + getImage().getWidth()/4*y);
        return new Point(x, y);
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public String getUrl() {
        return url;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
