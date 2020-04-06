package game.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameEngineImplementation implements GameEngine {

    /**
     * The height of the game engine
     */
    private double height;

    private ArrayList<String> jsonLevels;

    /**
     * The current level
     */
    private Level currentLevel;

    /**
     * Map of all the levels
     */
    private Map<Integer, Level> levels;

    /**
     * Used to create distinct level id's for each level
     */
    private int levelId;

    /**
     * Level id of the current level
     */
    private int currentLevelId;

    private int lastLevel;

    /**
     * Json path to the level configuration file
     */
    private String jsonPath;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Instant start;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Duration interval;

    private Score score;

    /**
     * The number of lives the hero has
     */
    private int lives;

    /**
     * Creates the game engine using the specified json configuration file and height
     * @param jsonPath The path to the json configuration file containing the level information
     * @param height The height of the game engine's window
     */
    public GameEngineImplementation(String jsonPath, double height) {
        this.jsonPath = jsonPath;
        this.height = height;
        this.levels = new HashMap<>();
        this.levelId = 1;
        this.currentLevelId = 1;
        this.lastLevel = 0;
        this.lives = 3;
        this.score = new Score();
        loadLevels(jsonPath);
        createLevels();
        startLevel();
    }

    private void loadLevels(String jsonPath){
        JSONLevelLoader levelLoader = new JSONLevelLoader(jsonPath);
        this.jsonLevels = levelLoader.getJsonLevels();
    }

    /**
     * Creates the levels associated with the json file
     */
    public void createLevels() {
        for(String level : jsonLevels){
            LevelBuilder levelBuilder = new LevelBuilder(level);
            LevelDirector levelDirector = new LevelDirector(levelBuilder);
            levelDirector.buildLevel();
            this.levels.put(this.levelId, levelDirector.getLevel());
            levelId += 1;
            lastLevel += 1;
        }
    }

    @Override
    public void startLevel() {
        this.currentLevel = levels.get(currentLevelId);
        score.setCurrentLevel(currentLevel);
        score.reset();
    }

    @Override
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    @Override
    public boolean jump() {
        return this.currentLevel.jump();
    }

    @Override
    public boolean moveLeft() {
        return this.currentLevel.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.currentLevel.moveRight();
    }

    @Override
    public boolean stopMoving() {
        return this.currentLevel.stopMoving();
    }

    @Override
    public void tick() {
        this.currentLevel.tick();
        score.update();
        //interval = Duration.between(start, Instant.now());
    }

    @Override
    public void resetCurrentLevel() {
        this.lives--;
        if (this.lives == 0) {
            return;
        }
        LevelBuilder levelBuilder = new LevelBuilder(jsonLevels.get(currentLevelId - 1));
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.currentLevelId, levelDirector.getLevel());
        startLevel();
    }

    @Override
    public boolean isFinished() {
        if(currentLevel.isFinished() && currentLevelId < lastLevel){
            score.updateTotalScore();
            currentLevelId += 1;
            startLevel();
        }
        else if(currentLevel.isFinished() && currentLevelId == lastLevel){
            score.updateTotalScore();
        }
        return currentLevel.isFinished();
    }

//    @Override
//    public Duration getDuration() {
//        return interval;
//    }

    public long getCurrentScore(){
        return score.getCurrentScore();
    }

    public long getTotalScore(){
        return score.getTotalScore();
    }

    @Override
    public boolean gameOver() {
        return this.lives == 0;
    }

    @Override
    public int getLives() {
        return this.lives;
    }

}

