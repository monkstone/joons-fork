import joons.JoonsRenderer;
import joons.util.ArcBall;
import joons.util.Constrain;

JoonsRenderer jr;
ArcBall arcball;
float eyeX = 0;
float eyeY = 115;
float eyeZ = 40;
float centerX = 0;
float centerY = 0;
float centerZ = 40;
float upX = 0;
float upY = 0;
float upZ = -1;
float fov = PI / 4; 
float aspect = 1.0;  
float zNear = 5;
float zFar = 10000;

void setup() {
  size(600, 600, P3D);
  jr = new JoonsRenderer(this);
  arcball = new ArcBall(this, 0, 0, width*0.8);
  arcball.constrain(Constrain.ZAXIS);  
  jr.setRenderSpeed(1);  
  //renderSpeed is set 1 by default, if this method is not called.
  //speed is inversely proportional to the render quality

  /*SC file format:-
   Image Settings
   Lights
   Shaders
   Modifiers
   Objects
   Instances
   */
}


void draw() {
  background(200);
  if (jr.displaySketch()){
  beginRecord("joons.OBJWriter", "");// just call like this. Leave the second parameter as "".

  perspective(fov, aspect, zNear, zFar);// use perspective() before camera()!!
  camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
  arcball.update();     // need this here for JoonsRenderer to record rotations
  render();
  endRecord();
  }
}

void render() {  // encapsulate the processing sketch as a function  
  translate(0, 0, 8);
  fill(255, 0, 0);
  box(15);
  translate(0, 0, 15);
  box(15);
  translate(15, 0, 0);
  box(15);
  translate(-15, 0, 15);
  box(15);
  translate(0, 0, 15);  
  box(15);
  translate(15, 0, 0);
  box(15);
}


void keyPressed() {
  if (key == 'r' || key == 'R' && jr.displaySketch()) {
    saveFrame("capture.png");
    jr.setShader("object0", "Red");
    jr.setSC(dataPath("cornell_box.sc"));
    
    jr.render("ipr");
    // render using render("ipr") to render quick and rough,
    // and render("bucket") to render slow and smooth
    // if successfully rendered, returns true
  }
}

