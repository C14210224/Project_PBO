package com.projekpbo.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public interface PickUp {
    void doEffect(Player player);
    boolean durationExpired();
}

class BulletSpeedPickup extends Rectangle implements PickUp {
    long effectDuration = 10000;
    long startTime;
    BulletSpeedPickup() {
        startTime = TimeUtils.millis();
    }
    @Override
    public void doEffect(Player player) {
        player.projectileFreq = 3;
    }

    @Override
    public boolean durationExpired() {
        if(TimeUtils.millis() - startTime > effectDuration) {
            return true;
        }
        return false;
    }

}

class BulletRatePickup extends Rectangle implements PickUp {
    long effectDuration = 10000;
    long startTime;
    BulletRatePickup() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void doEffect(Player player) {

    }

    @Override
    public boolean durationExpired() {
        if(TimeUtils.millis() - startTime > effectDuration) {
            return true;
        }
        return false;
    }
}

class PiercingBulletsPickup extends Rectangle implements PickUp {
    long effectDuration = 10000;
    long startTime;

    PiercingBulletsPickup() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void doEffect(Player player) {

    }

    @Override
    public boolean durationExpired() {
        if(TimeUtils.millis() - startTime > effectDuration) {
            return true;
        }
        return false;
    }
}
