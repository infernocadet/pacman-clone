package pacman.model.entity.factories;

import pacman.model.entity.Renderable;

/**
 * The base interface for creating Renderable objects - Creator factory
 */
public interface RenderableFactory {

    public Renderable createRenderable(char renderableType, int x, int y);
}
