package lunartools.ganimed.gui.capture.service;

import java.awt.AWTException;
import java.awt.Rectangle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ganimed.gui.selection.model.ImageSelectionModel;

public class SelectScreenRegionThread extends Thread{
	private static Logger logger = LoggerFactory.getLogger(SelectScreenRegionThread.class);
	private ImageSelectionModel imageSelectionModel;

	public SelectScreenRegionThread(ImageSelectionModel imageSelectionModel) {
		this.imageSelectionModel=imageSelectionModel;
	}

	@Override
	public void run() {
		super.run();
		SelectCaptureRegionService selectCaptureRegionService=new SelectCaptureRegionService();
		try {
			selectCaptureRegionService.selectScreenRegion();
			Rectangle rectangleScreenRegion=selectCaptureRegionService.getSelectedScreenRegion();
			if(rectangleScreenRegion!=null && rectangleScreenRegion.width!=0 && rectangleScreenRegion.getHeight()!=0) {
				imageSelectionModel.setCaptureRegion(rectangleScreenRegion);
				imageSelectionModel.setGraphicsDevice(selectCaptureRegionService.getSelectedGraphicsDevice());
				logger.debug("Device: "+selectCaptureRegionService.getSelectedGraphicsDevice());
				logger.debug("Region: "+rectangleScreenRegion);
			}
		} catch (AWTException e) {
			logger.error("error while selecting capture region",e);
		}

	}

}
