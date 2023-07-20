import bagel.Image;

/**
 * The Cherry class represents a type of item in Level 1 of the game.
 * Cherry objects are stationary and increases player's points.
 * It extends the Item abstract class.
 */
public class Cherry extends Item{
    private final Image IMAGE = new Image("res/cherry.png");
    public final static int POINTS = 20;

    /**
     * Constructs a new Cherry class.
     */
    public Cherry(double x, double y) {
        super(x, y);
        setImage(IMAGE);
    }
}
