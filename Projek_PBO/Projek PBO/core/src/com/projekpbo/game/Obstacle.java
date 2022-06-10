package com.projekpbo.game;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends Rectangle {
    static long totalObstacleNum = 0;
    private long obstacleNum;

    Obstacle() {
        super();
        Obstacle.totalObstacleNum++;
        this.obstacleNum = totalObstacleNum;
    }

    public long getObstacleNum() {
        return obstacleNum;
    }
}
