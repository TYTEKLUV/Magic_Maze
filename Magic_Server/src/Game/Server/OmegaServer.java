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
        Scanner console = new Scanner(System.in);

        server = new Server(serverPort);
        server.start();

        System.out.println("Server started");
        System.out.println("--------------");

        while (work) {
            commandHandler(console);
        }

        System.out.println("--------------");
        System.out.println("Server stopped");

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
            case "ip":
                System.out.println("OS: server ip = " + InetAddress.getLocalHost().getHostAddress());
                break;
            case "clients":
                System.out.println(server.getClients());
                break;
            case "say":
                if (input.hasNext()) {
                    server.sayToAll(input.nextLine().substring(1));
                } else {
                    System.out.println("OS: enter message [say <message>]");
                }
                break;
            case "kick":
                if (input.hasNext()) {
                    String next = input.next();
                    if (next.equals("all")) {
                        System.out.println(server.kickAll());
                    } else {
                        System.out.println(server.kick(next));
                    }
                } else {
                    System.out.println("OS: enter client nickname [kick <nickname>]");
                }
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
