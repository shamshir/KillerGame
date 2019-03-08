package killergame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javi
 */
public class Viewer extends Canvas implements Runnable {

    //private BufferedImage fondo;
    private KillerGame game;
    private int fps = 60;
    private double target = 1000 / fps;

    public Viewer(KillerGame game) {

        super();
        this.setSize(1400, 800);
        //this.fondo = new BufferedImage(this.getWidth(), this.getHeight(), TYPE_3BYTE_BGR);
        this.game = game;
    }

    @Override
    public void run() {

        while (true) {

            try {

                pintarFrame();

                Thread.sleep((long) target);

            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void paint(Graphics g) {

    }

    /*##########################################################################
    ###### Getters & Setters ###################################################
    ##########################################################################*/
    private void pintarFrame() {

        //BufferedImage frame = clonarBI(this.fondo);
        BufferedImage frame = new BufferedImage(this.getWidth(), this.getHeight(), TYPE_3BYTE_BGR);
        Graphics frameGraphics = frame.getGraphics();

        pintarElementos(frameGraphics);

        pintarInfoConexiones(frameGraphics);

        this.getGraphics().drawImage(frame, 0, 0, null);
    }

    private void pintarElementos(Graphics g) {

        ArrayList<VisibleObject> objetosPintables = this.game.getVisibleObjects();

        for (int i = 0; i < objetosPintables.size(); i++) {

            VisibleObject object = objetosPintables.get(i);

            object.pintar(g);
        }
    }

    private void pintarInfoConexiones(Graphics g) {

        /* Info Propia */
        String ip = this.game.getIp();
        String puerto = String.valueOf(this.game.getPuerto());

        g.setColor(Color.white);
        g.drawString(ip, (this.getWidth() / 2 - 30), 20);
        g.drawString(puerto, (this.getWidth() / 2 - 30), 40);

        /* Info Left */
        boolean leftOnline = this.game.getLeftKiller().getSocket() != null;
        String ipLeft = this.game.getLeftKiller().getIp();
        if (ipLeft == null) {
            ipLeft = "";
        }
        String puertoLeft = String.valueOf(this.game.getLeftKiller().getPuerto());

        if (leftOnline) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.red);
        }
        g.fillRect(5, 5, 90, 15);
        g.setColor(Color.white);
        g.drawString(ipLeft, 5, 40);
        g.drawString(puertoLeft, 5, 60);

        /* Info Right */
        boolean rightOnline = this.game.getRightKiller().getSocket() != null;
        String ipRight = this.game.getRightKiller().getIp();
        if (ipRight == null) {
            ipRight = "";
        }
        String puertoRight = String.valueOf(this.game.getRightKiller().getPuerto());

        if (rightOnline) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.red);
        }
        g.fillRect((this.getWidth() - 95), 5, 90, 15);
        g.setColor(Color.white);
        g.drawString(ipRight, (this.getWidth() - 95), 40);
        g.drawString(puertoRight, (this.getWidth() - 95), 60);
    }

    // Deprecated, por ahora
    private BufferedImage clonarBI(BufferedImage bi) {

        ColorModel colorM = bi.getColorModel();
        boolean alpha = colorM.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);

        return new BufferedImage(colorM, raster, alpha, null);
    }
}
