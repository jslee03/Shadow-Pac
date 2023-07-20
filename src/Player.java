import bagel.*;
import bagel.util.Point;
import bagel.DrawOptions;
import java.lang.Math;

/**
 * The Player class represents a player in Level 0 and Level 1 of the game.
 * Movement of player is controlled by the user.
 * Movement speed is 3 or 4 (during frenzy mode).
 */
public class Player {
    private final Image PAC_CLOSE = new Image("res/pac.png");
    private final Image PAC_OPEN = new Image("res/pacOpen.png");
    private final static Image HEART = new Image("res/heart.png");
    private final static int MAX_PLAYER_LIFE = 3;
    private final static int SWITCH_FRAME = 15;
    private final static double NORMAL_SPEED = 3;
    private final static double FRENZY_SPEED = 4;
    private final static double ANGLE_RIGHT = 0;
    private final static double ANGLE_LEFT = Math.PI;
    private final static double ANGLE_UP = (Math.PI * 3) / 2.0;
    private final static double ANGLE_DOWN = Math.PI / 2.0;

    private final Font SCORE_FONT = new Font("res/FSO8BITR.ttf", SCORE_FONT_SIZE);
    private final static int SCORE_FONT_SIZE = 20;
    private final static String SCORE_STRING = "SCORE ";
    private final static int SCORE_X = 25;
    private final static int SCORE_Y = 25;
    private final static int LIVES_X = 900;
    private final static int LIVES_Y = 10;
    private final static int LIVES_OFFSET = 30;

    private double speed;
    private int score;
    private int lives;
    private Point position;
    private final Point initialPosition;
    private Point previousPosition;
    private String currentDirection;

    private DrawOptions options = new DrawOptions();
    private int frameCounter = 0;
    private boolean isPacOpen = false;
    private boolean returnedToNormal;

    /**
     * Gets current position of player.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets the position of player.
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Gets the previous position of player.
     */
    public Point getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Sets the previous position of player.
     */
    public void setPreviousPosition(Point previousPosition) {
        this.previousPosition = previousPosition;
    }

    /**
     * Gets the image representing pac open for player.
     */
    public Image getPAC_OPEN() {
        return PAC_OPEN;
    }

    /**
     * Gets the player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the player's number of lives left.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Sets the speed of player.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Constructs a new Player class.
     */
    public Player(double x, double y) {
        this.position = new Point(x, y);
        this.initialPosition = position;
        this.currentDirection = "RIGHT";
        this.score = 0;
        this.lives = MAX_PLAYER_LIFE;
        this.speed = NORMAL_SPEED;
        this.returnedToNormal = false;
    }

    /**
     * Rotates and displays Player image, depending on the arrow keys pressed.
     */
    public void playerDisplay(double rotation) {
        // Change display of Player (PAC_CLOSE / PAC_OPEN) every 15 frames
        if (isPacOpen) {
            PAC_OPEN.drawFromTopLeft(position.x, position.y, options.setRotation(rotation));
            if (frameCounter == SWITCH_FRAME) {
                isPacOpen = false;
                frameCounter = 0;
            }
        } else {
            PAC_CLOSE.drawFromTopLeft(position.x, position.y, options.setRotation(rotation));
            if (frameCounter == SWITCH_FRAME) {
                isPacOpen = true;
                frameCounter = 0;
            }
        }
    }

    /**
     * Method that renders the player's score
     * (Method implemented from Project 1 sample solution provided by teaching team)
     */
    private void renderScore(){
        SCORE_FONT.drawString(SCORE_STRING + score, SCORE_X, SCORE_Y);
    }

    /**
     * Method that renders the player's lives
     * (Method implemented from Project 1 sample solution provided by teaching team)
     */
    private void renderLives(){
        for (int i = 0; i < lives; i++){
            HEART.drawFromTopLeft(LIVES_X + (LIVES_OFFSET*i), LIVES_Y);
        }
    }

    /**
     * Method that resets the player's position to the starting location
     * (Method implemented from Project 1 sample solution provided by teaching team)
     */
    public void resetPosition(){
        position = initialPosition;
        playerDisplay(0);
    }

    /**
     * Method that reduces the life of a player by 1.
     * This code snippet is adapted from Project 1 Sample solution provided by teaching team.
     */
    public void reduceLives() {
        lives--;
    }

    /**
     * Method that increments the player's score.
     * This code snippet is adapted from Project 1 Sample solution provided by teaching team.
     */
    public void incrementScore(int score) {
        this.score += score;
    }

    /**
     * Performs a state update.
     */
    public void update (Input input, boolean isFrenzyMode, boolean endFrenzyMode) {
        frameCounter++;
        previousPosition = position;

        // Movement logic
        if (input.isDown(Keys.RIGHT)) {
            currentDirection = "RIGHT";
            position = new Point(this.position.x + speed, this.position.y);
        } else if (input.isDown(Keys.LEFT)) {
            currentDirection = "LEFT";
            position = new Point(this.position.x - speed, this.position.y);
        } else if (input.isDown(Keys.UP)) {
            currentDirection = "UP";
            position = new Point(this.position.x, this.position.y - speed);
        } else if (input.isDown(Keys.DOWN)) {
            currentDirection = "DOWN";
            position = new Point(this.position.x, this.position.y + speed);
        }

         // Player rotation logic:
        switch (currentDirection) {
            case "RIGHT":
                playerDisplay(ANGLE_RIGHT);
                break;
            case "LEFT":
                playerDisplay(ANGLE_LEFT);
                break;
            case "UP":
                playerDisplay(ANGLE_UP);
                break;
            case "DOWN":
                playerDisplay(ANGLE_DOWN);
                break;
        }

        // Change player speed during frenzy mode.
        if (isFrenzyMode) {
            setSpeed(FRENZY_SPEED);
        } else if (!isFrenzyMode && endFrenzyMode && !returnedToNormal) {
            setSpeed(NORMAL_SPEED);
            returnedToNormal = true;
        }

        renderScore();
        renderLives();
    }
}


