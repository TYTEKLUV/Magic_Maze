package Model;

import javafx.scene.image.ImageView;

public class Card extends ImageView {
    private int map[][];
    int bridges;
    private String url;
    private boolean isUsed;

    public Card(int map[][], String url, int bridges) {
        super();
        this.map = map;
        this.url = url;
        this.bridges = bridges;
    }

    Point layoutXY(int x, int y) {
        x = (int)(getLayoutX() + getImage().getWidth()/4*x);
        y = (int)(getLayoutY() + getImage().getWidth()/4*y);
        return new Point(x, y);
    }

    int getBridges() {
        return bridges;
    }

    int[][] getMap() {
        return map;
    }

    void setMap(int[][] map) {
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
