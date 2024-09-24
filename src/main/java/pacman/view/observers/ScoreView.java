package pacman.view.observers;

import javafx.scene.control.Label;
import pacman.model.engine.GameState;

public class ScoreView implements ScoreObserver{
    private final Label scoreLabel;
    private ObserverType type;

    public ScoreView(Label scoreLabel){
        this.scoreLabel = scoreLabel;
        this.type = ObserverType.SCORE;
    }

    @Override
    public void updateScore(int score) {
        this.scoreLabel.setText(String.valueOf(score));
    }

    @Override
    public ObserverType getType(){
        return this.type;
    }

}
