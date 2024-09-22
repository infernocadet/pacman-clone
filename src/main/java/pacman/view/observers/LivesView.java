package pacman.view.observers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import pacman.model.engine.GameState;

import java.util.ArrayList;
import java.util.List;

public class LivesView implements GameObserver{
    private final ObserverType type;
    private final Pane pane;
    private final List<ImageView> lifeIcons = new ArrayList<>();
    private final Image lifeImage;

    public LivesView(Pane pane, Image lifeImage){
        this.type = ObserverType.LIVES;
        this.pane = pane;
        this.lifeImage = lifeImage;
        addLifeImages(3);
    }

    @Override
    public void updateScore(int score) {
        ;
    }

    @Override
    public ObserverType getType(){
        return this.type;
    }

    @Override
    public void updateLives(int lives){
        pane.getChildren().removeAll(lifeIcons); // clear display for lives
        addLifeImages(lives);
    }

    private void addLifeImages(int lives){
        lifeIcons.clear();
        for (int i = 0; i < lives; i++) {
            ImageView lifeIcon = new ImageView(lifeImage);
            lifeIcon.setFitHeight(20);
            lifeIcon.setFitWidth(20);
            lifeIcon.setX(10 + (i * 26));
            lifeIcon.setY(546);
            lifeIcons.add(lifeIcon);
            pane.getChildren().add(lifeIcon);
        }
    }

    @Override
    public void updateGameState(GameState gameState){
        ;
    }
}
