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
    private int ghostId;

    public GhostFactory(){
        this.ghostImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/ghosts/ghost.png")));
        this.random = new Random();
        this.targetCorners = new ArrayList<>(List.of(
                new Vector2D(RESIZING_FACTOR, RESIZING_FACTOR * 3),    // Top-left corner (0, 3)
                new Vector2D(26 * RESIZING_FACTOR, RESIZING_FACTOR * 3),  // Top-right corner (27, 3)
                new Vector2D(RESIZING_FACTOR, RESIZING_FACTOR * 33),   // Bottom-left corner (0, 32)
                new Vector2D(26 * RESIZING_FACTOR, 33 * RESIZING_FACTOR)  // Bottom-right corner (27, 32)
        ));

        Collections.shuffle(this.targetCorners, random);
        this.cornerIndex = 0;
        this.ghostId = 0;
    }


    @Override
    public Renderable createRenderable(char renderableType, int x, int y) {
        double width = this.ghostImage.getWidth();
        double height = this.ghostImage.getHeight();
        Vector2D position = new Vector2D(x * 16 , y * 16);
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

        System.out.println("Ghost " + ghostId + " is targeting corner: " + targetCorner);
        return new GhostImpl(this.ghostImage, boundingBox, kinematicState,
                GhostMode.SCATTER, targetCorner, kinematicState.getDirection(), ghostId++);
    }

    private Direction calculateInitialDirection(Vector2D startPosition, Vector2D targetCorner){
        if (Math.abs(startPosition.getX() - targetCorner.getX()) > Math.abs(startPosition.getY() - targetCorner.getY())) {
            return startPosition.getX() > targetCorner.getX() ? Direction.LEFT : Direction.RIGHT;
        } else {
            return startPosition.getY() > targetCorner.getY() ? Direction.UP : Direction.DOWN;
        }
    }
}