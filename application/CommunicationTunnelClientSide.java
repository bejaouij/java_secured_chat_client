package application;

import helper.encryption.CryptoManager;
import helper.encryption.CryptoManagerFactory;
import helper.encryption.RSACryptoManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.Arrays;

public class CommunicationTunnelClientSide implements CommunicationTunnel {
    private static final String REMOTE_HOST = "192.168.43.20";
    private static final int REMOTE_PORT = 8082;
    private Socket remoteSocket;
    private BufferedReader receiverBuffer;
    private PrintWriter emitterBuffer;
    private CryptoManager communicationCrypto;

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
            communicationCrypto = CryptoManagerFactory.getDESCryptoManager();

            String publicKeyString = receiverBuffer.readLine();

            RSACryptoManager keyEncryptor = (RSACryptoManager) CryptoManagerFactory.getRSACryptoManager();
            Key publicServerKey = keyEncryptor.parseBytesToKey(publicKeyString.getBytes());
            byte[] encryptedCommunicationKey = keyEncryptor.encrypt(communicationCrypto.getSymmetricEncryptionKey().getEncoded(), publicServerKey);

            emitterBuffer.println(Arrays.toString(encryptedCommunicationKey));
            emitterBuffer.flush();

            //#TODO JUSTE POUR LE TEST A REMOVE
            String message;

            while ((message = receiverBuffer.readLine()) != null) {
                System.out.println("server: " + Arrays.toString(communicationCrypto.decrypt(message.getBytes())));
            }

            emitterBuffer.close();
            remoteSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
