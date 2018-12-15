package Model;

import javafx.scene.layout.Pane;

public class Role {

    private int countActions;
    private int arrow;
    private boolean glass;
    private boolean portal;
    private boolean bridge;
    private Pane pane;

    public Role(int countActions, int arrow, boolean glass, boolean portal, boolean bridge, Pane pane) {
        this.countActions = countActions;
        this.arrow = arrow;
        this.glass = glass;
        this.portal = portal;
        this.bridge = bridge;
        this.pane = pane;
    }

    int getArrow() {
        return arrow;
    }

    boolean isBridge() {
        return bridge;
    }

    boolean isGlass() {
        return glass;
    }

    boolean isPortal() {
        return portal;
    }

    public Pane getPane() {
        return pane;
    }

    void setPane(Pane pane) {
        this.pane = pane;
    }

    public int getCountActions() {
        return countActions;
    }
}
