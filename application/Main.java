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
        // Retrieve the type of execution
        String typeOfExecution = args.length >= 1 ? args[0] : "client";

        //#TODO Abstract factory
        if (typeOfExecution.equals("server")) {
            System.out.println("I'm a server");

            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(8082);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    System.out.println("New connection");

                    Thread clientThread = new Thread(new ClientConnectionProcess(clientSocket));

                    clientThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else if (typeOfExecution.equals("client")) {
            System.out.println("I'm a client");

            InetAddress remoteInetAddress;

            try {
                remoteInetAddress = InetAddress.getByName("https://vps627012.ovh.net");
            } catch (UnknownHostException e) {
                throw new SecuredChatUnkownHostException();
            }

            Socket remoteSocket = null;

            try {
                remoteSocket = new Socket(remoteInetAddress, 80);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedInputStream bis = null;

            try {
                bis = new BufferedInputStream(remoteSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String content = "";
            int stream = -1;

            while(true) {
                try {
                    if ((stream = bis.read()) == -1) {
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                content += (char)stream;
            }
        }
        ////////////////////

        else {
            throw new SecuredChatWrongTypeOfExecutionException();
        }
    }
}
