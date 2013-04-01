package com.twopointfived.glowy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.twopointfived.engine.math.OverlapTester;
import com.twopointfived.engine.math.Vector2;

public class World {
    public interface WorldListener {
        public void jump();
        public void highJump();
        public void hit();
        public void coin();
    }

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 10;    
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(30, -200);
    public static final float EMMIT_GLOW_TIME = 10;

    public final Glowy glowy;           
    public final List<Platform> platforms;
    public final List<EvilStar> evilstars;
    public final List<Coin> coins;
    public final List<GlowBall> glowballs;
  
    public final WorldListener listener;
    public final Random rand;
    
    public float heightSoFar;
    public int score;    
    public int state;
    
    float accelX = 0;

    public World(WorldListener listener) {
        this.glowy = new Glowy(5, 1);    
        this.evilstars = new ArrayList<EvilStar>();

        this.platforms = new ArrayList<Platform>();
        this.glowballs = new ArrayList<GlowBall>();
        
        this.coins = new ArrayList<Coin>();        
        this.listener = listener;
        rand = new Random();
        generateLevel();
        
        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
    }

    private void generateLevel() {
    	
    	Platform cloud = new Platform(Platform.PLATFORM_TYPE_MOVING,
    			WORLD_WIDTH * rand.nextFloat(), WORLD_HEIGHT/2);
    	platforms.add(cloud);
        
        for(int i = 0; i < 8; i ++) {
        	if ( i < 3) {
	        	EvilStar squirrel = new EvilStar(WORLD_WIDTH * rand.nextFloat(), 
	        			(WORLD_HEIGHT * rand.nextFloat()) + 4f);
	        	evilstars.add(squirrel);
        	}
        	
	        Coin coin = new Coin(WORLD_WIDTH * rand.nextFloat(),
	        		(WORLD_HEIGHT * rand.nextFloat()) + 2f);
            coins.add(coin);
        }  
    }

    public void update(float deltaTime, boolean moveRight, boolean moveLeft, boolean jump) {
        updateGlowy(deltaTime, moveRight, moveLeft, jump);
        updatePlatforms(deltaTime);
        updateStars(deltaTime);
        updateCoins(deltaTime);
        updateGlowballs(deltaTime);
        if (glowy.state != Glowy.GLO_STATE_HIT)
            checkCollisions();
        checkGameOver();
    }

    private void updateGlowy(float deltaTime, boolean moveRight, boolean moveLeft, boolean jump) {
    	
    	glowy.update(deltaTime, moveRight, moveLeft, jump);
        heightSoFar = Math.max(glowy.position.y, heightSoFar);
    }

    private void updatePlatforms(float deltaTime) {
        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            platform.update(deltaTime);
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING
                    && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
                platforms.remove(platform);
                len = platforms.size();
            }
        }
    }
    
    private void updateGlowballs(float deltaTime) {
        int len = glowballs.size();
        for (int i = 0; i < len; i++) {
            GlowBall ball = glowballs.get(i);
            ball.update(deltaTime);
            if (ball.stateTime > GlowBall.GLOW_TIME) {
            	glowballs.remove(ball);
                len = glowballs.size();
            }
        }
    }

    private void updateStars(float deltaTime) {
        int len = evilstars.size();
        for (int i = 0; i < len; i++) {
        	EvilStar star = evilstars.get(i);
            star.update(deltaTime);
        }
    }

    private void updateCoins(float deltaTime) {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            coin.update(deltaTime);
        }
    }

    private void checkCollisions() {
        checkPlatformCollisions();
        checkStarCollisions();
        checkItemCollisions();
    }

    private void checkPlatformCollisions() {
        if (glowy.velocity.y > 0)
            return;

        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            if (glowy.position.y > platform.position.y) {
                if (OverlapTester
                        .overlapRectangles(glowy.bounds, platform.bounds)) {
                	glowy.hitPlatform(platform.bounds);
                	platform.pulverize();
                    break;
                }
            }
        }
    }

    private void checkStarCollisions() {
        int len = evilstars.size();
        for (int i = 0; i < len; i++) {
        	EvilStar squirrel = evilstars.get(i);
            if (OverlapTester.overlapRectangles(squirrel.bounds, glowy.bounds)) {
            	glowy.hitStar();
                listener.hit();
            }
        }
    }

    private void checkItemCollisions() {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            if (OverlapTester.overlapRectangles(glowy.bounds, coin.bounds)) {
                coins.remove(coin);
                len = coins.size();
                listener.coin();
                score += Coin.COIN_SCORE;
            }

        }

        if (glowy.velocity.y > 0)
            return;
    }

    private void checkGameOver() {
        if (glowy.position.y < -2) {
            state = WORLD_STATE_GAME_OVER;
        }
    }
}
