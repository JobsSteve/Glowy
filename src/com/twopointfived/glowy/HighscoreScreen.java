package com.twopointfived.glowy;


import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.twopointfived.engine.Game;
import com.twopointfived.engine.Input.TouchEvent;
import com.twopointfived.engine.gl.Camera2D;
import com.twopointfived.engine.gl.SpriteBatcher;
import com.twopointfived.engine.impl.GLScreen;
import com.twopointfived.engine.math.Rectangle;
import com.twopointfived.engine.math.Vector2;

public class HighscoreScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle backBounds;
    Vector2 touchPoint;
    String[] highScores;  
    float xOffset = 200f;
    float yOffset = 100f;
    
    public static final float WORLD_WIDTH = 450;
    public static final float WORLD_HEIGHT = 300;
    static final int NUM_BALLS = 30;
    Random rand;
    GlowBallMenu[] glowballs;

    public HighscoreScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 480, 320);
        backBounds = new Rectangle(0, 0, 64*2, 64*2);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 100);
        highScores = new String[5];        
        for(int i = 0; i < 5; i++) {
            highScores[i] = (i + 1) + ". " + Settings.highscores[i];
            xOffset = Math.max(highScores[i].length() * Assets.font.glyphWidth, xOffset);
        }  
        
        rand = new Random();
        glowballs = new GlowBallMenu[NUM_BALLS];
        
        for(int i = 0; i < NUM_BALLS; i ++) {
        	GlowBallMenu ball = new GlowBallMenu(WORLD_WIDTH * rand.nextFloat(), 
        			WORLD_HEIGHT * rand.nextFloat(),  rand.nextFloat());
	        glowballs[i] = ball;
        }  
    }    

    @Override
    public void update(float deltaTime) {
    	for(int i = 0; i < NUM_BALLS; i ++) {
	        glowballs[i].update(deltaTime);
        }  
    	
    	List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(event.type == TouchEvent.TOUCH_UP) {
            	Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(240, 160, 480, 320, Assets.backgroundRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);
        
        for(int i = 0; i < NUM_BALLS; i ++) {
	        batcher.drawSprite(glowballs[i].position.x, glowballs[i].position.y,
	        		glowballs[i].size, glowballs[i].size, Assets.glowBall);
        }
        
        batcher.drawSprite(240, 240, 270, 60, Assets.highScoresRegion);
        batcher.drawSprite(480 - 32, 32, -42, 42, Assets.arrow);
        
        yOffset = 70;
        for(int i = 4; i >= 0; i--) {
            Assets.font.drawText(batcher, highScores[i], xOffset, yOffset, 0.5f);
            yOffset += Assets.font.glyphHeight - 10;
        }
        
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    @Override
    public void resume() {        
    }
    
    @Override
    public void pause() {        
    }

    @Override
    public void dispose() {
    }
}
