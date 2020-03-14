package application;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnectionProcess implements Runnable {
    private Socket clientSocket;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;

    public ClientConnectionProcess(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.err.println("Lancement du traitement de la connexion cliente");

        boolean closeConnexion = false;
        //tant que la connexion est active, on traite les demandes
        while(!clientSocket.isClosed()){

            try {

                //Ici, nous n'utilisons pas les mêmes objets que précédemment
                //Je vous expliquerai pourquoi ensuite
                try {
                    writer = new PrintWriter(clientSocket.getOutputStream());
                    reader = new BufferedInputStream(clientSocket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //On attend la demande du client
                String response = read();
                InetSocketAddress remote = (InetSocketAddress)clientSocket.getRemoteSocketAddress();

                //On affiche quelques infos, pour le débuggage
                String debug = "";
                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
                debug += " Sur le port : " + remote.getPort() + ".\n";
                debug += "\t -> Commande reçue : " + response + "\n";
                System.err.println("\n" + debug);

                //On traite la demande du client en fonction de la commande envoyée
                String toSend = "";

                switch(response.toUpperCase()){
                    case "OK":
                        toSend = "Ok";
                        break;
                    case "CLOSE":
                        toSend = "Communication terminée";
                        closeConnexion = true;
                        break;
                    default :
                        toSend = "Commande inconnu !";
                        break;
                }

                //On envoie la réponse au client
                writer.write(toSend);
                //Il FAUT IMPERATIVEMENT UTILISER flush()
                //Sinon les données ne seront pas transmises au client
                //et il attendra indéfiniment
                writer.flush();

                if(closeConnexion){
                    System.err.println("COMMANDE CLOSE DETECTEE ! ");
                    writer = null;
                    reader = null;
                    clientSocket.close();
                    break;
                }
            } catch(SocketException e){
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //La méthode que nous utilisons pour lire les réponses
    private String read() throws IOException{
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}
