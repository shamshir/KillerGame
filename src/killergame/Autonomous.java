package killergame;

/**
 *
 * @author Javi
 */
public abstract class Autonomous extends Alive {

    public Autonomous(KillerGame game, int x, int y, int width, int height, double dX, double dY, double vel) {

        super(game, x, y, width, height, dX, dY, vel);
    }
}
