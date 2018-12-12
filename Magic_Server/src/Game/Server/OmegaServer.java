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
    private int serverPort = 4444;
    private Server server;
    private Room currentRoom;
    private String[] commandsList =
            {"ip                    - show server ip",
                    "create <name> <count> - create new room",
                    "destroy               - destroy CURRENT room",
                    "rooms                 - show rooms list",
                    "room [index]          - show current room or change it",
                    "clients               - show CURRENT room clients",
                    "status                - show CURRENT room players status",
                    "kick <client> or all  - kick client from room",
                    "exit                  - stop server"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        if (!(!getParameters().getRaw().isEmpty() && getParameters().getRaw().get(0).equals("-c"))) {
            showConsole(primaryStage);
            startCommands();
        } else {
            Scanner console = new Scanner(System.in);
            startCommands();
            while (true) commandHandler(console.nextLine());
        }
    }

    private void startCommands() throws IOException {
        server = new Server(this, serverPort);
        server.start();
        System.out.println("Server started");
        System.out.println("\"help\" to show commands list\n");
        commandHandler("create TEST_ROOM 2");
    }

    private void commandHandler(String message) throws IOException {
        Scanner command = new Scanner(message);
        switch (command.next()) {
            case "help":
                for (String aCommandsList : commandsList)
                    System.out.println("| " + aCommandsList);
                break;
            case "ip":
                System.out.println("| Server ip = " + InetAddress.getLocalHost().getHostAddress());
                break;
            case "create":
                Room room = new Room(command.next(), command.nextInt(), server);
                server.getRooms().add(room);
                currentRoom = room;
                System.out.println("| Room " + currentRoom.getName() + " created");
                break;
            case "destroy":
                if (checkCurrentRoom())
                    server.destroyRoom(currentRoom);
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
            case "clients":
                if (checkCurrentRoom())
                    System.out.println(getClientsList(currentRoom));
                break;
            case "status":
                if (checkCurrentRoom())
                    System.out.println(currentRoom.status());
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
            case "exit":
                System.out.print("\nServer stopped");
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