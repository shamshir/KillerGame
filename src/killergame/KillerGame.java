package killergame;

import java.awt.Color;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Javi
 */
public class KillerGame extends JFrame {

    private VisualHandler leftKiller;
    private VisualHandler rightKiller;
    private KillerServer server;
    private String ip;
    private int puerto;

    private boolean gameStarted = false;
    private KillerPanel killerPanel;
    private Viewer viewer;
    private ArrayList<VisibleObject> objetosVisibles;
    private ArrayList<KillerPad> killerPads;

    public KillerGame() {

        super();

        generarComunicaciones();

        generarUI();
    }

    /*##########################################################################
    ###### Game Flow ###########################################################
    ##########################################################################*/
    public void startGame() {

        if (!this.gameStarted) {

            /* Avisamos a los otros equipos */
            if (this.rightKiller.getSocket() != null) {

                this.rightKiller.sendStart();

            } else if (this.leftKiller.getSocket() != null) {

                this.leftKiller.sendStart();
                
            }

            /* Actualizamos estado y ocultamos el panel de configuración */
            this.gameStarted = true;
            this.killerPanel.setVisible(false);

            /* Iniciamos los threads necesarios */
            new Thread(this.viewer).start();
            for (VisibleObject object : this.objetosVisibles) {
                if (object instanceof Alive) {
                    new Thread((Runnable) object).start();
                }
            }
        }
    }

    /*#### Add & Create ######################################################*/
    public void addKillerPad(KillerPad killerPad) {

        this.killerPads.add(killerPad);
        new Thread(killerPad).start();
    }

    public void addVisibleObject(VisibleObject object) {

        this.objetosVisibles.add(object);
        new Thread((Runnable) object).start();
    }

    public void createKillerShip(String ip, String user, String color) {

        Color processedColor = new Color(Integer.parseInt(color, 16));
        KillerShip killerShip = new KillerShip(this, ip, user, processedColor);
        addVisibleObject(killerShip);
    }

    public void createKillerShip(String ip, String user, Color color, int x, int y, String cardinalidad) {

        KillerShip killerShip = new KillerShip(this, ip, user, color);
        killerShip.setX(x);
        killerShip.setY(y);
        killerShip.setCardinalidadMovimiento(cardinalidad);
        addVisibleObject(killerShip);
    }

    public void createKillerPadAndKillerShip(Socket socket, String user, String color) {

        /* Crear, Añadir e Iniciar KillerPad */
        KillerPad killerPad = new KillerPad(socket, this, user, color);
        addKillerPad(killerPad);

        /* Crear, Añadir e Iniciar KillerShip */
        String ip = killerPad.getIp();
        createKillerShip(ip, user, color);
    }

    /*#### Delete & Remove ###################################################*/
    public void deleteKillerPadAndKillerShip(String targetIp) {

        /* Eliminamos KillerShip */
        KillerShip killerShip = findShip(targetIp);

        if (killerShip == null) {

            this.rightKiller.sendKillerShipRemoval(targetIp);

        } else {

            removeVisibleObject(killerShip);

            /* Eliminar KillerPad */
            for (int i = 0; i < this.killerPads.size(); i++) {

                if (this.killerPads.get(i).getIp().equals(targetIp)) {

                    KillerPad killerPadAEliminar = this.killerPads.get(i);

                    this.killerPads.remove(killerPadAEliminar);
                }
            }

        }
    }

    public void removeKillerShip(KillerShip killerShip) {

        KillerPad killerPad = findPad(killerShip.getIp());

        if (killerPad == null) {

            this.rightKiller.sendDeathNotification(killerShip.getIp());

        } else {

            killerPad.sendDeath();
        }

        removeVisibleObject(killerShip);
    }

    public void removeVisibleObject(VisibleObject object) {

        for (int i = 0; i < this.objetosVisibles.size(); i++) {

            VisibleObject target = this.objetosVisibles.get(i);

            if (object.equals(target)) {

                object.setAlive(false);
                this.objetosVisibles.remove(object);
                break;
            }
        }
    }

    /*#### Bounce & Find #####################################################*/
    public void bounceAlive(Alive object) {

        object.setdX(object.getdX() * -1);
        object.setdY(object.getdY() * -1);
    }

    public KillerShip findShip(String targetIp) {

        KillerShip killerShip = null;

        for (int i = 0; i < this.objetosVisibles.size(); i++) {

            if (this.objetosVisibles.get(i) instanceof KillerShip) {

                KillerShip killerShipCandidato = (KillerShip) this.objetosVisibles.get(i);

                if (killerShipCandidato.getIp().equals(targetIp)) {

                    killerShip = killerShipCandidato;
                }
            }
        }

        return killerShip;
    }

    public KillerPad findPad(String targetIp) {

        KillerPad killerPad = null;

        for (int i = 0; i < this.killerPads.size(); i++) {

            if (this.killerPads.get(i) instanceof KillerPad) {

                KillerPad killerPadCandidato = (KillerPad) this.killerPads.get(i);

                if (killerPadCandidato.getIp().equals(targetIp)) {

                    killerPad = killerPadCandidato;
                }
            }
        }

        return killerPad;
    }

    /*##########################################################################
    ###### Collisions ##########################################################
    ##########################################################################*/
    public synchronized void comprobarColision(Alive object) {

        comprobarBordePantalla(object);

        comprobarOtrosObjetos(object);
    }

    private void comprobarBordePantalla(Alive object) {

        int objX = object.getX();
        int objY = object.getY();

        int limiteX = this.viewer.getWidth() - object.getWidth();
        int limiteY = this.viewer.getHeight() - object.getHeigth();

        if (object instanceof Bola) {
            /* La Bola choca Derecha */
            if (objX >= limiteX) {
                if (this.rightKiller.getSocket() != null) {
                    if (object instanceof Bola) {
                        removeVisibleObject(object);
                        this.rightKiller.sendBola((Bola) object);
                    }
                } else {
                    object.setdX(-object.getdX());
                }
            }
            /* La Bola choca Izquierda */
            if (objX <= 0) {
                if (this.leftKiller.getSocket() != null) {
                    if (object instanceof Bola) {
                        removeVisibleObject(object);
                        this.leftKiller.sendBola((Bola) object);
                    }
                } else {
                    object.setdX(-object.getdX());
                }
            }
            /* La Bola choca Arriba o Abajo */
            if (objY <= 0 || objY >= limiteY) {
                object.setdY(-object.getdY());
            }
        }
        if (object instanceof KillerShot) {
            /* El KillerShot choca Derecha o Izquierda */
            if ((objX <= 0 || objX >= limiteX) && object.getdY() != 0) {
                /* Gestión de los Rebotes */
                if (((KillerShot) object).getRebotes() < 3) {
                    ((KillerShot) object).addRebote();
                    object.setdX(-object.getdX());
                } else {
                    removeVisibleObject(object);
                }
            }
            /* El KillerShot Arriba o Abajo */
            if ((objY <= 0 || objY >= limiteY) && object.getdX() != 0) {
                /* Gestión de los Rebotes */
                if (((KillerShot) object).getRebotes() < 3) {
                    ((KillerShot) object).addRebote();
                    object.setdY(-object.getdY());
                } else {
                    removeVisibleObject(object);
                }
            }
        }
        if (object instanceof KillerShip) {
            /* El KillerShip choca Arriba */
            if (objY <= 0) {
                ((KillerShip) object).setStopUp();
            }
            /* El KillerShip choca Derecha */
            if (objX >= limiteX) {
                if (this.rightKiller.getSocket() != null) {
                    removeVisibleObject(object);
                    this.rightKiller.sendKillerShip((KillerShip) object);
                } else {
                    ((KillerShip) object).setStopRight();
                }
            }
            /* El KillerShip choca Abajo */
            if (objY >= limiteY) {
                ((KillerShip) object).setStopDown();
            }
            /* El KillerShip choca Izquierda */
            if (objX <= 0) {
                if (this.leftKiller.getSocket() != null) {
                    removeVisibleObject(object);
                    this.leftKiller.sendKillerShip((KillerShip) object);
                } else {
                    ((KillerShip) object).setStopLeft();
                }
            }
        }
    }

    private void comprobarOtrosObjetos(Alive object) {

        for (int i = 0; i < this.objetosVisibles.size(); i++) {

            VisibleObject target = this.objetosVisibles.get(i);

            if (!object.equals(target)) {

                if (object.intersects(target)) {

                    KillerRules.whatToDo(this, object, target);
                }
            }
        }
    }

    /*##########################################################################
    ###### Configuration & FeedBack ############################################
    ##########################################################################*/
    public void iniciarServidor(int puerto) {

        /* Si el servidor ya está online, cerramos su socket */
        if (this.server.isConnected()) {

            this.server.closeSocket();
        }
        /* Guardamos el nuevo puerto e iniciamos el hilo del servidor */
        this.puerto = puerto;
        this.server.setPort(this.puerto);
        new Thread(this.server).start();
        new Thread(this.leftKiller).start();
        new Thread(this.rightKiller).start();
    }

    public void conectarLeftKiller(String ip, String puerto) {

        if (this.leftKiller.getIp() == null) {

            this.leftKiller.setIp(ip);
            this.leftKiller.setPuerto(Integer.parseInt(puerto));
        }
    }

    public void conectarRightKiller(String ip, String puerto) {

        if (this.rightKiller.getIp() == null) {

            this.rightKiller.setIp(ip);
            this.rightKiller.setPuerto(Integer.parseInt(puerto));
        }
    }

    public void moduloVisualConectado(VisualHandler handler) {
        if (handler.equals(this.leftKiller)) {

            String ip = this.leftKiller.getIp();
            int puerto = this.leftKiller.getPuerto();
            this.killerPanel.leftKillerConectado(ip, puerto);

        } else if (handler.equals(this.rightKiller)) {

            String ip = this.rightKiller.getIp();
            int puerto = this.rightKiller.getPuerto();
            this.killerPanel.rightKillerConectado(ip, puerto);

        }
    }

    public void servidorConectado() {

        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException ex) {
            System.out.println(ex);
        }
        this.killerPanel.servidorConectado();
    }

    public void servidorPuertoOcupado() {

        this.killerPanel.servidorPuertoOcupado();
    }

    private void generarComunicaciones() {

        this.ip = "";

        this.leftKiller = new VisualHandler(this, "left");

        this.rightKiller = new VisualHandler(this, "right");

        this.server = new KillerServer(this);

        this.killerPads = new ArrayList<>();
    }

    private void generarUI() {

        this.killerPanel = new KillerPanel(this);
        this.getContentPane().add(killerPanel);

        this.objetosVisibles = generarObjetosVisiblesIniciales();
        this.viewer = new Viewer(this);
        this.getContentPane().add(viewer);

        this.pack();
        this.setTitle("Killer Game");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private ArrayList<VisibleObject> generarObjetosVisiblesIniciales() {

        ArrayList<VisibleObject> objetos = new ArrayList<>();

        objetos.add(new Bola(this, 10, 40, 30, 30, 1, 1, 2.5));
        objetos.add(new Bola(this, 90, 140, 30, 30, 1, -1, 2.5));
        objetos.add(new Bola(this, 500, 100, 30, 30, -1, -1, 2.5));

        return objetos;
    }

    /*##########################################################################
    ###### Getters & Setters ###################################################
    ##########################################################################*/
    public VisualHandler getLeftKiller() {

        return this.leftKiller;
    }

    public VisualHandler getRightKiller() {

        return this.rightKiller;
    }

    public String getIp() {

        return this.ip;
    }

    public int getPuerto() {

        return this.puerto;
    }

    public Viewer getViewer() {

        return this.viewer;
    }

    public ArrayList<VisibleObject> getVisibleObjects() {

        return this.objetosVisibles;
    }

    /*##########################################################################
    ###### Main ################################################################
    ##########################################################################*/
    public static void main(String[] args) {

        KillerGame game = new KillerGame();
    }
}
