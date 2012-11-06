/**
 * The purpose of this library is to allow the export of processing sketches 
 * to PovRAY Copyright (C) 2012 Martin Prout This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * Obtain a copy of the license at http://www.gnu.org/licenses/lgpl-2.1.html
 */
package joons.util;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Supports the ArcBall and MouseWheel zoom
 * manipulation of objects in processing based on code by Ariel Malka
 * ArcBall quaternion concept by Ken Shoemake
 *
 * @author Martin Prout
 */
public class ArcBall {

    private float center_x;
    private float center_y;
    private float radius;
    private AVector v_down;
    private AVector v_drag;
    private JQuat q_now;
    private JQuat q_down;
    private JQuat q_drag;
    private AVector[] axisSet;
    private Constrain axis;
    private boolean isActive = false;
    private final PApplet parent;

  

    /**
     *
     * @param parent PApplet
     * @param center_x float x coordinate of arcball center
     * @param center_y float y coordinate of arcball center
     * @param radius   float radius of arcball
     */
    public ArcBall(final PApplet parent, float center_x, float center_y, float radius) {
        this.parent = parent;
        this.parent.registerMethod("dispose", this);
        this.center_x = center_x;
        this.center_y = center_y;
        this.radius = radius;
        this.v_down = new AVector();
        this.v_drag = new AVector();
        this.q_now = new JQuat();
        this.q_down = new JQuat();
        this.q_drag = new JQuat();
        this.axisSet = new AVector[]{new AVector(1.0F, 0.0F, 0.0F), new AVector(0.0F, 1.0F, 0.0F), new AVector(0.0F, 0.0F, 1.0F)};
        axis = Constrain.FREE; // no constraints...
        setActive(true);
    }

    /**
     * Default centered arcball and half width or half height whichever the smaller
     *
     * @param parent
     */
    public ArcBall(final PApplet parent) {
        this(parent, parent.width * 0.5F, parent.height * 0.5F, Math.min(parent.width, parent.height) * 0.5F);
    }
    
    

    /**
     * mouse event to register
     *
     * @param e
     */
    public void mouseEvent(processing.event.MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        switch (e.getAction()) {
            case (MouseEvent.PRESSED):
                v_down = mouse2sphere(x, y);
                q_down.set(q_now);
                q_drag.reset();
                break;
            case (MouseEvent.DRAGGED):
                v_drag = mouse2sphere(x, y);
                q_drag.set(AVector.dot(v_down, v_drag), v_down.cross(v_drag));
                break;
            default:
        }
    }

    /**
     * key event to register
     *
     * @param e
     */
    public void keyEvent(processing.event.KeyEvent e) {
        if (e.getAction() == KeyEvent.PRESSED) {
            switch (e.getKey()) {
                case 'x':
                    constrain(Constrain.XAXIS);
                    break;
                case 'y':
                    constrain(Constrain.YAXIS);
                    break;
                case 'z':
                    constrain(Constrain.ZAXIS);
                    break;
            }
        }
        if (e.getAction() == KeyEvent.RELEASED) {
            constrain(Constrain.FREE);
        }
    }

 

    /**
     * May or may not be required for use in Web Applet it works so why worry
     * as used by Jonathan Feinberg peasycam, and that works OK
     * @param active
     */
    public final void setActive(boolean active) {
        if (active != isActive) {
            isActive = active;
            if (active) {
                this.parent.registerMethod("mouseEvent", this);
                this.parent.registerMethod("keyEvent", this);
            } else {
                this.parent.unregisterMethod("mouseEvent", this);
                this.parent.unregisterMethod("keyEvent", this);
            }
        }
    }
    

    /**
     * No need to call this in sketch
     */
    public void update() {
        q_now = JQuat.mult(q_drag, q_down);
        applyQuaternion2Matrix(q_now);
    }

    /**
     * Returns the PVector of the constrain PVector if constrained
     * @param x
     * @param y
     * @return
     */
    public AVector mouse2sphere(float x, float y) {
        AVector v = new AVector();
        v.x = (x - center_x) / radius;
        v.y = (y - center_y) / radius;
        float mag = v.x * v.x + v.y * v.y;
        if (mag > 1.0F) {
            v.normalize();
        } else {
            v.z = (float) Math.sqrt(1.0 - mag);
        }
        if (axis != Constrain.FREE){v = constrainVector(v, axisSet[axis.index()]);}
        return v; 
    }

    /**
     * Returns the PVector if the axis is constrained
     * @param vector
     * @param axis
     * @return
     */
    public AVector constrainVector(AVector vector, AVector axis) {
        AVector res = AVector.sub(vector, AVector.mult(axis, AVector.dot(axis, vector)));
        res.normalize();
        return res;
    }

    /**
     * Constrain rotation to this axis
     * @param axis
     */
    public void constrain(Constrain axis) {
        this.axis = axis;
    }

    /**
     * Rotate the parent sketch according to the quaternion
     * @param q
     */
    public void applyQuaternion2Matrix(JQuat q) {
        // instead of transforming q into a matrix and applying it...
        double[] aa = q.getValue();
        parent.rotate((float)aa[0], (float)aa[1], (float)aa[2], (float)aa[3]);
    }

    /**
     * A recommended inclusion for a processing library
     */
    public void dispose() {
        setActive(false);
    }
}
