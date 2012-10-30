/**
 * The purpose of this library is to allow the export of processing sketches to
 * sunflow Copyright (C) 2012 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package joons.util;

/**
 * Support ArcBall manipulation of objects in processing
 * @author Martin Prout
 */
public enum Constrain {
        /**
     * 
     */
    XAXIS(0),
    /**
     * 
     */
    YAXIS(1),
    /**
     * 
     */
    ZAXIS(2),
    /**
     * 
     */
    FREE(-1);
    private int index;

    Constrain(int idx) {
        this.index = idx;
    }

    /**
     * 
     * @return
     */
    public int index() {
        return index;
    }

}
