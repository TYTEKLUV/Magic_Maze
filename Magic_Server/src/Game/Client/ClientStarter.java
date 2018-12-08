package Game.Client;

import Game.Model.PlayerList;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientStarter extends Application {

    private PlayerList players = new PlayerList();

    private int serverPort = 4444;
    private Client client;
    private String nickname;
    private String[] commandsList =
            {"close         - close server",
                    "ip            - show server ip",
                    "clients       - show current server clients",
                    "sayToAllClients <message> - send message to clients",
                    "kick <client> - kick client from server"};

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner console = new Scanner(System.in);

        System.out.println("Client started");
        System.out.println("--------------");

        commandHandler("nick");
        commandHandler("connect 127.0.0.1");

        while (true) commandHandler(console.nextLine());
    }

    private void commandHandler(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "help":
                for (String aCommandsList : commandsList)
                    System.out.println("| " + aCommandsList);
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
            case "ready":
                client.firstCommand();
                break;
            case "nready":
                client.secondCommand();
                break;
            case "exit":
                System.out.println("--------------");
                System.out.println("Client stopped");
                System.exit(0);
                break;
            case "status":
                System.out.println(status());
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
