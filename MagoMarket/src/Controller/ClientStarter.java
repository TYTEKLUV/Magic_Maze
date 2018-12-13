package Controller;

import Model.PlayerList;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientStarter extends Thread {
    private int serverPort = 4444;
    private Client client;
    private String nickname;
    private PlayerList players = new PlayerList();
    private GameWindow gameWindow;

    public ClientStarter(ControllerFXML gameWindow) {
        this.gameWindow = (GameWindow) gameWindow;
        showConsole(new Stage());
    }

    @Override
    public void run() {
//        Scanner console = new Scanner(System.in);
        try {
            startCommands();
//            while (true) commandHandler(console.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCommands() throws IOException {
        System.out.println("Client started");
        System.out.println("--------------");
//        commandHandler("nick");
//        commandHandler("connect 127.0.0.1");
    }

    private void commandHandler(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "ip":
                System.out.println("| your ip = " + InetAddress.getLocalHost().getHostAddress());
                break;
            case "n":
                if (client == null || !client.isAlive())
                    if (command.hasNext()) {
                        nickname = command.next();
                        System.out.println("New nickname: " + nickname);
                        commandHandler("connect");
                    }
//                    else {
//                        System.out.print("Enter nickname: ");
//                        nickname = new Scanner(System.in).next();
//                    }
                else
                    System.out.println("disconnect before change nickname");
                break;
            case "connect":
                if (command.hasNext()) {
//                    client = new Client(this, nickname,"TEST_ROOM", 4, input.next());
                    client = new Client(this, nickname, "TEST_ROOM", 0, gameWindow, command.next());
                    client.start();
                } else {
//                    client = new Client(this, nickname,"TEST_ROOM", 4);
                    client = new Client(this, nickname, "TEST_ROOM", 0, gameWindow);
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

    private void showConsole(Stage stage) {
        TextArea text = new TextArea();
        TextField textField = new TextField();
        BorderPane root = new BorderPane(text, null, null, textField, null);
        text.setEditable(false);
        textField.requestFocus();
        textField.setOnAction(event -> {
            text.appendText(textField.getText() + "\n");
            if (textField.getText().equals("clear"))
                text.clear();
            else
                try {
                    commandHandler(textField.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            textField.clear();
        });
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                Platform.runLater(() -> text.appendText(String.valueOf((char) b)));
            }
        };
        System.setOut(new PrintStream(out, true));
        Scene scene = new Scene(root, 500, 300);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> textField.requestFocus());
//        scene.getStylesheets().add("Game/Server/Default.css");
        stage.setScene(scene);
        stage.setTitle("Active - Omega Server");
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
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