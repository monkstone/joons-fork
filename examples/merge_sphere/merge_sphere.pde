import joons.*;

JoonsRenderer jr;


float eyeX = 0;
float eyeY = 50;
float eyeZ = 150;
float centerX = 0;
float centerY = 0;
float centerZ = 0;
float upX = 0;
float upY = 0;
float upZ = -1;
float fov = PI / 4; 
float aspect = 1.3333;  
float zNear = 5;
float zFar = 10000;

public void setup() {
  size(400, 300, P3D);
  jr = new JoonsRenderer(this);
}

public void draw() {
  if (jr.displaySketch()) {
    background(128);
    lights();
    fill(255);

    beginRecord("joons.OBJWriter", "");//leave the second parameter as "".

    perspective(fov, aspect, zNear, zFar);//use perspective() before camera()!!
    camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);

    rect(-500, -500, 1000, 1000);//the floor plane

    noSmooth(); //In my library, noSmooth() is used to separate one object from another.

    translate(0, -20, 20);
    rotateX((float) 0.3);
    rotateY((float) 0.2);
    rotateZ((float) 0.1);

    translate(10, 0);
    sphere(10);
    translate(10, 0);
    sphere(10);
    translate(10, 0);
    sphere(10);
    translate(10, 0);
    sphere(10);
    translate(-40, 30);
    rotateZ((float)1.5);
    rotateX((float) 0.8);
    box(20);

    endRecord();
  }
}

public void keyPressed() {
  if (key == 'r' || key == 'R' && jr.displaySketch()) {
    saveFrame("capture.png");
    jr.setShader("object1", "Red");
    jr.setSC(dataPath("ambient.sc"));
    jr.render("bucket");
  }
}

