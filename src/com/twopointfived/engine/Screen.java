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

public abstract class Screen {
    protected final Game game;

    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
