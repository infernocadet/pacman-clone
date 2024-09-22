package pacman.model.engine;

import javafx.scene.input.KeyCode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.level.Level;
import pacman.model.level.LevelImpl;
import pacman.model.maze.Maze;
import pacman.model.maze.MazeCreator;
import pacman.view.Commands.DownCommand;
import pacman.view.Commands.LeftCommand;
import pacman.view.Commands.RightCommand;
import pacman.view.Commands.UpCommand;
import pacman.view.keyboard.*;
import pacman.view.observers.GameObserver;
import pacman.view.observers.GameSubject;
import pacman.view.observers.ObserverType;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GameEngine - responsible for coordinating the Pac-Man model
 */
public class GameEngineImpl implements GameEngine {

    private Level currentLevel;
    private int numLevels;
    private int currentLevelNo;
    private Maze maze;
    private JSONArray levelConfigs;
    private KeyboardInputHandler keyboardInputHandler;
    private int gameScore;
    private List<GameObserver> observers;
    private int livesLeft;

    private GameState currentState;
    private int readyFrameCounter;
    private static final int READY_DURATION = 100; // 100 frames

    public GameEngineImpl(String configPath) {
        this.currentLevelNo = 0;
        this.gameScore = 0;
        this.observers = new ArrayList<>();
        this.currentState = GameState.READY;
        this.readyFrameCounter = 0;

        init(new GameConfigurationReader(configPath));
        this.keyboardInputHandler = new KeyboardInputHandler(this);
    }

    private void init(GameConfigurationReader gameConfigurationReader) {
        // Set up map
        String mapFile = gameConfigurationReader.getMapFile();
        MazeCreator mazeCreator = new MazeCreator(mapFile);
        this.maze = mazeCreator.createMaze();
        int initialLives = gameConfigurationReader.getNumLives();
        this.maze.setNumLives(initialLives);
        this.livesLeft = initialLives;

        // Get level configurations
        this.levelConfigs = gameConfigurationReader.getLevelConfigs();
        this.numLevels = levelConfigs.size();
        if (levelConfigs.isEmpty()) {
            System.exit(0);
        }
    }

    @Override
    public KeyboardInputHandler getKeyboard(){
        return this.keyboardInputHandler;
    }



    @Override
    public List<Renderable> getRenderables() {
        return this.currentLevel.getRenderables();
    }

    @Override
    public void moveUp() {
        currentLevel.moveUp();
    }

    @Override
    public void moveDown() {
        currentLevel.moveDown();
    }

    @Override
    public void moveLeft() {
        currentLevel.moveLeft();
    }

    @Override
    public void moveRight() {
        currentLevel.moveRight();
    }

    @Override
    public void startGame() {

        startLevel();
    }

    private void startLevel() {
        JSONObject levelConfig = (JSONObject) levelConfigs.get(currentLevelNo);
        // reset renderables to starting state
        if (currentLevelNo != 0) { // if it is next level
            currentLevel.getControllable().reset();
            this.currentLevel = new LevelImpl(levelConfig, maze, this);
            this.currentLevel.getControllable().reset();
            maze.reset();
            notifyUpdateGameState(currentState);
        } else { // if it is first level
            this.currentLevel = new LevelImpl(levelConfig, maze, this);
            this.currentLevel.getControllable().reset();
            loadCommands();
            notifyUpdateGameState(currentState);
        }

    }

    @Override
    public void tick() {
        if (currentState == GameState.READY) {
            readyFrameCounter++;
            if (readyFrameCounter >= READY_DURATION) {
                currentState = GameState.PLAYING;
                notifyUpdateGameState(currentState);
            }
        } else if (currentState == GameState.PLAYING) {
            currentLevel.tick();
        }
    }

    @Override
    public Controllable getPlayer(){
        return this.currentLevel.getControllable();
    }

    @Override
    public void loadCommands(){
        keyboardInputHandler.setCommand(KeyCode.LEFT, new LeftCommand(this));
        keyboardInputHandler.setCommand(KeyCode.RIGHT, new RightCommand(this));
        keyboardInputHandler.setCommand(KeyCode.UP, new UpCommand(this));
        keyboardInputHandler.setCommand(KeyCode.DOWN, new DownCommand(this));
    }

    @Override
    public void updateScore(int score){
        this.gameScore += score;
        notifyUpdateScore();
    }

    public int getScore(){
        return this.gameScore;
    }

    @Override
    public void addObserver(GameObserver observer){
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer){
        this.observers.remove(observer);
    }

    @Override
    public void notifyUpdateScore(){
        for (GameObserver observer : observers) {
            if (observer.getType() == ObserverType.SCORE) {
                observer.updateScore(gameScore);
            }
        }
    }

    @Override
    public void updateLives() {
        System.out.println("before hit: " + livesLeft);
        livesLeft--;
        System.out.println("after hit: " + livesLeft);
        notifyUpdateLives();
        if (livesLeft < 1){
            currentLevel.handleGameEnd(); // level is only responsible for making everything disappear. only game engine will initiate game ending
            notifyUpdateGameState(GameState.GAME_OVER);
        } else {
            maze.resetEntities();
            currentState = GameState.READY;
            readyFrameCounter = 0;
            notifyUpdateGameState(currentState);
        }
    }

    @Override
    public void notifyUpdateLives(){
        for (GameObserver observer : observers) {
            if (observer.getType() == ObserverType.LIVES){
                observer.updateLives(livesLeft);
            }
        }
    }

    @Override
    public void notifyUpdateGameState(GameState gameState){
        for (GameObserver observer : observers){
            if (observer.getType() == ObserverType.GAME_STATE) {
                observer.updateGameState(gameState);
            }
        }
    }

    @Override
    public void handleLevelComplete(){
        currentLevelNo++;
        if (currentLevelNo == numLevels){
            currentLevel.handleGameEnd();
            notifyUpdateGameState(GameState.VICTORY);
        } else {
            currentState = GameState.READY;
            readyFrameCounter = 0;
            startLevel();
        }

    }
}

