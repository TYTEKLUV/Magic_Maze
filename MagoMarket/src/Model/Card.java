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

    public void rotate(int angle) {
        for (int i = 0; i < angle / 90; i++) {
            rotateMap(this);
        }
    }

    private Card rotateMap(Card card) {
        int[][] map = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                map[i][j] = card.getMap()[6 - j][i];
            }
        }
        // ---------------------- ТУТ МОСТЫ
        int b = card.bridges;
        if (card.bridges != 0) {
            int bridge = b;
            b = 0;
            while (bridge != 0) {
                int a = bridge % 10000;
                int a1 = a % 100;
                a = a / 100;
                a = 10000 + 1000 * (a % 10) + 100 * (3 - a / 10) + 10 * (a1 % 10) + (3 - a1 / 10);
                b = b * 100000 + a;
                bridge = bridge / 100000;
            }
        }
        card.bridges = b;
        // --------------------- ТУТ МОСТЫ
        card.setMap(map);
        card.setRotate(card.getRotate() + 90);
        return card;
    }

    public Point layoutXY(int x, int y) {
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
