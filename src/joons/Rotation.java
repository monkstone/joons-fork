package joons;

/**
 * This class/struct is of minimal design just to hold axis rotation pairs, 
 * making fields final prevents naughty changes (as if we should be worried)
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
    
    /**
     * Simple Constructor
     * @param axis
     * @param angle
     */
    public Rotation(Axis axis, float angle){
    this.axis = axis;
    this.angle = angle;
    }
}

