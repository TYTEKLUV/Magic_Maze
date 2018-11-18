package Game.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket client;
    private Server mainServer;
    DataOutputStream out;
    DataInputStream in;
    private String nickname;
    private String ip;

    public ClientHandler(Socket client, Server mainServer) {
        this.mainServer = mainServer;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            out.writeUTF("OS: Welcome");
            out.flush();

            setClientParams();

            System.out.println(nickname + " connected");

            while (!client.isClosed()) {
                String entry = in.readUTF();
                System.out.println(nickname + ": " + entry);
            }

            System.out.println(nickname + " disconnected");

            in.close();
            out.close();

        } catch (IOException e) {
            mainServer.clientDisconnect(nickname, ip, this);
            System.out.println(nickname + " disconnected");
        }
    }

    private void setClientParams() throws IOException {
        nickname = in.readUTF();
        mainServer.getClientsNickname().add(nickname);
        ip = client.getRemoteSocketAddress().toString();
        mainServer.getClientsIP().add(ip);
    }

    public void sayCommand(String text) throws IOException {
        out.writeUTF("OS: " + text);
        out.flush();
    }

    public void turnOff() throws IOException {
        client.close();
    }
}
