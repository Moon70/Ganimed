package lunartools.ganimed.worker;

import java.util.Vector;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.FileTools;
import lunartools.apng.ProgressCallback;
import lunartools.ganimed.AnimCreator;
import lunartools.ganimed.GanimedController;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.GifAnimCreator;
import lunartools.ganimed.ImageData;
import lunartools.ganimed.ImageService;
import lunartools.ganimed.PngAnimCreator;
import lunartools.ganimed.panel.optionspanel.ImageType;

public class SaveAnim extends SwingWorker<Void, Void> implements ProgressCallback{
	private static Logger logger = LoggerFactory.getLogger(SaveAnim.class);
	private GanimedModel ganimedModel;
	private GanimedController ganimedController;
	private ImageService imageService;
	private int progressX;
	private int progressStep;

	public SaveAnim(GanimedModel ganimedModel, GanimedController ganimedController,ImageService imageService) {
		this.ganimedModel=ganimedModel;
		this.ganimedController=ganimedController;
		this.imageService=imageService;
	}

	@Override
	public Void doInBackground() {
		logger.debug("saving animation...");
		ImageType imageType=ganimedModel.getImageType();
		String imageTypename=imageType.getName();
		try {
			Vector<Integer> vecIndexTable=imageService.createIndexTable();
			int size=vecIndexTable.size();
			progressX=0;
			progressStep=6400/size;
			setProgress(0);
			AnimCreator animCreator;
			//ToDo: A factory should create the AnimCreator. Before that, both GifAnimCreator and PngAnimCreator should use a Listener to monitor progress.
			if(imageType==ImageType.GIF) {
				animCreator=new GifAnimCreator(ganimedModel.getOptionsGifModel(),ganimedController);
			}else if(imageType==ImageType.PNG) {
				animCreator=new PngAnimCreator(ganimedModel.getOptionsPngModel(),ganimedController,this);
			}else {
				throw new RuntimeException("unknown image type: "+ganimedModel.getImageType());
			}
			for(int i=0;i<size;i++) {
				if(isCancelled() || ganimedController.isShutdownInProgress()) {
					return null;
				}
				if(imageType==ImageType.GIF) {
					setProgress((progressX+=progressStep)>>6);
				}
				ImageData imageData=ganimedModel.getAnimationData().getLogicalImageData(vecIndexTable.get(i));
				if(i<size-1) {
					animCreator.addImage(imageData, ganimedModel.getEditorModel().getAnimDelay(), false);
				}else {
					int endDelay=ganimedModel.getEditorModel().getAnimEndDelay();
					animCreator.addImage(imageData, endDelay>0?endDelay:ganimedModel.getEditorModel().getAnimDelay(), false);
				}
			}

			byte[] data=animCreator.toByteArray();
			FileTools.writeFile(ganimedModel.getAnimFile(), data);
			ganimedController.setStatusInfo(imageTypename+" file saved: "+ganimedModel.getAnimFile());

		} catch (Exception e) {
			logger.error("error while saving animation",e);
			ganimedController.setStatusError("error while saving animation"+e.getMessage());
		}
		return null;
	}

	@Override
	public void done() {
		logger.debug("saving animation done");
	}

	@Override
	public void setProgressStep(int step) {
		setProgress((progressX+=progressStep)>>6);
	}

}
