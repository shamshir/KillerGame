package killergame;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Javi
 */
public abstract class VisibleObject implements Renderizable, Colisionable {

    protected KillerGame game;
    protected boolean alive = true;
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public VisibleObject(KillerGame game, int x, int y, int width, int height) {

        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void pintar(Graphics g) {

    }

    @Override
    public boolean intersects(VisibleObject target) {

        Rectangle thisRectangle = new Rectangle(this.x - 1, this.y - 1, this.width + 2, this.height + 2);
        Rectangle tarRectangle = new Rectangle(target.getX() - 1, target.getY() - 1, target.getWidth() + 2, target.getHeigth() + 2);

        boolean doWeIntersect = thisRectangle.intersects(tarRectangle);

        return doWeIntersect;
    }

    /*##########################################################################
    ###### Getters & Setters ###################################################
    ##########################################################################*/
    public boolean isAlive() {

        return this.alive;
    }

    public int getX() {

        return this.x;
    }

    public int getY() {

        return this.y;
    }

    public int getWidth() {

        return this.width;
    }

    public int getHeigth() {

        return this.height;
    }

    public void setAlive(boolean alive) {

        this.alive = alive;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }
}
