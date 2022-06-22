package com.projekpbo.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends Rectangle {
    private long lastObstacleSpawn;
    private double obsFrequency = 1.5;
    private int obstacleWidth = 64;
    private int obstacleHeight = 64;
    private Texture obstacleSprite;
    protected int obstacleSpeed = 200;
    public static int totalObstacleNum = 0;
    private static int obstacleNum = 1;
    public Color color;


    public Obstacle(int obstacleSpeed) {
        super();
        Obstacle.totalObstacleNum++;
        this.obstacleNum = totalObstacleNum;
        this.obstacleSpeed = obstacleSpeed;
        this.color = new Color(MathUtils.random(0,255) / 255f, MathUtils.random(0,255) / 255f, MathUtils.random(0,255) / 255f, 1);
    }

    public Obstacle() {
        this(200);
    }

    public long getObstacleNum() {
        return obstacleNum;
    }

    public void moveObstacle(float delta) {
        this.x -= delta * obstacleSpeed;
    }
}
