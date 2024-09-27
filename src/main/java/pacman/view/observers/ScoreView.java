package pacman.view.observers;

import javafx.scene.control.Label;
import pacman.model.engine.GameState;

public class ScoreView implements ScoreObserver{
    private final Label scoreLabel;

    public ScoreView(Label scoreLabel){
        this.scoreLabel = scoreLabel;
    }

    @Override
    public void updateScore(int score) {
        this.scoreLabel.setText(String.valueOf(score));
    }

}
