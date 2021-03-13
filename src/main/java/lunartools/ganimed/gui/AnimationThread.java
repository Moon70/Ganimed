package lunartools.ganimed.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.ImageService;

public class AnimationThread extends Thread{
	private GanimedModel model;
	private GanimedView view;
	private ImageService imageService;
	private volatile boolean shutdown;
	
	public AnimationThread(GanimedModel model,GanimedView view,ImageService imageService) {
		this.model=model;
		this.view=view;
		this.imageService=imageService;
	}
	
	@Override
	public void run() {
		double index=0;
		BufferedImage[] bufferedImages;
		long timestamp;
		long sleep;
		Dimension lastResultImageSize=null;
		Dimension resultImageSize;
		int offsetY=240;
		while(!shutdown){
			timestamp=System.currentTimeMillis();
			bufferedImages=model.getBufferedImages();
			if(bufferedImages!=null) {
				resultImageSize=imageService.getResultImageSize();
				int offsetX=(view.getWidth()-resultImageSize.width)>>1;
				
				Graphics graphics=view.getGraphics();
				if(lastResultImageSize==null || lastResultImageSize.width!=resultImageSize.width ||lastResultImageSize.height!=resultImageSize.height) {
					graphics.setColor(view.getBackground());
					graphics.fillRect(0,offsetY,view.getWidth(),view.getHeight()-offsetY);
					lastResultImageSize=resultImageSize;
				}
				BufferedImage bufferedImage=imageService.getResultBufferedImage((int)index);
				graphics.drawImage(bufferedImage,offsetX, offsetY,null);
				index+=model.getNumberOfImagesToSkip();
				if(index>=bufferedImages.length) {
					index=0;
				}
			}else {
				index=0;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					shutdown();
				}
			}
			try {
				sleep=timestamp-System.currentTimeMillis()+model.getGifDelay();
				if(index==0) {
					sleep+=model.getGifEndDelay();
				}
				if(sleep>0) {
					Thread.sleep(sleep);
				}
			} catch (InterruptedException e) {
				shutdown();
			}
		}
	}

	public void shutdown() {
		shutdown=true;
	}
	
}
