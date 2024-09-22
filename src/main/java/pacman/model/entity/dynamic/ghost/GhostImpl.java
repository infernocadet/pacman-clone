package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;
import pacman.model.maze.Maze;
import pacman.model.maze.RenderableType;

import java.util.*;

/**
 * Concrete implemention of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {

    private Layer layer;
    private final Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Direction currentDirection;
    private Direction startingDirection;
    private double initialSpeed;
    private Set<Direction> possibleDirections;
    private Vector2D playerPosition;
    private Map<GhostMode, Double> speeds;
    private final int id;
    private KinematicState startingKinematicState;

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, Direction currentDirection, int id) {
        this.layer = Layer.FOREGROUND;
        this.image = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingKinematicState = kinematicState.deepCopy();
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.currentDirection = currentDirection;
        this.startingDirection = currentDirection;
        this.initialSpeed = kinematicState.getSpeed();
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds = speeds;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void update() {
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());


    }

    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (this.currentDirection == null){
            this.currentDirection = selectDirection(possibleDirections);
        } else if (Maze.isAtIntersection(this.possibleDirections)) {
            this.targetLocation = getTargetLocation();
            this.currentDirection = selectDirection(possibleDirections);
        }


        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }

        if (possibleDirections.isEmpty()){
            System.out.println("is empty");
        }


    }


    public Vector2D getTargetLocation() {
        return switch (this.ghostMode) {
            case CHASE -> this.playerPosition;
            case SCATTER -> this.targetCorner;
        };
    }

    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();
        Direction reverse = currentDirection.opposite();

        // calculate distances for non-reverse directions
        for (Direction direction : possibleDirections) {
            if (direction != reverse){ // aoviding reverse
                distances.put(direction, Vector2D.calculateEuclideanDistance(
                        this.kinematicState.getPotentialPosition(direction),
                        this.targetLocation));
            }
        }
        // if there are possible distances, select best one
        if (!distances.isEmpty()){
            return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
        }
        // else let them reverse
        System.out.println("nooo reversing");
        return reverse;
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.ghostMode = ghostMode;
        this.kinematicState.setSpeed(speeds.get(ghostMode));
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        if (kinematicState.getDirection() == null){
            System.out.println("its null");
        }
        return boundingBox.collidesWith(kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            ; // do nothing cos pacamn handles this
        }
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        // return ghost to starting position
        this.kinematicState = startingKinematicState.deepCopy();
        this.currentDirection = startingKinematicState.getDirection();

        setGhostMode(GhostMode.SCATTER);
        System.out.println(kinematicState.getDirection());
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        if (this.kinematicState.getDirection() == null){
            return Direction.RIGHT;
        }
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    public void updatePlayerPosition(Vector2D playerPosition) {
        this.playerPosition = playerPosition;
    }

    public void setLayer(Layer layer){
        this.layer = layer;
    }

    private Direction calculateInitialDirection(){
        if (this.kinematicState.getPosition().getX() > this.targetCorner.getX()){
            return Direction.LEFT;
        } else{
            return Direction.RIGHT;
        }
    }

}