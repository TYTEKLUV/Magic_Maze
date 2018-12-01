package Game.Server;

import Game.Model.Player;
import Game.Model.PlayerList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

    private PlayerList players = new PlayerList();
    private int playersCount = 4;
    private int leader = -1;

    private ExecutorService executeIt = Executors.newFixedThreadPool(4);
    private int serverPort;
    private ServerSocket server;
    private ArrayList<ClientHandler> clientList = new ArrayList<>();
    private boolean startReady = false;
    private ArrayList<Integer> roles = new ArrayList<>();

    public Server(int serverPort, int playersCount) {
        this.playersCount = playersCount;
        this.serverPort = serverPort;
        for (int i = 0; i < playersCount; i++) {
            players.add(new Player());
            roles.add(-1);
        }
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(serverPort);
            while (!server.isClosed()) {
                Socket client = server.accept();
                ClientHandler cH = new ClientHandler(client, this, getFreeSlot());
                clientList.add(cH);
                executeIt.execute(cH);
            }
        } catch (Exception ignored) {
        }
    }

    private int getFreeSlot() {
        int result = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getNickname().equals("NOT_CONNECTED")) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void turnOff() throws IOException {
        executeIt.shutdown();
        server.close();
    }

    public String getClients() {
        StringBuilder result = new StringBuilder("Count: " + clientList.size());
        for (int i = 0; i < clientList.size(); i++) {
            result.append("\n").append("| ").append(i + 1).append(": ").append(clientList.get(i).toString());
        }
        return result.toString();
    }

    public void clientDisconnect(ClientHandler cH) throws IOException {
        clientList.remove(cH);
        if (cH.leader && clientList.size() != 0) {
            clientList.get(0).leader = true;
            players.get(clientList.get(0).getNumber()).setLeader(true);
            sendToAll("SET LEADER " + clientList.get(0).getNumber());
            leader = clientList.get(0).getNumber();
        }
        sendToAll("REMOVE " + cH.getNumber());
        System.out.println(cH.getNickname() + " disconnected");
    }

    public void sayToAll(String text) throws IOException {
        for (ClientHandler aClientList : clientList) {
            aClientList.say(text);
        }
    }

    public boolean kick(int index) throws IOException {
        if (index == -1) {
            return false;
        } else {
            clientList.get(index).turnOff();
            return true;
        }
    }

    public String kick(String nickname) throws IOException {
        return kick(players.indexOf(nickname)) ? (nickname + " not found") : (nickname + " kicking...");
    }

    public String kickAll() throws IOException {
        for (int i = clientList.size() - 1; i >= 0; i--) {
            kick(i);
        }
        //TODO Сделать проверку на отключение всех клиентов перед Complete
        return "Complete";
    }

    public void sendToAll(String message) throws IOException {
        sendToOthers(message, null);
    }

    public void sendToOthers(String message, String nickname) throws IOException {
        for (ClientHandler clientHandler : clientList)
            if (!clientHandler.getNickname().equals(nickname))
                clientHandler.send(message);
    }

    public void startGame() throws IOException {
        sendToAll("START GAME");
    }

    public void rolesRandom() throws IOException {
        for (int i = 0; i < playersCount; i++)
            roles.set(i, i);
        Collections.shuffle(roles);
        players.setRoles(roles);
        sendRoles(roles);
     }

     public void loadGame() throws IOException {
        sendToAll("START LOAD");
        rolesRandom();
     }

    public void rolesChange() throws IOException {
        for (int i = 0; i < playersCount; i++)
            roles.set(i, (roles.get(i) + 1 == playersCount ? 0 : roles.get(i) + 1));
        players.setRoles(roles);
        sendRoles(roles);
    }

    public void sendRoles(ArrayList<Integer> roles) throws IOException {
        StringBuilder message = new StringBuilder("SET ROLES");
        for (int role : roles) message.append(" ").append(role);
        sendToAll(message.toString());
    }

    public void startStatus() throws IOException {
        setStartReady(isAllReady());
        sendToAll("SET START " + (isStartReady() ? "READY" : "NOT_READY"));
    }

    public String clientsStatus() {
        return "Player status: \n" + players.toString() + "\nSTART " + (isAllReady() ? "READY" : "NOT_READY");
    }

    public PlayerList getPlayers() {
        return players;
    }

    public ArrayList<ClientHandler> getClientList() {
        return clientList;
    }

    public boolean isAllReady() {
        int i = 0;
        for (Player player : players) if (player.isReady()) i++;
        return i == playersCount;
    }

    public boolean isStartReady() {
        return startReady;
    }

    public void setStartReady(boolean startReady) {
        this.startReady = startReady;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }
}