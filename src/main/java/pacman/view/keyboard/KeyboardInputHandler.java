package pacman.view.keyboard;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pacman.model.engine.GameEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for handling keyboard input from player
 */
public class KeyboardInputHandler {
    private final GameEngine model;
    private final Map<KeyCode, Command> moveCommands = new HashMap<>();

    /**
     * Here are using the command pattern
     * KeyBoardInputHandler is the invoker. holds all the commands and asks the commands to execute.
     * the client is the Level. Level will make the commands and set receivers (pacman)
     */
    public KeyboardInputHandler(GameEngine model) {
        this.model = model; // game engine already assigns movement to controllable
//        setCommand(KeyCode.LEFT, new LeftCommand(model));
//        setCommand(KeyCode.RIGHT, new RightCommand(model));
//        setCommand(KeyCode.UP, new UpCommand(model));
//        setCommand(KeyCode.DOWN, new DownCommand(model));
    }

    public void setCommand(KeyCode keyCode, Command command){
        this.moveCommands.put(keyCode, command);
    }

    public void handlePressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        Command command = this.moveCommands.get(keyCode);
        if (command != null){
            command.execute(); //the commands will know to queue it into pacman
        }
    }

}
