package com.twopointfived.glowy;


import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.twopointfived.engine.Game;
import com.twopointfived.engine.Input.TouchEvent;
import com.twopointfived.engine.gl.Camera2D;
import com.twopointfived.engine.gl.SpriteBatcher;
import com.twopointfived.engine.impl.GLScreen;
import com.twopointfived.engine.math.OverlapTester;
import com.twopointfived.engine.math.Rectangle;
import com.twopointfived.engine.math.Vector2;

public class MainMenuScreen extends GLScreen {
	public static final float WORLD_WIDTH = 450;
    public static final float WORLD_HEIGHT = 300;
    static final int NUM_BALLS = 30;
    
    Random rand;
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Rectangle helpBounds;
    Vector2 touchPoint;
    
    GlowBallMenu[] glowballs;
    

    public MainMenuScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 480, 320); 
        batcher = new SpriteBatcher(glGraphics, 100);
        soundBounds = new Rectangle(0, 0, 64, 64);
        
        // batcher.drawSprite(360, 160, 240, 135, Assets.mainMenu); 67.5
        
        playBounds = new Rectangle(240, 183, 240, 45);
        highscoresBounds = new Rectangle(240, 138, 240, 45);
        helpBounds = new Rectangle(240, 93, 240, 45);
        
        touchPoint = new Vector2();    
        
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
            if(event.type == TouchEvent.TOUCH_UP) {
        		touchPoint.set(event.x, event.y);
                guiCam.touchToWorld(touchPoint);
                
                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new GameScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HighscoreScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(helpBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HelpScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled) 
                        Assets.music.play();
                    else
                        Assets.music.pause();
                }
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
        
        batcher.drawSprite(145, 210, 276, 146, Assets.logo); // 552, 292
        batcher.drawSprite(360, 160, 240, 135, Assets.mainMenu);
        batcher.drawSprite(32, 32, 42, 42, Settings.soundEnabled?Assets.soundOn:Assets.soundOff); 
        Assets.font.drawText(batcher, "MUSIC BY EPSILON NOT", 64, 32, 0.3f);
                
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {        
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {        
    }       

    @Override
    public void dispose() {        
    }
}
