package Game.Model;

import java.util.ArrayList;

public class PlayerList extends ArrayList<Player> {

    public void reset() {
        for (Player player : this) {
            player.reset();
        }
    }

    public void resetRole() {
        for (Player player : this) {
            player.setRole(-1);
        }
    }

    public void setRoles(ArrayList<Integer> roles) {
        for (int i = 0; i < size(); i++) get(i).setRole(roles.get(i));
    }

    public void resetLeader() {
        for (Player player : this) {
            player.setRole(-1);
        }
    }

    public void resetNickname() {
        for (Player player : this) {
            player.setNickname("NOT_CONNECTED");
        }
    }

    public void resetReady() {
        for (Player player : this) {
            player.setReady(false);
        }
    }

    public Player get(String nickname) {
        return get(indexOf(nickname));
    }

    public int indexOf(String nickname) {
        for (int i = 0; i < size(); i++)
            if (nickname.equals(get(i).getNickname()))
                return i;
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            result.append("| ").append(i).append(": ")
                    .append(get(i).toString())
                    .append(i + 1 < size() ? "\n" : "");
        }
        return result.toString();
    }
}
