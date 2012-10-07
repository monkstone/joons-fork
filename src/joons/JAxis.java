package joons;


public class JAxis{
  
  private JVector ox, oy, oz;
  public JAxis(){
    ox=new JVector(1,0,0);
    oy=new JVector(0,1,0);
    oz=new JVector(0,0,1);
  }
  public void rotateAxis(JVector u, float th){
    ox.rotateVector(u,th);
    oy.rotateVector(u,th);
    oz.rotateVector(u,th);
  }
  
  public void rotateX(float th){
    ox.rotateVector(ox,th);
    oy.rotateVector(ox,th);
    oz.rotateVector(ox,th);
  }
  
  public void rotateY(float th){
    ox.rotateVector(oy,th);
    oy.rotateVector(oy,th);
    oz.rotateVector(oy,th);
  }
  
  public void rotateZ(float th){
    ox.rotateVector(oz,th);
    oy.rotateVector(oz,th);
    oz.rotateVector(oz,th);
  }
  
  public JVector getXAxis(){return this.ox;}
  public JVector getYAxis(){return this.oy;}
  public JVector getZAxis(){return this.oz;}
}