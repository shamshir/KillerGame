package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Javi
 */
public class ConnectionHandler implements Runnable {

    private KillerGame game;
    private Socket socket;
    private BufferedReader in;

    public ConnectionHandler(KillerGame game, Socket socket) {

        super();

        this.game = game;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            processSocket();

        } catch (IOException e) {

            System.out.println(e);
        }
    }

    private void processSocket() throws IOException {

        String line = this.in.readLine();

        /* Procesamiento KillerPads */
        String[] infoKillerPad = line.split(":");
        if (infoKillerPad[0].equals("fromPnew")) {

            String[] subInfoKillerPad = infoKillerPad[1].split("&");
            String user = subInfoKillerPad[0];
            String color = subInfoKillerPad[1];

            this.game.createKillerPadAndKillerShip(socket, user, color);
        }

        /* Procesamiento VisualHandlers */
        String[] infoContacto = line.split("/");
        if (infoContacto[0].equals("contacto")) {

            if (infoContacto[1].equals("visualHandler")) {

                VisualHandler handler = null;

                if (infoContacto[2].equals("left")) {

                    handler = game.getRightKiller();

                } else if (infoContacto[2].equals("right")) {

                    handler = game.getLeftKiller();
                }
                if (handler.getSocket() == null) {

                    handler.setPuerto(Integer.parseInt(infoContacto[3]));
                    handler.setSocket(this.socket);
                }
            }
        }
    }
}
