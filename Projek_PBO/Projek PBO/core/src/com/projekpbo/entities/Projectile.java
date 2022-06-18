package com.projekpbo.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends Rectangle {
    private static int projectileHeight = 16;
    private static int projectileWidth = 16;
    private double projectileSpeed;

    private int damage = 1;
    private Vector2 direction;

    public Projectile (float x, float y, Vector2 direction, double projectileSpeed) {
        super();
        this.x = x;
        this.y = y;
        this.height = projectileHeight;
        this.width = projectileWidth;
        this.direction = direction;
        this.projectileSpeed = projectileSpeed;
    }

    void takeDamage(Health target) {
        target.takeDamage(damage);
    }

    public void moveProjectile(float delta) {
        double magnitude =  Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        double x = direction.x / magnitude;
        double y = direction.y / magnitude;
        this.x += x * delta * projectileSpeed;
        this.y += y * delta * projectileSpeed;
    }


}
