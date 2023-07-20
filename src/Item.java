import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The abstract class Item for item entities in the game.
 * Subclasses of Item are specific types of items with unique characteristics.
 */
public abstract class Item {
    private final Point position;
    private Image image;
    private boolean isEaten;

    /**
     * Checks if item has been eaten.
     */
    public boolean isEaten() {
        return isEaten;
    }

    /**
     * Sets the eaten status of item.
     */
    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    /**
     * Gets current position of item.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets the image of item.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Constructs a new Item abstract class.
     */
    public Item(double x, double y) {
        position = new Point(x, y);
        isEaten = false;
    }


    /**
     * Performs a state update.
     */
    public void update() {
        if (!isEaten) {
            image.drawFromTopLeft(getPosition().x, getPosition().y);
        }
    }


    /**
     * Returns a new Rectangle object representing the current position and size of Item image.
     */
    public Rectangle getRect() {
        return new Rectangle(position.x, position.y, image.getWidth(), image.getHeight());
    }
}
