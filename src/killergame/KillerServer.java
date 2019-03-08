package killergame;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Javi
 */
public class KillerServer implements Runnable {

    private KillerGame game;
    private boolean online;
    private int puerto;
    private ServerSocket serverSock;
    private Socket clientSock;

    public KillerServer(KillerGame game) {

        this.game = game;
    }

    @Override
    public void run() {

        try {

            this.serverSock = new ServerSocket(puerto);
            this.online = true;
            this.game.servidorConectado();

            while (online) {

                System.out.println("Servidor en espera...");
                this.clientSock = serverSock.accept();
                String cliAddr = clientSock.getInetAddress().getHostAddress();
                System.out.println("Client connection from " + cliAddr);

                ConnectionHandler handler = new ConnectionHandler(this.game, clientSock);
                new Thread(handler).start();
            }
        } catch (BindException e) {
            this.game.servidorPuertoOcupado();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void closeSocket() {

        try {

            this.serverSock.close();
            this.serverSock = null;
            this.online = false;

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean isConnected() {

        if (this.serverSock == null) {
            return false;
        } else {
            return this.serverSock.isBound();
        }
    }

    public void setPort(int puerto) {

        this.puerto = puerto;
    }
}
