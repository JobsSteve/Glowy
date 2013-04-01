package com.twopointfived.glowy;

import com.twopointfived.engine.DynamicGameObject;
import com.twopointfived.engine.math.OverlapTester;
import com.twopointfived.engine.math.Rectangle;

public class Glowy extends DynamicGameObject{
    public static final int GLO_STATE_JUMP = 0;
    public static final int GLO_STATE_FALL = 1;
    public static final int GLO_STATE_HIT = 2;
    public static final float GLO_JUMP_VELOCITY = 44;    
    public static final float GLO_MOVE_VELOCITY = 11;
    public static final float GLO_MOVE_ACCEL = 5;
    public static final float GLO_WIDTH = 0.8f;
    public static final float GLO_HEIGHT = 0.8f;

    int state;
    float stateTime;    
    
    boolean onPlatform = false;
    Rectangle platformBounds;

    public Glowy(float x, float y) {
        super(x, y, GLO_WIDTH, GLO_HEIGHT);
        state = GLO_STATE_FALL;
        stateTime = 0;       
        platformBounds = new Rectangle(0,0,0,0);
    }

    public void update(float deltaTime, boolean moveRight, boolean moveLeft, boolean jump) {  
    	if (moveLeft) {
    		velocity.x -= 0.8f;
    		velocity.x = (velocity.x < -GLO_MOVE_VELOCITY) ? -GLO_MOVE_VELOCITY : velocity.x;
    	}
    	if (moveRight) {
    		velocity.x += 0.8f;
    		velocity.x = (velocity.x > GLO_MOVE_VELOCITY) ? GLO_MOVE_VELOCITY : velocity.x;
    	}
    	
    	if (velocity.x < 0) {
    		velocity.x += World.gravity.x * deltaTime;
    		velocity.x = (velocity.x > 0) ? 0 : velocity.x;
    	} else if (velocity.x > 0) {
    		velocity.x -= World.gravity.x * deltaTime;
    		velocity.x = (velocity.x < 0) ? 0 : velocity.x;
    	}
    	
    	if (!onPlatform)
    		velocity.y += World.gravity.y * deltaTime;

        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        
        if (position.y <= 2 && state != GLO_STATE_HIT && !onPlatform) {
        	position.y = 2f;
        	velocity.y = 0;
        }
        
        if (onPlatform) {
        	if (OverlapTester.overlapRectangles(this.bounds, platformBounds)) {
        		position.y = platformBounds.lowerLeft.y + platformBounds.height;
        		velocity.y = 0;
        	} else {
        		onPlatform = false;
        	}
        }

        if(velocity.y == 0 && jump)
        	jump();
        
        if(position.x < 0)
            position.x = World.WORLD_WIDTH;
        if(position.x > World.WORLD_WIDTH)
            position.x = 0;
        
        stateTime += deltaTime;
    }

    public void hitStar() {
        velocity.set(0,0);
        state = GLO_STATE_HIT;        
        stateTime = 0;
    }
    
    public void hitPlatform(Rectangle bounds) {
    	onPlatform = true;
    	platformBounds = bounds;
    	velocity.y = 0;
    	position.y = platformBounds.lowerLeft.y + 2*platformBounds.height;
    	
    }
    
    public void jump() {
    	velocity.y = GLO_JUMP_VELOCITY;
    	state = GLO_STATE_JUMP;
    	stateTime = 0;
    	Assets.playSound(Assets.highJumpSound);
    }
}
