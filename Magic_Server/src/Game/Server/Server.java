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
    private ServerSocket server;
    private ArrayList<ClientHandler> clientList = new ArrayList<>();

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
            }
        } catch (Exception ignored) {
        }
    }

    public void turnOff() throws IOException {
        executeIt.shutdown();
        server.close();
    }

    public String getClients() {
        StringBuilder result = new StringBuilder("OS: [Count = " + clientList.size() + "]");
        for (int i = 0; i < clientList.size(); i++) {
            result.append("\n").append("OS: ").append(i + 1).append(": ").append(clientList.get(i).getIp().substring(1)).append(" [").append(clientList.get(i).getNickname()).append("]");
        }
        return result.toString();
    }

    public void clientDisconnect(ClientHandler cH) {
        clientList.remove(cH);
        System.out.println(cH.getNickname() + " disconnected");
    }

    public void sayToAll(String text) throws IOException {
        for (ClientHandler aClientList : clientList) {
            aClientList.say(text);
        }
    }

    public boolean kick(int index) throws IOException {
        boolean result;
        if (index == -1) {
            result = false;
        } else {
            clientList.get(index).turnOff();
            result = true;
        }
        return result;
    }

    public String kick(String nickname) throws IOException {
        int index = -1;
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).getNickname().equals(nickname)) {
                index = i;
                break;
            }
        }
        return kick(index) ? (nickname + " not found") : (nickname + " kicking...");
    }

    public String kickAll() throws IOException {
        int n = clientList.size();
        for (int i = n - 1; i >= 0; i--) {
            kick(i);
        }
        //TODO Сделать проверку на отключение всех клиентов перед Complete
        return "Complete";
    }
}