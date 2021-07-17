package lunartools.ganimed.gui.capture.service;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ganimed.AnimationData;
import lunartools.ganimed.GanimedController;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.selection.model.ImageSelectionModel;

public class ScreenCaptureThread extends Thread{
	private static Logger logger = LoggerFactory.getLogger(ScreenCaptureThread.class);
	private GanimedModel ganimedModel;
	private GanimedController ganimedController;
	private volatile boolean enableScreenCapture;

	public ScreenCaptureThread(GanimedModel ganimedModel, GanimedController ganimedController) {
		this.ganimedModel=ganimedModel;
		this.ganimedController=ganimedController;
	}	

	@Override
	public void run() {
		super.run();
		try {
			ImageSelectionModel imageSelectionModel=ganimedModel.getImageSelectionModel();
			Rectangle captureRegion=imageSelectionModel.getCaptureRegion();
			if(captureRegion==null) {
				logger.trace("capture region is null");
				return;
			}
			int maxMemory=(int)(Runtime.getRuntime().maxMemory()>>20);
			int currentMemory;
			forceGarbageCollector();
			
			int breakMemory;
			breakMemory=maxMemory*80/100;
			logger.trace("maxMemory: "+maxMemory);
			logger.trace("breakMemory: "+breakMemory);
			ganimedController.enableProgressBar(breakMemory);

			int fps=imageSelectionModel.getImagesFps();
			logger.trace("FPS: "+fps);
			int delay=1000/fps;
			logger.trace("Delay: "+delay);

			GraphicsDevice graphicsDevice=imageSelectionModel.getGraphicsDevice();
			logger.trace("capture device: "+graphicsDevice);
			logger.trace("capture region: "+captureRegion);
			Robot robot=new Robot(graphicsDevice);

			BufferedImage bufferedImage;
			enableScreenCapture=true;
			AnimationData animationData=new AnimationData(ganimedModel,true);
			while(enableScreenCapture && !ganimedController.isShutdownInProgress()) {
				long timestamp=System.currentTimeMillis();
				currentMemory=(int)((Runtime.getRuntime().totalMemory()>>20)-(Runtime.getRuntime().freeMemory()>>20));
					if(currentMemory>breakMemory) {
						logger.warn("screen capturing stopped, less than "+breakMemory+" MB free");
						stopScreenCapture();
						ganimedController.setStatusError("screen capturing aborted, please allocate more memory");
						return;
					}
				ganimedController.setProgressBarValue(currentMemory, "memory: "+currentMemory+" MB");
				bufferedImage = robot.createScreenCapture(captureRegion);
				animationData.addImage(bufferedImage);
				try {
					int sleep=delay-(int)(System.currentTimeMillis()-timestamp);
					if(sleep>0) {
						Thread.sleep(sleep);
					}
				} catch (InterruptedException e) {
					logger.debug("screen capturing interrupted");
					stopScreenCapture();
				}
			}
			ganimedModel.setImageData(animationData);
			ganimedModel.getEditorModel().setAnimFps(fps);
			int numberOfImagesCaptured=animationData.logicalSize();
			ganimedController.setStatusInfo((numberOfImagesCaptured/fps)+" seconds ("+numberOfImagesCaptured+" images) captured");
		} catch (Exception e) {
			logger.error("error while screen capturing",e);
			stopScreenCapture();
		}
	}

	public void stopScreenCapture() {
		enableScreenCapture=false;
		ganimedController.disableProgressBar();
		logger.debug("stopScreenCapture");
	}

	@SuppressWarnings("unused")
	public static void forceGarbageCollector() {
		try {
			long maxMem=Runtime.getRuntime().maxMemory()>>2;
			if((maxMem)>Integer.MAX_VALUE) {
				int[][] temp=new int[1+(int)(maxMem/Integer.MAX_VALUE)][Integer.MAX_VALUE-8];
			}else {
				int[] temp=new int[(int)maxMem];
			}
		} catch (OutOfMemoryError e) {}
	}
	
}
