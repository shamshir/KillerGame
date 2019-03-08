package killergame;

/**
 *
 * @author Javi
 */
import java.awt.Color;
import java.awt.Graphics;

public class Bola extends Autonomous {

    protected Color color;

    public Bola(KillerGame game, int x, int y, int width, int height, double dX, double dY, double vel) {

        super(game, x, y, width, height, dX, dY, vel);
        this.color = Color.BLUE;
    }

    @Override
    public void pintar(Graphics g) {

        g.setColor(this.color);
        g.fillOval(this.x, this.y, this.width, this.height);
    }
}
