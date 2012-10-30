

import joons.JoonsRenderer;
import joons.util.ArcBall;
import joons.util.Constrain;
import processing.core.*;

/**
 *
 * @author sid
 */
public class FTest extends PApplet {
   
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
    float aspect = 1.0f;
    float zNear = 5;
    float zFar = 10000;
    ArcBall arcball;

    /**
     *  renderSpeed is set 1 by default, if this method is not called.
     *   speed is inversely proportional to the render quality
     *
     *   file format:-
         <code>
         Image Settings
         Lights
         Shaders
         Modifiers
         Objects
         Instances
         </code>
     */
    @Override
    public void setup() {
        size(600, 600, P3D);
        jr = new JoonsRenderer(this, width, height);
        jr.setRenderSpeed(1);
        arcball = new ArcBall(this, 0, 0, width * 0.8f);
        arcball.constrain(Constrain.ZAXIS);
    }

    /**
     * The processing draw loop
     */
    @Override
    public void draw() {
        if (jr.displaySketch()) {
            beginRecord("joons.OBJWriter", "");// just call like this. Leave the second parameter as "".
            perspective(fov, aspect, zNear, zFar);// use perspective() before camera()!!
            camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
            background(200);
            arcball.update();  // need to update here to get Joons Renderer to update
            render();
            endRecord();
        }
    }

    /**
     * Note up is positive Z
     */
    public void render() {  // encapsulate the processing sketch as a function  
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

    /**
     * @todo move keyPressed event out of sketch?
     */
    @Override
    public void keyPressed() {
        if (key == 'r' || key == 'R' && jr.displaySketch()) {
            saveFrame("capture.png");
            jr.setShader("object0", "Red");
//    jr.setShader("object1", "Blue");  // "F"is recognized as one object  
//    jr.setShader("object2","Blue");
//     jr.setShader("object3","Blue");
//     jr.setShader("object4","Blue");
//     jr.setShader("object5","Blue");

            // each sphere is recognized as an object in it self, and are
            // given names such as sphere0, sphere1, .. 
            // so have set shader separately for each sphere.
            jr.setSC(dataPath("cornell_box.sc"));
            jr.render("bucket");
            // render using render("ipr") to render quick and rough,
            // and render("bucket") to render slow and smooth
            // if successfully rendered, returns true
        }
    }

    /**
     *
     * @param passedArgs
     */
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"FTest"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
