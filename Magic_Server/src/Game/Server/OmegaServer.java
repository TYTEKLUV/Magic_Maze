package Game.Server;

import javafx.application.Application;
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

public class OmegaServer extends Application {
    private Room currentRoom;
    private int serverPort = 4444;
    private static boolean window;
    private Server server;
    private String[] commandsList =
            {"close         - close room",
                    "ip            - show room ip",
                    "clients       - show current room clients",
                    "say <message> - send message to clients",
                    "kick <client> - kick client from room"};

    public static void main(String[] args) {
        if (args.length != 0) window = !args[0].equals("c");
        else window = true;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        commandHandler("start");

        if (window) {
            showConsole(primaryStage);
            commandHandler("create TEST_ROOM 4");
        } else {
            Scanner console = new Scanner(System.in);
            commandHandler("create TEST_ROOM 4");
            System.out.println("Server started");
            while (true) commandHandler(console.nextLine());
        }
    }

    private void commandHandler(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "help":
                for (String aCommandsList : commandsList)
                    System.out.println("| " + aCommandsList);
                break;
            case "start":
                server = new Server(this, serverPort);
                server.start();
                break;
            case "create":
                Room room = new Room(command.next(), command.nextInt(), server);
                server.getRooms().add(room);
                currentRoom = room;
                System.out.println("Room " + currentRoom.getName() + " created");
                break;
            case "destroy":
                if (checkCurrentRoom())
                    server.destroyRoom(currentRoom);
                break;
            case "ip":
                System.out.println("| Server ip = " + InetAddress.getLocalHost().getHostAddress());
                break;
            case "clients":
                if (checkCurrentRoom())
                    System.out.println(getClientsList(currentRoom));
                break;
            case "kick":
                if (checkCurrentRoom())
                    if (command.hasNext()) {
                        String next = command.next();
                        if (next.equals("all"))
                            System.out.println(currentRoom.kickAll());
                        else
                            System.out.println(currentRoom.kick(next));
                    } else
                        System.out.println("| enter client nickname [kick <nickname>]");
                break;
            case "exit":
                System.out.println("Server stopped");
                System.exit(0);
                break;
            case "status":
                if (checkCurrentRoom())
                    System.out.println(currentRoom.status());
                break;
            case "roles":
                if (checkCurrentRoom())
                    switch (command.next()) {
                        case "random":
                            currentRoom.getPlayers().rolesRandom();
                            currentRoom.sendRoles();
                            break;
                        case "change":
                            currentRoom.rolesChange();
                            currentRoom.sendRoles();
                            break;
                    }
                break;
            case "rooms":
                if (checkCurrentRoom())
                    for (int i = 0; i < server.getRooms().size(); i++)
                        System.out.println("| " + i + ": " + server.getRooms().get(i).getName());
                break;
            case "room":
                if (checkCurrentRoom())
                    if (command.hasNextInt())
                        changeCurrentRoom(command.nextInt());
                    else
                        System.out.println("| Current room \"" + currentRoom.getName() + "\"");
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
        Scene scene = new Scene(root, 800, 600);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> textField.requestFocus());
        scene.getStylesheets().add("Game/Server/Default.css");
        stage.setScene(scene);
        stage.setTitle("Active - Omega Server");
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }

    private boolean checkCurrentRoom() {
        if (currentRoom == null) {
            System.out.println("| No room created");
            return false;
        } else
            return true;
    }

    public void changeCurrentRoom(int i) {
        currentRoom = null;
        if (!server.getRooms().isEmpty()) {
            currentRoom = server.getRooms().get(i);
            System.out.println("| Current room change to \"" + currentRoom.getName() + "\"");
        } else
            checkCurrentRoom();
    }

    public String getClientsList(Room room) {
        StringBuilder result = new StringBuilder("| Count: " + room.getClients().size());
        for (int i = 0; i < room.getClients().size(); i++) {
            result.append("\n").append("| ").append(i + 1).append(": ").append(room.getClients().get(i).toString());
        }
        return result.toString();
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}
