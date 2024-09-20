package pacman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
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
import pacman.view.observers.ScoreView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Responsible for managing the Pac-Man Game View
 */
public class GameWindow {

    public static final File FONT_FILE = new File("src/main/resources/maze/PressStart2P-Regular.ttf");

    private final Scene scene;
    private final Pane pane;
    private final GameEngine model;
    private final List<EntityView> entityViews;

    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;

        pane = new Pane();
        scene = new Scene(pane, width, height);

        Font font = Font.loadFont(getClass().getResourceAsStream("/maze/PressStart2P-Regular.ttf"), 20);

        entityViews = new ArrayList<>();

        Label scoreLabel = new Label();
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);
        scoreLabel.setFont(font);
        scoreLabel.setTextFill(Color.color(1,1,1));

        pane.getChildren().add(scoreLabel);

        ScoreView scoreView = new ScoreView(scoreLabel);
        model.addObserver(scoreView);

        KeyboardInputHandler keyboardInputHandler = this.model.getKeyboard();
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);

        BackgroundDrawer backgroundDrawer = new StandardBackgroundDrawer();
        backgroundDrawer.draw(model, pane);



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
