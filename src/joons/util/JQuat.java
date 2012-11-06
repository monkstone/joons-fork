/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package joons.util;

/**
 * The purpose of this library is to allow the export of processing sketches to
 * PovRAY Copyright (C) 2012 Martin Prout This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */


/**
 * 
 * @author Martin Prout
 */
public final class JQuat {

    private double w, x, y, z;

    /**
     * 
     */
    public JQuat() {
        reset();
    }

    /**
     * 
     * @param w
     * @param x
     * @param y
     * @param z
     */
    public JQuat(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 
     */
    public final void reset() {
        w = 1.0f;
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    /**
     * 
     * @param w scalar 
     * @param v custom Vector class
     */
    public void set(double w, AVector v) {
        this.w = w;
        x = v.x;
        y = v.y;
        z = v.z;
    }

    /**
     * 
     * @param q
     */
    public void set(JQuat q) {
        w = q.w;
        x = q.x;
        y = q.y;
        z = q.z;
    }

    /**
     * 
     * @param q1
     * @param q2
     * @return
     */
    public static JQuat mult(JQuat q1, JQuat q2) {
        double w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z;
        double x = q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y;
        double y = q1.w * q2.y + q1.y * q2.w + q1.z * q2.x - q1.x * q2.z;
        double z = q1.w * q2.z + q1.z * q2.w + q1.x * q2.y - q1.y * q2.x;
        return new JQuat(w, x, y, z);
    }
    
    /**
     * Transform this iernion into an angle (radians) and an axis vector, about 
     * which to rotate (avoids NaN by setting sa to 1.0F when sa < epsilon)
     * @return a new double[] where a0 = angle and a1 .. a3 are axis vector
     */

    public double[] getValue() {
        double sa = Math.sqrt(1.0 - w * w);
        if (sa < processing.core.PConstants.EPSILON) {
            sa = 1.0f;
        }
        return new double[]{Math.acos(w) * 2.0f, x / sa, y / sa, z / sa};
    }
}

