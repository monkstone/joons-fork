package joons;


import org.sunflow.SunflowAPI;
import org.sunflow.system.ImagePanel;

public class SunflowRenderer {

	private SunflowAPI api;
	private ImagePanel imagePanel = new ImagePanel();
	private String scFilePath, renderFilePath, buildTemplate;

	public SunflowRenderer() {
		super();
		api = null; //initialization
	}
	
	public void autoRender(String renderType) {
		//called by JoonsRenderer
		openFile();
		build();
		render(renderType);
		saveFile();
	}
	
	public void setSCFilePath(String scFilePath){
		//called by JoonsRenderer
		this.scFilePath=scFilePath;
	}
	
	public void setRenderFilePath(String renderFilePath){
		//called by JoonsRenderer
		this.renderFilePath=renderFilePath;
	}

	private void openFile() {
		//loads an sc file to fileDirectory (abs path to your sc), 
		//and creates a template for build.
		//use "parse" for versions lower than 0.7.3, "include" for 0.7.3 and higher
		/*
		buildTemplate = "import org.sunflow.core.*;\nimport org.sunflow.core.accel.*;\nimport org.sunflow.core.camera.*;\nimport org.sunflow.core.primitive.*;\nimport org.sunflow.core.shader.*;\nimport org.sunflow.image.Color;\nimport org.sunflow.math.*;\n\npublic void build() {\n  parse(\""
				+ scFilePath.replace("\\", "\\\\") + "\");\n}\n";
		*/	
	StringBuilder template = new StringBuilder("import org.sunflow.core.*;\n");
        template.append("import org.sunflow.core.accel.*;\n");
        template.append("import org.sunflow.core.camera.*;\n");
        template.append("import org.sunflow.core.primitive.*;\n");
        template.append("import org.sunflow.core.shader.*;\n");
        template.append("import org.sunflow.image.Color;\n");
        template.append("import org.sunflow.math.*;\n\n");
        template.append("public void build() {\n");
        template.append(" include(");
        template.append('"');
        template.append(scFilePath.replace("\\","\\\\"));
        template.append('"');
        template.append(");\n");
        template.append("}\n");
        buildTemplate = template.toString();
	}

	private void build() {
		api = SunflowAPI.compile(buildTemplate);
		api.build();
	}

	private void render(String renderType) {
		// in the parameter, set the second parameter "ipr" to render quick and rough,
		// or "bucket" to do it more slowly(takes about double the time) but
		// with a higher quality.
		api.parameter("sampler", renderType);
		api.options(SunflowAPI.DEFAULT_OPTIONS);
		api.render(SunflowAPI.DEFAULT_OPTIONS, imagePanel);
	}

	private void saveFile() {
		//saves the rendered image file to the renderFilePath
		imagePanel.save(renderFilePath);
	}
}
