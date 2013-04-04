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
 *  
 *  Audio interface: returns access to Music and Sound interfaces
 *
 */

package com.twopointfived.engine;

public interface Audio {
    public Music newMusic(String filename);

    public Sound newSound(String filename);
}
