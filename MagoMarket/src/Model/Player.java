package Model;

public class Player {
    private String nickname = "NOT_CONNECTED";
    private boolean ready = false;
    private int role = -1;

    public Player() {

    }

    public Player(int role) {
        this.role = role;
    }

    public void set(String nickname, boolean ready, int role) {
        this.nickname = nickname;
        this.ready = ready;
        this.role = role;
    }

    public void set(String nickname, boolean ready) {
        this.nickname = nickname;
        this.ready = ready;
    }

    public void reset() {
        set("NOT_CONNECTED", false, -1);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "[" + nickname + "] " +
                "ROLE(" + role + ") " +
                (ready ? "READY" : "NOT_READY");
    }
    public String forList() {
        return nickname + "    |    " +
        (ready ? "Ready" : "Not Ready");
    }
}