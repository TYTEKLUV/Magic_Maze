package Model;

import javafx.scene.image.ImageView;

public class Chip extends ImageView {
    boolean isSelected;
    public Chip(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
