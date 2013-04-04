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

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
	
    public interface PoolObjectFactory<T> {
        public T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);
    }

    public T newObject() {
        T object = null;

        if (freeObjects.isEmpty())
            object = factory.createObject();
        else
            object = freeObjects.remove(freeObjects.size() - 1);
        return object;
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
