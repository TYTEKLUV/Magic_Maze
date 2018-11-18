package Game.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client extends Thread {

    private int serverPort;
    private Socket client;
    private String nickname;
    private DataOutputStream out;
    private DataInputStream in;
    private String presetIP = "";

    public Client(int serverPort, String nickname) {
        this.serverPort = serverPort;
        this.nickname = nickname;
    }

    public Client(int serverPort, String nickname, String presetIP) {
        this.serverPort = serverPort;
        this.nickname = nickname;
        this.presetIP = presetIP;
    }

    @Override
    public void run() {
        try {
            searchServerIP();

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            out.writeUTF(nickname);

            System.out.println("connection complete");

            while (!client.isClosed()) {
                System.out.println(in.readUTF());
            }
        } catch (Exception e) {
            System.out.println("disconnect from server");
        }
    }

    public void turnOff() throws IOException {
        client.close();
    }

    public void makeAction() throws IOException {
        out.writeUTF("ACTION");
        out.flush();
    }

    public void makeMove() throws IOException {
        out.writeUTF("MOVE");
        out.flush();
    }

    private void searchServerIP() throws UnknownHostException {
        List<Thread> pool = new ArrayList<>();
        boolean preset = !presetIP.equals("");
        int searchStart;
        String localIP;

        if (preset) {
            localIP = presetIP;
        } else {
            localIP = InetAddress.getLocalHost().getHostAddress();
//            localIP = "127.0.0.1";
            if (localIP.equals("127.0.0.1")) {
                preset = true;
            } else {
                localIP = getipMask(localIP);
            }
        }

        searchStart = preset ? 255 : 1;

        for (int i = searchStart; i < 256; i++) {
            final String iIPv4 = preset ? localIP : localIP + i;

            System.out.println("searching " + iIPv4);

            Thread thread = new Thread(() -> {
                try {
                    InetAddress ip = InetAddress.getByName(iIPv4);
                    Socket socket = new Socket(ip, serverPort);

                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    String fromServer = in.readUTF();

                    if (fromServer.equals("OS: Welcome")) {
                        client = socket;
                        System.out.println("OmegaServer found: " + iIPv4);

                    }
                } catch (IOException ignored) {
                } 
            });
            pool.add(thread);
            thread.start();
        }
        for (Thread aThread : pool) {
            try {
                if (aThread.isAlive()) {
                    aThread.join();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    private String getipMask(String ip) {
        int l = 0;
        int pCount = 0;
        while (pCount < 3){
            if (String.valueOf(ip.charAt(l + pCount)).equals(".")) pCount++;
            else l++;
        }
        return ip.substring(0, l + pCount);
    }
}
