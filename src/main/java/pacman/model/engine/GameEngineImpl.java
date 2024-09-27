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
import pacman.view.observers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GameEngine - responsible for coordinating the Pac-Man model
 */
public class GameEngineImpl implements GameEngine, GameSubject {

    private Level currentLevel;
    private int numLevels;
    private int currentLevelNo;
    private Maze maze;
    private JSONArray levelConfigs;
    private KeyboardInputHandler keyboardInputHandler;
    private int gameScore;
    private List<ScoreObserver> scoreObservers;
    private List<LivesObserver> livesObservers;
    private List<GameStateObserver> gameStateObservers;
    private int livesLeft;

    private GameState currentState;
    private int readyFrameCounter;
    private static final int READY_DURATION = 100; // 100 frames

    public GameEngineImpl(String configPath) {
        this.currentLevelNo = 0;
        this.gameScore = 0;
        this.scoreObservers = new ArrayList<>();
        this.livesObservers = new ArrayList<>();
        this.gameStateObservers = new ArrayList<>();
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
            this.currentLevel = new LevelImpl(levelConfig, maze, this);
            maze.reset();

        } else { // if it is first level
            this.currentLevel = new LevelImpl(levelConfig, maze, this);
            this.currentLevel.getControllable().reset();
            loadCommands();

        }
        readyFrameCounter = 0;
        currentState = GameState.READY;
        notifyUpdateGameState(currentState);

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

            int numLives = currentLevel.getNumLives();
            if (numLives != livesLeft){
                updateLives(numLives);
            }

            updateScore(currentLevel.getScore()); // increments the engines reference of score, sets level score to 0, and notifies
            if (currentLevel.isLevelFinished()){
                handleLevelComplete();
            } else {
                currentLevel.tick();
            }
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
        currentLevel.setScore(0);
        notifyUpdateScore();
    }

    @Override
    public void addLivesObserver(LivesObserver observer){
        this.livesObservers.add(observer);
    }

    @Override
    public void addScoreObserver(ScoreObserver observer){
        this.scoreObservers.add(observer);
    }

    @Override
    public void addGameStateObserver(GameStateObserver observer){
        this.gameStateObservers.add(observer);
    }

    @Override
    public void removeScoreObserver(ScoreObserver observer){
        this.scoreObservers.remove(observer);
    }

    @Override
    public void removeLivesObserver(LivesObserver observer){
        this.livesObservers.remove(observer);
    }

    @Override
    public void removeGameStateObserver(GameStateObserver observer){
        this.gameStateObservers.remove(observer);
    }

    @Override
    public void notifyUpdateScore(){
        for (ScoreObserver observer : scoreObservers) {
            observer.updateScore(gameScore);
        }
    }

    @Override
    public void updateLives(int numLives) {
        notifyUpdateLives(numLives);
        livesLeft = numLives;
        if (numLives < 1){
            currentLevel.handleGameEnd(); // level is only responsible for making everything disappear. only game engine will initiate game ending
            notifyUpdateGameState(GameState.GAME_OVER);
        } else {
            if (currentState != GameState.READY) {  // Only reset if the game is not already in READY state
                currentState = GameState.READY;
                maze.resetEntities();
                readyFrameCounter = 0;
                notifyUpdateGameState(currentState);
            }
        }
    }

    @Override
    public void notifyUpdateLives(int numLives){
        for (LivesObserver observer : livesObservers) {
            observer.updateLives(numLives);
        }
    }

    @Override
    public void notifyUpdateGameState(GameState gameState){
        for (GameStateObserver observer : gameStateObservers){
            observer.updateGameState(gameState);
        }
    }

    @Override
    public void handleLevelComplete(){
        currentLevelNo++;
        if (currentLevelNo >= numLevels){
            currentLevel.handleGameEnd();
            notifyUpdateGameState(GameState.VICTORY);
        } else {
            startLevel();
        }

    }
}

