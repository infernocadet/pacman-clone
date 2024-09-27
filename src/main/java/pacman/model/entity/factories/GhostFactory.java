package pacman.model.entity.factories;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;

import java.util.*;

public class GhostFactory implements RenderableFactory{

    private final Image ghostImage;
    private final Random random;
    private  List<Vector2D> targetCorners;
    private final int RESIZING_FACTOR = 16;
    private  int cornerIndex;

    public GhostFactory(){
        this.ghostImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/ghosts/ghost.png")));
        this.random = new Random();
        this.targetCorners = new ArrayList<>(List.of(
                new Vector2D(RESIZING_FACTOR, RESIZING_FACTOR * 4),    // top-left  (1, 4)
                new Vector2D(26 * RESIZING_FACTOR, RESIZING_FACTOR * 4),  // top-right  (26, 4)
                new Vector2D(RESIZING_FACTOR, RESIZING_FACTOR * 32),   // bottom-left  (1, 32)
                new Vector2D(26 * RESIZING_FACTOR, 32 * RESIZING_FACTOR)  // bottom-right corer (26, 32)
        ));

        Collections.shuffle(this.targetCorners, random);
        this.cornerIndex = 0;
    }


    @Override
    public Renderable createRenderable(char renderableType, int x, int y) {
        double width = this.ghostImage.getWidth();
        double height = this.ghostImage.getHeight();
        Vector2D position = new Vector2D(x * 16 + 3, y * 16 - 2);
        BoundingBox boundingBox = new BoundingBoxImpl(position, 24, 24);
        double speed = 1.0; // initial
        Vector2D targetCorner = targetCorners.get(this.cornerIndex);
        cornerIndex++;
        Direction direction = calculateInitialDirection(position, targetCorner);
        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(position)
                .setSpeed(speed)
                .setDirection(direction)
                .build();
        return new GhostImpl(this.ghostImage, boundingBox, kinematicState,
                GhostMode.SCATTER, targetCorner, kinematicState.getDirection());
    }

    private Direction calculateInitialDirection(Vector2D startPosition, Vector2D targetCorner) {
        if (startPosition.getX() > targetCorner.getX()) {
            // The corner is on the left side
            return Direction.LEFT;
        } else {
            // The corner is on the right side
            return Direction.RIGHT;
        }
    }

}