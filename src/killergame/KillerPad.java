package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Javi
 */
public class KillerPad implements Runnable {

    private Socket socket;
    private boolean online;
    private String ip;
    private BufferedReader in;
    private PrintWriter out;
    private KillerGame game;
    private String user;
    private String color;

    public KillerPad(Socket socket, KillerGame game, String user, String color) {

        try {

            this.socket = socket;
            this.online = true;
            this.ip = this.socket.getInetAddress().getHostAddress();
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.game = game;
            this.user = user;
            this.color = color;

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /*##########################################################################
    ###### Behavior ############################################################
    ##########################################################################*/
    @Override
    public void run() {

        while (online) {

            try {

                processPadMessage();

                Thread.sleep(30);

            } catch (InterruptedException e) {
                System.out.println(e);
            } catch (IOException e) {
                try {
                    this.socket.close();
                    this.socket = null;
                    this.online = false;
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
        this.game.deleteKillerPadAndKillerShip(this.ip);
    }

    public void sendDeath() {

        this.out.println("ded");
    }

    private void processPadMessage() throws IOException {

        String mensaje = this.in.readLine();

        if (mensaje == null || mensaje.equals("bye")) {

            this.online = false;

        } else if (mensaje.equals("replay")) {

            this.game.createKillerShip(this.ip, this.user, this.color);

        } else {

            KillerShip killerShip = this.game.findShip(this.ip);

            if (killerShip == null) {

                this.game.getRightKiller().sendKillerShipOrder(this.ip, mensaje);

            } else {

                decodificarMensaje(this.game, this.ip, mensaje);
            }
        }
    }

    public static void decodificarMensaje(KillerGame game, String ip, String mensaje) {

        switch (mensaje) {
            case "shoot":
                game.findShip(ip).disparar();
                break;
            //case "idle":
            case "up":
            case "upright":
            case "right":
            case "downright":
            case "down":
            case "downleft":
            case "left":
            case "upleft":
                game.findShip(ip).setCardinalidadMovimiento(mensaje);
                break;
            default:
                System.out.println("Comando indecodificable (Activar 'idle'");
                break;
        }
    }

    /*##########################################################################
    ###### Getters & Setters ###################################################
    ##########################################################################*/
    public String getIp() {

        return this.ip;
    }
}
