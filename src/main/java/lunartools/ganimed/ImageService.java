package lunartools.ganimed;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import lunartools.FileTools;
import lunartools.colorquantizer.GPAC_experimental;
import lunartools.ganimed.imagetype.ImageType;
import lunartools.ganimed.imagetype.ImageTypeApng;
import lunartools.ganimed.imagetype.ImageTypeGif;

public class ImageService {
	private GanimedModel model;
	private GanimedController controller;
	private volatile Thread processImageFolderThread;
	private volatile Thread createAnimThread;

	public ImageService(GanimedModel model,GanimedController controller) {
		this.model=model;
		this.controller=controller;
	}

	public Dimension getResultImageSize() {
		int width=model.getCropRight()-model.getCropLeft();
		int height=model.getCropBottom()-model.getCropTop();
		int resizePercent=model.getResizePercent();
		if(resizePercent!=100) {
			width=width*resizePercent/100;
			height=height*resizePercent/100;
		}
		return new Dimension(width,height);
	}

	public BufferedImage getResultBufferedImage(int index) {
		Dimension resultImageSize=getResultImageSize();
		BufferedImage bufferedImage=new BufferedImage(resultImageSize.width,resultImageSize.height,BufferedImage.TYPE_INT_RGB);
		Graphics graphicsImage=bufferedImage.getGraphics();
		graphicsImage.drawImage(model.getBufferedImages()[index],
				0, 0,
				resultImageSize.width,resultImageSize.height,
				model.getCropLeft(), model.getCropTop(),
				model.getCropRight(),model.getCropBottom(),
				null);
		return bufferedImage;
	}

	public void processImageFolder() {
		if(processImageFolderThread!=null && processImageFolderThread.isAlive()) {
			processImageFolderThread.interrupt();
		}
		processImageFolderThread=new Thread() {
			public void run() {
				model.setBufferedImages(null);
				File fileFolder=model.getImageFolder();
				File[] files=FileTools.listFilesSorted(fileFolder);
				controller.enableProgressBar(files.length);
				BufferedImage[] bufferedImages=new BufferedImage[files.length];
				int imageWidth=0;
				int imageHeight=0;
				for(int i=0;i<files.length;i++) {
					if(isInterrupted() || controller.isShutdownInProgress()) {
						return;
					}
					controller.setProgressBarValue(i,"Reading: "+files[i]);
					try {
						bufferedImages[i]=ImageIO.read(files[i]);
						if(imageWidth<bufferedImages[i].getWidth()) {
							imageWidth=bufferedImages[i].getWidth();
						}
						if(imageHeight<bufferedImages[i].getHeight()) {
							imageHeight=bufferedImages[i].getHeight();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				controller.disableProgressBar();
				controller.setStatusInfo("READY, "+files.length+" files processed");
				model.setBufferedImages(bufferedImages);
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
			for(double index=0;(index+0.5)<model.getBufferedImages().length;index+=model.getNumberOfImagesToSkip()) {
				vecIndexTable.add((int)(index+0.5));
			}
			if(showProgressBar) {
				controller.enableProgressBar(vecIndexTable.size());
			}
			AnimCreator animCreator;
			if(imageType instanceof ImageTypeGif) {
				animCreator=new GifAnimCreator();
			}else if(model.getImageType() instanceof ImageTypeApng) {
				animCreator=new PngAnimCreator();
			}else {
				throw new RuntimeException("unknown image type: "+model.getImageType());
			}
			for(int i=0;i<vecIndexTable.size();i++) {
				if(showProgressBar) {
					controller.setProgressBarValue(i,"creating "+imageType.getName()+"...");
				}
				if(Thread.interrupted() || controller.isShutdownInProgress()) {
					return null;
				}
				BufferedImage bufferedImage=getResultBufferedImage(vecIndexTable.get(i));
				new GPAC_experimental().quantizeColors(bufferedImage,256);
				if(i<vecIndexTable.size()-1) {
					animCreator.addImage(bufferedImage, model.getAnimDelay(), false);
				}else {
					animCreator.addImage(bufferedImage, model.getAnimEndDelay(), false);
				}
				if(Thread.currentThread().isInterrupted()) {
					controller.disableProgressBar();
					return null;
				}
			}
			if(showProgressBar) {
				controller.disableProgressBar();
			}
			return animCreator.toByteArray();
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
