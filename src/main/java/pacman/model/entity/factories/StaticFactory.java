package pacman.model.entity.factories;

import pacman.model.entity.Renderable.Layer;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;
import javafx.scene.image.Image;
import pacman.model.maze.RenderableType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class StaticFactory implements RenderableFactory{

    private Map<Character, Image> imageSet = new HashMap<>();

    public StaticFactory(){
        loadImages();
    }

    @Override
    public Renderable createRenderable(char renderableType, int x, int y) {
        Image sprite = imageSet.get(renderableType);
        double width = sprite.getWidth();
        double height = sprite.getHeight();
        Vector2D position = new Vector2D(x * 16, y * 16);
        BoundingBox boundingBox = new BoundingBoxImpl(position, height, width);
        Layer layer = Layer.FOREGROUND; // i dont know if it should be this
        return new StaticEntityImpl(boundingBox, layer, sprite);
    }

    public void loadImages(){
        imageSet.put(RenderableType.HORIZONTAL_WALL,
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/walls/horizontal.png"))));
        imageSet.put(RenderableType.VERTICAL_WALL,
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/walls/vertical.png"))));
        imageSet.put(RenderableType.UP_LEFT_WALL,
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/walls/upLeft.png"))));
        imageSet.put(RenderableType.UP_RIGHT_WALL,
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/walls/upRight.png"))));
        imageSet.put(RenderableType.DOWN_LEFT_WALL,
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/walls/downLeft.png"))));
        imageSet.put(RenderableType.DOWN_RIGHT_WALL,
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/maze/walls/downRight.png"))));
    }
}