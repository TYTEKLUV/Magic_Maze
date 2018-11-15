package Game.Server;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class OmegaServer extends Application {
    private int serverPort = 4444;
    private Server server;
    boolean work = true;
    String[] commandsList =
            {"close         - close server",
            "ip            - show server ip",
            "clients       - show current server clients",
            "say <message> - send message to clients"};

    @Override
    public void start(Stage primaryStage) throws IOException {

        server = new Server(serverPort);
        server.start();

        System.out.println("Server started");
        System.out.println("--------------");

        Scanner input = new Scanner(System.in);

        while (work) {
            String command = input.next();
            switch (command) {
                default:
                    System.out.println("OS: command not found");
                    break;
                case "help":
                    for (String aCommandsList : commandsList) {
                        System.out.println("OS: " + aCommandsList);
                    }
                    break;
                case "close":
                    work = false;
                    server.turnOff();
                    System.out.println("--------------");
                    System.out.println("Server stopped");
                    break;
                case "ip":
                    System.out.println("OS: server ip = " + InetAddress.getLocalHost().getHostAddress());
                    break;
                case "clients":
                    System.out.println(server.getClients());
                    break;
                case "say":
                    server.sayCommand(input.next());
                    break;
            }
        }

        input.close();
        System.exit(0);
    }
}
