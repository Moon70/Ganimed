package lunartools.ganimed.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter{
	private String name;
	private String extension;

	public ImageFileFilter(String name, String extension) {
		this.name=name;
		this.extension=extension;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()) {
			return true;
		}
		return f.getName().toLowerCase().endsWith(extension);
	}

	@Override
	public String getDescription() {
		return name;
	}

}
