package lunartools.ganimed;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.Settings;
import lunartools.ganimed.gui.AnimationThread;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.SimpleEvents;
import lunartools.ganimed.gui.capture.service.ScreenCaptureThread;
import lunartools.ganimed.gui.capture.service.SelectScreenRegionThread;

public class GanimedController implements Observer{
	private static Logger logger = LoggerFactory.getLogger(GanimedController.class);
	private static final String SETTING__GANIMED_VIEW_LOCATION = "GanimedViewLocation";
	private static final String SETTING__IMAGE_FOLDER = "ImageFolder";
	private static final String SETTING__ANIM_FOLDER = "AnimFolder";
	private static final String SETTING__RAWIMAGE_FILE = "RawImageFile";
	private static final String SETTING__IMAGE_FILE = "ImageFile";
	private GanimedModel model;
	private GanimedView view;
	private ImageService imageService;
	private AnimationThread threadAnimation;
	private Settings settings;
	private volatile boolean shutdownInProgress;
	private SelectScreenRegionThread selectScreenRegionThread;
	private ScreenCaptureThread screenCaptureThread;
	
	public GanimedController(){
		settings=new Settings(GanimedModel.PROGRAMNAME,GanimedModel.determineProgramVersion());
		model=new GanimedModel();
		model.getImageSelectionModel().setSavedImageFolder(settings.getString(SETTING__IMAGE_FOLDER));
		model.setAnimFolder(settings.getString(SETTING__ANIM_FOLDER));
		model.addObserver(this);
		model.getImageSelectionModel().addObserver(this);
		String path=settings.getString(SETTING__RAWIMAGE_FILE);
		if(path!=null && path.length()>0) {
			model.setRawImagesFile(new File(path));
		}
		path=settings.getString(SETTING__IMAGE_FILE);
		if(path!=null && path.length()>0) {
			model.setImagesFile(new File(path));
		}
		view=new GanimedView(model);
		view.setLocation(settings.getPoint(SETTING__GANIMED_VIEW_LOCATION, view.getLocation()));
		view.addObserver(this);
		view.getImageSelectionView().addObserver(this);
		imageService=new ImageService(model,this);
	}

	public void openGUI(){
		view.setVisible(true);
		threadAnimation=new AnimationThread(model,view,imageService);
		threadAnimation.start();
		view.setStatusInfo("READY, select an image folder or capture images from screen");
	}

	@Override
	public void update(Observable observable, Object object) {
		if(object==SimpleEvents.EXIT) {
			exit();
		}else if(object==SimpleEvents.SAVEAS) {
			imageService.saveAs();
		}else if(object==SimpleEvents.VIEW_SAVEFRAMES) {
			imageService.saveFrames();
		}else if(object==SimpleEvents.VIEW_SAVERAWFRAMES) {
			imageService.saveRawFrames();
		}else if(object==SimpleEvents.MODEL_IMAGEFOLDERCHANGED) {
			imageService.processImageFolder();
		}else if(object==SimpleEvents.MODEL_IMAGESCHANGED) {
			imageService.createAnim();
		}else if(object==SimpleEvents.MODEL_ANIMNUMBEROFIMAGESCHANGED) {
			imageService.createAnim();
		}else if(object==SimpleEvents.MODEL_IMAGESIZECHANGED) {
			if(model.getImageSelectionModel().getImageFolder()!=null) {
				imageService.createAnim();
			}
		}else if(object==SimpleEvents.MODEL_ANIMBYTEARRAYCHANGED) {
			if(model.getBytearrayAnim()!=null) {
				Dimension resultImageSize=imageService.getResultImageSize();
				int animSizeInMb=model.getBytearrayAnim().length*10/1024/1024;
				String imageTypename=model.getImageType().getName();
				setStatusInfo(imageTypename+" size: "+resultImageSize.width+" x "+resultImageSize.height+", "+((double)animSizeInMb)/10+" mb");	
			}
		}else if(object==SimpleEvents.SELECT_CAPTURE_REGION) {
			selectCaptureRegion();
		}else if(object==SimpleEvents.START_CAPTURE) {
			startScreenCapture();
		}else if(object==SimpleEvents.STOP_CAPTURE) {
			stopScreenCapture();
		}else if(object instanceof ErrorMessage) {
			setStatusError(((ErrorMessage)object).getMessage());
		}else {
			logger.trace("ignored message: "+object);
		}
	}

	private void selectCaptureRegion() {
		if(selectScreenRegionThread!=null && selectScreenRegionThread.isAlive()) {
			logger.warn("screencapture selection thread already running!");
			return;
		}
		selectScreenRegionThread=new SelectScreenRegionThread(model.getImageSelectionModel());
		selectScreenRegionThread.start();
	}
	
	private void startScreenCapture() {
		if(screenCaptureThread!=null && screenCaptureThread.isAlive()) {
			logger.warn("screencapture thread already running!");
			return;
		}
		screenCaptureThread=new ScreenCaptureThread(model,this);
		screenCaptureThread.start();
	}

	private void stopScreenCapture() {
		screenCaptureThread.stopScreenCapture();
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
		File file=model.getImageSelectionModel().getImageFolder();
		if(file!=null) {
			settings.set(SETTING__IMAGE_FOLDER, file.getAbsolutePath());
		}
		file=model.getAnimFolder();
		if(file!=null) {
			settings.set(SETTING__ANIM_FOLDER, file.getAbsolutePath());
		}
		file=model.getRawImagesFile();
		if(file!=null) {
			settings.set(SETTING__RAWIMAGE_FILE, file.getAbsolutePath());
		}
		file=model.getImagesFile();
		if(file!=null) {
			settings.set(SETTING__IMAGE_FILE, file.getAbsolutePath());
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
	

	public JFrame getJFrame() {
		return view;
	}

}
