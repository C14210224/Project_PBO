package com.projekpbo.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Enemy {
    public enum State {
        POSITIONING, ATTACKING
    }
}

class EvilBird extends Rectangle implements Enemy {

}

class EvilElephant implements Enemy {

}
