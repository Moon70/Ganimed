package lunartools.ganimed;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import lunartools.Settings;
import lunartools.ganimed.gui.AnimationThread;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.SimpleEvents;

public class GanimedController implements Observer{
	private static final String SETTING__GANIMED_VIEW_LOCATION = "GanimedViewLocation";
	private static final String SETTING__IMAGE_FOLDER = "ImageFolder";
	private static final String SETTING__GIF_FOLDER = "GifFolder";
	private GanimedModel model;
	private GanimedView view;
	private ImageService imageService;
	private AnimationThread threadAnimation;
	private Settings settings;
	private volatile boolean shutdownInProgress;
	
	public GanimedController(){
		settings=new Settings(GanimedModel.PROGRAMNAME,GanimedModel.determineProgramVersion());
		model=new GanimedModel();
		model.setLastImageFolder(settings.getString(SETTING__IMAGE_FOLDER));
		model.setGifFolder(settings.getString(SETTING__GIF_FOLDER));
		model.addObserver(this);
		view=new GanimedView(model);
		view.setLocation(settings.getPoint(SETTING__GANIMED_VIEW_LOCATION, view.getLocation()));
		view.addObserver(this);
		imageService=new ImageService(model,this);
	}

	public void openGUI(){
		view.setVisible(true);
		threadAnimation=new AnimationThread(model,view,imageService);
		threadAnimation.start();
	}
	
	@Override
	public void update(Observable observable, Object object) {
		if(object==SimpleEvents.EXIT) {
			exit();
		}else if(object==SimpleEvents.SAVEGIF) {
			imageService.saveGif();
		}else if(object==SimpleEvents.MODEL_IMAGEFOLDERCHANGED) {
			imageService.processImageFolder();
		}else if(object==SimpleEvents.MODEL_IMAGESCHANGED) {
			imageService.createGif();
		}else if(object==SimpleEvents.MODEL_GIFNUMBEROFIMAGESCHANGED) {
			imageService.createGif();
		}else if(object==SimpleEvents.MODEL_IMAGESIZECHANGED) {
			if(model.getImageFolder()!=null) {
				imageService.createGif();
			}
		}else if(object==SimpleEvents.MODEL_GIFBYTEARRAYCHANGED) {
			if(model.getBytearrayGif()!=null) {
				Dimension resultImageSize=imageService.getResultImageSize();
				int gifSizeInMb=model.getBytearrayGif().length*10/1024/1024;
				setStatusInfo("GIF size: "+resultImageSize.width+" x "+resultImageSize.height+", "+((double)gifSizeInMb)/10+" mb");	
			}
		}
	}
	
	public void setStatusInfo(String message) {
		view.setStatusInfo(message);
	}

	public void setStatusError(String message) {
		view.setStatusError(message);
	}

	public void setProgressBarValue(int value) {
		view.setProgressBarValue(value);
	}
	
	public void setProgressBarValue(int value, String message) {
		view.setProgressBarValue(value,message);
	}

	public void enableProgressBar(int steps) {
		view.enableProgressBar(steps);
	}

	public void disableProgressBar() {
		view.disableProgressBar();
	}
	
	private void exit() {
		shutdownInProgress=true;
		settings.setPoint(SETTING__GANIMED_VIEW_LOCATION, view.getLocation());
		File file=model.getImageFolder();
		if(file!=null) {
			settings.set(SETTING__IMAGE_FOLDER, file.getAbsolutePath());
		}
		file=model.getGifFolder();
		if(file!=null) {
			settings.set(SETTING__GIF_FOLDER, file.getAbsolutePath());
		}

		try {
			settings.saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(threadAnimation!=null && threadAnimation.isAlive()) {
			threadAnimation.shutdown();
			try {
				threadAnimation.join(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		view.setVisible(false);
		view.dispose();
	}

	public boolean isShutdownInProgress() {
		return shutdownInProgress;
	}
	
}
