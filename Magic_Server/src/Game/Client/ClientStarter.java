package Game.Client;

import Game.Model.PlayerList;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientStarter extends Application {
    private int serverPort = 4444;
    private Client client;
    private String nickname;
    private PlayerList players = new PlayerList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner console = new Scanner(System.in);
        startCommands();
        while (true) commandHandler(console.nextLine());
    }

    private void startCommands() throws IOException {
        System.out.println("Client started");
        System.out.println("--------------");
        commandHandler("nick");
        commandHandler("connect 127.0.0.1");
    }

    private void commandHandler(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "ip":
                System.out.println("| your ip = " + InetAddress.getLocalHost().getHostAddress());
                break;
            case "nick":
                if (client == null || !client.isAlive())
                    if (command.hasNext())
                        nickname = command.next();
                    else {
                        System.out.print("Enter nickname: ");
                        nickname = new Scanner(System.in).next();
                    }
                else
                    System.out.println("disconnect before change nickname");
                break;
            case "connect":
                if (command.hasNext()) {
//                    client = new Client(this, nickname,"TEST_ROOM", 4, input.next());
                    client = new Client(this, nickname, "TEST_ROOM", 0, command.next());
                    client.start();
                } else {
//                    client = new Client(this, nickname,"TEST_ROOM", 4);
                    client = new Client(this, nickname, "TEST_ROOM", 0);
                    client.start();
                }
                break;
            case "close":
                client.close();
                break;
            case "status":
                System.out.println(status());
                break;
            case "ready":
                client.firstCommand();
                System.out.println("send ready");
                break;
            case "nready":
                client.secondCommand();
                System.out.println("send not ready");
                break;
            case "start":
                if (players.readyCount() == players.size()) {
                    if (players.getLeader() == client.getPlayer())
                        client.thirdCommand();
                    else
                        System.out.println("you not leader");
                } else
                    System.out.println("all not ready");
                break;
            case "send":
                client.send("GAME READY");
                break;
            case "exit":
                System.out.println("--------------");
                System.out.print("Client stopped");
                System.exit(0);
                break;
            default:
                System.out.println("| command not found");
                break;
        }
    }

    public String status() {
        return "Player status: \n" + players.toString() +
                "\nLEADER " + players.getLeader().getNickname() +
                "\nSTART " + (players.readyCount() == players.size() ? "READY" : "NOT_READY");
    }

    public PlayerList getPlayers() {
        return players;
    }

    public int getServerPort() {
        return serverPort;
    }
}