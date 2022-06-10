package com.projekpbo.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Enemy {
    int health = 3;
    int damage = 1;

    Vector2 destination = new Vector2(0,0);

    public enum State {
        POSITIONING, ATTACKING
    }

    State state = State.POSITIONING;
}

class EvilBird extends Rectangle implements Enemy {

}

class EvilElephant implements Enemy {

}
