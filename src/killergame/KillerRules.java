package killergame;

/**
 *
 * @author Javi
 */
public class KillerRules {

    public static void whatToDo(KillerGame game, VisibleObject object, VisibleObject target) {

        /* Colisión Bola con Bola */
        if (object instanceof Bola && target instanceof Bola) {

            game.bounceAlive((Alive) object);
            game.bounceAlive((Alive) target);
        }
        /* Colisión KillerShot con KillerShot */
        if (object instanceof KillerShot && target instanceof KillerShot) {

            game.removeVisibleObject(object);
            game.removeVisibleObject(target);
        }
        /* Colisión KillerShip con KillerShip */
        if (object instanceof KillerShip && target instanceof KillerShip) {

            game.removeKillerShip((KillerShip) object);
            game.removeKillerShip((KillerShip) target);
        }
        /* Colisión Bola con KillerShot o viceversa */
        if (object instanceof Bola && target instanceof KillerShot
                || object instanceof KillerShot && target instanceof Bola) {

            game.bounceAlive((Alive) object);
            game.bounceAlive((Alive) target);
        }
        /* Colisión Bola con KillerShip o viceversa */
        if (object instanceof Bola && target instanceof KillerShip
                || object instanceof KillerShip && target instanceof Bola) {

            if (object instanceof KillerShip) {
                game.removeKillerShip((KillerShip) object);
                game.removeVisibleObject(target);
            } else {
                game.removeVisibleObject(object);
                game.removeKillerShip((KillerShip) target);
            }
        }
        /* Colisión KillerShip con KillerShot o viceversa */
        if (object instanceof KillerShip && target instanceof KillerShot
                || object instanceof KillerShot && target instanceof KillerShip) {

            if (object instanceof KillerShip) {
                game.removeKillerShip((KillerShip) object);
                game.removeVisibleObject(target);
            } else {
                game.removeVisibleObject(object);
                game.removeKillerShip((KillerShip) target);
            }
        }
    }
}
