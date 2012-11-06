import joons.JoonsRenderer;
import joons.util.ArcBall;
import joons.util.Constrain;

/**
 * This sketch is adapted from the DXF export processing library example, and
 * demonstrates the use of the joons library to export processing spheres data to
 * sunflow rendering
 * @author Martin Prout
 */

JoonsRenderer jr;
float eyeX = 0;
float eyeY = -800;
float eyeZ = 400;
float centerX = 0;
float centerY = 0;
float centerZ = 0;
float upX = 0;
float upY = 0;
float upZ = 1;
float fov = PI / 2; 
float aspect = 1.0;  
float zNear = 5;
float zFar = 10000;
String[] shades = {
  "Glossy", "Blue", "Red", "Mirror", "myPhong"
};
int sphereCount = 0;
int boxCount = 0;
MyCube cube;
ArcBall arcball;
int[] DATA = {
  -1, 0, 1
};

/**
 * Processing Setup
 */

void setup() {
  size(800, 800, P3D);
  jr = new JoonsRenderer(this);  
  arcball = new ArcBall(this, 0, 0, width * 0.8);
  jr.setRenderSpeed(0.2); 
  noStroke();
  sphereDetail(18);
  jr.addAfterShader("shader {");             // add a shader from processing
  jr.addAfterShader("   name myPhong");
  jr.addAfterShader("   type phong");
  jr.addAfterShader("   diff {\"sRGB nonlinear\" 0.8 0.4 0.4 }");
  jr.addAfterShader("   spec {\"sRGB nonlinear\" 1.0 0.7 0.7 } 50");
  jr.addAfterShader("   samples 4");
  jr.addAfterShader("}");
}

/**
 * Processing Draw
 */

void draw() {
  if (jr.displaySketch()) {
    lights(); 
    background(200); 
    beginRecord("joons.OBJWriter", "");// just call like this. Leave the second parameter as "".  
    perspective(fov, aspect, zNear, zFar);// use perspective() before camera()!!
    camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    arcball.update();
    translate(0, 0, 200);
    render();
    endRecord();
  }
}


/**
 * Render processing sketch
 */
void render() {
  for (int y : DATA) {
    for (int x : DATA) {
      for (int z : DATA) {
        pushMatrix();
        cube = new MyCube(200, 200 * x, 200 * y, -200 * z);
        fill(200); 
        cube.render();
        popMatrix();
      }
    }
  }
}

void keyPressed() {
  noLoop();
  if (key == 'r' || key == 'R' && jr.displaySketch()) {
    saveFrame("capture.png");
    jr.setSC(dataPath("ambient.sc"));
    jr.render("bucket");
  }
}

