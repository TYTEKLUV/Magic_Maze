package Game.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

    private ExecutorService executeIt = Executors.newFixedThreadPool(4);
    private int serverPort;
    private int clientCount = 0;
    private ServerSocket server;
    private ArrayList<ClientHandler> clientList = new ArrayList<>();
    private ArrayList<String> clientsIP = new ArrayList<>();
    private ArrayList<String> clientsNickname = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(serverPort);
            while (!server.isClosed()) {
                Socket client = server.accept();
                ClientHandler cH = new ClientHandler(client, this);
                clientList.add(cH);
                executeIt.execute(cH);
                clientCount++;
            }
        } catch (Exception ignored) {
        }
    }

    public void turnOff() throws IOException {
        executeIt.shutdown();
        server.close();
    }

    public String getClients() {
        StringBuilder result = new StringBuilder("OS: [Count = " + clientCount + "]");
        for (int i = 0; i < clientsIP.size(); i++) {
            result.append("\n").append("OS: ").append(i + 1).append(": ").append(clientsIP.get(i).substring(1)).append(" [").append(clientsNickname.get(i)).append("]");
        }
        return result.toString();
    }

    public void clientDisconnect(String nickname, String ip, ClientHandler cH) {
        clientsNickname.remove(nickname);
        clientList.remove(cH);
        clientsIP.remove(ip);
        clientCount--;
    }

    public void sayCommand(String text) throws IOException {
        for (ClientHandler aClientList : clientList) {
            aClientList.sayCommand(text);
        }
    }

    public String kickClient(String nickname) throws IOException {
        String result;
        int index = clientsNickname.indexOf(nickname);
        if (index == -1) {
            result = nickname + " not found";
        } else {
            result = nickname + " kicking...";
            clientList.get(index).turnOff();
        }
        return result;
    }

    public String kickAllClient() throws IOException {
        while (clientsNickname.size() > 0) {
            kickClient(clientsNickname.get(0));
        }
        return "Complete";
    }

    public ArrayList<String> getClientsIP() {
        return clientsIP;
    }

    public ArrayList<String> getClientsNickname() {
        return clientsNickname;
    }
}