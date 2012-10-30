package joons;

/**
 * This class/struct is of minimal design just to hold axis rotation pairs,
 * making fields final prevents naughty changes (as if we should be worried)
 *
 * @author Martin Prout
 */
public class Rotation {

    /**
     *
     */
    public final Axis axis;
    /**
     *
     */
    public final float angle;
    public final JVector vect;

    public Rotation() {
        this(Axis.W, new JVector(0, 0, 0), 0f);
    }

    /**
     * Simple Constructor
     *
     * @param axis
     * @param angle
     */
    public Rotation(Axis axis, float angle) {
        this(axis, new JVector(0, 0, 0), angle);
    }

    /**
     * Constructor with vector
     *
     * @param axis
     * @param vect
     * @param angle
     */
    public Rotation(Axis axis, JVector vect, float angle) {
        this.axis = axis;
        this.angle = angle;
        this.vect = vect;
    }

    public JVector getVector() {
        return vect;
    }
}
