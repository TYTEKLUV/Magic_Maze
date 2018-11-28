package Model;

public class Player {
    private String nickname = "NOT_CONNECTED";
    private boolean ready = false;
    private boolean leader = false;
    private int role = -1;

    public void set(String nickname, boolean ready, boolean leader, int role) {
        this.nickname = nickname;
        this.ready = ready;
        this.leader = leader;
        this.role = role;
    }

    public void reset() {
        set("NOT_CONNECTED", false, false, -1);
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

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
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
                (ready ? "READY" : "NOT_READY") + " " +
                (leader ? "(LEADER)" : "");
    }
}
