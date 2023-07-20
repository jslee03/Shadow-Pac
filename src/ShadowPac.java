import bagel.*;
import bagel.util.Point;
import bagel.Window;

/**
 * SWEN20003 Project 2, Semester 1, 2023
 *
 * Please enter your name below
 * @author Jess Lee
 */
public class ShadowPac extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");

    private final static String GAME_TITLE = "SHADOW PAC";
    private final static String GAME_INSTRUCTIONS_0 = "PRESS SPACE TO START\nUSE ARROW KEYS TO MOVE";
    private final static Point TITLE_TEXT_POINT = new Point(260, 250);

    private final static int DEFAULT_FONT_SIZE = 64;
    private final Font DEFAULT_FONT = new Font("res/FSO8BITR.TTF", DEFAULT_FONT_SIZE);
    private final Font INSTRUCTIONS_FONT_0 = new Font("res/FSO8BITR.TTF", 24);
    private static final int INS_X_OFFSET = 60;
    private static final int INS_Y_OFFSET = 190;

    private static boolean isGameStart;
    private boolean isLevel1Start;
    private static Level currentLevel;


    /**
     * Gets the current level of game.
     */
    public static Level getCurrentLevel() {
        return currentLevel;
    }


    /**
     * Checks if game has started.
     */
    public static boolean isGameStart() {
        return isGameStart;
    }


    /**
     * Sets the status of game start.
     */
    public static void setIsGameStart(boolean isGameStart) {
        ShadowPac.isGameStart = isGameStart;
    }


    /**
     * Constructs a new ShadowPac class.
     */
    public ShadowPac() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        isGameStart = false;
        isLevel1Start = false;
        currentLevel = new Level0();
    }


    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        // Close game window if escape key is pressed
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // Display title and instructions
        if (!isGameStart) {
            drawStartScreen();
        }

        // Run level 1 logic if level 0 has ended
        if (currentLevel instanceof Level0 && ((Level0) currentLevel).isLevel0End()) {
            if (!isLevel1Start) {
                moveToLevel1();
                isLevel1Start = true;
            }
        }
        currentLevel.update(input);
    }


    /**
     * Method used to draw the start screen title and instructions.
     */
    public void drawStartScreen(){
        DEFAULT_FONT.drawString(GAME_TITLE, TITLE_TEXT_POINT.x, TITLE_TEXT_POINT.y);
        INSTRUCTIONS_FONT_0.drawString(GAME_INSTRUCTIONS_0, TITLE_TEXT_POINT.x + INS_X_OFFSET, TITLE_TEXT_POINT.y + INS_Y_OFFSET);
    }


    /**
     * Method used to move to Level 1.
     */
    public void moveToLevel1() {
        currentLevel = new Level1();
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPac game = new ShadowPac();
        game.run();
    }
}
