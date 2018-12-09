package Game.Server;

import Game.Model.Player;
import Game.Model.PlayerList;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Room {
    private String name;
    private Server server;
    private PlayerList players = new PlayerList();
    private ArrayList<ClientHandler> clients = new ArrayList<>();

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

    public void startGame() throws IOException {
        sendAll("START GAME");
    }

    public void loadGame() throws IOException {
        sendAll("START LOAD");
        players.rolesRandom();
        sendRoles();
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
        return "Complete";
    }

    public String status() {
        return "| Player status: \n" + players.toString() +
                "\n| LEADER " + players.getLeader().getNickname() +
                "\n| START " + (players.readyCount() == players.size() ? "READY" : "NOT_READY");
    }

    //endregion
}