package Model;

import javafx.scene.layout.Pane;

public class Role {
    private int countActions;
    private int arrow;
    private boolean glass;
    private boolean portal;
    private boolean bridge;
    private String nickname;
    private boolean ready;
    private Pane pane;
    public Role(int countActions, int arrow, boolean glass, boolean portal, boolean bridge, Pane pane) {
        this.countActions = countActions;
        this.arrow = arrow;
        this.glass = glass;
        this.portal = portal;
        this.bridge = bridge;
        this.pane = pane;
    }

    public int getArrow() {
        return arrow;
    }

    public boolean isBridge() {
        return bridge;
    }

    public boolean isGlass() {
        return glass;
    }

    public boolean isPortal() {
        return portal;
    }

    public void setPortal(boolean portal) {
        this.portal = portal;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public int getCountActions() {
        return countActions;
    }
}
