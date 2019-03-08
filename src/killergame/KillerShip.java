package killergame;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Javi
 */
public class KillerShip extends Controlled {

    int cargaDisparo;

    public KillerShip(KillerGame game, String ip, String user, Color color) {

        super(game, 0, 0, 60, 60, 3.5, ip, user, color);

        this.x = this.game.getViewer().getWidth() / 2;
        this.y = this.game.getViewer().getHeight() / 2;

        this.cargaDisparo = 1;
    }

    @Override
    public void pintar(Graphics g) {

        g.setColor(Color.LIGHT_GRAY);
        if (cargaDisparo >= 600) {
            this.cargaDisparo = 600;
            g.setColor(Color.ORANGE);
        }
        g.fillRect(this.x, this.y - 15, cargaDisparo / 10, 10);
        g.setColor(this.color);
        g.drawString(this.user, this.x, this.y + this.height + 15);
        g.fillOval(this.x, this.y, this.width, this.height);
    }

    @Override
    public void movimientoControlado() {

        ajustarMovimiento();

        this.x += (int) this.dX;
        this.y += (int) this.dY;

        this.dX = 0;
        this.dY = 0;

        this.cargaDisparo++;
    }

    public void disparar() {

        if (this.cargaDisparo >= 600) {
            disparoMultiple();
            this.cargaDisparo = 0;
        } else {
            disparoUnico();
        }
    }

    public void disparoUnico() {

        /* Inicialización de Variables del Disparo */
        int shotX = this.x + (this.width / 2) - 8;
        int shotY = this.y + (this.height / 2) - 8;
        double shotDX = 0;
        double shotDY = 0;

        /* Ajuste de Variables del Disparo */
        if (this.up) {
            shotY = this.y - (this.height / 2);
            shotDY = -1;
        }
        if (this.right) {
            shotX = this.x + this.width + (this.width / 2);
            shotDX = 1;
        }
        if (this.down) {
            shotY = this.y + this.height + (this.height / 2);
            shotDY = 1;
        }
        if (this.left) {
            shotX = this.x - (this.width / 2);
            shotDX = -1;
        }

        /* Crear Disparo */
        KillerShot disparo = new KillerShot(this.game, shotX, shotY, 16, 16, shotDX, shotDY, this.vel * 2, this);
        this.game.addVisibleObject(disparo);
    }

    public void disparoMultiple() {

        KillerShot dUp = new KillerShot(this.game, this.x + (this.width / 2) - 8, this.y - (this.height / 2), 16, 16, 0, -1, this.vel * 2, this);
        this.game.addVisibleObject(dUp);

        KillerShot dUpRight = new KillerShot(this.game, this.x + this.width + (this.width / 2), this.y - (this.height / 2), 16, 16, 1, -1, this.vel * 2, this);
        this.game.addVisibleObject(dUpRight);

        KillerShot dRight = new KillerShot(this.game, this.x + this.width + (this.width / 2), this.y + (this.height / 2) - 8, 16, 16, 1, 0, this.vel * 2, this);
        this.game.addVisibleObject(dRight);

        KillerShot dDownRight = new KillerShot(this.game, this.x + this.width + (this.width / 2), this.y + this.height + (this.height / 2), 16, 16, 1, 1, this.vel * 2, this);
        this.game.addVisibleObject(dDownRight);

        KillerShot dDown = new KillerShot(this.game, this.x + (this.width / 2) - 8, this.y + this.height + (this.height / 2), 16, 16, 0, 1, this.vel * 2, this);
        this.game.addVisibleObject(dDown);

        KillerShot dDownLeft = new KillerShot(this.game, this.x - (this.width / 2), this.y + this.height + (this.height / 2), 16, 16, -1, 1, this.vel * 2, this);
        this.game.addVisibleObject(dDownLeft);

        KillerShot dLeft = new KillerShot(this.game, this.x - (this.width / 2), this.y + (this.height / 2) - 8, 16, 16, -1, 0, this.vel * 2, this);
        this.game.addVisibleObject(dLeft);

        KillerShot dUpLeft = new KillerShot(this.game, this.x - (this.width / 2), this.y - (this.height / 2), 16, 16, -1, -1, this.vel * 2, this);
        this.game.addVisibleObject(dUpLeft);
    }
}
