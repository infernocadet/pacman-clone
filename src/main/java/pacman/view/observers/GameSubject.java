package pacman.view.observers;


import pacman.model.engine.GameState;

public interface GameSubject {
    void addScoreObserver(ScoreObserver observer);
    void addLivesObserver(LivesObserver observer);
    void addGameStateObserver(GameStateObserver observer);
    void removeScoreObserver(ScoreObserver observer);
    void removeLivesObserver(LivesObserver observer);
    void removeGameStateObserver(GameStateObserver observer);
    void notifyUpdateScore();
    void notifyUpdateLives(int numLives);
    void notifyUpdateGameState(GameState gameState);
}
