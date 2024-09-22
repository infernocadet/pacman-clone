package pacman.view.observers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import pacman.model.engine.GameState;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class GameStateView implements GameObserver{
    private final ObserverType type;
    private final Label label;
    private final Pane pane;
    private final Font font;
    private final Map<ObserverType, String> stateLabels = new HashMap<>();


    public GameStateView(Pane pane, Font font){
        this.type = ObserverType.GAME_STATE;
        this.pane = pane;
        this.font = font;

        this.label = new Label();
        this.label.setFont(font);
        this.label.setLayoutX(174);
        this.label.setLayoutY(322);
        this.label.setVisible(false); // hide initially. will only appear when the game state is updated
        pane.getChildren().add(label);
    }

    @Override
    public void updateScore(int score){
     ;
    }

    @Override
    public void updateLives(int lives){
        ;
    }

    public void updateGameState(GameState gameState){
        showLabel(gameState);
    }

    public ObserverType getType(){
        return this.type;
    }

    /**
     * This determines the text inside the label, the colour of the text etc and then game over ends game
     * @param gameState
     */
    private void showLabel(GameState gameState){
        switch (gameState) {
            case READY -> {
                label.setText("READY!");
                label.setTextFill(Color.YELLOW);
                label.setVisible(true);

                // hide after 100 frames
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> label.setTextFill(Color.BLACK)));
                timeline.setCycleCount(1);
                timeline.play();
            }
            case GAME_OVER -> {
                label.setLayoutX(144);
                label.setLayoutY(324);
                label.setText("GAME OVER");
                label.setTextFill(Color.RED);
                label.setVisible(true);

                // after 5 seconds end the game
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    label.setVisible(false);
                    System.exit(0);
                }));
                timeline.setCycleCount(1);
                timeline.play();
            }
            case VICTORY -> {
                label.setLayoutX(128);
                label.setText("YOU WIN");
                label.setTextFill(Color.WHITE);
                label.setVisible(true);;

                // after 5 seconds end the game
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    label.setVisible(false);
                    System.exit(0);
                }));
                timeline.setCycleCount(1);
                timeline.play();
            }
            default -> label.setVisible(false);  // for when playing, hide label

        }
    }
}
