package pacman.view.Commands;

import pacman.model.engine.GameEngine;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Controllable;

public class LeftCommand implements Command {

    private final GameEngine model;
    private final Controllable pacman;

    public LeftCommand(GameEngine model){
        this.model = model;
        this.pacman = model.getPlayer();
    }

    public void execute(){
        this.pacman.setQueuedDirection(Direction.LEFT);
        System.out.println("pressing left");
    }
}
