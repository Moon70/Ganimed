package lunartools.ganimed.gui.loader.model;

import java.io.File;
import java.util.Observable;

import lunartools.ganimed.ErrorMessage;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.SimpleEvents;

public class LoaderModel extends Observable{
	private GanimedModel ganimedModel;
	private File fileImageFolder;
	private File fileSavedImageFolder;
	private int imagesFps;

	public LoaderModel(GanimedModel ganimedModel) {
		this.ganimedModel=ganimedModel;
		imagesFps=25;
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

	public void setImagesFps(int fps) {
		if(fps<1 || fps>60) {
			sendMessage(SimpleEvents.MODEL_REFRESH_SELECTION_GUI);
			sendMessage(new ErrorMessage("Image fps value out of range, choose a value from 1 to 60!"));
			return;
		}
		if(this.imagesFps==fps) {
			return;
		}
		this.imagesFps=fps;
		ganimedModel.calculateAnimPlaybackParameter();
	}

	/** The Frames Per Second of the source images */
	public int getImagesFps() {
		return imagesFps;
	}

}
