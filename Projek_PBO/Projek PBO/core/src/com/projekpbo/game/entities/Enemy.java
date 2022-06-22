package com.projekpbo.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Enemy {
    public enum State {
        POSITIONING, ATTACKING
    }
}

class EvilBird extends Rectangle implements Enemy , Health {
    int health = 10;

    public void takeDamage(int damage) {
        this.health -= damage;
    }
}

class EvilElephant implements Enemy, Health {
    int health = 10;

    public void takeDamage(int damage) {
        this.health -= damage;
    }
}
