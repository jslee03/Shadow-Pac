import bagel.Input;
import bagel.util.Rectangle;

/**
 * The Level interface represents a game level.
 */
public interface Level {
    /**
     * Method that performs a state update of the level based on user input.
     */
    void update(Input input);

    /**
     * Method used to read file and create objects.
     */
    void readCSV();

    /**
     * Draws winning message on screen.
     */
    void drawWinMessage();

    /**
     * Draws losing message on screen.
     */
    void drawLoseMessage();

    /**
     * Method that checks for collisions between the player and Wall objects.
     * Prevents player from overlapping with walls.
     */
    void wallCollision(Rectangle playerRectangle);

    /**
     * Method that checks for collisions between the player and Dot objects.
     * Dot stops rendering on screen and player gains points.
     */
    void dotCollision(Rectangle playerRectangle);

    /**
     * Method that checks for collisions between the player and Enemy objects.
     * Player loses life.
     * Player gains points during Frenzy Mode
     */
    void enemyCollision(Rectangle playerRectangle);

    /**
     * Checks player's score to determine if target winning score has been reached
     *
     */
    void winDetection(int score);
}
