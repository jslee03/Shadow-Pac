import bagel.Font;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Level1 class represents the second level implementation for the game.
 * It implements the Level interface.
 */
public class Level1 implements Level{
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int DEFAULT_FONT_SIZE = 64;
    private final Font DEFAULT_FONT = new Font("res/FSO8BITR.TTF", DEFAULT_FONT_SIZE);

    private final static Point INSTRUCTIONS_TEXT_POINT = new Point(200, 350);
    private final Font INSTRUCTIONS_FONT_1 = new Font("res/FSO8BITR.TTF", 40);
    private final static String GAME_INSTRUCTIONS_1 = "PRESS SPACE TO START\nUSE ARROW KEYS TO MOVE\n" +
            "EAT THE PELLET TO ATTACK";

    private final static String LEVEL1_WORLD_FILE = "res/level1.csv";
    private final static String WIN_MESSAGE = "WELL DONE!";
    private final static String LOSE_MESSAGE = "GAME OVER!";
    private final static int WINNING_SCORE = 800;
    private static final int END_FRENZY_FRAME_COUNT = 1000;
    private int frenzyFrameCount = 0;

    private ArrayList<Wall> wallArr = new ArrayList<Wall>();
    private ArrayList<Dot> dotArr = new ArrayList<Dot>();
    private ArrayList<Cherry> cherryArr = new ArrayList<Cherry>();
    private ArrayList<Enemy> enemyArr = new ArrayList<Enemy>();
    private Player player;
    private Pellet pellet;

    private boolean isPlayerWin;
    private boolean isPlayerLose;
    private boolean isLevel1Start;
    private boolean isFrenzyMode;
    private boolean endFrenzyMode;


    /**
     * Constructs a new Level1 class.
     */
    public Level1() {
        isLevel1Start = false;
        isFrenzyMode = false;
        endFrenzyMode = false;
        isPlayerWin = false;
        isPlayerLose = false;
        readCSV();
    }


    @Override
    public void readCSV() {
        // Read the level 0 world file
        try (Scanner worldFile = new Scanner(new FileReader(LEVEL1_WORLD_FILE))) {
            String[] entry;

            while (worldFile.hasNextLine()) {
                entry = worldFile.nextLine().split(",");
                // Parse the type and position from each row of CSV file to entry array
                String type = entry[0];
                double x = Double.parseDouble(entry[1]);
                double y = Double.parseDouble(entry[2]);

                // Create a new object for each type
                switch (type) {
                    case "Player":
                        player = new Player(x, y);
                        break;

                    case "Pellet":
                        pellet = new Pellet(x, y);
                        break;

                    case "GhostRed":
                        enemyArr.add(new GhostRed(x, y));
                        break;

                    case "GhostBlue":
                        enemyArr.add(new GhostBlue(x, y));
                        break;

                    case "GhostGreen":
                        enemyArr.add(new GhostGreen(x, y));
                        break;

                    case "GhostPink":
                        enemyArr.add(new GhostPink(x, y));
                        break;

                    case "Cherry":
                        cherryArr.add(new Cherry(x, y));
                        break;

                    case "Wall":
                        wallArr.add(new Wall(x, y));
                        break;

                    case "Dot":
                        dotArr.add(new Dot(x, y));
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Input input) {
        // Display instructions
        if (!isLevel1Start) {
            drawInstructions();
        }

        if (input.wasPressed(Keys.SPACE)) {
            isLevel1Start = true;
        }

        // Display winning message when player has won
        if (isPlayerWin) {
            drawWinMessage();

        // Player Lose
        } else if (isPlayerLose) {
            drawLoseMessage();

        // Run Level 1 game logic if game has started
        } else if (isLevel1Start) {
            player.update(input, isFrenzyMode, endFrenzyMode);
            pellet.update();

            // Display world (walls, dots, enemies)
            for (Wall wall : wallArr) {
                wall.update();
            }
            for (Dot dot : dotArr) {
                dot.update();
            }
            for (Cherry cherry : cherryArr) {
                cherry.update();
            }
            for (Enemy enemy : enemyArr) {
                enemy.update(wallArr, isFrenzyMode, endFrenzyMode);
            }

            // Create a new Rectangle object corresponding to player's current position
            Rectangle playerRectangle = new Rectangle(player.getPosition(), player.getPAC_OPEN().getWidth(),
                    player.getPAC_OPEN().getHeight());

            // Run logics for player's collision with walls, ghosts and dots
            wallCollision(playerRectangle);
            dotCollision(playerRectangle);
            enemyCollision(playerRectangle);
            cherryCollision(playerRectangle);
            pelletCollision(playerRectangle);

            if (isFrenzyMode) {
                // Execute frenzy mode for 1000 frames
                frenzyFrameCount++;
                if (frenzyFrameCount == END_FRENZY_FRAME_COUNT) {
                    isFrenzyMode = false;
                    endFrenzyMode = true;
                }
            }
            winDetection(player.getScore());
        }
    }


    /**
     * Method that draws level 1 game instructions.
     */
    public void drawInstructions() {
        INSTRUCTIONS_FONT_1.drawString(GAME_INSTRUCTIONS_1, INSTRUCTIONS_TEXT_POINT.x, INSTRUCTIONS_TEXT_POINT.y);
    }


    @Override
    public void wallCollision(Rectangle playerRectangle) {
        // Create a new Rectangle object for each wall object
        for (Wall wall : wallArr) {
            Rectangle wallRectangle = wall.getWallRect();
            // Check player's position for collision with wall and prevent overlapping
            if (wallRectangle.intersects(playerRectangle)) {
                player.setPosition(player.getPreviousPosition());
                player.setPreviousPosition(player.getPosition());
            }
        }
    }


    @Override
    public void dotCollision(Rectangle playerRectangle) {
        // Create a new Rectangle object for each dot object
        for (Dot dot : dotArr) {
            Rectangle dotRectangle = dot.getRect();
            // Check player's position for collision with dot
            if (dotRectangle.intersects(playerRectangle) && !dot.isEaten()) {
                // Increase player score and deactivate dot
                dot.setEaten(true);
                player.incrementScore(Dot.POINTS);
            }
        }
    }


    @Override
    public void enemyCollision(Rectangle playerRectangle) {
        // Create a new Rectangle object for each ghost object
        for (Enemy enemy : enemyArr) {
            Rectangle enemyRectangle = enemy.getRect();
            // Check player's position for collision with ghosts
            if (enemyRectangle.intersects(playerRectangle) && !enemy.isEaten()) {
                if (isFrenzyMode) {
                    // Increase player score by 30 points during frenzy mode
                    enemy.setEaten(true);
                    player.incrementScore(Enemy.FRENZY_POINTS);
                } else {
                    // Otherwise reduce player lives and reset player and enemy position
                    player.resetPosition();
                    player.playerDisplay(0);
                    player.reduceLives();
                    enemy.resetPosition();
                    if (player.getLives() == 0) {
                        isPlayerLose = true;
                    }
                }

            }
        }
    }


    /**
     * Method that checks for collisions between the player and Cherry objects.
     * Cherry stops rendering on screen and player gains points.
     */
    public void cherryCollision(Rectangle playerRectangle) {
        // Create a new Rectangle object for each cherry object
        for (Cherry cherry : cherryArr) {
            Rectangle cherryRectangle = cherry.getRect();
            // Check player's position for collision with cherry
            if (cherryRectangle.intersects(playerRectangle) && !cherry.isEaten()) {
                // Increase player score and deactivate cherry
                cherry.setEaten(true);
                player.incrementScore(Cherry.POINTS);
            }
        }
    }


    /**
     * Method that checks for collisions between the player and Pellet object.
     * Pellet stops rendering on screen and initiates frenzy mode.
     */
    public void pelletCollision(Rectangle playerRectangle) {
        // Create a new Rectangle object for pellet object
            Rectangle pelletRectangle = pellet.getRect();
            // Check player's position for collision with pellet
            if (pelletRectangle.intersects(playerRectangle) && !pellet.isEaten()) {
                // Remove pellet from screen and begin frenzy mode
                pellet.setEaten(true);
                isFrenzyMode = true;
            }
    }


    @Override
    public void winDetection(int score) {
        if (score >= WINNING_SCORE) {
            isPlayerWin = true;
        }
    }


    @Override
    public void drawWinMessage() {
        DEFAULT_FONT.drawString(WIN_MESSAGE, (WINDOW_WIDTH-DEFAULT_FONT.getWidth(WIN_MESSAGE))/2.0,
                (WINDOW_HEIGHT-DEFAULT_FONT_SIZE)/2.0 + DEFAULT_FONT_SIZE);
    }


    @Override
    public void drawLoseMessage() {
        DEFAULT_FONT.drawString(LOSE_MESSAGE, (WINDOW_WIDTH-DEFAULT_FONT.getWidth(LOSE_MESSAGE))/2.0,
                (WINDOW_HEIGHT-DEFAULT_FONT_SIZE)/2.0 + DEFAULT_FONT_SIZE);
    }
}
