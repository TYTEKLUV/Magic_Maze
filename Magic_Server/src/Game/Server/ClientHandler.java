package Game.Server;

import Game.Model.GameRules;
import Game.Model.Player;
import Game.Model.Point;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket client;
    private Room room;
    private Player player;
    private volatile DataInputStream in;
    private volatile DataOutputStream out;
    private String ip;

    public ClientHandler(Socket client, Room room, Player player) {
        this.client = client;
        this.room = room;
        this.player = player;
        ip = client.getRemoteSocketAddress().toString();
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            connect();
            while (true) commandHandler(in.readUTF());
        } catch (IOException e) {
            try {
                room.disconnectClient(this, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void commandHandler(String message) throws IOException {
        System.out.println(player.getNickname() + " message = [" + message + "]");
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "STATUS":
                changeStatus(command.next().equals("READY"));
                break;
            case "GAME":
                switch (command.next()) {
                    case "START":
                        if (room.getPlayers().getLeader() == player)
                            room.loadGame();
                        else
                            System.out.println("| " + player.getNickname() + " not leader");
                        break;
                    case "READY":
                        player.setReady(true);
                        room.startGame();
                        break;
                    case "CLICK":
                        Platform.runLater(() -> {
                            Point point = new Point(command.nextInt(), command.nextInt());
                            try {
                                new GameRules().mouseReleased(point, room.getGameWindow());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            int c = point.getCardId(room.getGameWindow());
                            System.out.println("click place = " + room.getGameWindow().getCards().get(c).getMap()[(int) point.getPosition(true, room.getGameWindow()).localToMap().y][(int) point.getPosition(true, room.getGameWindow()).localToMap().x]);
                        });
                        break;
                    case "SELECT":
                        break;
                    case "MOVE":
                        break;
                    case "CARD":
                        Platform.runLater(() -> {
                            try {
                                new GameRules().moveReleased(room.getGameWindow());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        room.addCard(command.nextInt(), command.nextInt(), command.nextInt(), command.nextInt(), this);
                        break;
                }
                break;
        }
    }

    public void send(String message) throws IOException {
        System.out.println(player.getNickname() + " send: " + message);
        out.writeUTF(message);
        out.flush();
        System.out.println(player.getNickname() + " send: " + message + " complete");
    }

    private void connect() throws IOException {
        send("SET ROOM " + room.getName() + " " + room.getPlayers().size());
        send("SET PLAYER " + room.getPlayers().indexOf(player));
        send("SET LEADER " + room.getPlayers().indexOf(room.getPlayers().getLeader()));
        for (Player player : room.getPlayers())
            if (!player.getNickname().equals("NOT_CONNECTED"))
                send("ADD " +
                        room.getPlayers().indexOf(player) + " " +
                        player.getNickname() + " " +
                        (player.isReady() ? "READY" : "NOT_READY"));
            else
                send("REMOVE " + room.getPlayers().indexOf(player));
        room.sendOthers("ADD " + room.getPlayers().indexOf(player) + " " + player.getNickname() + " NOT_READY", this);
        System.out.println(player.getNickname() + " connected to room " + room.getName());
    }

    public void disconnect() throws IOException {
        room.disconnectClient(this, false);
    }

    private void changeStatus(boolean ready) throws IOException {
        player.setReady(ready);
        room.sendOthers("SET STATUS " + room.getPlayers().indexOf(player) + " " + (ready ? "READY" : "NOT_READY"), this);
        System.out.println(player.getNickname() + " " + (ready ? "READY" : "NOT_READY"));
    }

    public void close() throws IOException {
        client.close();
    }

    public Player getPlayer() {
        return player;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return ip.substring(1) + " " +
                "[" + player.getNickname() + "] " +
                "NUMBER(" + room.getPlayers().indexOf(player) + ")";
    }
}