package pacman.view.observers;

import pacman.model.engine.GameState;

public interface GameStateObserver extends GameObserver{
    void updateGameState(GameState gameState);

}
