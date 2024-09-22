package pacman.model.engine;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.view.keyboard.KeyboardInputHandler;
import pacman.view.observers.GameSubject;

import java.util.List;


/**
 * The base interface for interacting with the Pac-Man model
 */
public interface GameEngine extends GameSubject {

    /**
     * Gets the list of renderables in the game
     *
     * @return The list of renderables
     */
    List<Renderable> getRenderables();

    /**
     * Starts the game
     */
    void startGame();


    /**
     * Move the player up
     */
    void moveUp();

    /**
     * Move the player down
     */
    void moveDown();

    /**
     * Move the player left
     */
    void moveLeft();

    /**
     * Move the player right
     */
    void moveRight();

    /**
     * Instruct the model to progress forward in time by one increment.
     */
    void tick();


    Controllable getPlayer();

    KeyboardInputHandler getKeyboard();

    void loadCommands();

    void updateScore(int score);

    void updateLives();

    void handleLevelComplete();

}
