package application;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    private static byte[] CLEAR_MESSAGE = {'t', 'e', 's', 't'};

    public static void main(String[] args) {
        String typeOfExecution = args.length >= 1 ? args[0] : CommunicationTunnelFactory.CLIENT_KIND;

        CommunicationTunnel communicationTunnel = CommunicationTunnelFactory.getFactory(typeOfExecution).getInstance();

        communicationTunnel.init();
    }
}
