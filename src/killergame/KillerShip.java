package killergame;

import java.awt.Color;

/**
 *
 * @author Javi
 */
public class KillerShip extends Controlled {

    public KillerShip(KillerGame game, String ip, String user, Color color) {

        super(game, 0, 0, 60, 60, 3.5, ip, user, color);

        this.x = this.game.getViewer().getWidth() / 2;
        this.y = this.game.getViewer().getHeight() / 2;
    }

    public void disparar() {

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
}
