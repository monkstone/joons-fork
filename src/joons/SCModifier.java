package joons;

import java.io.*;
import java.util.ArrayList;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class SCModifier {

    PApplet parent;
    private BufferedReader scReader, cameraReader, perspReader, fmReader, sphereReader;
    private ArrayList<String> scLines, cameraImport, perspImport, fmImport, sphereImport;
    private String scFilePath, absolutePath;
    private File scFile;
    private PrintWriter scWriter;

    public SCModifier(PApplet parent, String scFilePath) {
        //it reads the given sc file, and write out to a dummy sc file.

        absolutePath = parent.sketchPath(JoonsRenderer.renderFolderName + JoonsRenderer.SEPARATOR);
        this.scFilePath = absolutePath + "temp.sc";

        scReader = parent.createReader(scFilePath);
        cameraReader = parent.createReader(absolutePath + "CameraExport.txt");
        perspReader = parent.createReader(absolutePath + "PerspectiveExport.txt");
        fmReader = parent.createReader(absolutePath + "FileMeshExport.txt");
        sphereReader = parent.createReader(absolutePath + "SphereExport.txt");

        scLines = new ArrayList<String>();
        cameraImport = new ArrayList<String>();
        perspImport = new ArrayList<String>();
        fmImport = new ArrayList<String>();
        sphereImport = new ArrayList<String>();
    }

    public void readSC() {
        String line = "";
        while (line != null) {
            try {
                line = scReader.readLine();
            } catch (IOException e) {
                
                line = null;
            }
            scLines.add(line);
        }
        scLines.remove(scLines.size() - 1);// removes the last rubbish line
    }

    public int lookForTheFirst(String[] keywords) {
        //it returns the index upon the FIRST encounter with a line
        //which contains all the keywords given
        //if not found, it returns -1;
        boolean found;
        for (int i = 0; i < scLines.size(); i++) {
            found = true;
            for (int j = 0; j < keywords.length; j++) {
                found = found & (scLines.get(i).contains(keywords[j]));
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public int lookForTheFirstFrom(String[] keywords, int indexFrom) {
        //this filter begins looking FROM the specified index, moves down,
        //and returns the index upon the FIRST encounter with a line
        //which contains all the keywords given
        //if not found, it returns -1;
        boolean found;
        for (int i = indexFrom; i < scLines.size(); i++) {
            found = true;
            for (int j = 0; j < keywords.length; j++) {
                found = found & (scLines.get(i).contains(keywords[j]));
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public int lookForTheLast(String[] keywords) {
        //it returns the index upon the LAST encounter with a line
        //which contains all the keywords given
        //if not found, it returns -1;
        int foundIndex = -1;
        boolean found;
        for (int i = 0; i < scLines.size(); i++) {
            found = true;
            for (int j = 0; j < keywords.length; j++) {
                found = found & (scLines.get(i).contains(keywords[j]));
            }
            if (found) {
                foundIndex = i;
            }
        }
        return foundIndex;
    }

    public void importPerspective() {
        String line = "";
        while (line != null) {
            try {
                line = perspReader.readLine();
            } catch (IOException e) {
                
                line = null;
            }

            perspImport.add(line);
        }
        perspImport.remove(perspImport.size() - 1);// removes the last rubbish
        // line
    }

    public void importCamera() {
        String line = "";
        while (line != null) {
            try {
                line = cameraReader.readLine();
            } catch (IOException e) {
                
                line = null;
            }

            cameraImport.add(line);
        }
        cameraImport.remove(cameraImport.size() - 1);// removes the last rubbish
        // line
    }

    public void writeBeforeShader(ArrayList<String> lines) {
        //Below is a typical shader layout
        //at the top of the shaders stack
		/*
        shader {				<--shaderBeginsAt
        name debug_caustics
        type view-caustics
        }
         */
        String[] keywords = {"shader", "{"};
        int shaderBeginsAt = lookForTheFirst(keywords);
        for (int i = lines.size(); i > 0; i--) {
            scLines.add(shaderBeginsAt, (String) lines.get(i - 1));
        }
    }

    public void writeAfterShader(ArrayList<String> lines) {
        //Below is a typical shader layout
        //at the top of the shaders stack
		/*
        shader {			<--lastShaderBeginsAt
        name Glass
        type glass
        eta 1.6
        color 1 1 1
        }					<--lastShaderEndsAt
         */
        String[] startKeywords = {"shader", "{"};
        String[] endKeywords = {"}"};
        int lastShaderBeginsAt = lookForTheLast(startKeywords);
        int lastShaderEndsAt = lookForTheFirstFrom(endKeywords, lastShaderBeginsAt);
        for (int i = lines.size(); i > 0; i--) {
            scLines.add(lastShaderEndsAt + 1, (String) lines.get(i - 1));
        }
    }

    public void swapCamPersp() {
        //Below is the default camera layout
		/*
         * camera{				<--cameraBeginsAt(index)
         *   type pinhole 		<--cameraBeginsAt+1
         *   eye 0 -205 50 		<--cameraBeginsAt+2
         *   target 0 0 50 
         *   up 0 0 1 
         *   fov 45
         *   aspect 1.333333
         * }
         */
        String[] keywords = {"camera", "{"};
        int cameraBeginsAt = lookForTheFirst(keywords);
        scLines.set(cameraBeginsAt + 2, cameraImport.get(0));
        scLines.set(cameraBeginsAt + 3, cameraImport.get(1));
        scLines.set(cameraBeginsAt + 4, cameraImport.get(2));
        scLines.set(cameraBeginsAt + 5, perspImport.get(0));
        scLines.set(cameraBeginsAt + 6, perspImport.get(1));
    }

    public void swapResolution(int width, int height) {
        //Below is the default image setting layout
		/*
        image {
        resolution 565 424	<--resolutionBeginsAt
        aa 0 2
        filter gaussian
        }
         */
        String[] keywords = {"resolution"};
        int resolutionBeginsAt = lookForTheFirst(keywords);
        scLines.set(resolutionBeginsAt, "   resolution " + width + " " + height);
    }

    public void wipeFileMesh() {
        //Below is the defalut file-mesh type object layout
		/*
        object {					<--fmBeginsAt-2
        shader DefaultGrey		<--fmBeginsAt-1
        type file-mesh			<--fmBeginsAt
        name object0
        filename object0.obj
        }
         */
        String[] keywords = {"file-mesh"};
        int fmBeginsAt = lookForTheFirst(keywords);

        if (fmBeginsAt != -1) {
            //-1 means nothing to wipe
            int iterate = scLines.size() - (fmBeginsAt - 2);
            for (int i = 0; i < iterate; i++) {
                scLines.remove(scLines.size() - 1);
            }
        }

    }

    public void addFileMesh() {
        String line = "";
        while (line != null) {
            try {
                line = fmReader.readLine();
            } catch (IOException e) {
                
                line = null;
            }

            fmImport.add(line);
        }
        fmImport.remove(fmImport.size() - 1);// removes the last rubbish line

        for (int i = 0; i < fmImport.size(); i++) {
            scLines.add((String) fmImport.get(i));
        }
    }

    public void addSphere() {
        String line = "";
        while (line != null) {
            try {
                line = sphereReader.readLine();
            } catch (IOException e) {
                
                line = null;
            }

            sphereImport.add(line);
        }
        sphereImport.remove(sphereImport.size() - 1);// removes the last rubbish line

        for (int i = 0; i < sphereImport.size(); i++) {
            scLines.add((String) sphereImport.get(i));
        }
    }

    public void setShader(ArrayList<String> objectNames, ArrayList<String> shaderNames) {
        //Below is the defalut object layout
		/*
        object {
        shader DefaultGrey		<--objectNameIsAt-2
        type file-mesh			<--objectNameIsAt-1
        name object0				<--objectNameIsAt
        filename object0.obj
        }
        
        object {
        shader DefaultGrey
        type sphere
        name sphere0
        c 9.751703 -1.5379201 -1.5934509
        r 10.0
        }
         */
        for (int i = 0; i < objectNames.size(); i++) {
            String objectName = (String) objectNames.get(i);
            String shaderName = (String) shaderNames.get(i);
            String[] keywords = {"name", objectName};
            int objectNameIsAt = lookForTheFirst(keywords);
            scLines.set(objectNameIsAt - 2, "   shader " + shaderName);
            if (objectNameIsAt != -1) {
                System.out.println(objectName + "'s shader was set to " + shaderName);
            }
        }
    }

    public void writeAfterEverything(ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            scLines.add((String) lines.get(i));
        }
    }

    public void writeSC() {

        String path = scFilePath;

        if (path != null) {
            scFile = new File(path);
            if (!scFile.isAbsolute()) {
                scFile = null;
            }
        }
        if (scFile == null) {
            throw new RuntimeException("OBJExport requires an absolute path "
                    + "for the location of the output file.");
        }

        if (scWriter == null) {
            try {
                scWriter = new PrintWriter(new FileWriter(scFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < scLines.size(); i++) {
            scWriter.println((String) scLines.get(i));
        }

        scWriter.flush();
        scWriter.close();
        scWriter = null;
    }
}
