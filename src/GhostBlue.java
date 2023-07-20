import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;

/**
 * The GhostBlue class represents a type of enemy in Level 1 of the game.
 * Movement is restricted vertically.
 * Movement speed is 1 or 0.5 (during frenzy mode).
 * It extends the abstract class Enemy.
 */
public class GhostBlue extends Enemy {
    private final Image IMAGE = new Image("res/ghostBlue.png");
    private final static double NORMAL_SPEED = 2;
    private boolean isMovingDown;

    /**
     * Constructs a new GhostBlue class.
     */
    public GhostBlue(double x, double y) {
        super(x, y);
        setImage(IMAGE);
        setSpeed(NORMAL_SPEED);
        setOriginal_image(IMAGE);
        isMovingDown = true;
    }


    @Override
    public void move(ArrayList<Wall> walls) {
        Point newPosition = getPosition();

        // Change direction of Blue Ghost when after colliding with a Wall object
        for (Wall wall : walls) {
            if (getRect().intersects(wall.getWallRect()) && isMovingDown) {
                isMovingDown = false;
                newPosition = new Point(getPosition().x, getPosition().y - getSpeed());
                break;
            } else if (getRect().intersects(wall.getWallRect()) && !isMovingDown) {
                isMovingDown = true;
                newPosition = new Point(getPosition().x, getPosition().y + getSpeed());
                break;
            } else if (isMovingDown){
                newPosition = new Point(getPosition().x, getPosition().y + getSpeed());
            } else if (!isMovingDown) {
                newPosition = new Point(getPosition().x, getPosition().y - getSpeed());
            }
        }
        setPosition(newPosition);
    }
}
