import bagel.*;
import bagel.util.Point;
import java.util.ArrayList;

/**
 * The GhostRed class represents a type of enemy in Level 0 and Level 1 of the game.
 * Red ghosts are stationary in Level 0.
 * Movement is restricted horizontally in Level 1.
 * Movement speed is 1 or 0.5 (during frenzy mode).
 * It extends the abstract class Enemy.
 */
public class GhostRed extends Enemy {
    private final Image IMAGE = new Image("res/ghostRed.png");
    private final static double NORMAL_SPEED = 1;
    private boolean isMovingRight;

    /**
     * Constructs a new GhostRed class.
     */
    public GhostRed(double x, double y) {
        super(x, y);
        setImage(IMAGE);
        setSpeed(NORMAL_SPEED);
        setOriginal_image(IMAGE);
        isMovingRight = true;
    }


    @Override
    public void move(ArrayList<Wall> walls) {
        Point newPosition = getPosition();

        // Change direction of Red Ghost when after colliding with a Wall object
        for (Wall wall : walls) {
            if (getRect().intersects(wall.getWallRect()) && isMovingRight) {
                isMovingRight = false;
                newPosition = new Point(getPosition().x - getSpeed(), getPosition().y);
                break;
            } else if (getRect().intersects(wall.getWallRect()) && !isMovingRight) {
                isMovingRight = true;
                newPosition = new Point(getPosition().x + getSpeed(), getPosition().y);
                break;
            } else if (isMovingRight){
                newPosition = new Point(getPosition().x + getSpeed(), getPosition().y);
            } else if (!isMovingRight) {
                newPosition = new Point(getPosition().x - getSpeed(), getPosition().y);
            }
        }
        setPosition(newPosition);
    }


    /**
     * Performs a state update.
     * Only calls movement logic method of Red Ghost if game is in Level 1.
     */
    @Override
    public void update(ArrayList<Wall> walls, boolean isFrenzyMode, boolean endFrenzyMode) {
        if (!isEaten()) {
            getImage().drawFromTopLeft(getPosition().x, getPosition().y);
        }

        // Frenzy mode
        if (isFrenzyMode) {
            frenzyMode();
        } else if (!isFrenzyMode && endFrenzyMode && !isReturnedToNormal()) {
            // Reset all speed, image, position to original
            setSpeed(NORMAL_SPEED);
            setImage(IMAGE);
            setPosition(getInitialPosition());
            setEaten(false);
            setReturnedToNormal(true);
        }

        if (ShadowPac.getCurrentLevel() instanceof Level1) {
            move(walls);
        }
    }







}
