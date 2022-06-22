package com.projekpbo.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public interface PickUp {
    void pickedUp(Player player);
    void doEffect();
    void reverseEffect();
    boolean durationExpired();
}

class PiercingBulletsPickup extends Rectangle implements PickUp {
    long effectDuration = 10000;
    long startTime;

    PiercingBulletsPickup() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void pickedUp(Player player) {

    }
    @Override
    public void doEffect() {

    }

    @Override
    public void reverseEffect() {

    }

    @Override
    public boolean durationExpired() {
        if(TimeUtils.millis() - startTime > effectDuration) {
            return true;
        }
        return false;
    }
}
