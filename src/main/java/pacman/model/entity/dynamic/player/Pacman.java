package pacman.model.entity.dynamic.player;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.Level;

import java.util.*;

public class Pacman implements Controllable {

    public static final int PACMAN_IMAGE_SWAP_TICK_COUNT = 8;
    private final Layer layer = Layer.FOREGROUND;
    private final Map<PacmanVisual, Image> images;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private KinematicState kinematicState;
    private Image currentImage;
    private Set<Direction> possibleDirections;
    private boolean isClosedImage;
    private Direction queuedDirection = null; // this is gonna store the queued direction player wants to go

    public Pacman(
            Image currentImage,
            Map<PacmanVisual, Image> images,
            BoundingBox boundingBox,
            KinematicState kinematicState
    ){
        this.currentImage = currentImage;
        this.images = images;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.possibleDirections = new HashSet<>();
        this.isClosedImage = false;
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Image getImage() {
        if (isClosedImage){
            return images.get(PacmanVisual.CLOSED);
        } else {
            return currentImage;
        }
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    public void update() {
        kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());

        if (queuedDirection != null) {
            System.out.println("Trying to go " + queuedDirection.name());
            if (possibleDirections.contains(queuedDirection)){
                System.out.println("POSSIBLE - " + queuedDirection);
                switch (queuedDirection) {
                    case UP:
                        this.up();
                        break;
                    case DOWN:
                        this.down();
                        break;
                    case RIGHT:
                        this.right();
                        break;
                    case LEFT:
                        this.left();
                        break;
                }
                queuedDirection = null;
            } else {
                System.out.println("NOT POSSIBLE");
            }
        }

//        if ((queuedDirection != null) && possibleDirections.contains(queuedDirection)){
//            System.out.println("going: " + queuedDirection + " - POSSIBLE");
//            switch (queuedDirection) {
//                case UP:
//                    this.up();
//                    break;
//                case DOWN:
//                    this.down();
//                    break;
//                case RIGHT:
//                    this.right();
//                    break;
//                case LEFT:
//                    this.left();
//                    break;
//            }
//            queuedDirection = null;
//        }
    }

    public void setQueuedDirection(Direction direction){
        this.queuedDirection = direction;
    }

    @Override
    public void setSpeed(double speed){
        this.kinematicState.setSpeed(speed);
    }

    @Override
    public void up() {
        this.kinematicState.up();
        this.currentImage = images.get(PacmanVisual.UP);
        snapToGrid();
    }

    @Override
    public void down() {
        this.kinematicState.down();
        this.currentImage = images.get(PacmanVisual.DOWN);
        snapToGrid();
    }

    @Override
    public void left() {
        this.kinematicState.left();
        this.currentImage = images.get(PacmanVisual.LEFT);
        snapToGrid();
    }

    @Override
    public void right() {
        this.kinematicState.right();
        this.currentImage = images.get(PacmanVisual.RIGHT);
        snapToGrid();
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public void collideWith(Level level, Renderable renderable){
        if (level.isCollectable(renderable)){
            Collectable collectable = (Collectable) renderable;
            level.collect(collectable);
            collectable.collect();
        }
//        if (level.)
    }

    @Override
    public boolean collidesWith(Renderable renderable){
       return boundingBox.collidesWith(kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void reset(){
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .setSpeed(kinematicState.getSpeed())
                .build();

        // go left by default
        left();
    }

    @Override
    public BoundingBox getBoundingBox(){
        return this.boundingBox;
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
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter(){
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    @Override
    public void switchImage(){
        this.isClosedImage = !this.isClosedImage;
    }

    private void snapToGrid(){
        double gridSize = 16.0;
        Vector2D currentPos = this.kinematicState.getPosition();

        double snappedX = Math.round(currentPos.getX() / gridSize) * gridSize;
        double snappedY = Math.round(currentPos.getY() / gridSize) * gridSize;
        this.kinematicState.setPosition(new Vector2D(snappedX, snappedY));
    }
}
