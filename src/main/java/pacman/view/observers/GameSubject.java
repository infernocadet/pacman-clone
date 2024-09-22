package pacman.view.observers;


import pacman.model.engine.GameState;

public interface GameSubject {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyUpdateScore();
    void notifyUpdateLives();
    void notifyUpdateGameState(GameState gameState);
}
