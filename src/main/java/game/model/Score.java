package game.model;

import java.time.Duration;
import java.time.Instant;

public class Score {

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Duration interval;

    private Instant start;

    private long currentScore;

    private long totalScore;

    private Level currentLevel;

    public Score(){
        totalScore = 0;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void update(){
        interval = Duration.between(start, Instant.now());
        if (interval.getSeconds() <= currentLevel.getTargetTime()){
            currentScore = currentLevel.getTargetTime() - interval.getSeconds();
        }
        else{
            currentScore = 0;
        }
        currentScore += currentLevel.getEnemyKilled() * 100;
    }

    public void reset() {
        start = Instant.now();
        currentScore = currentLevel.getTargetTime();
    }

    public long getCurrentScore() {
        return currentScore;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void updateTotalScore(){
        totalScore += currentScore;
    }

}
