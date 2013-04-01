package com.twopointfived.glowy;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.twopointfived.engine.Game;
import com.twopointfived.engine.Input.TouchEvent;
import com.twopointfived.engine.gl.Camera2D;
import com.twopointfived.engine.gl.SpriteBatcher;
import com.twopointfived.engine.gl.Texture;
import com.twopointfived.engine.gl.TextureRegion;
import com.twopointfived.engine.impl.GLScreen;
import com.twopointfived.engine.math.Rectangle;
import com.twopointfived.engine.math.Vector2;

public class HelpScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle nextBounds;
    Vector2 touchPoint;
    Texture helpImage;
    TextureRegion helpRegion;

    public HelpScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 480, 320);
        nextBounds = new Rectangle(320 - 64, 0, 64, 64);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1);
    }

    @Override
    public void resume() {
        helpImage = new Texture(glGame, "help.png" );
        helpRegion = new TextureRegion(helpImage, 0, 0, 480*2, 320*2);
    }
    
    @Override
    public void pause() {
        helpImage.dispose();
    }

    @Override
    public void update(float deltaTime) {
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
        
        batcher.beginBatch(helpImage);
        batcher.drawSprite(240, 160, 480, 320, helpRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);          
        batcher.drawSprite(480 - 32, 32, -64, 64, Assets.arrow);
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void dispose() {
    }
}
