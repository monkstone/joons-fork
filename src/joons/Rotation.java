package joons;

/**
 * This class/struct is of minimal design just to hold axis rotation pairs,
 * making fields final prevents naughty changes (as if we should be worried)
 *
 * @author Martin Prout
 */
public class Rotation {


    /**
     * Angle of rotation need to be public can be final
     */
    public final double w;
    /**
     * Originally vector in x-axis
     */
   private final double x,
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
        this(0, 0, 1.0f, 0);
    }

    /**
     *
     * @param w
     * @param x
     * @param y
     * @param z
     */
    public Rotation(double w, double x, double y, double z) {
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
