package com.twopointfived.engine.impl;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.twopointfived.engine.Input;

public class AndroidInput implements Input {    
    // AccelerometerHandler accelHandler;
    // KeyboardHandler keyHandler;
	MultiTouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        // accelHandler = new AccelerometerHandler(context);
        // keyHandler = new KeyboardHandler(view);               
        
    	touchHandler = new MultiTouchHandler(view, scaleX, scaleY);        
    }

    public boolean isKeyPressed(int keyCode) {
        return false; // keyHandler.isKeyPressed(keyCode);
    }

    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    public float getAccelX() {
        return 0f; // accelHandler.getAccelX();
    }

    public float getAccelY() {
        return 0f; // accelHandler.getAccelY();
    }

    public float getAccelZ() {
        return 0f; // accelHandler.getAccelZ();
    }

    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    public List<KeyEvent> getKeyEvents() {
        return null; // keyHandler.getKeyEvents();
    }
}
