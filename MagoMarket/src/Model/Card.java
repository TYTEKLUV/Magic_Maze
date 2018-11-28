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
