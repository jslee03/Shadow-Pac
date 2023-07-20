import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * The abstract class Enemy for enemy entities in the game.
 * Subclasses of Enemy are specific types of enemies with unique behaviour and characteristics.
 */
public abstract class Enemy {
    /**
     * The constant value representing number of points player gains when colliding with an enemy during frenzy mode.
     */
    public static final int FRENZY_POINTS = 30;
    private final Image FRENZY_IMAGE = new Image("res/ghostFrenzy.png");
    private final static double SPEED_CHANGE = 0.5;
    private Image image;
    private Image original_image;
    private Point position;
    private boolean isEaten;
    private final Point initialPosition;
    private double speed;
    private boolean isDecreaseSpeed;
    private boolean returnedToNormal;

    /**
     * Checks if enemy has been eaten.
     */
    public boolean isEaten() {
        return isEaten;
    }

    /**
     * Sets the eaten status of enemy.
     */
    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    /**
     * Checks if all enemies has returned to normal after Frenzy Mode.
     */
    public boolean isReturnedToNormal() {
        return returnedToNormal;
    }

    /**
     * Sets the status of enemies to show if it has returned back to normal state.
     */
    public void setReturnedToNormal(boolean returnedToNormal) {
        this.returnedToNormal = returnedToNormal;
    }

    /**
     * Gets the initial position.
     */
    public Point getInitialPosition() {
        return initialPosition;
    }

    /**
     * Sets image to original enemy image.
     */
    public void setOriginal_image(Image original_image) {
        this.original_image = original_image;
    }

    /**
     * Gets the current speed of enemy.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of enemy.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Gets the current position of enemy.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets position of enemy.
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Sets image of enemy.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Gets image of enemy.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Resets the position of enemy.
     */
    public void resetPosition() {
        setPosition(initialPosition);
    }

    /**
     * Constructs a new Enemy abstract class.
     */
    public Enemy(double x, double y) {
        this.position = new Point (x, y);
        this.initialPosition = position;
        this.isDecreaseSpeed = false;
        this.returnedToNormal = false;
    }


    /**
     * Method that moves Enemy in appropriate direction.
     */
    public abstract void move(ArrayList<Wall> walls);


    /**
     * Returns a new Rectangle object representing the current position and size of Enemy image.
     */
    public Rectangle getRect() {
        return new Rectangle(getPosition().x, getPosition().y, image.getWidth(), image.getHeight());
    }


    /**
     * Method that starts frenzy mode.
     * Appearance of enemies change.
     * Speed of enemies are decreased.
     */
    public void frenzyMode() {
        // Change image to frenzy ghost image
        setImage(FRENZY_IMAGE);
        // Decrease speed by 0.5
        if (!isDecreaseSpeed) {
            setSpeed(getSpeed() - SPEED_CHANGE);
            isDecreaseSpeed = true;
        }
    }


    /**
     * Performs a state update.
     */
    public void update(ArrayList<Wall> walls, boolean isFrenzyMode, boolean endFrenzyMode) {
        if (!isEaten) {
            image.drawFromTopLeft(getPosition().x, getPosition().y);
        }

        // Frenzy mode
        if (isFrenzyMode) {
            frenzyMode();
        } else if (!isFrenzyMode && endFrenzyMode && !returnedToNormal) {
            // Reset all speed, image, position to original
            setSpeed(speed + SPEED_CHANGE);
            setImage(original_image);
            setPosition(initialPosition);
            isEaten = false;
            returnedToNormal = true;
        }
        move(walls);
    }
}
