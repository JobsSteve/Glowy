/*
 * 
 *  Hi there, have a look at my source files here:
 *  https://github.com/stefanRitter/
 *  
 *  
 *  based on Udacity game dev course: 
 *  https://www.udacity.com/course/cs255
 *  
 *  & Mario Zechner's https://code.google.com/p/libgdx/
 * 
 *  shared under the Creative Commons CC BY-NC-SA license:
 *  http://creativecommons.org/licenses/by-nc-sa/3.0/
 *
 */

package com.twopointfived.engine;

import com.twopointfived.engine.math.Rectangle;
import com.twopointfived.engine.math.Vector2;

public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;
    
    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x-width/2, y-height/2, width, height);
    }
}
