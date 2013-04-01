package com.twopointfived.glowy;

import java.util.Random;

import com.twopointfived.engine.DynamicGameObject;

public class GlowBall extends DynamicGameObject {
    public static final float GLOW_WIDTH = 1;
    public static final float GLOW_HEIGHT = 0.6f;
    public static final float GLOW_VELOCITY = 4f;
    public static final float GLOW_TIME = 0.2f * 4;
    
    Random rand;
    
    float stateTime = 0;
    
    public GlowBall() {
    	super(0, 0, GLOW_WIDTH, GLOW_HEIGHT);
    	rand = new Random();
        velocity.set(GLOW_VELOCITY * rand.nextFloat(), GLOW_VELOCITY * rand.nextFloat());
    }
    public GlowBall(float x, float y) {
        super(x, y, GLOW_WIDTH, GLOW_HEIGHT);
        rand = new Random();
        velocity.set(GLOW_VELOCITY * rand.nextFloat(), GLOW_VELOCITY * rand.nextFloat());
    }
    
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(GLOW_WIDTH / 2, GLOW_HEIGHT / 2);
        
        if(position.x < GLOW_WIDTH / 2 ) {
            position.x = GLOW_WIDTH / 2;
            velocity.x = GLOW_VELOCITY;
        }
        if(position.x > World.WORLD_WIDTH - GLOW_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - GLOW_WIDTH / 2;
            velocity.x = -GLOW_VELOCITY;
        }
        stateTime += deltaTime;
    }
}
