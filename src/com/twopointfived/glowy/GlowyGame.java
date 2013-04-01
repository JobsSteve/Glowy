package com.twopointfived.glowy;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.view.KeyEvent;

import com.twopointfived.engine.Screen;
import com.twopointfived.engine.impl.GLGame;

public class GlowyGame extends GLGame {
    boolean firstTimeCreate = true;
    
    public Screen getStartScreen() {
        return new MainMenuScreen(this);
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
        if(Settings.soundEnabled)
            Assets.music.pause();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	if (this.screen instanceof MainMenuScreen)
        		return super.onKeyDown(keyCode, event);
        	
        	setScreen(new MainMenuScreen(this));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}