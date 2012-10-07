package joons;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author Joon Hyub Lee
 */
public class JoonsRenderer {
    //this class will write OBJ, will write SC, render it, write PNG file
    //and display it

    private PApplet parent;
    private SunflowRenderer sunflowRenderer;
    private String scFileName, renderFileName;
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
     *
     */
    public static String SEPARATOR;

    /**
     *
     * @param parent
     * @param width
     * @param height
     */
    public JoonsRenderer(PApplet parent, int width, int height) {
        this.parent = parent;
        parent.registerMethod("dispose", this);
        SEPARATOR = System.getProperty("file.separator");
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
     * @return
     */
    public boolean render(String renderType) {
        sunflowRenderer.autoRender(renderType);
        return true;
    }

    /**
     *
     */
    public void display() {
        parent.background(0);
        //this is to reset the display before displaying the rendered image
        PImage renderResult;
        renderResult = parent.loadImage("renderWIP.png");
        parent.noLights();
        parent.camera();
        parent.perspective();
        parent.image(renderResult, 0, 0, width, height);
    }

    /**
     *
     */
    public void dispose() {
        scm.dispose();
    }
}
