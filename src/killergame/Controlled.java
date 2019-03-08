package killergame;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Javi
 */
public abstract class Controlled extends Alive {

    protected boolean up;
    protected boolean right;
    protected boolean down;
    protected boolean left;
    protected boolean stopUp;
    protected boolean stopRight;
    protected boolean stopDown;
    protected boolean stopLeft;
    protected String cardinalidad;
    protected String ip;
    protected String user;
    protected Color color;

    public Controlled(KillerGame game, int x, int y, int width, int height, double vel, String ip, String user, Color color) {

        super(game, x, y, width, height, 0, 0, vel);
        this.ip = ip;
        this.user = user;
        this.color = color;

        this.up = false;
        this.right = false;
        this.down = false;
        this.left = false;
        this.stopUp = false;
        this.stopRight = false;
        this.stopDown = false;
        this.stopLeft = false;
        this.cardinalidad = "idle";
    }

    @Override
    public void run() {

        while (this.alive) {
            try {

                movimientoControlado();

                this.game.comprobarColision(this);

                Thread.sleep(15);

            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void pintar(Graphics g) {

        g.setColor(this.color);
        g.drawString(this.user, this.x, this.y + this.height + 15);
        g.fillOval(this.x, this.y, this.width, this.height);
    }

    public void setCardinalidadMovimiento(String cardinalidad) {

        switch (cardinalidad) {
            case "idle":
                this.up = false;
                this.right = false;
                this.down = false;
                this.left = false;
                break;
            case "up":
                this.up = true;
                this.right = false;
                this.down = false;
                this.left = false;
                break;
            case "upright":
                this.up = true;
                this.right = true;
                this.down = false;
                this.left = false;
                break;
            case "right":
                this.up = false;
                this.right = true;
                this.down = false;
                this.left = false;
                break;
            case "downright":
                this.up = false;
                this.right = true;
                this.down = true;
                this.left = false;
                break;
            case "down":
                this.up = false;
                this.right = false;
                this.down = true;
                this.left = false;
                break;
            case "downleft":
                this.up = false;
                this.right = false;
                this.down = true;
                this.left = true;
                break;
            case "left":
                this.up = false;
                this.right = false;
                this.down = false;
                this.left = true;
                break;
            case "upleft":
                this.up = true;
                this.right = false;
                this.down = false;
                this.left = true;
                break;
        }

        this.cardinalidad = cardinalidad;
    }

    public void movimientoControlado() {

        ajustarMovimiento();

        this.x += (int) this.dX;
        this.y += (int) this.dY;

        this.dX = 0;
        this.dY = 0;
    }

    public void ajustarMovimiento() {

        if (this.up && !this.stopUp) {

            this.dY = -this.vel;

            if (this.stopDown) {

                this.stopDown = false;
            }
        }
        if (this.right && !this.stopRight) {

            this.dX = this.vel;

            if (this.stopLeft) {

                this.stopLeft = false;
            }
        }
        if (this.down && !this.stopDown) {

            this.dY = this.vel;

            if (this.stopUp) {

                this.stopUp = false;
            }
        }
        if (this.left && !this.stopLeft) {

            this.dX = -this.vel;

            if (this.stopRight) {

                this.stopRight = false;
            }
        }
    }

    /*##########################################################################
    ###### Getters & Setters ###################################################
    ##########################################################################*/
    public String getCardinalidad() {

        return this.cardinalidad;
    }

    public Color getColor() {

        return this.color;
    }

    public String getIp() {

        return this.ip;
    }

    public String getUser() {

        return this.user;
    }

    public void setStopUp() {

        this.stopUp = true;
    }

    public void setStopRight() {

        this.stopRight = true;
    }

    public void setStopDown() {

        this.stopDown = true;
    }

    public void setStopLeft() {

        this.stopLeft = true;
    }
}
