import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * The GhostPink class represents a type of enemy in Level 1 of the game.
 * Movement is restricted up, down, left or right, chosen randomly.
 * Movement speed is 3 or 2.5 (during frenzy mode).
 * It extends the abstract class Enemy.
 */
public class GhostPink extends Enemy {
    private final Image IMAGE = new Image("res/ghostPink.png");
    private final static double NORMAL_SPEED = 3;
    private final static int NO_EXCLUDE = 5;

    private int currDirection;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;

    private Point newPosition;
    private Point previousPosition;

    /**
     * Constructs a new GhostPink class.
     */
    public GhostPink(double x, double y) {
        super(x, y);
        setImage(IMAGE);
        setOriginal_image(IMAGE);
        setSpeed(NORMAL_SPEED);
        setRandomDirection(NO_EXCLUDE);
    }


    @Override
    public void move(ArrayList<Wall> walls) {
        // Check for collision with each wall
        for (Wall wall : walls) {
            if (getRect().intersects(wall.getWallRect())) {
                setPosition(previousPosition);
                // Generate a random direction that is not the current direction
                setRandomDirection(currDirection);
                break;
            }
        }

        // Movement logic
        if (isMovingRight) {
            newPosition = new Point(getPosition().x + getSpeed(), getPosition().y);
        } else if (isMovingLeft) {
            newPosition = new Point(getPosition().x - getSpeed(), getPosition().y);
        } else if (isMovingUp) {
            newPosition = new Point(getPosition().x, getPosition().y + getSpeed());
        } else if (isMovingDown) {
            newPosition = new Point(getPosition().x, getPosition().y - getSpeed());
        }
        previousPosition = getPosition();
        setPosition(newPosition);
    }


    /**
     * Method that assigns a new random direction that is different from the current direction.
     */
    public void setRandomDirection(int excluding) {
        Random random = new Random();
        int newDirection = excluding;

        // Generates a random number between 0 and 3 for first direction change
        if (excluding == NO_EXCLUDE)  {
            newDirection = random.nextInt(4);
        }

        // Generate a random number between 0 and 3 that excludes integer "excluding"
        while (newDirection == excluding) {
            newDirection = random.nextInt(4);
        }

        // Assign a new direction based on the previously produced random number
        switch (newDirection) {
            case 0: // Right
                isMovingLeft = false;
                isMovingRight = true;
                isMovingUp = false;
                isMovingDown = false;
                break;
            case 1: // Left
                isMovingLeft = true;
                isMovingRight = false;
                isMovingUp = false;
                isMovingDown = false;
                break;
            case 2: // Up
                isMovingLeft = false;
                isMovingRight = false;
                isMovingUp = true;
                isMovingDown = false;
                break;
            case 3: // Down
                isMovingLeft = false;
                isMovingRight = false;
                isMovingUp = false;
                isMovingDown = true;
                break;
        }
        currDirection = newDirection;
    }
}
