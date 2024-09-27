package pacman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import pacman.model.engine.GameEngine;
import pacman.model.entity.Renderable;
import pacman.view.background.BackgroundDrawer;
import pacman.view.background.StandardBackgroundDrawer;
import pacman.view.entity.EntityView;
import pacman.view.entity.EntityViewImpl;
import pacman.view.keyboard.KeyboardInputHandler;
import javafx.scene.control.Label;
import pacman.view.observers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Responsible for managing the Pac-Man Game View
 * This will be the singleton class
 */
public class GameWindow {

    // singleton instance for class access
    private static GameWindow instance;

    public static final File FONT_FILE = new File("src/main/resources/maze/PressStart2P-Regular.ttf");

    private final Scene scene;
    private final Pane pane;
    private final GameEngine model;
    private final List<EntityView> entityViews;
    private final List<ImageView> lifeViews;

    /**
     * Private Constructor for Singleton Gamewindow
     * @param model the gameengine made in app.java
     * @param width the width of the window
     * @param height the height of the window
     */
    private GameWindow(GameEngine model, int width, int height) {
        this.model = model;

        pane = new Pane();
        scene = new Scene(pane, width, height);

        Font font = Font.loadFont(getClass().getResourceAsStream("/maze/PressStart2P-Regular.ttf"), 18);
        Image lifeImage = new Image(getClass().getResourceAsStream("/maze/pacman/playerRight.png"));

        entityViews = new ArrayList<>();
        lifeViews = new ArrayList<>();

        Label scoreLabel = new Label();
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);
        scoreLabel.setFont(font);
        scoreLabel.setTextFill(Color.color(1,1,1));

        pane.getChildren().add(scoreLabel);

        GameSubject gameModel = (GameSubject) model;

        ScoreObserver scoreView = new ScoreView(scoreLabel);
        gameModel.addScoreObserver(scoreView);
        LivesObserver livesView = new LivesView(pane, lifeImage);
        gameModel.addLivesObserver(livesView);
        GameStateObserver gameStateView = new GameStateView(pane, font);
        gameModel.addGameStateObserver(gameStateView);

        KeyboardInputHandler keyboardInputHandler = this.model.getKeyboard();
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);

        BackgroundDrawer backgroundDrawer = new StandardBackgroundDrawer();
        backgroundDrawer.draw(model, pane);
    }

    public static synchronized GameWindow getInstance(GameEngine model, int width, int height){
        if (instance == null){
            instance = new GameWindow(model, width, height);
        }
        return instance;
    }


    public Scene getScene() {
        return scene;
    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(34),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        model.startGame();
    }

    private void draw() {
        model.tick();

        List<Renderable> entities = model.getRenderables();

        for (EntityView entityView : entityViews) {
            entityView.markForDelete();
        }

        for (Renderable entity : entities) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update();
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
//                pane.getChildren().add(entityView.getBoundingBoxNode());

            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
//                pane.getChildren().remove(entityView.getBoundingBoxNode());

            }
        }

        entityViews.removeIf(EntityView::isMarkedForDelete);
    }
}
