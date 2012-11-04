package joons;

/**
 * This class/struct is of minimal design just to hold axis rotation pairs,
 * making fields final prevents naughty changes (as if we should be worried)
 *
 * @author Martin Prout
 */
public class Rotation {


    /**
     * Angle of rotation
     */
    public final float w,
    /**
     * Originally x axis
     */
    x,
    /**
     * Originally y axis
     */
    y,
    /**
     * Originally z axis
     */
    z;


    /**
     *
     */
    public Rotation() {
        this(0, 0, 0, 0);
    }

    /**
     *
     * @param w
     * @param x
     * @param y
     * @param z
     */
    public Rotation(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Convenience function create new JVector (better than storing?)
     * @return
     */
    public JVector getVector() {
        return new JVector(x, y, z);
    }
}
