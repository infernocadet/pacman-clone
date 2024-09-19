package pacman.view.keyboard;

import pacman.model.engine.GameEngine;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Controllable;

public class DownCommand implements Command{

    private final GameEngine model;
    private final Controllable pacman;

    public DownCommand(GameEngine model){
        this.model = model;
        this.pacman = model.getPlayer();
    }

    public void execute(){
        this.pacman.setQueuedDirection(Direction.DOWN);
        System.out.println("pressing down");
    }
}
