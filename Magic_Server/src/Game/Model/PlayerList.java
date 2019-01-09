package Game.Model;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerList extends ArrayList<Player> {
    private Player leader;

    public void reset() {
        for (Player player : this)
            player.reset();
    }

    public void resetRoles() {
        for (Player player : this)
            player.setRole(-1);
    }

    public void resetLeader() {
        for (Player player : this)
            player.setRole(-1);
    }

    public void resetNicknames() {
        for (Player player : this)
            player.setNickname("NOT_CONNECTED");
    }

    public void resetReady() {
        for (Player player : this)
            player.setReady(false);
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

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }

    public void createPlayers(int count) {
        clear();
        for (int i = 0; i < count; i++)
            add(new Player());
        leader = get(0);
    }

    public void rolesChange() {
        for (int i = 0; i < size(); i++)
            get(i).setRole(get(i).getRole() + 1 == size() ? 0 : get(i).getRole() + 1);
    }

    public void rolesRandom() {
        ArrayList<Integer> roles = new ArrayList<>();
        for (int i = 0; i < size(); i++)
            roles.add(i);
        Collections.shuffle(roles);
        for (int i = 0; i < size(); i++)
            get(i).setRole(roles.get(i));
    }

    public String getRoles() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size(); i++)
            result.append(get(i).getRole()).append(i + 1 < size() ? " " : "");
        return result.toString();
    }

    public int readyCount() {
        int k = 0;
        for (Player player : this)
            if (player.isReady())
                k++;
        return k;
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