package joons;

//TODO gotta put the methods in the right order, in relevant groups
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import joons.util.LUT;
import static processing.core.PConstants.*;
import processing.core.PGraphics;
import processing.core.PShape;

//implement camera() and perspective() to attain camera data
/**
 *
 * @author Joon Hyub Lee
 */
public class OBJWriter extends PGraphics {
    //OBJ file and objFile writer

    /**
     *
     */
    public enum Axis {

        /**
         * The x axis
         */
        X,
        /**
         * The y axis
         */
        Y,
        /**
         * The z axis
         */
        Z
    }
    private File objFile, cameraFile, perspFile, fmFile, sphereFile;
    private String absolutePath, fileName, cameraFileName, perspFileName, fmFileName, sphereFileName;
    private PrintWriter objWriter, cameraWriter, perspWriter, fmWriter, sphereWriter;
    //other necessary variables
    private ArrayList<String> vertices_list;
    private ArrayList<String> faces;
    private ArrayList<Axis> axisList;
    private ArrayList<Axis> axisListPushed;
    private ArrayList<Float> angleList;
    private ArrayList<Float> angleListPushed;
    private ArrayList<String> sphereLines;
    private JVector initVertexVector, translation, oldVertex1, oldVertex2;
    private JVector initVertexVectorPushed, translationPushed, oldVertex1Pushed, oldVertex2Pushed;
    private boolean pushed;
    private int kind;
    private int vertexCnt;
    private int objectIndex = 0;
    private int sphereIndex = 0;
    private float aspect = 0;
    boolean OBJWriteEnabled = false;

    /**
     *
     */
    public OBJWriter() {
        //initializing
        vertices_list = new ArrayList<String>(); // Create an empty ArrayList
        faces = new ArrayList<String>();
        axisList = new ArrayList<Axis>();
        angleList = new ArrayList<Float>();
        translation = new JVector(0, 0, 0);
        sphereLines = new ArrayList<String>();
        LUT.initialize();
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
     *
     */
    @Override
    public void dispose() {
        //is called at endRecord(),
        //but is also called at noSmooth()

        //Generating strings for faces
        for (int i = 0; i < (vertices_list.size() / 3); i++) {
            int j = i * 3;
            faces.add(String.format("f %d %d %d", (j + 1), (j + 2), (j + 3)));
        }

        writeOBJ();
        fileMeshExport();
        objWriter.flush();
        objWriter.close();
        objWriter = null;
    }

    /**
     *
     */
    @Override
    public void noSmooth() {
        //I'm sorry for the confusion, but this is the only possible circumvention
        //to create separate objects and write them into separate files

        dispose();
        objectIndex++;
        fileName = "object" + objectIndex + ".obj";
        refreshVertices();
        setPath(absolutePath);
        beginDraw();
    }

    /**
     *
     */
    public void refreshVertices() {
        //this nullifies the current vertices and faces matrices,
        //so that a new object can have its own coordinates
        vertices_list = new ArrayList<String>(); // Create an empty ArrayList
        faces = new ArrayList<String>();
    }

    /**
     *
     * @param kind
     */
    @Override
    public void beginShape(int kind) {
        // only the vertices in between beginShape() are recorded
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
                        vertices_list.add(generateString("v", oldVertex1));
                        vertices_list.add(generateString("v", oldVertex2));
                        vertices_list.add(generateString("v", vertexVector));
                    } else {
                        vertices_list.add(generateString("v", oldVertex2));
                        vertices_list.add(generateString("v", vertexVector));
                        vertices_list.add(generateString("v", oldVertex1));
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
                    vertices_list.add(generateString("v", vertexVector));
                    vertices_list.add(generateString("v", initVertexVector));
                    vertexCnt = 2;
                }
                vertices_list.add(generateString("v", vertexVector));
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
     * This would be a handy method if it could be implemented, to support
     * the "retained 3D shape", a feature introduced in processing 2.0.
     * @param sh 
     */

    @Override
    public void shape(PShape sh) {
        if (OBJWriteEnabled && sh.is3D()) {
            throw new UnsupportedOperationException("not implemented");
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
        axisList.add(Axis.X);
        angleList.add(angle);
    }

    /**
     *
     * @param angle
     */
    @Override
    public void rotateY(float angle) {
        axisList.add(Axis.Y);
        angleList.add(angle);
    }

    /**
     *
     * @param angle
     */
    @Override
    public void rotateZ(float angle) {
        axisList.add(Axis.Z);
        angleList.add(angle);
    }

    /**
     *
     * @param v
     * @return
     */
    public JVector rotateAll(JVector v) {
        //axisList and angleList keep track of all the rotation user calls,
        //and this method applies all of those rotations to a given vector
        //in the right order, when called, and returns the calculated vector.
        if (angleList.size() == axisList.size()) {
            JAxis jaxis = new JAxis();
            for (int i = 0; i < axisList.size(); i++) {
                switch (axisList.get(i)) {
                    case X:
                        jaxis.rotateX(angleList.get(i));
                        v.rotateX(jaxis, angleList.get(i));
                        break;
                    case Y:
                        jaxis.rotateX(angleList.get(i));
                        v.rotateY(jaxis, angleList.get(i));
                        break;
                    case Z:
                        jaxis.rotateX(angleList.get(i));
                        v.rotateZ(jaxis, angleList.get(i));
                        break;
                }
            }
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
        return String.format("%s %f %f %f", s, v.getX(), -1 * v.getY(), v.getZ());
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
        ArrayList<String> fmLines = new ArrayList<String>();
        ArrayList<String> objLines; // = new ArrayList<String>();

        for (int i = 0; i <= objectIndex; i++) {
            fmLines.add("");
            objLines = writeObjectTemplate(i);
            for (int j = 0; j < objLines.size(); j++) {
                fmLines.add((String) objLines.get(j));
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
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OBJWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
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
        for (int i = 0; i < perspLines.size(); i++) {
            perspWriter.println((String) perspLines.get(i));
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
            eyeX = centerX + aspect * sightV.getX();
            eyeY = -(centerY + aspect * sightV.getY());//minus y because of p5's coord. properties
            eyeZ = centerZ + aspect * sightV.getZ();
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
                cameraWriter = new PrintWriter(new FileWriter(cameraFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < cameraLines.size(); i++) {
            cameraWriter.println((String) cameraLines.get(i));
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
                    for (int i = 0; i < sphereLines.size(); i++) {
                        sphereWriter.println((String) sphereLines.get(i));
                    }
                } finally {
                    sphereWriter.flush();
                    sphereWriter.close();
                    sphereWriter = null;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OBJWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(OBJWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param r
     */
    @Override
    public void sphere(float r) {
        //I've implemented sphere in the external export form,
        //because sunflow seems to offer an optimized render for a perfect sphere,
        //meaning no triangle polygonal mess
		/* template
         object {
         shader Mirror
         type sphere
         name mirror
         c -30 30 20
         r 20
         }
         */
        JVector c = new JVector(0, 0, 0);
        //c=rotateAll(c);
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
                    for (int i = 0; i < sphereLines.size(); i++) {
                        sphereWriter.println((String) sphereLines.get(i));
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
            vertex(x + width * LUT.cos(th * i) / 2, y + height * LUT.sin(th * i) / 2);
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
        quad(0 + x, 0 + y, width + x, 0 + y, width + x, height + y, 0 + x, height + y);
    }

    /**
     *
     */
    @Override
    public void pushMatrix() {
        if (!pushed) {
            pushed = true;
            translationPushed = new JVector(translation.getX(), translation.getY(), translation.getZ());
            axisListPushed = new ArrayList(axisList);
            angleListPushed = new ArrayList(angleList);
        }
    }

    /**
     *
     */
    @Override
    public void popMatrix() {
        if (pushed) {
            pushed = false;
            translation = translationPushed;
            axisList = axisListPushed;
            angleList = angleListPushed;
        }
    }
}
