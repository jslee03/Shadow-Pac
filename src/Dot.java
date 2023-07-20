import bagel.*;

/**
 * The Dot class represents a type of item in Level 0 and Level 1 of the game.
 * Dot objects are stationary throughout both levels and increases player's points.
 * It extends the Item abstract class.
 */
public class Dot extends Item{
    private final Image IMAGE = new Image("res/dot.png");
    protected final static int POINTS = 10;

    /**
     * Constructs a new Dot class.
     */
    public Dot(double x, double y) {
        super(x, y);
        setImage(IMAGE);
    }
}
