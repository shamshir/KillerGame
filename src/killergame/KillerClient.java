package killergame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Javi
 */
public class KillerClient implements Runnable {

    private VisualHandler handler;

    public KillerClient(VisualHandler handler) {

        this.handler = handler;
    }

    @Override
    public void run() {

        while (true) {

            try {

                if (handler.getSocket() == null) {

                    if (handler.getIp() != null && handler.getPuerto() > 0) {

                        Socket socket = new Socket(handler.getIp(), handler.getPuerto());
                        iniciarContacto(socket);
                        handler.setSocket(socket);
                        handler.sendOk();
                    }
                } else {

                    double time = System.currentTimeMillis();
                    if ((time - handler.getOkTime()) >= 1000) {
                        handler.setSocketNull();
                    }
                }

                Thread.sleep(200);

            } catch (InterruptedException | IOException e) {
                System.out.println(e);
            }
        }
    }

    private void iniciarContacto(Socket socket) throws IOException {

        String mensajeContacto = "contacto/visualHandler/";

        if (this.handler.getSide().equals("left")) {

            mensajeContacto += "left/";

        } else if (this.handler.getSide().equals("right")) {

            mensajeContacto += "right/";

        }

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(mensajeContacto + handler.getGame().getPuerto());

        // contacto/visualHandler/<left/right>/<puertoServidor>
    }
}
