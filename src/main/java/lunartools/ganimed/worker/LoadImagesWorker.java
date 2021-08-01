package lunartools.ganimed.worker;

import java.io.File;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.FileTools;
import lunartools.ganimed.AnimationData;
import lunartools.ganimed.GanimedController;
import lunartools.ganimed.GanimedModel;
import lunartools.progressdialog.ProgressDialog;

public class LoadImagesWorker extends SwingWorker<Void, Void> {
	private static Logger logger = LoggerFactory.getLogger(LoadImagesWorker.class);
	private GanimedModel ganimedModel;
	private GanimedController ganimedController;

	public LoadImagesWorker(GanimedModel ganimedModel, GanimedController ganimedController) {
		this.ganimedModel=ganimedModel;
		this.ganimedController=ganimedController;
	}

	@Override
	public Void doInBackground() {
		try {
			logger.debug("loading images...");
			ganimedModel.setImageData(null);
			File fileFolder=ganimedModel.getImageSelectionModel().getImageFolder();
			File[] fileImages=FileTools.listFilesSorted(fileFolder);
			int size=fileImages.length;
			float progressX=0;
			float progressStep=100.0f/size;

			setProgress(0);
			AnimationData animationData=new AnimationData(ganimedModel);
			for(int i=0;i<fileImages.length;i++) {
				if(isCancelled() || ganimedController.isShutdownInProgress()) {
					return null;
				}
				firePropertyChange(ProgressDialog.PROPERTY_LINE2, null,fileImages[i]);
				setProgress((int)(progressX+=progressStep));
				try {
					animationData.addFile(fileImages[i]);
				} catch (Exception e) {
					logger.error("error while processing image "+i+": "+fileImages[i],e);
					ganimedController.setStatusError(e.getMessage());
					return null;
				}
			}
			ganimedController.setStatusInfo("READY, "+fileImages.length+" files processed");
			ganimedModel.setImageData(animationData);
		} catch (Exception e) {
			logger.error("error while loading images",e);
			ganimedController.setStatusError("error while loading images: "+e.getMessage());
		}
		return null;
	}

	@Override
	public void done() {
		logger.debug("loading images done");	}

}
