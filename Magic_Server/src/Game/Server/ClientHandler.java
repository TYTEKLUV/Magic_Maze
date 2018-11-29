package Game.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {

    private int number;
    private Socket client;
    private Server mainServer;
    DataOutputStream out;
    DataInputStream in;
    private String nickname;
    private String ip;
    boolean ready = false;
    boolean leader = false;
    private int role = -1;

    public ClientHandler(Socket client, Server mainServer, int number) {
        this.number = number;
        this.mainServer = mainServer;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            out.writeUTF("OS: Welcome");

            commandHandler(in.readUTF());

            while (!client.isClosed()) {
                commandHandler(in.readUTF());
            }
        } catch (IOException e) {
            try {
                disconnectPerform();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void commandHandler(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "CONNECT":
                connectPerform();
                break;
            case "STATUS":
                if (command.next().equals("READY")) {
                    changeStatus(true);
                } else {
                    changeStatus(false);
                }
                break;
            case "START":
                commandPerformer("START LOAD");
                break;
        }
    }

    private void commandPerformer(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "START":
                switch (command.next()) {
                    case "LOAD":
                        mainServer.loadGame();
                        commandPerformer("START GAME");
                        break;
                    case "GAME":
                        mainServer.startGame();
                        break;
                    case "STATUS":
                        mainServer.startStatus();
                        break;
                }
                break;
            case "ROLES":
                if (command.next().equals("CHANGE")) {
                    mainServer.rolesChange();
                }
                break;
            case "PING":
                break;
        }
    }

    private void connectPerform() throws IOException {
        setClientParams();

        for (int i = 0; i < mainServer.getPlayers().size(); i++) {
            if (i != number) {
                if (!mainServer.getPlayers().get(i).getNickname().equals("NOT_CONNECTED"))
                    send("ADD " + i + " " +
                            mainServer.getPlayers().get(i).getNickname() + " " +
                            (mainServer.getPlayers().get(i).isReady() ? "READY" : "NOT_READY"));
                else
                    send("REMOVE " + i);
            }
        }

        send("SET LEADER " + mainServer.getLeader());

        mainServer.getPlayers().get(number).set(nickname, ready, leader, role);

        mainServer.sendToOthers("ADD " + number + " " + nickname + " NOT_READY", nickname);

        System.out.println(nickname + " connected");
    }

    private void disconnectPerform() throws IOException {
        mainServer.getPlayers().get(nickname).reset();
        mainServer.clientDisconnect(this);
    }

    private void setClientParams() throws IOException {
        mainServer.getPlayers().get(number).setNickname("CONNECTED");

        nickname = in.readUTF();
        out.writeInt(number);
        ip = client.getRemoteSocketAddress().toString();

        if (mainServer.getClientList().size() == 1) {
            leader = true;
            mainServer.setLeader(number);
            mainServer.sendToOthers("SET LEADER " + number, nickname);
        }

        mainServer.getPlayers().get(number).set(nickname, ready, leader, role);
    }

    public void say(String text) throws IOException {
        out.writeUTF("SAY " + text);
        out.flush();
    }

    public void send(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    private void changeStatus(boolean ready) throws IOException {
        this.ready = ready;
        mainServer.getPlayers().get(number).setReady(ready);
        mainServer.sendToOthers("SET STATUS " + number + " " + (ready ? "READY" : "NOT_READY"), nickname);

        System.out.println(nickname + " " + (ready ? "READY" : "NOT_READY"));

        if (mainServer.isAllReady() != mainServer.isStartReady()) {
            commandPerformer("START STATUS");
        }

    }

    @Override
    public String toString() {
        return ip.substring(1) + " " +
                "[" + nickname + "] " +
                "NUMBER(" + number + ")";
    }

    public void turnOff() throws IOException {
        client.close();
    }

    public int getNumber() {
        return number;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return ip;
    }

    public boolean isReady() {
        return ready;
    }
}
