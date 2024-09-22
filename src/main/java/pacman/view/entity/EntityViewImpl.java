package pacman.view.entity;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pacman.model.entity.Renderable;



/**
 * Concrete implementation of EntityView
 */
public class EntityViewImpl implements EntityView {
    private final Renderable entity;
    private boolean delete = false;
    private final ImageView node;
    private final HBox box;
//    private final Rectangle boundingBox;

    public EntityViewImpl(Renderable entity) {
        this.entity = entity;
        box = new HBox();
        node = new ImageView(entity.getImage());
        box.getChildren().add(node);
        box.setViewOrder(getViewOrder(entity.getLayer()));
        box.setFillHeight(true);
        // this was to visualise the entity space / bounding boxes
//        boundingBox = new Rectangle(
//                (int) entity.getBoundingBox().getLeftX(),
//                (int) entity.getBoundingBox().getTopY(),
//                (int) entity.getBoundingBox().getWidth(),
//                (int) entity.getBoundingBox().getHeight()
//        );
//        boundingBox.setStroke(Color.RED);
//        boundingBox.setFill(null);

        update();
    }

    private static double getViewOrder(Renderable.Layer layer) {
        return switch (layer) {
            case BACKGROUND -> 100.0;
            case FOREGROUND -> 50.0;
            case EFFECT -> 25.0;
            case INVISIBLE -> 0.0;
        };
    }

    @Override
    public void update() {
        if (entity.getLayer() != Renderable.Layer.INVISIBLE) {
            node.setVisible(true);
            if (!node.getImage().equals(entity.getImage())) {
                node.setImage(entity.getImage());
            }
            box.setLayoutX(entity.getPosition().getX());
            box.setLayoutY(entity.getPosition().getY());
            node.setFitHeight(entity.getHeight());
            node.setFitWidth(entity.getWidth());
            node.setPreserveRatio(true);

//            boundingBox.setX(entity.getBoundingBox().getLeftX());
//            boundingBox.setY(entity.getBoundingBox().getTopY());
//            boundingBox.setWidth(entity.getBoundingBox().getWidth());
//            boundingBox.setHeight(entity.getBoundingBox().getHeight());
        } else {
            node.setVisible(false);
        }

        delete = false;
    }

    @Override
    public boolean matchesEntity(Renderable entity) {
        return this.entity.equals(entity);
    }

    @Override
    public void markForDelete() {
        delete = true;
    }

    @Override
    public Node getNode() {
        return box;
    }

    @Override
    public boolean isMarkedForDelete() {
        return delete;
    }

//    @Override
//    public Rectangle getBoundingBoxNode() {
//        return boundingBox;
//    }
}

