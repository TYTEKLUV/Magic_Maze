package Game.Server;

import Game.Model.Player;
import Game.Model.PlayerList;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Room {
    private String name;
    private Server server;
    private volatile PlayerList players = new PlayerList();
    private volatile ArrayList<ClientHandler> clients = new ArrayList<>();

    public Room(String name, int playersCount) {
        this.name = name;
        server = (Server) Thread.currentThread();
        players.createPlayers(playersCount);
    }

    public Room(String name, int playersCount, Server server) {
        this.name = name;
        this.server = server;
        players.createPlayers(playersCount);
    }

    public void connectClient(Socket client, Player player) {
        ClientHandler clientHandler = new ClientHandler(client, this, player);
        clients.add(clientHandler);
        clientHandler.start();
    }

    public synchronized void disconnectClient(ClientHandler client, boolean terminate) throws IOException {
        if (!terminate)
            client.close();
        else {
            clients.remove(client);
            if (!clients.isEmpty()) {
                if (client.getPlayer() == players.getLeader())
                    changeLeader(clients.get(0).getPlayer());
                sendAll("REMOVE " + players.indexOf(client.getPlayer()));
                System.out.println(client.getPlayer().getNickname() + " disconnected");
                client.getPlayer().reset();
            } else {
                System.out.println(client.getPlayer().getNickname() + " disconnected");
                server.destroyRoom(this);
            }
        }
    }

    public Player getFreeSlot() {
        for (Player player : players)
            if (player.getNickname().equals("NOT_CONNECTED"))
                return player;
        return null;
    }

    public void changeLeader(Player player) throws IOException {
        players.setLeader(player);
        sendAll("SET LEADER " + players.indexOf(player));
    }

    public void sendAll(String message) throws IOException {
        sendOthers(message, null);
    }

    public void sendOthers(String message, ClientHandler client) throws IOException {
        for (ClientHandler clientHandler : clients)
            if (clientHandler != client)
                clientHandler.send(message);
    }

    private String randomChips() {
        StringBuilder chips = new StringBuilder();
        ArrayList<Integer> chipsList = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        for (int i = 0; i < 4; i++)
            chips.append(i != 0 ? " " : "").append(chipsList.get(i));
        return chips.toString();
    }

    public void addCard(int id, int x, int y, ClientHandler setter) throws IOException {
        sendOthers("GAME CARD " + id + " " + x + " " + y, setter);
    }

    public void loadGame() throws IOException {
        players.resetReady();
        players.rolesRandom();
        sendAll("GAME LOAD");
        sendRoles();
        sendAll("SET ROTATE " + new Random().nextInt(4));
        sendAll("SET CHIPS " + randomChips());
    }

    public void startGame() throws IOException {
        if (players.readyCount() == players.size())
            sendAll("START GAME");
    }

    public void rolesChange() throws IOException {
        players.rolesChange();
        sendRoles();
    }

    public void sendRoles() throws IOException {
        sendAll("SET ROLES " + players.getRoles());
    }

    public String getName() {
        return name;
    }

    public PlayerList getPlayers() {
        return players;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    //region Баловство

    public boolean kick(int index) throws IOException {
        if (index == -1)
            return false;
        else {
            clients.get(index).disconnect();
            return true;
        }
    }

    public String kick(String nickname) throws IOException {
        return kick(players.indexOf(nickname)) ? ("| " + nickname + " kicking...") : ("| " + nickname + " not found");
    }

    public String kickAll() throws IOException {
        while (!clients.isEmpty())
            kick(0);
        return "| complete";
    }

    public String status() {
        return "| Player status: \n" + players.toString() +
                "\n| LEADER " + players.getLeader().getNickname() +
                "\n| START " + (players.readyCount() == players.size() ? "READY" : "NOT_READY");
    }

    //endregion
}