package Game.Client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientStarter extends Application {

    private int serverPort = 4444;
    private Client client;
    boolean work = true;
    private String nickname;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.println("Client started");
        System.out.println("--------------");

        System.out.print("Enter nickname: ");
        nickname = input.next();
        client = new Client(serverPort, nickname);
        client.start();

        commandHandler(input);

//        input.close();
//        System.exit(0);
    }

    private void commandHandler(Scanner input) throws IOException {
        while (work) {
            String command = input.next();
            switch (command) {
                default:
                    System.out.println("OS: command not found");
                    break;
                case "close":
//                    work = false;
                    client.turnOff();
                    break;
                case "ip":
                    System.out.println("OS: server ip = " + InetAddress.getLocalHost().getHostAddress());
                    break;
                case "a":
                    client.makeAction();
                    break;
                case "m":
                    client.makeMove();
                    break;
                case "connect":
                    client = new Client(serverPort, nickname);
                    client.start();
                    break;
                case "exit":
                    System.out.println("--------------");
                    System.out.println("Client stopped");
                    System.exit(0);
                    break;
            }
        }
    }
}
