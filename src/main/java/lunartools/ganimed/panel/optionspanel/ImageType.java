package lunartools.ganimed.panel.optionspanel;

public enum ImageType {
	GIF(".gif","GIF"),
	PNG(".png","APNG");
	
	private String fileextension;
	private String name;
	
	ImageType(String fileextension, String name) {
		this.fileextension=fileextension;
		this.name=name;
	}

	public String getFileExtension(){
		return fileextension;
	}

	public String getName(){
		return name;
	}

}
