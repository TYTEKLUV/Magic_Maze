package Model;

import java.util.ArrayList;

public class GamePrams {

    private int cardsCount = 6;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Chip> chips = new ArrayList<>();
    private ArrayList<Role> roles = new ArrayList<>();
    private PlayerList players = new PlayerList();
    private ArrayList<Integer> findGlasses = new ArrayList<>();
    private boolean isMoveCard = false;
    private int moveCardId;
    private int closestFindGlassId;
    private int currentPlayer = -1;

}
