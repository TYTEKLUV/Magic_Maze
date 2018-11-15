package Game.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {

    private int serverPort;
    private Socket client;
    private String nickname;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(int serverPort, String nickname) {
        this.serverPort = serverPort;
        this.nickname = nickname;
    }

    @Override
    public void run() {
        try {
            searchServerIP();

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            out.writeUTF(nickname);

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

        String localIP = InetAddress.getLocalHost().getHostAddress();

//        if (localIP.substring(0, 8).equals("192.168.")) {
//            System.out.println("substring " + localIP.substring(0, 8));
//        } else if (localIP.substring(0, 4).equals("10.")) {
//
//        }

        for (int j = 254; j < 255; j++) {
            for (int i = 254; i < 255; i++) {
//                final String iIPv4 = "192.168." + j + "." + i;
                final String iIPv4 = "localhost";
                System.out.println("searching " + iIPv4);

                Thread thread = new Thread(() -> {
                    try {
                        InetAddress ip = InetAddress.getByName(iIPv4);
                        Socket socket = new Socket(ip, serverPort);
//                        SocketAddress address = new InetSocketAddress(ip, serverPort);
//                        socket.connect(address, 1);

                        InputStream sout = socket.getInputStream();
                        DataInputStream in = new DataInputStream(sout);

                        String fromServer = in.readUTF();

//                        while (!((fromServer = in.readUTF()).equals(""))) {
                        if (fromServer.equals("OS: Welcome")) {
                            client = socket;
                            System.out.println("OmegaServer found : " + iIPv4);

                        }
//                        }
                    } catch (IOException ignored) {
                    }
                });
                pool.add(thread);
                thread.start();
            }
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
}
