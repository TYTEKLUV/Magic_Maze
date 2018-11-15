package Model;

import javafx.scene.image.ImageView;

public class Card extends ImageView {
    int floor[][];
    int wall[][];
    int door[][];
    String url;

    public Card(int floor[][], String url) {
        super();
        this.floor = floor;
        this.wall = wall;
        this.url = url;
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
}
