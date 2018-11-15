package Model;

import javafx.scene.image.ImageView;

public class Card extends ImageView {
    int floor[][];
    int wall[][];
    int door[][];
    String url;
    boolean isUsed;

    public Card(int floor[][], String url, boolean isUsed) {
        super();
        this.floor = floor;
        this.url = url;
        this.isUsed = isUsed;
    }

    public int[][] getFloor() {
        return floor;
    }

    public int[][] getWall() {
        return wall;
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
