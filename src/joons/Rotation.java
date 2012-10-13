package joons;

/**
 * This class/struct is of minimal design just to hold axis rotation pairs
 * @author Martin Prout
 */
public class Rotation {
    /**
     *
     */
    public Axis axis;
    /**
     *
     */
    public float angle;
    
    /**
     *
     * @param axis
     * @param angle
     */
    public Rotation(Axis axis, float angle){
    this.axis = axis;
    this.angle = angle;
    }
}

