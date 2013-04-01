package com.twopointfived.glowy;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.twopointfived.engine.Game;
import com.twopointfived.engine.Input.TouchEvent;
import com.twopointfived.engine.gl.Camera2D;
import com.twopointfived.engine.gl.SpriteBatcher;
import com.twopointfived.engine.impl.GLScreen;
import com.twopointfived.engine.math.OverlapTester;
import com.twopointfived.engine.math.Rectangle;
import com.twopointfived.engine.math.Vector2;
import com.twopointfived.glowy.World.WorldListener;

public class GameScreen extends GLScreen {
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;    
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;    
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    
    Rectangle rightUI;
    Rectangle leftUI;
    Rectangle jumpUI;
    Rectangle attachUI;
    
    boolean moveLeft =false;
    boolean moveRight = false;
    boolean jump = false;
    
    int moveLeftID = -1;
    int moveRightID = -1;
    int jumpID = -1;
    
    int lastScore;
    String scoreString;    
    
    public float stateTime = 0;
    int inverse = 0;
    int current = 10;
    public static final float MAXTIME = 0.2f * 60;
    String timeString;

    public GameScreen(Game game) {
        super(game);
		
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 480, 320);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        worldListener = new WorldListener() {
            public void jump() {            
                Assets.playSound(Assets.jumpSound);
            }

            public void highJump() {
                Assets.playSound(Assets.highJumpSound);
            }

            public void hit() {
                Assets.playSound(Assets.hitSound);
            }

            public void coin() {
                Assets.playSound(Assets.coinSound);
            }                      
        };
        world = new World(worldListener);
        
        renderer = new WorldRenderer(glGraphics, batcher, world);
        
        pauseBounds = new Rectangle(480 - 64, 320 - 64, 64, 64);
        
        resumeBounds = new Rectangle(240-87, 160, 173, 33);
        quitBounds = new Rectangle(240-87, 160 - 33, 173, 33);
    	
        leftUI = new Rectangle(4, 57, 60, 60);
        rightUI = new Rectangle(49, 18, 60, 60);
        jumpUI = new Rectangle(415, 57, 60, 60);
        attachUI = new Rectangle(370, 18, 60, 60);
        
        lastScore = 0;
        current = 10;
        inverse = 0;
        scoreString = "SCORE: 0";
        timeString = "TIME: 10";
    }
	
    // ************************************************************************ update
    @Override
    public void update(float deltaTime) {
        if(deltaTime > 0.1f)
            deltaTime = 0.1f;
        
        switch(state) {
        case GAME_READY:
            updateReady();
            break;
        case GAME_RUNNING:
            updateRunning(deltaTime);
            break;
        case GAME_PAUSED:
            updatePaused();
            break;
        case GAME_LEVEL_END:
            updateLevelEnd();
            break;
        case GAME_OVER:
            updateGameOver();
            break;
        }
    }

    private void updateReady() {
    	List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        
        if (len > 0) {
            state = GAME_RUNNING;
            stateTime = 0;
        }
    }

    private void updateRunning(float deltaTime) {
    	stateTime += deltaTime;
    	if (stateTime >= MAXTIME)
    		world.state = World.WORLD_STATE_GAME_OVER;
    	
    	if (stateTime > (MAXTIME/10) * inverse) {
    		current--;
    		inverse++;
    		timeString = "TIME: " + current;
    		
    	}
    	
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(event.type == TouchEvent.TOUCH_UP) {
            	
            	if (event.pointer == moveRightID)
            		moveRight = false;

            	if (event.pointer == moveLeftID)
            		moveLeft = false;

            	if (event.pointer == jumpID)
            		jump = false;

            	
	            if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    state = GAME_PAUSED;
                    return;
                }
	            
            	/*
            	if(OverlapTester.pointInRectangle(leftUI, touchPoint)) {
	                moveLeft = false;
	                return;
	            }  
	            
	            if(OverlapTester.pointInRectangle(rightUI, touchPoint)) {
	                moveRight = false;
	                return;
	            }
	            if(OverlapTester.pointInRectangle(jumpUI, touchPoint)) {
	                jump = false;
	                return;
	            } */
            	
            } else if(event.type == TouchEvent.TOUCH_DOWN) { 
	            
	            if(OverlapTester.pointInRectangle(leftUI, touchPoint)) {
	                Assets.playSound(Assets.clickSound);
	                moveLeft = true;
	                moveLeftID = event.pointer;
	                return;
	            }  
	            
	            if(OverlapTester.pointInRectangle(rightUI, touchPoint)) {
	                Assets.playSound(Assets.clickSound);
	                moveRight = true;
	                moveRightID = event.pointer;
	                return;
	            }          
	            
	            if(OverlapTester.pointInRectangle(jumpUI, touchPoint)) {
	                jump = true;
	                jumpID = event.pointer;
	                return;
	            }  
           }
        }
        
        world.update(deltaTime, moveRight, moveLeft, jump);
        
        if(world.score != lastScore) {
            lastScore = world.score;
            scoreString = "" + lastScore;
        }
        if(world.state == World.WORLD_STATE_NEXT_LEVEL) {
            state = GAME_LEVEL_END;        
        }
        if(world.state == World.WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            if(lastScore >= Settings.highscores[4]) 
                scoreString = "NEW HIGHSCORE: " + lastScore;
            else
                scoreString = "SCORE: " + lastScore;
            Settings.addScore(lastScore);
            Settings.save(game.getFileIO());
        }
    }

    private void updatePaused() {
    	moveLeft = moveRight = jump = false;
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
    	
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
            	continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                return;
            }
            
            if(OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                return;
            
            }
        }
    }

    private void updateLevelEnd() {
    	List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            world = new World(worldListener);
            renderer = new WorldRenderer(glGraphics, batcher, world);
            world.score = lastScore;
            state = GAME_READY;
        }
    }

    private void updateGameOver() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            game.setScreen(new MainMenuScreen(game));
        }
    }
    
    // ************************************************************************ present
    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        renderer.render();
        
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        switch(state) {
        case GAME_READY:
            presentReady();
            break;
        case GAME_RUNNING:
            presentRunning();
            break;
        case GAME_PAUSED:
            presentPaused();
            break;
        case GAME_LEVEL_END:
        case GAME_OVER:
            presentGameOver();
            break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void presentReady() {
        batcher.drawSprite(240, 160, 177*1.3f, 35*1.3f, Assets.ready);
    }

    private void presentRunning() {
    	// left UI elements
    	batcher.drawSprite(34, 87, 60, 60, Assets.UIcircle);
    	batcher.drawSprite(79, 48, 60, 60, Assets.UIcircle);
    	
    	// right UI elements
    	batcher.drawSprite(445, 87, 60, 60, Assets.UIcircle);
    	// batcher.drawSprite(400, 48, 60, 60, Assets.UIcircle);
    	
        batcher.drawSprite(480 - 32, 320 - 32, 42, 42, Assets.pause);
        Assets.font.drawText(batcher, scoreString, 16, 320-50, 0.5f);
        Assets.font.drawText(batcher, timeString, 16, 320-20, 0.5f);
    }

    private void presentPaused() {        
        batcher.drawSprite(240, 160, 173, 67, Assets.pauseMenu);
        Assets.font.drawText(batcher, scoreString, 16, 320-20, 0.5f);
    }

    private void presentGameOver() {
        batcher.drawSprite(240, 160, 126*1.3f, 36*1.3f, Assets.gameOver);
        float scoreWidth = Assets.font.glyphWidth * 0.5f * scoreString.length();
        Assets.font.drawText(batcher, scoreString, 245 - scoreWidth/2, 320-20, 0.5f);
    }

    // ************************************************************************ Pause & Resume
    @Override
    public void pause() {
        if(state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
}
