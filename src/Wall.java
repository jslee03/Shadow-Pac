import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The Wall class represents a wall in Level 0 and Level 1 of the game.
 * Wall is a stationary object where no other entities of the game can overlap
 *
 */
public class Wall {
    private final Image WALL_IMAGE = new Image("res/wall.png");
    private final Point wallPosition;

    /**
     * Constructs a new Wall class.
     */
    public Wall(double x, double y) {
        wallPosition = new Point(x, y);
    }

    /**
     * Performs a state update.
     */
    public void update() {
        WALL_IMAGE.drawFromTopLeft(wallPosition.x, wallPosition.y);
    }

    /**
     * Returns a new Rectangle object representing the current position and size of Wall image.
     */
    public Rectangle getWallRect() {
        return new Rectangle(wallPosition, WALL_IMAGE.getWidth(), WALL_IMAGE.getHeight());
    }
}
