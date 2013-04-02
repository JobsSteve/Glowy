// Coins are collected by Glowy
// they are non dynamic game objects that just sit in the world waiting to be collided with

package com.twopointfived.glowy;

import com.twopointfived.engine.GameObject;

public class Coin extends GameObject {
    public static final float COIN_WIDTH = 0.5f;
    public static final float COIN_HEIGHT = 0.8f;
    public static final int COIN_SCORE = 10;

    float stateTime;
    public Coin(float x, float y) {
        super(x, y, COIN_WIDTH, COIN_HEIGHT);
        stateTime = 0;
    }
    
    public void update(float deltaTime) {
        stateTime += deltaTime;
    }
}
