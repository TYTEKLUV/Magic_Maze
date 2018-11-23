package Game.Client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientStarter extends Application {

    private int serverPort = 4444;
    private Client client;
    private boolean work = true;
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

        commandHandler(new Scanner("nick rar"));

        commandHandler(new Scanner("connect"));

        while (work) {
            commandHandler(console);
        }

        System.out.println("--------------");
        System.out.println("Client stopped");
        System.exit(0);
    }

    private void commandHandler(Scanner console) throws IOException {
        String line = console.nextLine();
        Scanner input = new Scanner(line);
        String command = input.next();
        switch (command) {
            case "help":
                for (String aCommandsList : commandsList) {
                    System.out.println("OS: " + aCommandsList);
                }
                break;
            case "connect":
                if (input.hasNext()) {
                    client = new Client(serverPort, nickname, input.next());
                    client.start();
                } else {
                    client = new Client(serverPort, nickname);
                    client.start();
                }
                break;
            case "close":
                client.turnOff();
                break;
            case "ip":
                System.out.println("OS: your ip = " + InetAddress.getLocalHost().getHostAddress());
                break;
            case "nick":
                if (client == null || !client.isAlive()) {
                    if (input.hasNext()) {
                        nickname = input.next();
                    } else {
                        System.out.print("Enter nickname: ");
                        nickname = new Scanner(System.in).next();
                    }
                } else {
                    System.out.println("disconnect before change nickname");
                }
                break;
            case "a":
                client.makeAction();
                break;
            case "m":
                client.makeMove();
                break;
            case "exit":
                work = false;
                input.close();
                break;
            default:
                System.out.println("OS: command not found");
                break;
        }
    }
}
