package lunartools.ganimed.gui.selection.model;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.io.File;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.SimpleEvents;

public class ImageSelectionModel extends Observable{
	private static Logger logger = LoggerFactory.getLogger(ImageSelectionModel.class);
	private GanimedModel ganimedModel;
	private File fileImageFolder;
	private File fileSavedImageFolder;
	private int imagesFps;
	private Rectangle captureRegion;
	private GraphicsDevice graphicsDevice;

	public ImageSelectionModel(GanimedModel ganimedModel) {
		this.ganimedModel=ganimedModel;
		this.imagesFps=12;
	}

	public void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public void setImageFolder(File file) {
		this.fileImageFolder=file;
		sendMessage(SimpleEvents.MODEL_IMAGEFOLDERCHANGED);
	}

	public File getImageFolder() {
		return fileImageFolder;
	}

	public void setSavedImageFolder(String path) {
		if(path!=null) {
			this.fileSavedImageFolder=new File(path);
		}
	}

	/** @return the image folder that was saved when exiting the last session */
	public File getSavedImageFolder() {
		return fileSavedImageFolder;
	}

	public void setImagesFps(int captureFps) {
		if(this.imagesFps==captureFps) {
			return;
		}
		if(captureFps<getImagesFpsMin()) {
			captureFps=getImagesFpsMin();
		}else if(captureFps>getImagesFpsMax()) {
			captureFps=getImagesFpsMax();
		}
		this.imagesFps=captureFps;
		sendMessage(SimpleEvents.MODEL_REFRESH_SELECTION_GUI);
	}

	public int getImagesFps() {
		return imagesFps;
	}

	public int getImagesFpsMin() {
		return 1;
	}

	public int getImagesFpsMax() {
		return 25;
	}

	public void setCaptureRegion(Rectangle captureRegion) {
		logger.trace("CaptureRegion: "+captureRegion);
		this.captureRegion=captureRegion;
		sendMessage(SimpleEvents.MODEL_REFRESH_SELECTION_GUI);
	}

	public Rectangle getCaptureRegion() {
		return captureRegion;
	}

	public void setGraphicsDevice(GraphicsDevice graphicsDevice) {
		this.graphicsDevice=graphicsDevice;
	}

	public GraphicsDevice getGraphicsDevice() {
		return graphicsDevice;
	}

}
