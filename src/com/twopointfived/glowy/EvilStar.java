// EvilStar are Glowy's enemies killing him on touch
// they are dynamic game objects, moving from left to right

package com.twopointfived.glowy;

import com.twopointfived.engine.DynamicGameObject;

public class EvilStar extends DynamicGameObject {
    public static final float EVILSTAR_WIDTH = 1;
    public static final float EVILSTAR_HEIGHT = 1;// 0.6f;
    public static final float EVILSTAR_VELOCITY = 3f;
    
    float stateTime = 0;
    
    public EvilStar(float x, float y) {
        super(x, y, EVILSTAR_WIDTH, EVILSTAR_HEIGHT);
        velocity.set(EVILSTAR_VELOCITY, 0);
    }
    
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(EVILSTAR_WIDTH / 2, EVILSTAR_HEIGHT / 2);
        
        if(position.x < EVILSTAR_WIDTH / 2 ) {
            position.x = EVILSTAR_WIDTH / 2;
            velocity.x = EVILSTAR_VELOCITY;
        }
        if(position.x > World.WORLD_WIDTH - EVILSTAR_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - EVILSTAR_WIDTH / 2;
            velocity.x = -EVILSTAR_VELOCITY;
        }
        stateTime += deltaTime;
    }
}
