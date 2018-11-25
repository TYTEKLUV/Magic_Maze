package Model;

public class Player {
    private int arrow;
    private boolean glass;
    private boolean portal;
    private boolean bridge;
    public Player (int arrow, boolean glass, boolean portal, boolean bridge ) {
        this.arrow = arrow;
        this.glass = glass;
        this.portal = portal;
        this.bridge = bridge;
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
}
