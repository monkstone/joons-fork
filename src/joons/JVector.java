package joons;

import joons.util.LUT;
import static processing.core.PConstants.*;
/**
 *
 * @author Joon Hyub Lee
 */
public class JVector {

    /**
     *
     */
    public float x;
    /**
     *
     */
    public float y;
    /**
     *
     */
    public float z;

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public JVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        LUT.initialize();
    }

    /**
     * Implement a copy constructor
     *
     * @param v
     */
    public JVector(JVector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     *
     * @param x
     */
    public void addX(float x) {
        this.x += x;
    }

    /**
     *
     * @param y
     */
    public void addY(float y) {
        this.y += y;
    }

    /**
     *
     * @param z
     */
    public void addZ(float z) {
        this.z += z;
    }

    /**
     *
     * @param v
     * @return
     */
    public JVector add(JVector v) {
        return new JVector(x + v.x, y + v.y, z + v.z);
    }

    /**
     *
     * @param u
     * @param th
     */
    public void rotateVector(JVector u, float th) {
        //this method rotates this vector about the u vector by the angle th.//
        ///////////////////////////////////////////////////////////////////////  

        float ux = u.x;
        float uy = u.y;
        float uz = u.z;
        float vlengthBefore = (float) Math.sqrt(x * x + y * y + z * z);

        //normalizing the u vector
        float ulength = (float) Math.sqrt(ux * ux + uy * uy + uz * uz);
        if (ulength < EPSILON) {
            ulength = 1.0f;
        }
        ux = ux / ulength;
        uy = uy / ulength;
        uz = uz / ulength;

        //actual rotation calculation
        float xprime = (ux * ux + (1 - ux * ux) * LUT.cos(th)) * x + (ux * uy * (1 - LUT.cos(th)) - uz * LUT.sin(th)) * y + (ux * uz * (1 - LUT.cos(th)) + uy * LUT.sin(th)) * z;
        float yprime = (ux * uy * (1 - LUT.cos(th)) + uz * LUT.sin(th)) * x + (uy * uy + (1 - uy * uy) * LUT.cos(th)) * y + (uy * uz * (1 - LUT.cos(th)) - ux * LUT.sin(th)) * z;
        float zprime = (ux * uz * (1 - LUT.cos(th)) - uy * LUT.sin(th)) * x + (uy * uz * (1 - LUT.cos(th)) + ux * LUT.sin(th)) * y + (uz * uz + (1 - uz * uz) * LUT.cos(th)) * z;

        //seeing if the above calculation has reduced or enlarged the length due to calculation inaccuracies, and compensating
        float vlengthAfter = (float) Math.sqrt(xprime * xprime + yprime * yprime + zprime * zprime);

        if (vlengthAfter > EPSILON) {
            xprime = xprime * vlengthBefore / vlengthAfter;
            yprime = yprime * vlengthBefore / vlengthAfter;
            zprime = zprime * vlengthBefore / vlengthAfter;
        }

        //setting this vector with the new coordinate
        this.x = xprime;
        this.y = yprime;
        this.z = zprime;
    }

    /**
     *
     * @param jaxis
     * @param th
     */
    public void rotateX(JAxis jaxis, float th) {
        rotateVector(jaxis.getXAxis(), th);
    }

    /**
     *
     * @param jaxis
     * @param th
     */
    public void rotateY(JAxis jaxis, float th) {
        rotateVector(jaxis.getYAxis(), th);
    }

    /**
     *
     * @param jaxis
     * @param th
     */
    public void rotateZ(JAxis jaxis, float th) {
        rotateVector(jaxis.getZAxis(), th);
    }

    /**
     *
     * @return
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }


}