package killergame;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Javi
 */
public class KillerShot extends Autonomous {

    private KillerShip killerShip;
    private int rebotes;

    public KillerShot(KillerGame game, int x, int y, int width, int height, double dX, double dY, double vel, KillerShip killerShip) {

        super(game, x, y, width, height, dX, dY, vel);
        this.killerShip = killerShip;
        this.rebotes = 0;
    }

    @Override
    public void pintar(Graphics g) {

        g.setColor(Color.GREEN);
        g.fillOval(this.x, this.y, this.width, this.height);
    }

    public void addRebote() {

        this.rebotes++;
    }

    public String getKillerShipIp() {

        return this.killerShip.getIp();
    }

    public int getRebotes() {

        return this.rebotes;
    }
}
