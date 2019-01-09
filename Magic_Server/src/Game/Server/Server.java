package Game.Server;

import Game.Model.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {
    private OmegaServer main;
    private int port;
    private volatile ArrayList<Room> rooms = new ArrayList<>();

    public Server(OmegaServer main, int port) {
        this.port = port;
        this.main = main;
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            while (!server.isClosed()) {
                Socket client = server.accept();
                connect(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect(Socket client) throws IOException {
        String nickname;
        String roomName;

        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        out.writeUTF("OMEGA_WELCOME");

        Scanner command = new Scanner(in.readUTF());
        if (command.next().equals("CONNECT")) {
            nickname = command.next();
            while (true) {
                String s = in.readUTF();
                if ("ROOMS".equals(s))
                    out.writeUTF(getRoomsStatus());
                else if ("START".equals(s))
                    break;
            }
            roomName = command.next();
            if (roomName.equals("CREATE")) {
                roomName = command.next();
                Room room = new Room(roomName, command.nextInt());
                rooms.add(room);
                main.setCurrentRoom(room);
                out.writeUTF("ACCEPT");
                Player player = room.getFreeSlot();
                player.setNickname(nickname);
                room.changeLeader(player);
                room.connectClient(client, player);
            } else {
                Room room = searchRoom(roomName);
                if (room != null) {
                    Player player = room.getFreeSlot();
                    if (player != null) {
                        out.writeUTF("ACCEPT");
                        player.setNickname(nickname);
                        room.connectClient(client, player);
                    } else
                        out.writeUTF("BUSY");
                } else
                    out.writeUTF("NOT_FOUND");
            }
        }
    }

    public void destroyRoom(Room room) throws IOException {
        room.kickAll();
        rooms.remove(room);
        System.out.println("| Room " + room.getName() + " destroyed");
        main.changeCurrentRoom(0);
    }

    private Room searchRoom(String roomName) {
        for (Room room : rooms)
            if (roomName.equals(room.getName()))
                return room;
        return null;
    }

    private String getRoomsStatus() {
        StringBuilder result = new StringBuilder("ROOMS ");
        for (int i = 0; i < rooms.size(); i++)
            result.append(rooms.get(i).getName()).append(" ")
                    .append(rooms.get(i).getPlayers().playersCount()).append(" ")
                    .append(rooms.get(i).getPlayers().size())
                    .append(i != rooms.size() ? " " : "");
        return result.toString();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}