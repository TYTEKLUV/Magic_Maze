package Model;

import java.util.ArrayList;

public class Factory {

    public ArrayList<Player> chooseActions (int count) {
        ArrayList <Player> list = new ArrayList<>();
        switch (count) {
            case 2:
                list.add(new Player(12 , false, true, false));
                list.add(new Player(34 , true, false, true));
                break;
            case 3:
                list.add(new Player(12 , false, false, false));
                list.add(new Player(3, true, false, true));
                list.add(new Player(4, false, true, false));
                break;
            case 4:
                list.add(new Player(1, false, false, false));
                list.add(new Player(2, false, false, true));
                list.add(new Player(3, false, true, false));
                list.add(new Player(4, true, false, false));
                break;
        }
        return list;
    }
}
