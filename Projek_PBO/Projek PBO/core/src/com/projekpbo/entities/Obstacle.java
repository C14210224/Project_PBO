package com.projekpbo.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends Rectangle {
    private long lastObstacleSpawn;
    private double obsFrequency = 1.5;
    private int obstacleWidth = 64;
    private int obstacleHeight = 64;
    private Texture obstacleSprite;
    private int obstacleSpeed = 200;
    public static int totalObstacleNum = 0;
    private static int obstacleNum = 1;


    public Obstacle() {
        super();
        Obstacle.totalObstacleNum++;
        this.obstacleNum = totalObstacleNum;
    }

    public long getObstacleNum() {
        return obstacleNum;
    }
}
