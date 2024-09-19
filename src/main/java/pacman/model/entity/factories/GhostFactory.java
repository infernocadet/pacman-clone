package pacman.model.entity.factories;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GhostFactory implements RenderableFactory{

    private final Image ghostImage;
    private final Random random;
    private final List<Vector2D> targetCorners;
    private final int RESIZING_FACTOR = 16;

    public GhostFactory(){
        this.ghostImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/ghosts/ghost.png")));
        this.random = new Random();
        this.targetCorners = List.of(
                new Vector2D(RESIZING_FACTOR, RESIZING_FACTOR * 3),    // Top-left corner (0, 3)
                new Vector2D(26 * RESIZING_FACTOR, RESIZING_FACTOR * 3),  // Top-right corner (27, 3)
                new Vector2D(RESIZING_FACTOR, RESIZING_FACTOR * 33),   // Bottom-left corner (0, 32)
                new Vector2D(26 * RESIZING_FACTOR, 33 * RESIZING_FACTOR)  // Bottom-right corner (27, 32)
        );
    }


    @Override
    public Renderable createRenderable(char renderableType, int x, int y) {
        double width = this.ghostImage.getWidth();
        double height = this.ghostImage.getHeight();
        Direction direction = null;
        Vector2D position = new Vector2D(x * 16, y * 16);
        BoundingBox boundingBox = new BoundingBoxImpl(position, 22, 22);
        double speed = 1.0; // initial
        switch(random.nextInt(4)) {
            case 0:
                direction = Direction.RIGHT;
                break;
            case 1:
                direction = Direction.LEFT;
                break;
            case 2:
                direction = Direction.UP;
                break;
            case 3:
                direction = Direction.DOWN;
                break;
        }

        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(position)
                .setSpeed(speed)
                .setDirection(direction)
                .build();
        Vector2D targetCorner = targetCorners.get(random.nextInt(targetCorners.size()));
        return new GhostImpl(this.ghostImage, boundingBox, kinematicState,
                GhostMode.SCATTER, targetCorner, kinematicState.getDirection());
    }
}
