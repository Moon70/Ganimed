package lunartools.ganimed;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import lunartools.ganimed.gui.SimpleEvents;

public class GanimedModel extends Observable{
	public static final String PROGRAMNAME = "Ganimed";

	private File fileImageFolder;
	private File fileLastImageFolder;
	private File fileGifFolder;
	private File fileGif;
	
	private int imagesFps;
	private double numberOfImagesToSkip;
	private int gifFps;
	private int gifDelay;
	private int gifEndDelay;
	private final int gifEndDelayMaxDefault=5000;
	private final int gifEndDelayMaxMax=216000;
	private int gifEndDelayMax=gifEndDelayMaxDefault;

	private int imageWidth;
	private int imageHeight;

	private int cropLeft;
	private int cropLeftMin;
	private int cropLeftMax;
	private int cropTop;
	private int cropTopMin;
	private int cropTopMax;
	private int cropRight;
	private int cropRightMin;
	private int cropRightMax;
	private int cropBottom;
	private int cropBottomMin;
	private int cropBottomMax;
	private int resizePercent;
	private int resizeMin;
	private int resizeMax;
	private static final int MINIMUM_GIF_SIZE=16;
	
	private BufferedImage[] bufferedImages;
	private byte[] bytearrayGif;
	private static String versionProgram;

	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
	}

	public void setImageFolder(File file) {
		this.fileImageFolder=file;
		sendMessage(SimpleEvents.MODEL_IMAGEFOLDERCHANGED);
	}
	
	public File getImageFolder() {
		return fileImageFolder;
	}
	
	public void setLastImageFolder(String path) {
		if(path!=null) {
			this.fileLastImageFolder=new File(path);
		}
	}
	
	public File getLastImageFolder() {
		return fileLastImageFolder;
	}
	
	public void setGifFolder(String path) {
		if(path!=null) {
			this.fileGifFolder=new File(path);
		}
	}
	
	public File getGifFolder() {
		return fileGifFolder;
	}
	
	public void setGifFile(File file) {
		this.fileGif=file;
	}
	
	public File getGifFile() {
		return fileGif;
	}
	
	public void setImagesFps(int fps) {
		if(this.imagesFps==fps) {
			return;
		}
		if(fps<1) {
			fps=1;
		}
		this.imagesFps=fps;
		calculateGifPlaybackParameter();
	}

	public int getImagesFps() {
		return imagesFps;
	}
	
	public void setGifFps(int fps) {
		if(this.gifFps==fps) {
			return;
		}
		if(fps<1) {
			fps=1;
		}
		if(fps>imagesFps) {
			fps=imagesFps;
		}
		this.gifFps=fps;
		calculateGifPlaybackParameter();
	}
	
	public int getGifFps() {
		return gifFps;
	}

	public int getGifFpsMax() {
		return imagesFps+1;
	}

	public void calculateGifPlaybackParameter() {
		if(gifFps==0) {
			numberOfImagesToSkip=0;
			gifDelay=0;
		}else {
			gifDelay=(int)(1000.0/gifFps+0.5); 
			setNumberOfImagesToSkip(1.0*imagesFps/gifFps);
		}
		sendMessage(SimpleEvents.MODEL_GIFPLAYBACKVALUESCHANGED);
	}
	
	public void setGifDelay(int delay) {
		if(this.gifDelay==delay) {
			return;
		}
		if(delay<16) {
			delay=16;
		}
		if(delay>1000) {
			delay=1000;
		}
		this.gifDelay=delay;
		sendMessage(SimpleEvents.MODEL_GIFPLAYBACKVALUESCHANGED);
	}
	
	public int getGifDelay() {
		return gifDelay;
	}
	
	public void setGifEndDelay(int delay) {
		if(this.gifEndDelay==delay) {
			return;
		}
		if(delay<0) {
			delay=0;
		}
		if(delay>gifEndDelayMaxMax) {
			delay=gifEndDelayMaxMax;
		}
		this.gifEndDelay=delay;
		sendMessage(SimpleEvents.MODEL_GIFPLAYBACKVALUESCHANGED);
	}
	
	public int getGifEndDelay() {
		return gifEndDelay;
	}
	
	public int getGifEndDelayMax() {
		return gifEndDelayMax;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setCropLeft(int cropLeft) {
		if(cropLeft==this.cropLeft) {
			return;
		}else if(cropLeft<cropLeftMin) {
			this.cropLeft = cropLeftMin;
		}else if(cropLeft>cropLeftMax) {
			this.cropLeft=cropLeftMax;
		}else {
			this.cropLeft = cropLeft;
		}
		cropRightMin=cropLeft+MINIMUM_GIF_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}
	
	public int getCropLeft() {
		return cropLeft;
	}
	
	public void setCropRight(int cropRight) {
		if(cropRight==this.cropRight) {
			return;
		}else if(cropRight<cropRightMin) {
			this.cropRight = cropRightMin;
		}else if(cropRight>cropRightMax) {
			this.cropRight=cropRightMax;
		}else {
			this.cropRight = cropRight;
		}
		cropLeftMax=cropRight-MINIMUM_GIF_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}
	
	public int getCropRight() {
		return cropRight;
	}

	public void setCropTop(int cropTop) {
		if(cropTop==this.cropTop) {
			return;
		}else if(cropTop<cropTopMin) {
			this.cropTop = cropTopMin;
		}else if(cropTop>cropTopMax) {
			this.cropTop=cropTopMax;
		}else {
			this.cropTop = cropTop;
		}
		cropBottomMin=cropTop+MINIMUM_GIF_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}
	
	public int getCropTop() {
		return cropTop;
	}

	public void setCropBottom(int cropBottom) {
		if(cropBottom==this.cropBottom) {
			return;
		}else if(cropBottom<cropBottomMin) {
			this.cropBottom = cropBottomMin;
		}else if(cropBottom>cropBottomMax) {
			this.cropBottom=cropBottomMax;
		}else {
			this.cropBottom = cropBottom;
		}
		cropTopMax=cropBottom-MINIMUM_GIF_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}
	
	public int getCropBottom() {
		return cropBottom;
	}

	public void setResizePercent(int resizePercent) {
		if(resizePercent==this.resizePercent) {
			return;
		}
		if(resizePercent<1) {
			resizePercent=1;
		}
		if(resizePercent>100) {
			resizePercent=100;
		}
		this.resizePercent = resizePercent;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}
	
	public int getResizePercent() {
		return resizePercent;
	}

	private void setNumberOfImagesToSkip(double number) {
		if(this.numberOfImagesToSkip==number) {
			return;
		}
		this.numberOfImagesToSkip=number;
		sendMessage(SimpleEvents.MODEL_GIFNUMBEROFIMAGESCHANGED);
	}
	
	public double getNumberOfImagesToSkip() {
		return numberOfImagesToSkip;
	}
	
	public void setBufferedImages(BufferedImage[] bufferedImages) {
		this.bufferedImages=bufferedImages;
		if(bufferedImages!=null) {
			this.imageWidth=bufferedImages[0].getWidth();
			this.imageHeight=bufferedImages[0].getHeight();
			cropLeft=0;
			cropLeftMin=0;
			cropLeftMax=this.imageWidth-MINIMUM_GIF_SIZE;
			cropTop=0;
			cropTopMin=0;
			cropTopMax=this.imageHeight-MINIMUM_GIF_SIZE;
			cropRight=this.imageWidth;
			cropRightMin=MINIMUM_GIF_SIZE;
			cropRightMax=this.imageWidth;
			cropBottom=this.imageHeight;
			cropBottomMin=0;
			cropBottomMax=this.imageHeight-MINIMUM_GIF_SIZE;
			resizePercent=100;
			resizeMin=1;
			resizeMax=101;
			if(imagesFps==0) {
				imagesFps=25;
			}
			if(gifFps==0) {
				setGifFps(imagesFps>>1);
			}
			sendMessage(SimpleEvents.MODEL_IMAGESCHANGED);
		}
	}

	public BufferedImage[] getBufferedImages() {
		return bufferedImages;
	}
	
	private void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public byte[] getBytearrayGif() {
		return bytearrayGif;
	}

	public void setBytearrayGif(byte[] bytearrayGif) {
		this.bytearrayGif = bytearrayGif;
		sendMessage(SimpleEvents.MODEL_GIFBYTEARRAYCHANGED);
	}

	public int getCropLeftMin() {
		return cropLeftMin;
	}

	public int getCropLeftMax() {
		return cropLeftMax;
	}
	
	public int getCropRightMin() {
		return cropRightMin;
	}
	
	public int getCropRightMax() {
		return cropRightMax;
	}

	public int getCropTopMin() {
		return cropTopMin;
	}

	public int getCropTopMax() {
		return cropTopMax;
	}

	public int getCropBottomMin() {
		return cropBottomMin;
	}

	public int getCropBottomMax() {
		return cropBottomMax;
	}

	public int getResizeMin() {
		return resizeMin;
	}

	public int getResizeMax() {
		return resizeMax;
	}

	public static String determineProgramVersion() {
		if(versionProgram==null) {
			versionProgram="";
			Properties properties = new Properties();
			InputStream inputStream=GanimedModel.class.getClassLoader().getResourceAsStream("project.properties");
			if(inputStream==null) {
				System.err.println("project.properties not found");
			}else {
			}
			try {
				properties.load(inputStream);
				versionProgram=properties.getProperty("version");
			} catch (IOException e) {
				System.err.println("error loading project.properties");
				e.printStackTrace();
			}
			if("${project.version}".equals(versionProgram)) {
				versionProgram="";
			}
		}
		return versionProgram;
	}

}
