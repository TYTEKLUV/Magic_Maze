package Game.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

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
        } catch (IOException e) {
            mainServer.clientDisconnect(this);
        }
    }

    private void setClientParams() throws IOException {
        nickname = in.readUTF();
        ip = client.getRemoteSocketAddress().toString();
    }

    public void say(String text) throws IOException {
        out.writeUTF("OS: " + text);
        out.flush();
    }

    public void turnOff() throws IOException {
        client.close();
    }

    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return ip;
    }
}
