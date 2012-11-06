/**
 * Rotate Push Pop. 
 * 
 * The push() and pop() functions allow for more control over transformations.
 * The push function saves the current coordinate system to the stack 
 * and pop() restores the prior coordinate system. 
 */

import joons.JoonsRenderer;


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
float a;                          // Angle of rotation
float offset = PI/24.0;             // Angle offset between boxes
int num = 12;                     // Number of boxes
color[] colors = new color[num];  // Colors of each box
color safecolor;

boolean pink = true;

void setup() 
{ 
  size(600, 600, P3D);
  jr = new JoonsRenderer(this);
  jr.setRenderSpeed(1);  
  noStroke();  
  for (int i=0; i<num; i++) {
    colors[i] = color(255 * (i+1)/num);
  }  
} 


void draw() 
{ 
  if (jr.displaySketch()) {  
    background(200);
    lights();
    beginRecord("joons.OBJWriter", "");// just call like this. Leave the second parameter as "".
    perspective(fov, aspect, zNear, zFar);// use perspective() before camera()!!
    camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    a += 0.01;   
    translate(0, 0, 25);  // Z is up in sunflow
    for (int i = 0; i < num; i++) {
      pushMatrix();
      fill(colors[i]);
      rotateX(a + offset*i);
      rotateZ(a/2 + offset*i);
      box(50);
      popMatrix();
    }
    endRecord();
  }
}

void keyPressed() {
  if (key == 'r' || key == 'R' && jr.displaySketch()) {
    saveFrame("capture.png");
    jr.setShader("object0", "Blue");  // shape is recognized as one object 
    jr.setSC(dataPath("ambient.sc"));
    jr.render("bucket");
  }
}
