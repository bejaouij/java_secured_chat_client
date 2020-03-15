package application;

import helper.encryption.CryptoManager;
import helper.encryption.CryptoManagerFactory;
import helper.encryption.RSACryptoManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Arrays;

public class CommunicationTunnelServerSide implements CommunicationTunnel {
    private static final int SERVER_PORT = 8082;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader receiverBuffer;
    private PrintWriter emitterBuffer;
    private CryptoManager communicationCrypto;

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

            RSACryptoManager keyEncryptor = (RSACryptoManager) CryptoManagerFactory.getRSACryptoManager();
            emitterBuffer.println(Arrays.toString(keyEncryptor.getAsymmetricPublicKey().getEncoded()));
            emitterBuffer.flush();


            String encryptedCommunicationCryptoKeyString = receiverBuffer.readLine();
            byte[] communicationCryptoKeyContent = keyEncryptor.decrypt(encryptedCommunicationCryptoKeyString.getBytes());

            Key communicationCryptoKey = communicationCrypto.parseBytesToKey(communicationCryptoKeyContent);
            communicationCrypto = CryptoManagerFactory.getDESCryptoManager(communicationCryptoKey);

            String messageClair = "Coucou";
            emitterBuffer.println(Arrays.toString(communicationCrypto.encrypt(messageClair.getBytes())));
            messageClair = "World!";
            emitterBuffer.println(Arrays.toString(communicationCrypto.encrypt(messageClair.getBytes())));
            messageClair = "Oui";
            emitterBuffer.println(Arrays.toString(communicationCrypto.encrypt(messageClair.getBytes())));

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
