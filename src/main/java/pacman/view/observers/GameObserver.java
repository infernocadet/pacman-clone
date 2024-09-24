package pacman.view.observers;


import pacman.model.engine.GameState;

/**
 * Responsible for updating the UI including score and lives
 */
public interface GameObserver {

    ObserverType getType();
}
