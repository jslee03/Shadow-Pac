import bagel.*;
import bagel.util.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Level0 class represents the first level implementation for the game.
 * It implements the Level interface.
 */
public class Level0 implements Level {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int DEFAULT_FONT_SIZE = 64;
    private final Font DEFAULT_FONT = new Font("res/FSO8BITR.TTF", DEFAULT_FONT_SIZE);

    private final static String LEVEL0_WORLD_FILE = "res/level0.csv";
    private final static int WINNING_SCORE = 1210;
    private final static String WIN_MESSAGE = "LEVEL COMPLETE!";
    private final static String LOSE_MESSAGE = "GAME OVER!";


    private ArrayList<Wall> wallArr = new ArrayList<Wall>();
    private ArrayList<Dot> dotArr = new ArrayList<Dot>();
    private ArrayList<GhostRed> ghostRedArr = new ArrayList<GhostRed>();
    private Player player;
    private int countDown = 300;

    private boolean isPlayerWin;
    private boolean isPlayerLose;
    private boolean isLevel0Complete;
    private boolean isLevel0End;

    /**
     * Checks if game has ended.
     */
    public boolean isLevel0End() {
        return isLevel0End;
    }

    /**
     * Sets the status of game end.
     */
    public void setLevel0End(boolean level0End) {
        this.isLevel0End = level0End;
    }

    /**
     * Constructs a new Level0 class.
     */
    public Level0() {
        isLevel0End = false;
        isLevel0Complete = false;
        isPlayerWin = false;
        isPlayerLose = false;
        readCSV();
    }


    @Override
    public void readCSV() {
        // Read the level 0 world file
        try (Scanner worldFile = new Scanner(new FileReader(LEVEL0_WORLD_FILE))) {
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

                    case "Ghost":
                        ghostRedArr.add(new GhostRed(x, y));
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
        // Check if space key is pressed to start the game
        if ((!ShadowPac.isGameStart()) && (!isLevel0Complete)) {
            if (input.wasPressed(Keys.SPACE)) {
                ShadowPac.setIsGameStart(true);
            }
        }

        // Display winning message whe player has won
        if (isPlayerWin) {
            if (countDown >= 0) {
                drawWinMessage();
                countDown--;
            } else {
                // Set status of level 0 as ended
                setLevel0End(true);
            }

        // Player lose
        } else if (isPlayerLose) {
            drawLoseMessage();

        // Run Level 0 game logic if game has started
        } else if (ShadowPac.isGameStart() && !isLevel0Complete) {

            player.update(input, false, false);

            // Display world (ghosts, walls, dots)
            for (Wall wall : wallArr) {
                wall.update();
            }
            for (Dot dot : dotArr) {
                dot.update();
            }
            for (GhostRed ghost : ghostRedArr) {
                ghost.update(wallArr, false, false);
            }

            // Create a new Rectangle object corresponding to player's current position
            Rectangle playerRectangle = new Rectangle(player.getPosition(), player.getPAC_OPEN().getWidth(),
                    player.getPAC_OPEN().getHeight());

            // Run logics for player's collision with walls, ghosts and dots
            wallCollision(playerRectangle);
            enemyCollision(playerRectangle);
            dotCollision(playerRectangle);
        }
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
    public void enemyCollision(Rectangle playerRectangle) {
        // Create a new Rectangle object for each ghost object
        for (GhostRed ghost : ghostRedArr) {
            Rectangle ghostRectangle = ghost.getRect();
            // Check player's position for collision with ghost
            if (ghostRectangle.intersects(playerRectangle)) {
                player.resetPosition();
                player.playerDisplay(0);
                player.reduceLives();
                if (player.getLives() == 0) {
                    isLevel0Complete = true;
                    isPlayerLose = true;
                }
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
                winDetection(player.getScore());
            }
        }
    }


    @Override
    public void winDetection(int score) {
        if (score == WINNING_SCORE) {
            isLevel0Complete = true;
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
