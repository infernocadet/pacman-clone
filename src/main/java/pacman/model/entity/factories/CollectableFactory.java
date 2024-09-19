package pacman.model.entity.factories;

import pacman.model.entity.Renderable;

import java.awt.*;
import java.util.Objects;
import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;

public class CollectableFactory implements RenderableFactory{

    private final Image pelletImage;

    public CollectableFactory() {
        this.pelletImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/pellet.png")));
    }

    @Override
    public Renderable createRenderable(char RenderableType, int x, int y){
        Image pelletImage = this.pelletImage;
        double width = pelletImage.getWidth();
        double height = pelletImage.getHeight();
        Vector2D position = new Vector2D(x * 16, y * 16);
        BoundingBox boundingBox = new BoundingBoxImpl(position, height, width);

        int points = 1; // default - change points surely later
        Renderable.Layer layer = Renderable.Layer.FOREGROUND;
        return new Pellet(boundingBox, layer, pelletImage, points);
    }
}
