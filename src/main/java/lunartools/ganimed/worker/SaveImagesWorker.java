package lunartools.ganimed.worker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ganimed.AnimationData;
import lunartools.ganimed.GanimedController;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.ImageService;
import lunartools.progressdialog.ProgressDialog;

public class SaveImagesWorker extends SwingWorker<Void, Void> {
	private static Logger logger = LoggerFactory.getLogger(SaveImagesWorker.class);
	private GanimedModel ganimedModel;
	private GanimedController ganimedController;
	private ImageService imageService;

	public SaveImagesWorker(GanimedModel ganimedModel, GanimedController ganimedController,ImageService imageService) {
		this.ganimedModel=ganimedModel;
		this.ganimedController=ganimedController;
		this.imageService=imageService;
	}

	@Override
	public Void doInBackground() {
		try {
			logger.debug("saving images...");
			File fileImages=ganimedModel.getImagesFile();
			AnimationData animationdata=ganimedModel.getAnimationData();
			Vector<Integer> vecIndexTable=imageService.createIndexTable();
			int size=vecIndexTable.size();
			int progressX=0;
			int progressStep=6400/size;
			setProgress(0);
			File[] files=new File[size];
			String filename=fileImages.getName();
			if(filename.toLowerCase().endsWith(".png")) {
				filename=filename.substring(0, filename.length()-4);
			}
			filename+="_";
			for(int i=0;i<size;i++) {
				String index="0000"+(i+1);
				index=index.substring(index.length()-4);
				files[i]=new File(fileImages.getParentFile(),filename+index+".png");
			}

			for(int i=0;i<size;i++) {
				if(isCancelled() || ganimedController.isShutdownInProgress()) {
					return null;
				}
				firePropertyChange(ProgressDialog.PROPERTY_BAR1, null, "saving image "+(i+1)+" / "+size);
				setProgress((progressX+=progressStep)>>6);
				BufferedImage bufferedImage=animationdata.getLogicalImageData(vecIndexTable.get(i)).getResultBufferedImage();
				try {
					ImageIO.write(bufferedImage, "PNG", files[i]);
				} catch (IOException e) {
					logger.error("error while writing image "+i+": "+files[i],e);
					ganimedController.setStatusError(e.getMessage());
					return null;
				}
			}
		} catch (Exception e) {
			logger.error("error while saving images",e);
			ganimedController.setStatusError("error while saving images: "+e.getMessage());
		}
		return null;
	}

	@Override
	public void done() {
		logger.debug("saving images done");
	}

}
