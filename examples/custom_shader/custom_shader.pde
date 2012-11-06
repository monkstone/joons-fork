import joons.JoonsRenderer;


/**
 * This sketch is adapted from the DXF export processing library example, and
 * demonstrates the use of the joons library to export processing spheres data to
 * sunflow rendering
 * @author Martin Prout
 */

JoonsRenderer jr;
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
String[] shades = {
  "Glossy", "Blue", "Red", "Mirror", "myPhong"
};

int[] DATA = {
  -1, 0, 1
};

/**
 * Processing Setup
 */

void setup() {
  size(400, 400, P3D);
  jr = new JoonsRenderer(this);
  jr.setRenderSpeed(1); 
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
    beginRecord("joons.OBJWriter", "");// just call like this. Leave the second parameter as "".  
    perspective(fov, aspect, zNear, zFar);// use perspective() before camera()!!
    camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    rotateY(PI/30);
    translate(0, 0, 40);
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
        translate(22 * x, 22 * y, 22 * z);
        fill(random(255), random(255), random(255)); 
        sphere(8);
        popMatrix();
      }
    }
  }
}

void keyPressed() {
  if (key == 'r' || key == 'R' && jr.displaySketch()) {
    saveFrame("capture.png");
    for (int i = 0; i < 27; i++)  // assign a shader to each sphere
    {
      jr.setShader("sphere" + i, shades[i % 5]);
    }
    jr.setSC(dataPath("cornell_box.sc"));
    jr.render("bucket");
  }
}

