package joons;


import static processing.core.PConstants.*;

/**
 *
 * @author Joon Hyub Lee
 */
public class JVector {

    /**
     *
     */
    private double x;
    /**
     *
     */
    private double y;
    /**
     *
     */
    private double z;

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public JVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
     * @return
     */
    public final double getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public final double getY() {
        return y;
    }

    /**
     *
     * @return
     */
    public final double getZ() {
        return z;
    }

    /**
     *
     * @param x
     */
    public void addX(double x) {
        this.x += x;
    }

    /**
     *
     * @param y
     */
    public void addY(double y) {
        this.y += y;
    }

    /**
     *
     * @param z
     */
    public void addZ(double z) {
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
    public void rotateVector(JVector u, double th) {
        //this method rotates this vector about the u vector by the angle th.//
        ///////////////////////////////////////////////////////////////////////  

        double ux = u.x;
        double uy = u.y;
        double uz = u.z;
        double vlengthBefore = Math.sqrt(x * x + y * y + z * z);

        //normalizing the u vector
        double ulength = Math.sqrt(ux * ux + uy * uy + uz * uz);
        if (ulength < EPSILON) {
            ulength = 1.0f;
        }
        ux = ux / ulength;
        uy = uy / ulength;
        uz = uz / ulength;

        //actual rotation calculation
        double xprime = (ux * ux + (1 - ux * ux) * Math.cos(th)) * x + (ux * uy * (1 - Math.cos(th)) - uz * Math.sin(th)) * y + (ux * uz * (1 - Math.cos(th)) + uy * Math.sin(th)) * z;
        double yprime = (ux * uy * (1 - Math.cos(th)) + uz * Math.sin(th)) * x + (uy * uy + (1 - uy * uy) * Math.cos(th)) * y + (uy * uz * (1 - Math.cos(th)) - ux * Math.sin(th)) * z;
        double zprime = (ux * uz * (1 - Math.cos(th)) - uy * Math.sin(th)) * x + (uy * uz * (1 - Math.cos(th)) + ux * Math.sin(th)) * y + (uz * uz + (1 - uz * uz) * Math.cos(th)) * z;

        //seeing if the above calculation has reduced or enlarged the length due to calculation inaccuracies, and compensating
        double vlengthAfter = Math.sqrt(xprime * xprime + yprime * yprime + zprime * zprime);

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
     * @return
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }
}