package killergame;

/**
 *
 * @author Javi
 */
public abstract class Alive extends VisibleObject implements Runnable {

    protected double dX;
    protected double dY;
    protected double vel;

    public Alive(KillerGame game, int x, int y, int width, int height, double dX, double dY, double vel) {

        super(game, x, y, width, height);
        this.dX = dX;
        this.dY = dY;
        this.vel = vel;
    }

    @Override
    public void run() {

        while (this.alive) {
            try {

                movimiento();

                this.game.comprobarColision(this);

                Thread.sleep(15);

            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    private void movimiento() {

        this.x += (int) (this.dX * this.vel);
        this.y += (int) (this.dY * this.vel);
    }

    /*##########################################################################
    ###### Getters & Setters ###################################################
    ##########################################################################*/
    public double getVel() {

        return this.vel;
    }

    public double getdX() {

        return this.dX;
    }

    public double getdY() {

        return this.dY;
    }

    public void setdX(double newDX) {

        this.dX = newDX;
    }

    public void setdY(double newDY) {

        this.dY = newDY;
    }
}
