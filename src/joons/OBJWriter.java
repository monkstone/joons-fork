package joons;

//TODO gotta put the methods in the right order, in relevant groups
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import static processing.core.PConstants.*;
import processing.core.PGraphics;
import processing.core.PShape;

//implement camera() and perspective() to attain camera data
/**
 * Since processing-2.0 PGraphics3D doesn't exist (only 2D and 3D version of
 * PGraphics)
 *
 * @author Joon Hyub Lee (updated for processing-2.0 and further modified by
 * Martin Prout)
 */
public class OBJWriter extends PGraphics {

    private File objFile, cameraFile, perspFile, fmFile, sphereFile;
    private String absolutePath, fileName, cameraFileName, perspFileName, fmFileName, sphereFileName;
    private PrintWriter objWriter, cameraWriter, perspWriter, fmWriter, sphereWriter;
    private List<String> vertices_list;
    private List<String> faces;
    private List<Rotation> rotList;     // original had two ArrayLists one Float one String
    private Stack<List<Rotation>> rotStack;
    private Stack<JVector> transStack;
    private List<String> sphereLines;
    private JVector initVertexVector, translation, oldVertex1, oldVertex2;
    private int kind;
    private int vertexCnt;
    private int objectIndex = 0;
    private int sphereIndex = 0;
    private float aspect = 0;
    boolean OBJWriteEnabled = false;
    final String VECT = "v";
    final double ROT_AXIS = 1.0;

    /**
     * Constructor
     */
    public OBJWriter() {
        vertices_list = new ArrayList<String>(); // Create an empty ArrayList
        faces = new ArrayList<String>();
        rotList = new ArrayList<Rotation>();
        translation = new JVector(0, 0, 0);
        sphereLines = new ArrayList<String>();
        rotStack = new Stack<List<Rotation>>();
        transStack = new Stack<JVector>();

        fileName = "object" + objectIndex + ".obj";
        cameraFileName = "CameraExport.txt";
        perspFileName = "PerspectiveExport.txt";
        fmFileName = "FileMeshExport.txt";
        sphereFileName = "SphereExport.txt";
    }

    /**
     *
     * @param notUsed
     */
    @Override
    public void setPath(String notUsed) {
        absolutePath = parent.sketchPath("") + "JoonsWIP" + java.io.File.separatorChar;
        new File(absolutePath).mkdirs();

        this.path = this.absolutePath + fileName;

        if (path != null) {
            objFile = new File(path);
            if (!objFile.isAbsolute()) {
                objFile = null;
            }
        }
        if (objFile == null) {
            throw new RuntimeException("OBJExport requires an absolute path "
                    + "for the location of the output objFile.");
        }
    }

    /**
     *
     */
    @Override
    protected void allocate() {
    }

    /**
     *
     */
    @Override
    public void beginDraw() {
        // have to create file object here, because the name isn't yet
        // available in allocate()
        if (objWriter == null) {
            try {
                objWriter = new PrintWriter(new FileWriter(objFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        wipeSphereFile();
        // This initializes the sphereExport file, makes it blank.
    }

    /**
     * Called at endRecord(), also called at noSmooth() generates strings for
     * faces
     */
    @Override
    public void dispose() {
        for (int i = 0; i < vertices_list.size(); i += 3) {
            faces.add(String.format("f %d %d %d", (i + 1), (i + 2), (i + 3)));
        }
        writeOBJ();
        fileMeshExport();
        objWriter.flush();
        objWriter.close();
        objWriter = null;
    }

    /**
     * This method is a bit of hack, and is used to generate separate objects.
     * The writes objects to separate files (but it messes with display needs a
     * good fix)
     */
    @Override
    public void noSmooth() {
        dispose();
        objectIndex++;
        fileName = "object" + objectIndex + ".obj";
        refreshVertices();
        setPath(absolutePath);
        beginDraw();
    }

    /**
     * this nullifies the current vertices and faces matrices, so that a new
     * object can have its own coordinates
     */
    public void refreshVertices() {
        vertices_list.clear();
        faces.clear();
    }

    /**
     * only the vertices in between beginShape() are recorded
     *
     * @param kind
     */
    @Override
    public void beginShape(int kind) {
        vertexCnt = 0;
        OBJWriteEnabled = true;
        this.kind = kind;
    }

    /**
     *
     * @param x
     * @param y
     */
    @Override
    public void vertex(float x, float y) {
        vertex(x, y, 0);
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void vertex(float x, float y, float z) {
        // is called when the user uses vertex() in p5.
        // Contributed by koeberle in enabling quad strip (when kind=QUAD_STRIP)
        JVector vertexVector = new JVector(x, y, z);
        vertexCnt++;
        if (OBJWriteEnabled) {
            if (this.kind == QUAD_STRIP) {
                int mod = vertexCnt % 2;
                vertexVector = rotateAll(vertexVector);
                vertexVector = translateAll(vertexVector);
                if (oldVertex1 != null && oldVertex2 != null) {
                    if (mod == 0) {
                        vertices_list.add(generateString(VECT, oldVertex1));
                        vertices_list.add(generateString(VECT, oldVertex2));
                        vertices_list.add(generateString(VECT, vertexVector));
                    } else {
                        vertices_list.add(generateString(VECT, oldVertex2));
                        vertices_list.add(generateString(VECT, vertexVector));
                        vertices_list.add(generateString(VECT, oldVertex1));
                    }
                }
                if (mod == 0) {
                    oldVertex1 = vertexVector;
                } else {
                    oldVertex2 = vertexVector;
                }
            } else {
                if (vertexCnt == 1) {
                    // the initial vertex coordinates in the beginShape()
                    initVertexVector = new JVector(x, y, z);
                    initVertexVector = rotateAll(initVertexVector);
                    initVertexVector = translateAll(initVertexVector);
                }

                vertexVector = rotateAll(vertexVector);
                vertexVector = translateAll(vertexVector);

                if (vertexCnt == 3) {
                    //when there are more than 3 vertices in a beginShape(),
                    //we need to create another, new triangle
                    vertices_list.add(generateString(VECT, vertexVector));
                    vertices_list.add(generateString(VECT, initVertexVector));
                    vertexCnt = 2;
                }
                vertices_list.add(generateString(VECT, vertexVector));
            }

        }
    }

    /**
     *
     * @param mode
     */
    @Override
    public void endShape(int mode) {
        //remove two unnecessary remnants from the strings list
        if (kind == QUAD_STRIP) {
        } else {
            vertices_list.remove(vertices_list.size() - 1);
            vertices_list.remove(vertices_list.size() - 1);
        }
        OBJWriteEnabled = false;
    }

    /**
     * This would be a handy method if it could be implemented, to support the
     * "retained 3D shape", a feature introduced in processing 2.0.
     *
     * @param sh
     */
    @Override
    public void shape(PShape sh) {
        if (OBJWriteEnabled && sh.is3D()) {
            if (sh.getKind() == PShape.SPHERE) {
                float r = sh.getWidth() / 2.0f;
                JVector c = new JVector(0, 0, 0);
                c = rotateAll(c);
                c = translateAll(c);

                sphereLines.add("object {");
                sphereLines.add("   shader DefaultGrey");
                sphereLines.add("   type sphere");
                sphereLines.add("   name sphere" + sphereIndex);
                sphereLines.add(String.format("   c %f %f %f", c.getX(), -1 * c.getY(), c.getZ())); //times -1 to y because p5's y = sunflow's -y
                sphereLines.add(String.format("   r %f", r));
                sphereLines.add("}");
                sphereLines.add("");

                sphereIndex++;

                String fpath = absolutePath + sphereFileName;

                if (fpath != null) {
                    sphereFile = new File(fpath);
                    if (!sphereFile.isAbsolute()) {
                        sphereFile = null;
                    }
                }
                if (sphereFile == null) {
                    throw new RuntimeException("OBJExport requires an absolute path "
                            + "for the location of the output objFile.");
                }
                if (sphereWriter == null) {
                    try {
                        sphereWriter = new PrintWriter(sphereFile, "UTF-8");
                        try {
                            for (String sphere : sphereLines) {
                                sphereWriter.println(sphere);
                            }
                        } finally {
                            sphereWriter.flush();
                            sphereWriter.close();
                            sphereWriter = null;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(OBJWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //throw new UnsupportedOperationException("not implemented");
            //OBJWriteEnabled = false;
        }
    }

    /**
     *
     * @param transX
     * @param transY
     */
    @Override
    public void translate(float transX, float transY) {
        translate(transX, transY, 0);
    }

    /**
     *
     * @param transX
     * @param transY
     * @param transZ
     */
    @Override
    public void translate(float transX, float transY, float transZ) {
        JVector t = new JVector(transX, transY, transZ);
        t = rotateAll(t);
        translation = translation.add(t);
    }

    /**
     *
     * @param v
     * @return
     */
    public JVector translateAll(JVector v) {
        //translation keeps track of all the translations user calls,
        //and this method applies all of those translations to a given vector
        //in the right order, when called, and returns the calculated vector.
        v = v.add(translation);
        return v;
    }

    /**
     *
     * @param angle
     */
    @Override
    public void rotateX(float angle) {
        rotate(angle, ROT_AXIS, 0, 0);
    }

    /**
     *
     * @param angle
     */
    @Override
    public void rotateY(float angle) {
        rotate(angle, 0, ROT_AXIS, 0);
    }

    /**
     *
     * @param angle
     */
    @Override
    public void rotateZ(float angle) {
        rotate(angle, 0, 0, ROT_AXIS);
    }

    /**
     * Rotate function about arbitary axis
     *
     * @param w
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void rotate(float w, float x, float y, float z) {
        rotList.add(new Rotation(w, x, y, z));
    }

    /**
     *
     * @param w
     * @param x
     * @param y
     * @param z
     */
    public void rotate(double w, double x, double y, double z) {
        rotList.add(new Rotation(w, x, y, z));
    }

    /**
     * rotList keeps track of all the rotation user calls, and this method
     * applies all of those rotations to a given vector in the right order, when
     * called
     *
     * @param v
     * @return v the calculated vector
     */
    public JVector rotateAll(JVector v) {
        JAxis jaxis = new JAxis();
        for (Rotation rot : rotList) {
            jaxis.rotateAxis(rot.getVector(), rot.w);
            v.rotateVector(rot.getVector(), rot.w);
        }
        return v;
    }

    /**
     *
     * @param s
     * @param v
     * @return
     */
    public String generateString(String s, JVector v) {
        //times -1 to y because Processing's coordinate's y is 
        //inverse of that of sunflow
        return String.format("%s %.6f %.6f %.6f", s, v.getX(), -1 * v.getY(), v.getZ());
    }

    private void writeOBJ() {
        //this writes the contents in the .obj files
        for (String vertex : vertices_list) {
            objWriter.println(vertex);
        }
        for (String face : faces) {
            objWriter.println(face);
        }
    }

    /**
     *
     */
    public void fileMeshExport() {
        //this writes the fileMeshExport.txt
        List<String> fmLines = new ArrayList<String>();
        ArrayList<String> objLines; // = new ArrayList<String>();

        for (int i = 0; i <= objectIndex; i++) {
            fmLines.add("");
            objLines = writeObjectTemplate(i);
            for (String objLine : objLines) {
                fmLines.add(objLine);
            }
        }

        String fpath = absolutePath + fmFileName;

        if (fpath != null) {
            fmFile = new File(fpath);
            if (!fmFile.isAbsolute()) {
                fmFile = null;
            }
        }
        if (fmFile == null) {
            throw new RuntimeException("OBJExport requires an absolute path "
                    + "for the location of the output file.");
        }
        if (fmWriter == null) {
            try {
                fmWriter = new PrintWriter(fmFile, "UTF-8");
                try {
                    for (String fmLine : fmLines) {
                        fmWriter.println(fmLine);
                    }
                } finally {
                    fmWriter.flush();
                    fmWriter.close();
                    fmWriter = null;
                }
            } catch (Exception ex) {
                Logger.getLogger(OBJWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param i
     * @return
     */
    public ArrayList<String> writeObjectTemplate(int i) {
        //this writes the object template
		/*template
         object { 
         shader DefaultGrey
         type file-mesh
         name object0
         filename object0.obj 
         }
         */
        ArrayList<String> objLines = new ArrayList<String>();
        objLines.add("object {");
        objLines.add("   shader DefaultGrey");
        objLines.add("   type file-mesh");
        objLines.add("   name object" + i);
        objLines.add("   filename object" + i + ".obj");
        objLines.add("}");
        return objLines;
    }

    /**
     *
     * @param fov
     * @param aspect
     * @param zNear
     * @param zFar
     */
    @Override
    public void perspective(float fov, float aspect, float zNear, float zFar) {
        ArrayList<String> perspLines = new ArrayList<String>();

        //////////////////converting from p5 to sunflow parameters///////////////////
        ///converting p5's radian input to sunflow's degree input                 ///
        ///																		  ///
        fov = fov * 360 / (2 * PI);
        ///																		  ///
        /////////////////////////////////////////////////////////////////////////////

        perspLines.add("  fov    " + fov);
        perspLines.add("  aspect " + aspect);

        this.aspect = aspect;

        String fpath = absolutePath + perspFileName;

        if (fpath != null) {
            perspFile = new File(fpath);
            if (!perspFile.isAbsolute()) {
                perspFile = null;
            }
        }
        if (perspFile == null) {
            throw new RuntimeException("OBJExport requires an absolute path "
                    + "for the location of the output file.");
        }
        if (perspWriter == null) {
            try {
                perspWriter = new PrintWriter(new FileWriter(perspFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (String perspective : perspLines) {
            perspWriter.println(perspective);
        }

        perspWriter.flush();
        perspWriter.close();
        perspWriter = null;
    }

    /**
     *
     * @param eyeX
     * @param eyeY
     * @param eyeZ
     * @param centerX
     * @param centerY
     * @param centerZ
     * @param upX
     * @param upY
     * @param upZ
     */
    @Override
    public void camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        ArrayList<String> cameraLines = new ArrayList<String>();

        //////////////////converting from p5 to sunflow parameters/////////////////////
        ///compensating for the different ways p5 and sunflow implement aspect ratio///
        ///																		    ///
        if (aspect != 0) {
            JVector sightV = new JVector(eyeX - centerX, eyeY - centerY, eyeZ - centerZ);
            eyeX = (float) (centerX + aspect * sightV.getX());
            eyeY = (float) -(centerY + aspect * sightV.getY());//minus y because of p5's coord. properties
            eyeZ = (float) (centerZ + aspect * sightV.getZ());
        } else {
            System.out.println("JoonsRender: Please use perspective() before camera()");
            parent.exit();
        }
        ///compensating for the different ways p5 and sunflow implement up-vector   ///
        upX = (int) -upX;
        upY = (int) upY; //-1 * -1 = 1 for upY, because p5's y's inversed
        upZ = (int) -upZ;
        ///compensating for p5's inversed y direction
        centerY = -centerY;
        ///																		    ///
        ///////////////////////////////////////////////////////////////////////////////

        cameraLines.add("  eye    " + eyeX + " " + eyeY + " " + eyeZ);
        cameraLines.add("  target " + centerX + " " + centerY + " " + centerZ);
        cameraLines.add("  up     " + upX + " " + upY + " " + upZ);

        String fpath = absolutePath + cameraFileName;

        if (fpath != null) {
            cameraFile = new File(fpath);
            if (!cameraFile.isAbsolute()) {
                cameraFile = null;
            }
        }
        if (cameraFile == null) {
            throw new RuntimeException("OBJExport requires an absolute path "
                    + "for the location of the output objFile.");
        }
        if (cameraWriter == null) {
            try {
                cameraWriter = new PrintWriter(cameraFile, "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (String camline : cameraLines) {
            cameraWriter.println(camline);
        }

        cameraWriter.flush();
        cameraWriter.close();
        cameraWriter = null;

    }

    /////////////////////////From hereon are the 3D and 2D primitives/////////////////////////
    /**
     *
     * @param d
     */
    @Override
    public void box(float d) {
        //is called when the user uses vertex() in p5.
        float r = d / 2;

        beginShape();// top
        vertex(-r, r, r);
        vertex(r, r, r);
        vertex(r, -r, r);
        vertex(-r, -r, r);
        endShape();

        beginShape();// +x 
        vertex(r, r, r);
        vertex(r, -r, r);
        vertex(r, -r, -r);
        vertex(r, r, -r);
        endShape();

        beginShape();// -x 
        vertex(-r, r, r);
        vertex(-r, -r, r);
        vertex(-r, -r, -r);
        vertex(-r, r, -r);
        endShape();

        beginShape();// -y 
        vertex(-r, r, r);
        vertex(-r, r, -r);
        vertex(r, r, -r);
        vertex(r, r, r);
        endShape();

        beginShape();// -y 
        vertex(-r, -r, r);
        vertex(-r, -r, -r);
        vertex(r, -r, -r);
        vertex(r, -r, r);
        endShape();

        beginShape();// bottom
        vertex(-r, r, -r);
        vertex(r, r, -r);
        vertex(r, -r, -r);
        vertex(-r, -r, -r);
        endShape();
    }

    /**
     *
     * @param width
     * @param height
     * @param depth
     */
    @Override
    public void box(float width, float height, float depth) {
        //is called when the user uses vertex() in p5.
        float w = width / 2;
        float h = height / 2;
        float d = depth / 2;

        beginShape();// top
        vertex(-w, h, d);
        vertex(w, h, d);
        vertex(w, -h, d);
        vertex(-w, -h, d);
        endShape();

        beginShape();// +x 
        vertex(w, h, d);
        vertex(w, -h, d);
        vertex(w, -h, -d);
        vertex(w, h, -d);
        endShape();

        beginShape();// -x 
        vertex(-w, h, d);
        vertex(-w, -h, d);
        vertex(-w, -h, -d);
        vertex(-w, h, -d);
        endShape();

        beginShape();// -y 
        vertex(-w, h, d);
        vertex(-w, h, -d);
        vertex(w, h, -d);
        vertex(w, h, d);
        endShape();

        beginShape();// -y 
        vertex(-w, -h, d);
        vertex(-w, -h, -d);
        vertex(w, -h, -d);
        vertex(w, -h, d);
        endShape();

        beginShape();// bottom
        vertex(-w, h, -d);
        vertex(w, h, -d);
        vertex(w, -h, -d);
        vertex(-w, -h, -d);
        endShape();
    }

    /**
     *
     */
    public void wipeSphereFile() {
        sphereLines.add("");
        String fpath = absolutePath + sphereFileName;

        if (fpath != null) {
            sphereFile = new File(fpath);
            if (!sphereFile.isAbsolute()) {
                sphereFile = null;
            }
        }
        if (sphereFile == null) {
            throw new RuntimeException("OBJExport requires an absolute path "
                    + "for the location of the output objFile.");
        }
        if (sphereWriter == null) {
            try {
                sphereWriter = new PrintWriter(sphereFile, "UTF-8");
                try {
                    for (String sphere : sphereLines) {
                        sphereWriter.println(sphere);
                    }
                } finally {
                    sphereWriter.flush();
                    sphereWriter.close();
                    sphereWriter = null;
                }
            } catch (Exception ex) {
                Logger.getLogger(OBJWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Joons implemented the sphere in the external export form, because sunflow
     * seems to offer an optimized render for a perfect sphere, meaning no
     * triangle polygonal mess (I wan't to do this for my povwriter library)
     * <code>
     * object {
     * shader Mirror
     * type sphere
     * name mirror
     * c -30 30 20
     * r 20
     * }
     * </code>
     *
     * @param r
     */
    @Override
    public void sphere(float r) {

        JVector c = new JVector(0, 0, 0);
        c = rotateAll(c);
        c = translateAll(c);

        sphereLines.add("object {");
        sphereLines.add("   shader DefaultGrey");
        sphereLines.add("   type sphere");
        sphereLines.add("   name sphere" + sphereIndex);
        sphereLines.add(String.format("   c %f %f %f", c.getX(), -1 * c.getY(), c.getZ())); //times -1 to y because p5's y = sunflow's -y
        sphereLines.add(String.format("   r %f", r));
        sphereLines.add("}");
        sphereLines.add("");

        sphereIndex++;

        String fpath = absolutePath + sphereFileName;

        if (fpath != null) {
            sphereFile = new File(fpath);
            if (!sphereFile.isAbsolute()) {
                sphereFile = null;
            }
        }
        if (sphereFile == null) {
            throw new RuntimeException("OBJExport requires an absolute path "
                    + "for the location of the output objFile.");
        }
        if (sphereWriter == null) {
            try {
                sphereWriter = new PrintWriter(sphereFile, "UTF-8");
                try {
                    for (String sphere : sphereLines) {
                        sphereWriter.println(sphere);
                    }
                } finally {
                    sphereWriter.flush();
                    sphereWriter.close();
                    sphereWriter = null;
                }
            } catch (IOException ex) {
                Logger.getLogger(OBJWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void ellipse(float x, float y, float width, float height) {
        int n = 20;
        float th = 2 * PI / n; //the circle will be represented by a 30-sided polygon
        beginShape();
        for (int i = 0; i <= n; i++) {
            vertex((float) (x + width * Math.cos(th * i) / 2), (float) (y + height * Math.sin(th * i) / 2));
        }
        endShape();
    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     */
    @Override
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        beginShape();
        vertex(x1, y1);
        vertex(x2, y2);
        vertex(x3, y3);
        vertex(x4, y4);
        endShape();
    }

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void rect(float x, float y, float width, float height) {
        //rect modes are not supported. Use quad.
        quad(x, y, width + x, y, width + x, height + y, x, height + y);
    }

    /**
     * Using stack store more than one push
     */
    @Override
    public void pushMatrix() {
        // translationPushed = new JVector(translation);
        // pushedList = new ArrayList(rotList);
        transStack.push(new JVector(translation));
        rotStack.push(new ArrayList<Rotation>(rotList));
    }

    /**
     * A radical untested implementation, I've no idea of consequences, but it
     * might just behave, I don't see any use for it.
     */
    @Override
    public void resetMatrix() {
        transStack.clear();
        rotStack.clear();
    }

    /**
     * Code from DXF export, probably irrelevant here. The default is zero.
     *
     * @return
     */
    @Override
    public boolean displayable() {
        return false;  // just in case someone wants to use this on its own
    }

    /**
     * Assert this is not a 2D renderer
     *
     * @return
     */
    @Override
    public boolean is2D() {
        return false;
    }

    /**
     * Assert this is a 3D renderer
     *
     * @return true
     */
    @Override
    public boolean is3D() {
        return true;
    }

    /**
     * restore Matrix
     */
    @Override
    public void popMatrix() {
        if (!transStack.empty()) {
            translation = transStack.pop();
            rotList = rotStack.pop();
        }
    }
}
