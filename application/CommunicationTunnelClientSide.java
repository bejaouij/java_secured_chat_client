package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationTunnelClientSide implements CommunicationTunnel {
    private static final String REMOTE_HOST = "127.0.0.1";
    private static final int REMOTE_PORT = 8082;
    private Socket remoteSocket;
    private BufferedReader receiverBuffer;
    private PrintWriter emitterBuffer;

    @Override
    public void init() {
        try {
            try {
                remoteSocket = new Socket(REMOTE_HOST, REMOTE_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            emitterBuffer = new PrintWriter(remoteSocket.getOutputStream());
            receiverBuffer = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));

            emitterBuffer.println("Test envoi");
            emitterBuffer.flush();

            String message;

            while ((message = receiverBuffer.readLine()) != null) {
                System.out.println("server: " + message);
            }

            emitterBuffer.close();
            remoteSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
