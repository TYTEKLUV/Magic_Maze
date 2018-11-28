package Model;

import java.util.ArrayList;

public class PlayerList extends ArrayList<Player> {

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
