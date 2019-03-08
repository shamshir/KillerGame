package killergame;

import java.awt.Color;

/**
 *
 * @author Javi
 */
public class BolaInmortal extends Bola {
    
    public BolaInmortal(KillerGame game, int x, int y, int width, int height, double dX, double dY, double vel) {
        
        super(game, x, y, width, height, dX, dY, vel);
        this.color = Color.WHITE;
    }
    
    public void grow() {
        
        this.width += 5;
        this.height += 5;
    }
    
}
