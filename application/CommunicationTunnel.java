package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class CommunicationTunnel {
    private String remoteAddressName = "http://vps627012.ovh.net/";
    private int remotePort = 3180;
    Socket remote;
    Socket client;

    public CommunicationTunnel() {
        try {
            remote = new Socket(InetAddress.getByName(remoteAddressName), remotePort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
