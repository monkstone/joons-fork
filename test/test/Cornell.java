package test;
/////////////////////////////// joons-renderer library for Processing ///////////////////////////////
///																					              ///
/// v0.70
///
/// Visit http://code.google.com/p/joons-renderer/
///
/// This is my library for bridging p5 and sunflow
///
/// OBJWriter is completely insulated from external access, 
/// so it exports into text file whatever data necessary for other tasks.
///
/// SCModifier reads in template SC file, edits it with the data it imports from JoonsRenderer and 
/// OBJWriter, in text files or in variables, and produces a working version of the scene file.
/// 
/// JoonsRenderer is the one which interacts with the end-user, so all the rendering functionalities 
/// must be made accessible through this class.
///																					              ///
/////////////////////////////////////////////////////////////////////////////////////////////////////


import joons.JoonsRenderer;
import processing.core.PApplet;

/**
 *
 * @author sid
 */
public class Cornell extends PApplet {

	JoonsRenderer jr;
	boolean rendered = false;
	
	////////////////////////////////////camera declaration////////////////////////////////////
	///																					   ///
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
	float aspect = 1.3333f;  
	float zNear = 5;
	float zFar = 10000;
	///																					   ///
	//////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    @Override
	public void setup() {
		size(400, 300, P3D);
		jr = new JoonsRenderer(this,width,height);
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

    /**
     *
     */
    @Override
	public void draw() {

		beginRecord("joons.OBJWriter", "");// just call like this. Leave the second parameter as "".

		perspective(fov, aspect, zNear, zFar);// use perspective() before camera()!!
		camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);

		translate(30, 0, 0);
		pushMatrix();
		// box
		translate(-20, -10, 20);
		rotateZ(PI / 8);
		rotateX(-PI / 8);
		sphere(18);
		popMatrix();
		translate(10, 0, 30);
		rotateX(PI / 8);
		box(20, 20, 40);

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
		if (key == 'r' || key == 'R' && !rendered) {
			saveFrame("capture.png");
                        jr.setShader("object0","Blue");
                        jr.setShader("sphere0","Glass");
			/*
			jr.setShader("object0","Blue");
			jr.setShader("sphere0","Red");
			jr.setShader("sphere1","Mirror");
			jr.setShader("sphere2","Glass");
			*/
			// each sphere is recognized as an object in it self, and are
			// given names such as sphere0, sphere1, .. 
			// so have set shader separately for each sphere.
			jr.setSC("cornell_box.sc");
			rendered=jr.render("bucket");
			// render using render("ipr") to render quick and rough,
			// and render("bucket") to render slow and smooth
			// if successfully rendered, returns true
		}
	}
        /**
     *
     * @param args
     */
    public static void main(String[] args){
            PApplet.main(new String[]{"test.Cornell"});
        }
}