import bagel.Image;

/**
 * The Pellet class represents a type of item in Level 1 of the game.
 * Pellet objects are stationary and initiates frenzy mode.
 * It extends the Item abstract class.
 */
public class Pellet extends Item{
    private final Image IMAGE = new Image("res/pellet.png");

    /**
     * Constructs a new Pellet class.
     */
    public Pellet(double x, double y) {
        super(x, y);
        setImage(IMAGE);
    }
}
