/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package joons.util;

/* 
 * Copyright (c) 2012 Martin Prout
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */



/**
 * A lighter weight vector class
 *
 * @author Martin Prout
 */
public final class AVector {

    /**
     * publicly accessible float
     */
    public float x;
    /**
     * publicly accessible float
     */
    public float y;
    /**
     * publicly accessible float
     */
    public float z;

    /**
     * Default Constructor
     */
    public AVector() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    /**
     * Parameterized constructor
     *
     * @param x
     * @param y
     * @param z
     */
    public AVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copy constructor
     * @param v1
     */
    public AVector(AVector v1) {
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;
    }

    /**
     * Normalize this vector (and sensibly return it) 
     * @return
     */
    public AVector normalize() {
        double orig_dist = Math.sqrt(x * x + y * y + z * z);
        this.x /= orig_dist;
        this.y /= orig_dist;
        this.z /= orig_dist;
        return this;
    }

    static float dot(AVector v1, AVector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    static AVector mult(AVector v, float scalar) {
        return new AVector(v.x * scalar, v.y * scalar, v.z * scalar);
    }

    static AVector sub(AVector v1, AVector v2) {
        return new AVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
     *
     * @param v
     * @return
     */
    public AVector cross(AVector v) {
        return new AVector(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x * y);
    }
}


