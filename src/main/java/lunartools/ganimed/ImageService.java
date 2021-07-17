package lunartools.ganimed;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ganimed.gui.editor.model.EditorModel;
import lunartools.ganimed.panel.optionspanel.ImageType;
import lunartools.ganimed.worker.LoadImagesWorker;
import lunartools.ganimed.worker.SaveAnim;
import lunartools.ganimed.worker.SaveImagesWorker;
import lunartools.ganimed.worker.SaveRawImagesWorker;
import lunartools.progressdialog.ProgressDialog;

public class ImageService {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ImageService.class);
	private GanimedModel ganimedModel;
	private GanimedController ganimedController;
	private volatile Thread createAnimThread;

	public ImageService(GanimedModel ganimedModel,GanimedController ganimedController) {
		this.ganimedModel=ganimedModel;
		this.ganimedController=ganimedController;
	}

	public Dimension getResultImageSize() {
		int width=ganimedModel.getEditorModel().getCropRight()-ganimedModel.getEditorModel().getCropLeft()+1;
		int height=ganimedModel.getEditorModel().getCropBottom()-ganimedModel.getEditorModel().getCropTop()+1;
		int resizePercent=ganimedModel.getEditorModel().getResizePercent();
		if(resizePercent!=100) {
			width=width*resizePercent/100;
			height=height*resizePercent/100;
		}
		return new Dimension(width,height);
	}

	public BufferedImage getLogicalResultBufferedImage(int index) {
		Dimension resultImageSize=getResultImageSize();
		BufferedImage bufferedImageResult=new BufferedImage(resultImageSize.width,resultImageSize.height,BufferedImage.TYPE_INT_RGB);
		Graphics graphicsImage=bufferedImageResult.getGraphics();
		AnimationData animationData=ganimedModel.getAnimationData();
		BufferedImage bufferedImage=animationData.getLogicalImageData(index).getBufferedImage();
		graphicsImage.drawImage(bufferedImage,
				0, 0,
				resultImageSize.width,resultImageSize.height,
				ganimedModel.getEditorModel().getCropLeft()-1, ganimedModel.getEditorModel().getCropTop()-1,
				ganimedModel.getEditorModel().getCropRight(),ganimedModel.getEditorModel().getCropBottom(),
				null);
		return bufferedImageResult;
	}

	public void processImageFolder() {
		LoadImagesWorker worker=new LoadImagesWorker(ganimedModel,ganimedController);
		ProgressDialog.executeWithProgresssDialog(ganimedController.getJFrame() ,GanimedModel.PROGRAMNAME, "Load images",worker);
	}

	private volatile boolean createAnim_sleeping=false;
	private volatile long createAnim_sleepUntil=0;
	public void createAnim() {
		Thread thread=createAnimThread;
		if(thread!=null) {
			if(createAnim_sleeping && createAnim_sleepUntil+200>System.currentTimeMillis()) {
				createAnim_sleepUntil=System.currentTimeMillis()+5000;
				return;
			}
			thread.interrupt();	
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
		}
		createAnimThread=new Thread() {
			public void run() {
				try {
					createAnim_sleeping=true;
					createAnim_sleepUntil=System.currentTimeMillis()+5000;
					while(createAnim_sleepUntil>System.currentTimeMillis() || ganimedModel.getNumberOfImagesToSkip()==0) {
						if(ganimedController.isShutdownInProgress()) {
							return;
						}
						Thread.sleep(500);
					}
					createAnim_sleeping=false;
					ganimedModel.setBytearrayAnim(null);
					String imageTypename=ganimedModel.getImageType().getName();
					ganimedController.setStatusInfo("estimating "+imageTypename+" size in background task...");
					ganimedModel.setBytearrayAnim(createAnimByteArray(false,createIndexTable()));
				} catch (InterruptedException e1) {
					ganimedController.setStatusInfo("");
					return;
				}finally {
					createAnimThread=null;
				}
			};
		};
		//createAnimThread.start();
	}


	public Vector<Integer> createIndexTable() {
		Vector<Integer> vecIndexTable=new Vector<Integer>();
		EditorModel editorModel=ganimedModel.getEditorModel();
		for(double index=editorModel.getCutLeft()-1;(index+0.5)<editorModel.getCutRight();index+=ganimedModel.getNumberOfImagesToSkip()) {
			vecIndexTable.add((int)(index+0.5));
		}
		return vecIndexTable;
	}

	public byte[] createAnimByteArray(boolean showProgressBar,Vector<Integer> vecIndexTable) {
		ImageType imageType=ganimedModel.getImageType();
		try {
			if(showProgressBar) {
				ganimedController.enableProgressBar(vecIndexTable.size());
			}
			AnimCreator animCreator;
			if(imageType==ImageType.GIF) {
				animCreator=new GifAnimCreator(ganimedModel.getOptionsGifModel(),ganimedController);
			}else if(imageType==ImageType.PNG) {
				animCreator=new PngAnimCreator(ganimedModel.getOptionsPngModel(),ganimedController,null);
			}else {
				throw new RuntimeException("unknown image type: "+ganimedModel.getImageType());
			}
			for(int i=0;i<vecIndexTable.size();i++) {
				if(Thread.interrupted() || ganimedController.isShutdownInProgress()) {
					return null;
				}
				ImageData imageData=ganimedModel.getAnimationData().getLogicalImageData(vecIndexTable.get(i));
				if(i<vecIndexTable.size()-1) {
					animCreator.addImage(imageData, ganimedModel.getEditorModel().getAnimDelay(), false);
				}else {
					int endDelay=ganimedModel.getEditorModel().getAnimEndDelay();
					animCreator.addImage(imageData, endDelay>0?endDelay:ganimedModel.getEditorModel().getAnimDelay(), false);
				}
				if(Thread.currentThread().isInterrupted()) {
					ganimedController.disableProgressBar();
					return null;
				}
			}
			byte[] anim=animCreator.toByteArray();
			if(showProgressBar) {
				ganimedController.disableProgressBar();
			}
			return anim;
		} catch (Exception e) {
			ganimedController.setStatusError("Error creating "+imageType.getName()+": "+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public void saveAs() {
		String imageTypename=ganimedModel.getImageType().getName();
		SaveAnim worker=new SaveAnim(ganimedModel,ganimedController,this);
		ProgressDialog.executeWithProgresssDialog(ganimedController.getJFrame() ,GanimedModel.PROGRAMNAME, "Save "+imageTypename, worker);
	}

	public void saveFrames() {
		SaveImagesWorker worker=new SaveImagesWorker(ganimedModel,ganimedController,this);
		ProgressDialog.executeWithProgresssDialog(ganimedController.getJFrame() ,GanimedModel.PROGRAMNAME, "Save Frames", worker);
	}

	public void saveRawFrames() {
		SaveRawImagesWorker worker=new SaveRawImagesWorker(ganimedModel,ganimedController);
		ProgressDialog.executeWithProgresssDialog(ganimedController.getJFrame() ,GanimedModel.PROGRAMNAME, "Save Raw Frames", worker);
	}

}
