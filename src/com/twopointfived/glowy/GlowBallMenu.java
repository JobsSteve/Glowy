package com.twopointfived.glowy;

import com.twopointfived.engine.DynamicGameObject;

public class GlowBallMenu extends DynamicGameObject {
    public static final float GLOW_WIDTH = 1;
    public static final float GLOW_HEIGHT = 0.6f;
    public static final float GLOW_VELOCITY = 3f;
    
    float stateTime = 0;
    
    public int size;
    
    public GlowBallMenu() {
    	super(0, 0, GLOW_WIDTH, GLOW_HEIGHT);
        velocity.set(GLOW_VELOCITY, 0);
    }
    public GlowBallMenu(float x, float y, float rand) {
        super(x, y, GLOW_WIDTH, GLOW_HEIGHT);
        velocity.set(GLOW_VELOCITY, 0);
        
        size = (int) (25 * rand) + 5;
    }
    
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(GLOW_WIDTH / 2, GLOW_HEIGHT / 2);
        
        if(position.x < GLOW_WIDTH / 2 ) {
            position.x = GLOW_WIDTH / 2;
            velocity.x = GLOW_VELOCITY;
        }
        if(position.x > MainMenuScreen.WORLD_WIDTH - GLOW_WIDTH / 2) {
            position.x = MainMenuScreen.WORLD_WIDTH - GLOW_WIDTH / 2;
            velocity.x = -GLOW_VELOCITY;
        }
        stateTime += deltaTime;
    }
}
