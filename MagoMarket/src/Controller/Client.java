package Controller;

import Model.Player;
import Model.Point;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {
    private ClientStarter main;
    private Player player = new Player();
    private String roomName;
    private int newRoomPlayersCount;
    private String presetIP = "";
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    GameWindow gameWindow;

    public Client(ClientStarter main, String nickname, String roomName, int newRoomPlayersCount, GameWindow gameWindow) {
        this.main = main;
        player.setNickname(nickname);
        this.roomName = roomName;
        this.newRoomPlayersCount = newRoomPlayersCount;
        this.gameWindow = gameWindow;
    }

    public Client(ClientStarter main, String nickname, String roomName, int newRoomPlayersCount, GameWindow gameWindow, String presetIP) {
        this.main = main;
        player.setNickname(nickname);
        this.roomName = roomName;
        this.newRoomPlayersCount = newRoomPlayersCount;
        this.gameWindow = gameWindow;
        this.presetIP = presetIP;
    }

    @Override
    public void run() {
        try {
            searchServerIP();
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            connect();
            while (true) commandHandler(in.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("disconnect from server");
        }
    }

    private void commandHandler(String message) throws IOException {
        System.out.println("message = [" + message + "]");
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "ADD":
                main.getPlayers().get(command.nextInt()).set(command.next(), command.next().equals("READY"));
                break;
            case "REMOVE":
                main.getPlayers().get(command.nextInt()).reset();
                break;
            case "SET":
                switch (command.next()) {
                    case "STATUS":
                        main.getPlayers().get(command.nextInt()).setReady(command.next().equals("READY"));
                        //Обработка кнопки старт
                        break;
                    case "LEADER":
                        main.getPlayers().setLeader(main.getPlayers().get(command.nextInt()));
                        break;
                    case "ROLES":
                        for (Player player : main.getPlayers())
                            player.setRole(command.nextInt());
                        break;
                    case "ROOM":
                        roomName = command.next();
                        main.getPlayers().createPlayers(command.nextInt());
                        break;
                    case "PLAYER":
                        player = main.getPlayers().get(command.nextInt());
                        break;
                    case "ROTATE":
                        gameWindow.clientRotate(command.nextInt() * 90);
                        break;
                    case "CHIPS":
                        gameWindow.create(this, main.getPlayers().indexOf(player), main.getPlayers().size(), main.getPlayers());

                        ArrayList<Integer> chipsOrder = new ArrayList<>();
                        for (int i = 0; i < 4; i++)
                            chipsOrder.add(command.nextInt());
                        gameWindow.clientChips(chipsOrder);

                        send("GAME READY");
                        break;
                }
                break;
            case "GAME":
                switch (command.next()) {
                    case "LOAD":
                        //Включить загрузочный экран
                        //Настроить интерфейс
                        break;
                    case "START":
                        Platform.runLater(() -> gameWindow.getStage().show());
                        //Выключить загрузочный экран
                        //Включить игровое поле
                        break;
                    case "BUSY":
                        gameWindow.getChips().get(command.nextInt()).setBusy(true);
                        break;
                    case "MOVE":
                        int id = command.nextInt();
                        gameWindow.gameRules.commandMove(id, command.nextInt(), command.nextInt(), gameWindow);
                        break;
                    case "DISAPPEAR":
                        id = command.nextInt();
                        Platform.runLater(() -> gameWindow.getChips().get(id).delete());
                        break;
                    case "ROLES":
                        break;
                    case "GLACES":
                        gameWindow.getFindGlasses().clear();
                        while (command.hasNextInt())
                            gameWindow.getFindGlasses().add(command.nextInt());
                        Platform.runLater(() -> gameWindow.getNewCard());
                        break;
                    case "CARD":
                        Platform.runLater(() -> gameWindow.sendCard(command.nextInt(), new Point(command.nextInt(), command.nextInt()), command.nextInt()));
                        break;
                    case "TIMER":
                        if (command.next().equals("SWAP")) {
                            gameWindow.getTimer().swap();
                        }
                        break;
                }
                break;
            default:
                System.out.println("NOT_COMMAND");
                break;
        }
    }

    public void send(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    private void connect() throws IOException {
        if (newRoomPlayersCount != 0)
            send("CONNECT " + player.getNickname() + " CREATE " + roomName + " " + newRoomPlayersCount);
        else
            send("CONNECT " + player.getNickname() + " " + roomName);
        switch (in.readUTF()) {
            case "ACCEPT":
                System.out.println("Подключилось к комнате");
                break;
            case "BUSY":
                System.out.println("Комната занята");
                close();
                break;
            case "NOT_FOUND":
                System.out.println("Комната не найдена");
                close();
                break;
        }
    }

    public void selectChip(int id) throws IOException {
        send("GAME BUSY " + id);
    }

    public void addCard(int id, int x, int y, int angle) throws IOException {
        send("GAME CARD " + id + " " + x + " " + y + " " + angle);
    }

    public void sendClick(int x, int y) throws IOException {
        send("GAME CLICK " + x + " " + y);
    }

    public void firstCommand() throws IOException {
        player.setReady(true);
        send("STATUS READY");
    }

    public void secondCommand() throws IOException {
        player.setReady(false);
        send("STATUS NOT_READY");
    }

    public void thirdCommand() throws IOException {
        send("GAME START");
    }

    private void searchServerIP() throws UnknownHostException {
        boolean preset = !presetIP.equals("");
        ArrayList<Thread> searchThreads = new ArrayList<>();
        final ArrayList<Socket> sockets = new ArrayList<>();
        String localIP;
        if (preset)
            localIP = presetIP;
        else {
            localIP = InetAddress.getLocalHost().getHostAddress();
            if (localIP.equals("127.0.0.1"))
                preset = true;
            else
                localIP = getIpMask(localIP);
        }
        for (int i = (preset ? 255 : 1); i < 256; i++) {
            final String iIPv4 = preset ? localIP : localIP + i;
            Socket socket = new Socket();
            sockets.add(socket);
            searchThreads.add(createSearchThread(iIPv4, socket, preset, sockets));
        }
        for (Thread thread : searchThreads)
            try {
                if (thread.isAlive())
                    thread.join();
            } catch (InterruptedException ignored) {
            }
    }

    private String getIpMask(String ip) {
        int l = 0;
        int pCount = 0;
        while (pCount < 3)
            if (String.valueOf(ip.charAt(l + pCount)).equals("."))
                pCount++;
            else
                l++;
        return ip.substring(0, l + pCount);
    }

    private Thread createSearchThread(String iIPv4, Socket socket, Boolean preset, ArrayList<Socket> sockets) {
        Thread thread = new Thread(() -> {
            try {
                InetAddress ip = InetAddress.getByName(iIPv4);
                socket.connect(new InetSocketAddress(ip, main.getServerPort()));
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String fromServer = in.readUTF();
                if (fromServer.equals("OMEGA_WELCOME")) {
                    while ((sockets.size() != 255) && !preset)
                        Thread.sleep(100);
                    sockets.remove(socket);
                    for (Socket threadSocket : sockets)
                        threadSocket.close();
                    client = socket;
                    System.out.println("OmegaServer found: " + iIPv4);
                }
            } catch (IOException | InterruptedException ignored) {
            }
        });
        thread.start();
        return thread;
    }

    public void close() throws IOException {
        client.close();
    }

    public Player getPlayer() {
        return player;
    }
}