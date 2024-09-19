package pacman.view.keyboard;

import pacman.model.engine.GameEngine;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;

public class RightCommand implements Command{

    private GameEngine model;
    private Controllable pacman;

    public RightCommand(GameEngine model){
        this.model = model;
        this.pacman = model.getPlayer();
    }

    public void execute(){
        this.pacman.setQueuedDirection(Direction.RIGHT);
        System.out.println("pressing right");
    }
}
