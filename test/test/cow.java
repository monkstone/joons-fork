package test;

import joons.*;
import processing.core.*;

/**
 *
 * @author sid
 */
public class cow extends PApplet {

    JoonsRenderer jr;
    boolean rendered = false;
    float eyeX = 0;
    float eyeY = 120;
    float eyeZ = 40;
    float centerX = 0;
    float centerY = 0;
    float centerZ = 40;
    float upX = 0;
    float upY = 0;
    float upZ = -1;
    float fov = PI / 4;
    float aspect = (float) 1.3333f;
    float zNear = 5;
    float zFar = 10000;
    PShape cow;

    /**
     *
     */
    @Override
    public void setup() {
        size(1024, 768, P3D);
        jr = new JoonsRenderer(this, width, height);
        jr.setRenderSpeed(1);
        cow = loadShape(dataPath("cow.obj"));
        cow.rotateX(PI / 2);
        cow.scale(5, 5, 5);
        cow.translate(0, 5, 0);
    }

    /**
     *
     */
    @Override
    public void draw() {
        background(128);
        beginRecord("joons.OBJWriter", "");
        perspective(fov, aspect, zNear, zFar);
        camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        shape(cow);
        endRecord();
        if (rendered) {
            jr.display();
        }
    }

    /**
     *
     */
    @Override
    public void keyPressed() {
        if (key == 'e' || key == 'E') {
            rendered = false;
            return;
        }
        if (key == 'r' || key == 'R') {
            jr.setShader("object0", "DefaultGrey");
            jr.setSC(dataPath("ambient.sc"));
            rendered = jr.render("ipr");
        } else if (key == 'm' || key == 'M') {
            jr.setShader("object0", "Mirror");
            jr.setSC(dataPath("cornell_box.sc"));
            rendered = jr.render("bucket");
        } else if (key == 'g' || key == 'G') {
            jr.setShader("object0", "Glass");
            jr.setSC(dataPath("cornell_box.sc"));
            rendered = jr.render("ipr");
        }
    }

    /**
     *
     * @param passedArgs
     */
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"test.cow"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
