package killergame;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Javi
 */
public class VisualHandler implements Runnable {

    private Socket socket;
    private String ip;
    private int puerto;
    private BufferedReader in;
    private PrintWriter out;
    private KillerGame game;
    private String side;
    private KillerClient client;
    private boolean conectado;
    private long okTime;

    public VisualHandler(KillerGame game, String side) {

        this.game = game;
        this.side = side;
        this.client = new KillerClient(this);
    }

    /*##########################################################################
    ###### Behavior ############################################################
    ##########################################################################*/
    @Override
    public void run() {

        new Thread(this.client).start();

        while (true) {

            try {

                if (conectado && this.socket != null) {

                    processMessage();
                }

                Thread.sleep(50);

            } catch (InterruptedException e) {
                System.out.println(e);
            } catch (IOException | NullPointerException e) {

                setSocketNull();
            }
        }

    }

    public void processMessage() throws IOException {

        String line = this.in.readLine();

        String[] info = line.split("/");

        switch (info[0]) {
            case "ok":
                receiveOk();
                break;
            case "start":
                receiveStart();
                break;
            case "bola":
                receiveBola(info);
                break;
            case "killerShip":
                receiveKillerShip(info);
                break;
            case "relay":
                receiveRelay(line, info);
                break;
            default:
                System.out.println("Mensaje desconocido: " + info[0]);
                break;
        }
    }

    public void receiveOk() {

        this.okTime = System.currentTimeMillis();
        this.out.println("ok");
    }

    public void receiveStart() {

        this.game.startGame();
    }

    public void receiveBola(String[] info) {

        String[] infoBola = info[1].split(";");

        double percentage = Double.parseDouble(infoBola[0]);
        int width = Integer.parseInt(infoBola[1]);
        int height = Integer.parseInt(infoBola[2]);
        double dX = Double.parseDouble(infoBola[3]);
        double dY = Double.parseDouble(infoBola[4]);
        double vel = Double.parseDouble(infoBola[5]);

        int x;
        if (this.side.equals("left")) {
            x = 1;
        } else {
            x = this.game.getViewer().getWidth() - width - 1;
        }
        int y = (int) (this.game.getViewer().getHeight() * percentage);

        Bola bola = new Bola(this.game, x, y, width, height, dX, dY, vel);

        this.game.addVisibleObject(bola);
    }

    public void receiveKillerShip(String[] info) {

        String[] infoKillerShip = info[1].split(";");

        double percentage = Double.parseDouble(infoKillerShip[0]);
        String cardinalidad = infoKillerShip[1];
        String ipShip = infoKillerShip[2];
        String user = infoKillerShip[3];
        Color color = new Color(Integer.parseInt(infoKillerShip[4]), Integer.parseInt(infoKillerShip[5]), Integer.parseInt(infoKillerShip[6]));

        int x;
        if (this.side.equals("left")) {
            x = 1;
        } else {
            x = this.game.getViewer().getWidth() - 60 - 1;
        }
        int y = (int) (this.game.getViewer().getHeight() * percentage);

        this.game.createKillerShip(ipShip, user, color, x, y, cardinalidad);
    }

    public void receiveRelay(String relayIntacto, String[] info) {

        /* Desmenuzamos la Información del Relay */
        String asunto = info[1];
        String[] infoRelay = info[2].split(";");
        String ipOrigen = infoRelay[0];
        int puertoOrigen = Integer.parseInt(infoRelay[1]);
        String ipShip = infoRelay[2];
        String orden;

        /* Comprobamos que no sea nuestro propio Relay */
        if (!ipOrigen.equals(this.game.getIp()) || puertoOrigen != this.game.getPuerto()) {

            /* Aplicamos el Relay Localmente */
            switch (asunto) {
                case "order":
                    orden = infoRelay[3];
                    if (this.game.findShip(ipShip) != null) {
                        KillerPad.decodificarMensaje(this.game, ipShip, orden);
                    } else {
                        this.game.getRightKiller().reSendRelay(relayIntacto);
                    }
                    break;
                case "death":
                    if (this.game.findPad(ipShip) != null) {
                        this.game.findPad(ipShip).sendDeath();
                    } else {
                        this.game.getRightKiller().reSendRelay(relayIntacto);
                    }
                    break;
                case "removal":
                    if (this.game.findShip(ipShip) != null) {
                        KillerShip killerShip = this.game.findShip(ipShip);
                        this.game.removeVisibleObject(killerShip);
                    } else {
                        this.game.getRightKiller().reSendRelay(relayIntacto);
                    }
                    break;
                default:
                    System.out.println("Asunto desconocido: " + asunto);
                    break;
            }
        }
    }

    public void reSendRelay(String mensaje) {

        this.out.println(mensaje);
    }

    public void sendOk() {

        this.out.println("ok");
    }

    public void sendStart() {

        this.out.println("start");
    }

    public void sendBola(Bola bola) {

        double percentage = (double) bola.getY() / this.game.getViewer().getHeight();

        String info
                = "bola/"
                + percentage + ";"
                + bola.getWidth() + ";"
                + bola.getHeigth() + ";"
                + bola.getdX() + ";"
                + bola.getdY() + ";"
                + bola.getVel();

        this.out.println(info);

        // bola/<percentage>;<width>;<height>;<dX>;<dY>;<vel>
    }

    public void sendKillerShip(KillerShip killerShip) {

        double percentage = (double) killerShip.getY() / this.game.getViewer().getHeight();

        String info
                = "killerShip/"
                + percentage + ";"
                + killerShip.getCardinalidad() + ";"
                + killerShip.getIp() + ";"
                + killerShip.getUser() + ";"
                + killerShip.getColor().getRed() + ";"
                + killerShip.getColor().getGreen() + ";"
                + killerShip.getColor().getBlue();

        this.out.println(info);

        // killerShip/<percentage>;<cardinalidad>;<ip>;<user>;<r>;<g>;<b>
    }

    public void sendKillerShipOrder(String ip, String orden) {

        String info
                = "relay/order/"
                + this.game.getIp() + ";"
                + this.game.getPuerto() + ";"
                + ip + ";"
                + orden;

        this.out.println(info);

        // relay/order/<ipOrigen>;<puertoOrigen>;<ip>;<orden>
    }

    public void sendDeathNotification(String ip) {

        String info
                = "relay/death/"
                + this.game.getIp() + ";"
                + this.game.getPuerto() + ";"
                + ip;

        this.out.println(info);

        // relay/death/<ipOrigen>;<puertoOrigen>;<ip>
    }

    public void sendKillerShipRemoval(String ip) {

        String info
                = "relay/removal/"
                + this.game.getIp() + ";"
                + this.game.getPuerto() + ";"
                + ip;

        this.out.println(info);

        // relay/removal/<ipOrigen>;<puertoOrigen>;<ip>
    }

    /*##########################################################################
    ###### Getters & Setters ###################################################
    ##########################################################################*/
    public KillerGame getGame() {

        return this.game;
    }

    public String getIp() {

        return this.ip;
    }

    public long getOkTime() {

        return this.okTime;
    }

    public int getPuerto() {

        return this.puerto;
    }

    public String getSide() {

        return this.side;
    }

    public synchronized Socket getSocket() {

        return this.socket;
    }

    public void setIp(String ip) {

        this.ip = ip;
    }

    public void setPuerto(int puerto) {

        this.puerto = puerto;
    }

    public synchronized void setSocket(Socket socket) {

        try {

            this.socket = socket;
            this.ip = socket.getInetAddress().getHostAddress();
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.conectado = true;
            sendOk();
            this.okTime = System.currentTimeMillis();
            this.game.moduloVisualConectado(this);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public synchronized void setSocketNull() {

        try {

            if (this.socket != null) {
                this.socket.close();
            }
            this.socket = null;
            this.in = null;
            this.out = null;
            this.conectado = false;
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
