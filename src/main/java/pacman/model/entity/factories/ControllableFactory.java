package pacman.model.entity.factories;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.Pacman;
import javafx.scene.image.Image;
import pacman.model.entity.dynamic.player.PacmanVisual;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllableFactory implements RenderableFactory{

    private final Image defaultImage;
    private final Map<PacmanVisual, Image> imageSet = new HashMap<>();

    public ControllableFactory() {
        loadImages();
        this.defaultImage = imageSet.get(PacmanVisual.CLOSED);
    }

    @Override
    public Renderable createRenderable(char RenderableType, int x, int y){
        Image pacmanDefault = getDefaultImage();
        Map<PacmanVisual, Image> pacmanImages = getImageSet();

        double width = pacmanDefault.getWidth();
        double height = pacmanDefault.getHeight();
        
        Vector2D position = new Vector2D(x * 16, y * 16);
        double speed = 1.0;
        Direction direction = Direction.LEFT;
        
        BoundingBox boundingBox = new BoundingBoxImpl(position, height, width);
        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(position)
                .setSpeed(speed)
                .setDirection(direction)
                .build();
        return new Pacman(pacmanDefault, pacmanImages, boundingBox, kinematicState);
    }

    public void loadImages(){
        this.imageSet.put(PacmanVisual.UP, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/pacman/playerUp.png"))));
        this.imageSet.put(PacmanVisual.DOWN, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/pacman/playerDown.png"))));
        this.imageSet.put(PacmanVisual.LEFT, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/pacman/playerLeft.png"))));
        this.imageSet.put(PacmanVisual.RIGHT, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/pacman/playerRight.png"))));
        this.imageSet.put(PacmanVisual.CLOSED, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/pacman/playerClosed.png"))));
    }

    public Image getDefaultImage(){
        return this.defaultImage;
    }

    public Map<PacmanVisual, Image> getImageSet(){
        return this.imageSet;
    }
}
