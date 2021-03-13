package lunartools;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import lunartools.ganimed.GanimedController;

public class Settings {
	private static final String JAVAPROPERTY_SETTINGS_FOLDER="lunartools.ganimed.settings.folder";
	private Properties properties;
	private String programName;
	private String version;
	private File fileProperties;
	
	public Settings(String programName,String version){
		this.programName=programName;
		this.version=version;
		File folder=determinePropertiesFolder();
		if(folder!=null) {
			this.fileProperties=new File(folder,programName+".properties");
		}
		loadSettings();
	}
	
	public void saveSettings() throws IOException {
		FileWriter fileWriter=new FileWriter(fileProperties);
		try {
			properties.store(fileWriter,programName+" "+version);
		} finally{
			fileWriter.flush();
			fileWriter.close();
		}
	}
	
	private void loadSettings(){
		properties=new Properties();
		File file=fileProperties;
		if(!file.exists()) {
			InputStream inputStream = this.getClass().getResourceAsStream("/DefaultSettings.properties");
			if(inputStream==null) {
				throw new RuntimeException("error loading DefaultSettings.properties");
			}
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				throw new RuntimeException("error loading DefaultSettings.properties");
			}
			return;
		}
		FileReader fileReader=null;
		try {
			fileReader=new FileReader(file);
			properties.load(fileReader);
		} catch (IOException e) {
			throw new RuntimeException("error loading settings",e);
		} finally {
			if(fileReader!=null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					throw new RuntimeException("error closing settings file",e);
				}
			}
		}
	}
	
	public void setRectangle(String name, Rectangle rectangle) {
		properties.setProperty(name,""+rectangle.x+","+rectangle.y+","+rectangle.width+","+rectangle.height);
	}
	
	public Rectangle getRectangle(String name, Rectangle defaultRectangle) {
		String s=properties.getProperty(name);
		if(s!=null) {
			String[] sa=s.split(",");
			if(sa.length==4) {
				return new Rectangle(Integer.parseInt(sa[0]),Integer.parseInt(sa[1]),Integer.parseInt(sa[2]),Integer.parseInt(sa[3]));
			}
		}
		setRectangle(name,defaultRectangle);
		return defaultRectangle;
	}
	
	public void setPoint(String name, Point point) {
		properties.setProperty(name,""+point.x+","+point.y);
	}
	
	public Point getPoint(String name, Point defaultPoint) {
		String s=properties.getProperty(name);
		if(s!=null) {
			String[] sa=s.split(",");
			if(sa.length==2) {
				return new Point(Integer.parseInt(sa[0]),Integer.parseInt(sa[1]));
			}
		}
		setPoint(name,defaultPoint);
		return defaultPoint;
	}
	
	public void set(String name, String string) {
		properties.setProperty(name,string);
	}
	
	public String getString(String name) {
		return properties.getProperty(name);
	}

	private File determinePropertiesFolder() {
		File file=null;
		String path=System.getProperty(JAVAPROPERTY_SETTINGS_FOLDER);
		if(path!=null) {
			file=new File(path);
			if(file.exists()) {
				return file;
			}
			file=null;
		}
		path = GanimedController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			if(path.toLowerCase().endsWith(".jar")) {
				file=new File(URLDecoder.decode(path, "UTF-8")).getParentFile();
			}
		} catch (UnsupportedEncodingException e) {
			System.err.println("error decoding codesource path: "+path);
		}
		if(file==null) {
			String temp=System.getenv("temp");
			if(temp!=null) {
				file=new File(temp);
			}
		}
		if(file==null) {
			String tmp=System.getenv("tmp");
			if(tmp!=null) {
				file=new File(tmp);
			}
		}
		return file;
	}

}
