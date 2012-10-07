package joons;

import joons.util.LUT;

/**
 *
 * @author Joon Hyub Lee
 */
public class JVector {

    private float x;
    private float y;
    private float z;

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
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     *
     * @param z
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     *
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @return
     */
    public float getZ() {
        return z;
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
        return new JVector(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    /**
     *
     * @param u
     * @param th
     */
    public void rotateVector(JVector u, float th) {
        //this method rotates this vector about the u vector by the angle th.//
        ///////////////////////////////////////////////////////////////////////  

        float ux = u.getX();
        float uy = u.getY();
        float uz = u.getZ();
        float vlengthBefore = (float) Math.sqrt(sq(this.getX()) + sq(this.getY()) + sq(this.getZ()));

        //normalizing the u vector
        float ulength = (float) Math.sqrt(sq(ux) + sq(uy) + sq(uz));
        if (ulength < 0.000001) {
            ulength = 1.0f;
        }
        ux = ux / ulength;
        uy = uy / ulength;
        uz = uz / ulength;

        //actual rotation calculation
        float xprime = (sq(ux) + (1 - sq(ux)) * LUT.cos(th)) * x + (ux * uy * (1 - LUT.cos(th)) - uz * LUT.sin(th)) * y + (ux * uz * (1 - LUT.cos(th)) + uy * LUT.sin(th)) * z;
        float yprime = (ux * uy * (1 - LUT.cos(th)) + uz * LUT.sin(th)) * x + (sq(uy) + (1 - sq(uy)) * LUT.cos(th)) * y + (uy * uz * (1 - LUT.cos(th)) - ux * LUT.sin(th)) * z;
        float zprime = (ux * uz * (1 - LUT.cos(th)) - uy * LUT.sin(th)) * x + (uy * uz * (1 - LUT.cos(th)) + ux * LUT.sin(th)) * y + (sq(uz) + (1 - sq(uz)) * LUT.cos(th)) * z;

        //seeing if the above calculation has reduced or enlarged the length due to calculation inaccuracies, and compensating
        float vlengthAfter = (float) Math.sqrt(sq(xprime) + sq(yprime) + sq(zprime));

        if (vlengthAfter > 0.000001) {
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
        return (float) Math.sqrt(sq(x) + sq(y) + sq(z));
    }

    //Defined math functions for convenience
    /**
     *
     * @param x
     * @return
     */
    public static float sq(float x) {
        return x * x;
    }
}