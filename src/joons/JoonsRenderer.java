package joons;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author Joon Hyub Lee, modified for processing-2.0 by Martin Prout
 */
public class JoonsRenderer {
    //this class will write OBJ, will write SC, render it, write PNG file
    //and display it
    static String VERSION = "beta2.0";
    private PApplet parent;
    private SunflowRenderer sunflowRenderer;
    private String scFileName, renderFileName;
    private RenderStatus status = RenderStatus.START;
    private PImage renderResult;
    /**
     *
     */
    protected static String renderFolderName;
    private int width, height;
    private double renderSpeed = 1;
    private SCModifier scm;
    private ArrayList<String> objectNames, shaderNames;
    private ArrayList<String> beforeShaderLines, afterShaderLines, afterEverythingLines;
    /**
     * Convenience constant for creating file paths
     */
    public static String SEPARATOR = System.getProperty("file.separator");

    /**
     *
     * @param parent
     * @param width
     * @param height
     */
    public JoonsRenderer(PApplet parent, int width, int height) {
        this.parent = parent;
        setActive();
        sunflowRenderer = new SunflowRenderer();
        renderFolderName = "JoonsWIP";
        renderFileName = "renderWIP.png";

        sunflowRenderer.setRenderFilePath(parent.sketchPath(renderFileName));

        this.width = width;
        this.height = height;

        objectNames = new ArrayList<String>();
        shaderNames = new ArrayList<String>();
        beforeShaderLines = new ArrayList<String>();
        afterShaderLines = new ArrayList<String>();
        afterEverythingLines = new ArrayList<String>();
    }

    private void setActive() {
        parent.registerMethod("dispose", this);
        parent.registerMethod("pre", this);
        parent.registerMethod("draw", this);
    }

    private void setInactive() {
        parent.unregisterMethod("pre", this);
        parent.unregisterMethod("draw", this);
    }

    /**
     *
     * @param s
     */
    public void setRenderSpeed(double s) {
        this.renderSpeed = Math.sqrt(s);
    }

    /**
     *
     * @param beforeShaderLine
     */
    public void addBeforeShader(String beforeShaderLine) {
        beforeShaderLines.add(beforeShaderLine);
    }

    /**
     *
     * @param afterShaderLine
     */
    public void addAfterShader(String afterShaderLine) {
        afterShaderLines.add(afterShaderLine);
    }

    /**
     *
     * @param afterEverythingLine
     */
    public void addAfterEverything(String afterEverythingLine) {
        afterEverythingLines.add(afterEverythingLine);
    }

    /**
     *
     * @param objectName
     * @param shaderName
     */
    public void setShader(String objectName, String shaderName) {
        objectNames.add(objectName);
        shaderNames.add(shaderName);
    }

    /**
     *
     * @param scFileName
     */
    public void setSC(String scFileName) {
        int w = (int) (width / renderSpeed);
        int h = (int) (height / renderSpeed);
        this.scFileName = scFileName;
        scm = new SCModifier(parent, this.scFileName);

        scm.readSC();
        scm.swapResolution(w, h);
        scm.writeBeforeShader(beforeShaderLines);
        scm.writeAfterShader(afterShaderLines);
        scm.importPerspective();
        scm.importCamera();
        scm.swapCamPersp();
        scm.wipeFileMesh();
        scm.addFileMesh();
        scm.addSphere();
        scm.setShader(objectNames, shaderNames);
        scm.writeAfterEverything(afterEverythingLines);
        scm.writeSC();

        sunflowRenderer.setSCFilePath(parent.sketchPath(renderFolderName + SEPARATOR + "temp.sc"));
    }

    /**
     *
     * @param renderType
     */
    public void render(String renderType) {
        if (status.equals(RenderStatus.START)) {
            status = RenderStatus.TRACING;
            parent.noLoop();
            sunflowRenderer.autoRender(renderType);
            parent.loop();
            status = RenderStatus.TRACED;
        }
    }

    /**
     * Used to guard the display of the original sketch
     *
     * @return not traced
     */
    public boolean displaySketch() {
        return !status.equals(RenderStatus.TRACED);

    }

    /**
     * Called before processing draw
     */
    public void pre() {
        if (status.equals(RenderStatus.TRACED)) {
            renderResult = parent.loadImage("renderWIP.png");
            status = RenderStatus.DISPLAYED;
        }
    }

    /**
     * Called at processing draw
     */
    public void draw() {
        if (status.equals(RenderStatus.DISPLAYED)) {
            parent.background(0);
            parent.noLights();
            parent.camera();
            parent.perspective();
            parent.image(renderResult, 0, 0, width, height);
            parent.noLoop();
        }
    }
    
    /**
     * Required by processing
     *
     * @return version String
     */
    public String version() {
        return VERSION;
    }

    /**
     *
     */
    public void dispose() {
        setInactive();
        //   scm.dispose();
    }
}
