package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicationTunnelServerSide implements CommunicationTunnel {
    private static final int SERVER_PORT = 8081;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader receiverBuffer;
    private PrintWriter emitterBuffer;

    @Override
    public void init() {
        try {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            clientSocket = serverSocket.accept();
            emitterBuffer = new PrintWriter(clientSocket.getOutputStream());
            receiverBuffer = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

            String message;

            while ((message = receiverBuffer.readLine()) != null) {
                System.out.println("client: " + message);
                emitterBuffer.println("Ok response");
                emitterBuffer.flush();
            }

            emitterBuffer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
