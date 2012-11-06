 package joons;


/**
 * The original axis as three vectors
 * @author Joon Hyub Lee, simplified by Martin Prout
 */
public class JAxis{
  
  private JVector ox, oy, oz;
    /**
     *
     */
    public JAxis(){
    ox=new JVector(1,0,0);
    oy=new JVector(0,1,0);
    oz=new JVector(0,0,1);
  }
    /**
     *
     * @param u
     * @param th
     */
    public void rotateAxis(JVector u, double th){
    ox.rotateVector(u,th);
    oy.rotateVector(u,th);
    oz.rotateVector(u,th);
  } 
    

    /**
     * 
     * @return
     */
    public JVector getXAxis(){return this.ox;}
    /**
     *
     * @return
     */
    public JVector getYAxis(){return this.oy;}
    /**
     *
     * @return
     */
    public JVector getZAxis(){return this.oz;}
}
