import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * The GhostGreen class represents a type of enemy in Level 1 of the game.
 * Movement is restricted horizontally or vertically, chosen randomly.
 * Movement speed is 4 or 3.5 (during frenzy mode).
 * It extends the abstract class Enemy.
 */
public class GhostGreen extends Enemy {
    private final Image IMAGE = new Image("res/ghostGreen.png");
    private final static double NORMAL_SPEED = 4;
    private int direction;
    private boolean isMovingRight;
    private boolean isMovingDown;


    /**
     * Constructs a new GhostGreen class.
     */
    public GhostGreen(double x, double y) {
        super(x, y);
        setImage(IMAGE);
        setOriginal_image(IMAGE);
        setSpeed(NORMAL_SPEED);

        // Randomly initialise a direction at the start
        Random random = new Random();
        direction = random.nextInt(2);
        if (direction == 0) {
            isMovingRight = true;
            isMovingDown = false;
        } else {
            isMovingRight = false;
            isMovingDown = true;
        }
    }


    @Override
    public void move(ArrayList<Wall> walls) {
        Point newPosition = getPosition();

        // Change direction of Green Ghost when after colliding with a Wall object
        switch (direction) {
            case 0: // Moving horizontally
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
                break;
            case 1: // Moving vertically
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
                break;
        }
        setPosition(newPosition);
    }
}
