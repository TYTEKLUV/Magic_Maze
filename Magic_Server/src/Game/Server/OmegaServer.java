package Game.Server;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class OmegaServer extends Application {
    private int serverPort = 4444;
    private Server server;
    private boolean work = true;
    private String[] commandsList =
            {"close         - close server",
            "ip            - show server ip",
            "clients       - show current server clients",
            "say <message> - send message to clients",
            "kick <client> - kick client from server"};

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scanner input = new Scanner(System.in);

        server = new Server(serverPort);
        server.start();

        System.out.println("Server started");
        System.out.println("--------------");

        commandHandler(input);

        System.out.println("--------------");
        System.out.println("Server stopped");

        System.exit(0);
    }

    private void commandHandler(Scanner input) throws IOException {
        while (work) {
            String command = input.next();
            switch (command) {
                case "help":
                    for (String aCommandsList : commandsList) {
                        System.out.println("OS: " + aCommandsList);
                    }
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
                case "kick":
                    System.out.println(server.kickClient(input.next()));
                    break;
                case "close":
                    work = false;
                    server.turnOff();
                    input.close();
                    break;
                default:
                    System.out.println("OS: command not found");
                    break;
            }
        }
    }
}
