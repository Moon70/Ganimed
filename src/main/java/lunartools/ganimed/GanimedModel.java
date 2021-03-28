package lunartools.ganimed;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import lunartools.ganimed.gui.SimpleEvents;
import lunartools.ganimed.imagetype.ImageType;
import lunartools.ganimed.imagetype.ImageTypeApng;
import lunartools.ganimed.imagetype.ImageTypeGif;

public class GanimedModel extends Observable{
	public static final String PROGRAMNAME = "Ganimed";

	private ImageType imageType;

	private File fileImageFolder;
	private File fileLastImageFolder;
	private File fileAnimFolder;
	private File fileAnim;

	private int imagesFps;
	private double numberOfImagesToSkip;
	private int animFps;
	private int animDelay;
	private int animEndDelay;
	private final int animEndDelayMaxDefault=5000;
	private final int animEndDelayMaxMax=216000;
	private int animEndDelayMax=animEndDelayMaxDefault;

	private int imageWidth;
	private int imageHeight;

	private int cutLeft;
	private int cutLeftMin;
	private int cutLeftMax;
	private int cutRight;
	private int cutRightMin;
	private int cutRightMax;
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
	private static final int MINIMUM_ANIM_SIZE=16;
	private static final int MINIMUM_ANIM_FRAMECOUNT=1;
	
	private BufferedImage[] bufferedImages;
	private byte[] bytearrayAnim;
	private static String versionProgram;
	private ArrayList<ImageType> imageTypes;

	public GanimedModel() {
		setImageType(ImageTypeGif.getInstance());
	}

	public ArrayList<ImageType> getImageTypes(){
		if(imageTypes==null) {
			imageTypes=new ArrayList<ImageType>();
			imageTypes.add(ImageTypeGif.getInstance());
			imageTypes.add(ImageTypeApng.getInstance());
		}
		return imageTypes;
	}

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

	public void setAnimFolder(String path) {
		if(path!=null) {
			this.fileAnimFolder=new File(path);
		}
	}

	public File getAnimFolder() {
		return fileAnimFolder;
	}

	public void setAnimFile(File file) {
		this.fileAnim=file;
		int p=file.getName().lastIndexOf('.');
		if(p!=-1) {
			String fileextension=file.getName().substring(p);
			for(ImageType imageType:imageTypes) {
				if(imageType.getFileExtension().equalsIgnoreCase(fileextension)) {
					setImageType(imageType);
					break;
				}
			}
		}
	}

	public File getAnimFile() {
		return fileAnim;
	}

	public void setImagesFps(int fps) {
		if(this.imagesFps==fps) {
			return;
		}
		if(fps<1) {
			fps=1;
		}
		this.imagesFps=fps;
		calculateAnimPlaybackParameter();
	}

	public int getImagesFps() {
		return imagesFps;
	}

	public void setAnimFps(int fps) {
		if(this.animFps==fps) {
			return;
		}
		if(fps<1) {
			fps=1;
		}
		if(fps>imagesFps) {
			fps=imagesFps;
		}
		this.animFps=fps;
		calculateAnimPlaybackParameter();
	}

	public int getAnimFps() {
		return animFps;
	}

	public int getAnimFpsMax() {
		return imagesFps+1;
	}

	public void calculateAnimPlaybackParameter() {
		if(animFps==0) {
			numberOfImagesToSkip=0;
			animDelay=0;
		}else {
			animDelay=(int)(1000.0/animFps+0.5); 
			setNumberOfImagesToSkip(1.0*imagesFps/animFps);
		}
		sendMessage(SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED);
	}

	public void setAnimDelay(int delay) {
		if(this.animDelay==delay) {
			return;
		}
		if(delay<16) {
			delay=16;
		}
		if(delay>1000) {
			delay=1000;
		}
		this.animDelay=delay;
		sendMessage(SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED);
	}

	public int getAnimDelay() {
		return animDelay;
	}

	public void setAnimEndDelay(int delay) {
		if(this.animEndDelay==delay) {
			return;
		}
		if(delay<0) {
			delay=0;
		}
		if(delay>animEndDelayMaxMax) {
			delay=animEndDelayMaxMax;
		}
		this.animEndDelay=delay;
		sendMessage(SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED);
	}

	public int getAnimEndDelay() {
		return animEndDelay;
	}

	public int getAnimEndDelayMax() {
		return animEndDelayMax;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setCutLeft(int cutLeft) {
		if(cutLeft==this.cutLeft) {
			return;
		}else if(cutLeft<cutLeftMin) {
			this.cutLeft = cutLeftMin;
		}else if(cutLeft>cutLeftMax) {
			this.cutLeft=cutLeftMax;
		}else {
			this.cutLeft = cutLeft;
		}
		cutRightMin=cutLeft+MINIMUM_ANIM_FRAMECOUNT;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}
	
	public int getCutLeft() {
		return cutLeft;
	}

	public void setCutRight(int cutRight) {
		if(cutRight==this.cutRight) {
			return;
		}else if(cutRight<cutRightMin) {
			this.cutRight = cutRightMin;
		}else if(cutRight>cutRightMax) {
			this.cutRight=cutRightMax;
		}else {
			this.cutRight = cutRight;
		}
		cutLeftMax=cutRight-MINIMUM_ANIM_FRAMECOUNT;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getCutRight() {
		return cutRight;
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
		cropRightMin=cropLeft+MINIMUM_ANIM_SIZE;
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
		cropLeftMax=cropRight-MINIMUM_ANIM_SIZE;
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
		cropBottomMin=cropTop+MINIMUM_ANIM_SIZE;
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
		cropTopMax=cropBottom-MINIMUM_ANIM_SIZE;
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
		sendMessage(SimpleEvents.MODEL_ANIMNUMBEROFIMAGESCHANGED);
	}

	public double getNumberOfImagesToSkip() {
		return numberOfImagesToSkip;
	}

	public void setBufferedImages(BufferedImage[] bufferedImages) {
		this.bufferedImages=bufferedImages;
		if(bufferedImages!=null) {
			this.imageWidth=bufferedImages[0].getWidth();
			this.imageHeight=bufferedImages[0].getHeight();
			cutLeft=1;
			cutLeftMin=1;
			cutLeftMax=bufferedImages.length-MINIMUM_ANIM_FRAMECOUNT;
			cutRight=bufferedImages.length;
			cutRightMin=1+MINIMUM_ANIM_FRAMECOUNT;
			cutRightMax=bufferedImages.length;
			cropLeft=0;
			cropLeftMin=0;
			cropLeftMax=this.imageWidth-MINIMUM_ANIM_SIZE;
			cropTop=0;
			cropTopMin=0;
			cropTopMax=this.imageHeight-MINIMUM_ANIM_SIZE;
			cropRight=this.imageWidth;
			cropRightMin=MINIMUM_ANIM_SIZE;
			cropRightMax=this.imageWidth;
			cropBottom=this.imageHeight;
			cropBottomMin=0;
			cropBottomMax=this.imageHeight-MINIMUM_ANIM_SIZE;
			resizePercent=100;
			resizeMin=1;
			resizeMax=101;
			if(imagesFps==0) {
				imagesFps=25;
			}
			if(animFps==0) {
				setAnimFps(imagesFps>>1);
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

	public byte[] getBytearrayAnim() {
		return bytearrayAnim;
	}

	public void setBytearrayAnim(byte[] bytearrayAnim) {
		this.bytearrayAnim = bytearrayAnim;
		sendMessage(SimpleEvents.MODEL_ANIMBYTEARRAYCHANGED);
	}

	public int getCutLeftMin() {
		return cutLeftMin;
	}

	public int getCutLeftMax() {
		return cutLeftMax;
	}

	public int getCutRightMin() {
		return cutRightMin;
	}

	public int getCutRightMax() {
		return cutRightMax;
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

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		if(this.imageType==imageType) {
			return;
		}
		this.imageType = imageType;
		sendMessage(SimpleEvents.MODEL_IMAGETYPECHANGED);
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
