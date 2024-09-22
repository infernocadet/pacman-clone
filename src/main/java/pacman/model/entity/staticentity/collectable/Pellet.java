package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;

/**
 * Represents the Pellet in Pac-Man game
 */
public class Pellet extends StaticEntityImpl implements Collectable {

    private final int points;
    private boolean isCollectable;
    private boolean isCollected;

    public Pellet(BoundingBox boundingBox, Layer layer, Image image, int points) {
        super(boundingBox, layer, image);
        this.points = points;
        this.isCollectable = true;
        this.isCollected = false;
    }

    @Override
    public void collect() {
        this.isCollectable = false;
        this.isCollected = true;
        setLayer(Layer.INVISIBLE);
    }

    @Override
    public void reset() {
        this.isCollectable = true;
        this.isCollected = false;
        setLayer(Layer.FOREGROUND);
    }

    @Override
    public boolean isCollectable() {
        return this.isCollectable && !isCollected;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

}
