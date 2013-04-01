package com.twopointfived.glowy;

import javax.microedition.khronos.opengles.GL10;

import com.twopointfived.engine.gl.Animation;
import com.twopointfived.engine.gl.Camera2D;
import com.twopointfived.engine.gl.SpriteBatcher;
import com.twopointfived.engine.gl.TextureRegion;
import com.twopointfived.engine.impl.GLGraphics;

public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;    
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;    
    
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;        
    }
    
    public void render() {
        
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();        
    }

    public void renderBackground() {
        batcher.beginBatch(Assets.backgroundGame);
        batcher.drawSprite(cam.position.x, cam.position.y,
                           FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                           Assets.backgroundRegionGame);
        batcher.endBatch();
    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);
        renderGlowy();
        renderPlatforms();
        renderCoins();
        renderStars();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderGlowy() {
        TextureRegion keyFrame;
        switch(world.glowy.state) {
        case Glowy.GLO_STATE_FALL:
            keyFrame = Assets.glowyFall.getKeyFrame(world.glowy.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Glowy.GLO_STATE_JUMP:
            keyFrame = Assets.glowyJump.getKeyFrame(world.glowy.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Glowy.GLO_STATE_HIT:
        default:
            keyFrame = Assets.glowyHit;                       
        }
        
        float side = world.glowy.velocity.x < 0? -1: 1;        
        batcher.drawSprite(world.glowy.position.x, world.glowy.position.y, side * 1, 1.3f, keyFrame);        
    }

    private void renderPlatforms() {
        int len = world.platforms.size();
        for(int i = 0; i < len; i++) {
            Platform platform = world.platforms.get(i);
            TextureRegion keyFrame = Assets.platform;
            if(platform.state == Platform.PLATFORM_STATE_PULVERIZING) {                
                keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
            }            
                                   
            batcher.drawSprite(platform.position.x, platform.position.y, 
                               2, 0.5f, keyFrame);            
        }
    }

    private void renderCoins() {
        int len = world.coins.size();
        for(int i = 0; i < len; i++) {
            Coin coin = world.coins.get(i);
            TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(coin.position.x, coin.position.y, 0.8f, 1, keyFrame);
        }
    }

    private void renderStars() {
        int len = world.evilstars.size();
        for(int i = 0; i < len; i++) {
        	EvilStar star = world.evilstars.get(i);
        	TextureRegion keyFrame = Assets.starFly.getKeyFrame(star.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(star.position.x, star.position.y, 1, 1, keyFrame);
        }
    }
}

