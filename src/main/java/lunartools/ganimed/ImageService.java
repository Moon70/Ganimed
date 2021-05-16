package lunartools.ganimed;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.FileTools;
import lunartools.colorquantizer.GPAC_experimental;
import lunartools.ganimed.panel.optionspanel.ImageType;

public class ImageService {
	private static Logger logger = LoggerFactory.getLogger(ImageService.class);
	private GanimedModel model;
	private GanimedController controller;
	private volatile Thread processImageFolderThread;
	private volatile Thread createAnimThread;

	public ImageService(GanimedModel model,GanimedController controller) {
		this.model=model;
		this.controller=controller;
	}

	public Dimension getResultImageSize() {
		int width=model.getEditorModel().getCropRight()-model.getEditorModel().getCropLeft();
		int height=model.getEditorModel().getCropBottom()-model.getEditorModel().getCropTop();
		int resizePercent=model.getEditorModel().getResizePercent();
		if(resizePercent!=100) {
			width=width*resizePercent/100;
			height=height*resizePercent/100;
		}
		return new Dimension(width,height);
	}

	@Deprecated
	public BufferedImage getResultBufferedImage(int index){
		Dimension resultImageSize=getResultImageSize();
		BufferedImage bufferedImage=new BufferedImage(resultImageSize.width,resultImageSize.height,BufferedImage.TYPE_INT_RGB);
		Graphics graphicsImage=bufferedImage.getGraphics();
		graphicsImage.drawImage(model.getImageDataArray()[index].getBufferedImage(),
				0, 0,
				resultImageSize.width,resultImageSize.height,
				model.getEditorModel().getCropLeft(), model.getEditorModel().getCropTop(),
				model.getEditorModel().getCropRight(),model.getEditorModel().getCropBottom(),
				null);
		return bufferedImage;
	}

	public void processImageFolder() {
		if(processImageFolderThread!=null && processImageFolderThread.isAlive()) {
			processImageFolderThread.interrupt();
		}
		processImageFolderThread=new Thread() {
			public void run() {
				model.setImageData(null);
				File fileFolder=model.getLoaderModel().getImageFolder();
				File[] files=FileTools.listFilesSorted(fileFolder);
				controller.enableProgressBar(files.length);
				AnimationData animationData=new AnimationData(model);
				for(int i=0;i<files.length;i++) {
					if(isInterrupted() || controller.isShutdownInProgress()) {
						return;
					}
					controller.setProgressBarValue(i,"Reading: "+files[i]);
					try {
						animationData.addFile(files[i]);
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
						controller.setStatusError(e.getMessage());
						controller.disableProgressBar();
						return;
//					} catch (IOException e) {
//						e.printStackTrace();
					}
				}
				controller.disableProgressBar();
				controller.setStatusInfo("READY, "+files.length+" files processed");
				model.setImageData(animationData);
			};
		};
		processImageFolderThread.start();
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
					while(createAnim_sleepUntil>System.currentTimeMillis() || model.getNumberOfImagesToSkip()==0) {
						if(controller.isShutdownInProgress()) {
							return;
						}
						Thread.sleep(500);
					}
					createAnim_sleeping=false;
					model.setBytearrayAnim(null);
					String imageTypename=model.getImageType().getName();
					controller.setStatusInfo("estimating "+imageTypename+" size in background task...");
					model.setBytearrayAnim(createAnimByteArray(false));
				} catch (InterruptedException e1) {
					controller.setStatusInfo("");
					return;
				}finally {
					createAnimThread=null;
				}
			};
		};
		createAnimThread.start();
	}

	public byte[] createAnimByteArray(boolean showProgressBar) {
		ImageType imageType=model.getImageType();
		try {
			Vector<Integer> vecIndexTable=new Vector<Integer>();
			for(double index=model.getEditorModel().getCutLeft();(index+0.5)<model.getEditorModel().getCutRight();index+=model.getNumberOfImagesToSkip()) {
				vecIndexTable.add((int)(index+0.5));
			}
			if(showProgressBar) {
				controller.enableProgressBar(vecIndexTable.size());
			}
			AnimCreator animCreator;
			if(imageType==ImageType.GIF) {
				animCreator=new GifAnimCreator(controller);
			}else if(imageType==ImageType.PNG) {
				animCreator=new PngAnimCreator(model.getOptionsPngModel(),controller);
			}else {
				throw new RuntimeException("unknown image type: "+model.getImageType());
			}
			for(int i=0;i<vecIndexTable.size();i++) {
				if(Thread.interrupted() || controller.isShutdownInProgress()) {
					return null;
				}
				//BufferedImage bufferedImage=getResultBufferedImage(vecIndexTable.get(i));
				ImageData imageData=model.getAnimationData().getImageData(vecIndexTable.get(i));
//				if(imageType instanceof ImageTypeGif) {
//					new GPAC_experimental().quantizeColors(bufferedImage,256);
//				}
				if(i<vecIndexTable.size()-1) {
					animCreator.addImage(imageData, model.getEditorModel().getAnimDelay(), false);
				}else {
					int endDelay=model.getEditorModel().getAnimEndDelay();
					animCreator.addImage(imageData, endDelay>0?endDelay:model.getEditorModel().getAnimDelay(), false);
				}
				if(Thread.currentThread().isInterrupted()) {
					controller.disableProgressBar();
					return null;
				}
			}
			byte[] anim=animCreator.toByteArray();
			if(showProgressBar) {
				controller.disableProgressBar();
			}
			return anim;
		} catch (Exception e) {
			controller.setStatusError("Error creating "+imageType.getName()+": "+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public void saveAs() {
		Thread threadSave=new Thread() {
			public void run() {
				String imageTypename=model.getImageType().getName();
				try {
					byte[] data=createAnimByteArray(true);
					FileTools.writeFile(model.getAnimFile(), data);
					controller.setStatusInfo(imageTypename+" file saved: "+model.getAnimFile());
				} catch (Exception e) {
					controller.setStatusError("ERROR creating/saving "+imageTypename+" file: "+e.getMessage());
					e.printStackTrace();
				}
			}
		};

		threadSave.setDaemon(true);
		threadSave.start();
	}

}
