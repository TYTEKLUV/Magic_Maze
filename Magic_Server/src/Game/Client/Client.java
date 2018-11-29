package Game.Client;

import Game.Model.Player;
import Game.Model.PlayerList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client extends Thread {

    private PlayerList players = new PlayerList();

    private int playersCount = 4;
    private int number;
    private int serverPort;
    private Socket client;
    private String nickname;
    private DataOutputStream out;
    private DataInputStream in;
    private String presetIP = "";
    private ArrayList<Socket> threadSockets = new ArrayList<>();
    private List<Thread> pool = new ArrayList<>();
    private boolean startReady = false;


    public Client(int serverPort, String nickname) {
        this.serverPort = serverPort;
        this.nickname = nickname;

        for (int i = 0; i < playersCount; i++) {
            players.add(new Player());
        }
    }

    public Client(int serverPort, String nickname, String presetIP) {
        this.serverPort = serverPort;
        this.nickname = nickname;
        this.presetIP = presetIP;

        for (int i = 0; i < 4; i++) {
            players.add(new Player());
        }
    }

    @Override
    public void run() {
        try {
            searchServerIP();

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            commandPerformer("CONNECT");

            while (!client.isClosed()) {
                commandHandler(in.readUTF());
            }
        } catch (Exception e) {
            System.out.println("disconnect from server");
        }
    }

    private void commandPerformer(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "CONNECT":
                connectPerform();
                break;
            case "START":
                send("START");
                break;
        }
    }

    private void commandHandler(String message) {
        System.out.println("message = [" + message + "]");
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "ADD":
                int i = command.nextInt();
                players.get(i).setNickname(command.next());
                players.get(i).setReady(command.next().equals("READY"));
                if (command.hasNext()) {
                    if (command.next().equals("LEADER")) {
                        players.get(i).setLeader(true);
                    }
                }
                break;
            case "REMOVE":
                int j = command.nextInt();
                players.get(j).reset();
                break;
            case "SET":
                switch (command.next()) {
                    case "STATUS":
                        players.get(command.nextInt()).setReady(command.next().equals("READY"));
                        break;
                    case "START":
                        startReady = (command.next().equals("READY"));
                        break;
                    case "LEADER":
                        players.resetLeader();
                        players.get(command.nextInt()).setLeader(true);
                        break;
                    case "ROLES":
                        for (int k = 0; k < playersCount; k++) players.get(k).setRole(command.nextInt());
                        System.out.println("Роли загружены");
                        break;
                }
                break;
            case "START":
                switch (command.next()) {
                    case "LOAD":
                        System.out.println("Начинаем загрузку");

                        //Включить загрузочный экран
                        //Настроить интерфейс
                        break;
                    case "GAME":
                        System.out.println("Начинаем игру");
                        //Выключить загрузочный экран
                        //Включить игровое поле
                        break;
                }
                break;
            default:
                System.out.println("NOT_COMMAND: " + message);
                break;
        }
    }

    private void connectPerform() throws IOException {
        send("CONNECT");
        send(nickname);
        number = in.readInt();

        players.get(number).setNickname(nickname);
        players.get(number).set(nickname, false, false, -1);

        for (int i = 0; i < players.size() - 1; i++) {
            commandHandler(in.readUTF());
        }

        commandHandler(in.readUTF());

        System.out.println("connection complete");
    }

    public void firstCommand() throws IOException {
        players.get(number).setReady(true);
        send("STATUS READY");
    }

    public void secondCommand() throws IOException {
        players.get(number).setReady(false);
        send("STATUS NOT_READY");
    }

    public void thirdCommand() throws IOException {
        if (startReady) {
            System.out.println("Не все готовы");
        } else if (players.get(number).isLeader()) {
            commandPerformer("START");
        } else {
            System.out.println("You are not Leader");
        }
    }

    public void send(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    public String clientsStatus() {
        return "Player status: \n" + players.toString() + "\nSTART " + (startReady ? "READY" : "NOT_READY");
    }

    private void searchServerIP() throws UnknownHostException {
        boolean preset = !presetIP.equals("");
        int searchStart;
        String localIP;

        if (preset) {
            localIP = presetIP;
        } else {
            localIP = InetAddress.getLocalHost().getHostAddress();
            if (localIP.equals("127.0.0.1")) {
                preset = true;
            } else {
                localIP = getIpMask(localIP);
            }
        }

        searchStart = preset ? 255 : 1;

        for (int i = searchStart; i < 256; i++) {
            final String iIPv4 = preset ? localIP : localIP + i;
            System.out.println("searching " + iIPv4);

            Socket socket = new Socket();

            threadSockets.add(socket);

            createSearchThread(iIPv4, socket, preset);
        }
        for (Thread aThread : pool) {
            try {
                if (aThread.isAlive()) {
                    aThread.join();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    private String getIpMask(String ip) {
        int l = 0;
        int pCount = 0;
        while (pCount < 3) {
            if (String.valueOf(ip.charAt(l + pCount)).equals("."))
                pCount++;
            else
                l++;
        }
        return ip.substring(0, l + pCount);
    }

    private void createSearchThread(String iIPv4, Socket socket, Boolean preset) {
        Thread thread = new Thread(() -> {
            try {
                InetAddress ip = InetAddress.getByName(iIPv4);

                socket.connect(new InetSocketAddress(ip, serverPort));

                DataInputStream in = new DataInputStream(socket.getInputStream());

                String fromServer = in.readUTF();

                if (fromServer.equals("OS: Welcome")) {
                    while ((threadSockets.size() != 255) && !preset) {
                        System.out.println("wait " + threadSockets.size());
                    }
                    threadSockets.remove(socket);
                    for (Socket threadSocket : threadSockets) {
                        threadSocket.close();
                    }

                    client = socket;
                    System.out.println("OmegaServer found: " + iIPv4);

                }
            } catch (IOException ignored) {
            }
        });
        pool.add(thread);
        thread.start();
    }

    public void turnOff() throws IOException {
        client.close();
    }
}
