package pacman.model.maze;

import pacman.model.entity.Renderable;
import pacman.model.entity.factories.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Responsible for creating renderables and storing it in the Maze
 */
public class MazeCreator {

    private final String fileName;
    public static final int RESIZING_FACTOR = 16;
    private final Map<Character, RenderableFactory> factoryRegistry = new HashMap<>();

    public MazeCreator(String fileName){
        this.fileName = fileName;

        RenderableFactory controllableFactory = new ControllableFactory(); // creates Pacman
        RenderableFactory collectableFactory = new CollectableFactory(); // creates Pellets
        RenderableFactory ghostFactory = new GhostFactory(); // creates Ghosts
        RenderableFactory staticFactory = new StaticFactory(); // creates Walls
        factoryRegistry.put(RenderableType.PACMAN, controllableFactory);
        factoryRegistry.put(RenderableType.PELLET, collectableFactory);
        factoryRegistry.put(RenderableType.GHOST, ghostFactory);

        factoryRegistry.put(RenderableType.HORIZONTAL_WALL, staticFactory);
        factoryRegistry.put(RenderableType.VERTICAL_WALL, staticFactory);
        factoryRegistry.put(RenderableType.UP_LEFT_WALL, staticFactory);
        factoryRegistry.put(RenderableType.UP_RIGHT_WALL, staticFactory);
        factoryRegistry.put(RenderableType.DOWN_LEFT_WALL, staticFactory);
        factoryRegistry.put(RenderableType.DOWN_RIGHT_WALL, staticFactory);
    }

    public Maze createMaze(){
        File f = new File(this.fileName);
        Maze maze = new Maze();

        try {
            Scanner scanner = new Scanner(f);

            int y = 0;

            while (scanner.hasNextLine()){

                String line = scanner.nextLine();
                char[] row = line.toCharArray();

                for (int x = 0; x < row.length; x++){
                    /**
                     * TO DO: use factory method to make all the renderables. we'll have a renderable creator which
                     * other concrete factories (static entity maker, dynamic entity maker, ghost maker, collectable maker etc
                     */
                    char renderableType = row[x];
                    RenderableFactory factory = factoryRegistry.get(renderableType);
                    if (factory != null){
                        Renderable renderable = factory.createRenderable(renderableType, x, y);
                        maze.addRenderable(renderable, renderableType, x, y);
                    }
                }

                y += 1;
            }

            scanner.close();
        }
        catch (FileNotFoundException e){
            System.out.println("No maze file was found.");
            exit(0);
        } catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            exit(0);
        }

        return maze;
    }
}
