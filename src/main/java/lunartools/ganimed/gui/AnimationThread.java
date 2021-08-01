package lunartools.ganimed.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ganimed.AnimationData;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.ImageService;

public class AnimationThread extends Thread{
	private static Logger logger = LoggerFactory.getLogger(AnimationThread.class);
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private ImageService imageService;
	private volatile boolean shutdown;

	public AnimationThread(GanimedModel ganimedModel,GanimedView ganimedView,ImageService imageService) {
		this.ganimedModel=ganimedModel;
		this.ganimedView=ganimedView;
		this.imageService=imageService;
	}

	@Override
	public void run() {
		double index=0;
		long timestamp;
		long sleep;
		Dimension lastResultImageSize=null;
		Dimension resultImageSize;
		int offsetY=240+30;
		int cutLeft;
		int cutRight;
		AnimationData animationData;
		while(!shutdown){
			timestamp=System.currentTimeMillis();
			animationData=ganimedModel.getAnimationData();
			cutLeft=ganimedModel.getEditorModel().getCutLeft()-1;
			cutRight=ganimedModel.getEditorModel().getCutRight()-1;
			Graphics graphics=ganimedView.getGraphics();
			if(animationData!=null) {
				resultImageSize=imageService.getResultImageSize();
				int offsetX=(ganimedView.getWidth()-resultImageSize.width)>>1;

				if(lastResultImageSize==null || lastResultImageSize.width!=resultImageSize.width ||lastResultImageSize.height!=resultImageSize.height) {
					graphics.setColor(ganimedView.getBackground());
					graphics.fillRect(0,offsetY,ganimedView.getWidth(),ganimedView.getHeight()-offsetY);
					lastResultImageSize=resultImageSize;
				}
				BufferedImage bufferedImage=imageService.getLogicalResultBufferedImage((int)index);
				graphics.drawImage(bufferedImage,offsetX, offsetY,null);
				index+=ganimedModel.getAnimationIncrement();
				if(index>cutRight) {
					index=cutLeft;
				}
			}else {
				if(lastResultImageSize!=null) {
					lastResultImageSize=null;
					graphics.setColor(ganimedView.getBackground());
					graphics.fillRect(0,offsetY,ganimedView.getWidth(),ganimedView.getHeight()-offsetY);
				}
				index=0;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					shutdown();
				}
			}
			try {
				sleep=timestamp-System.currentTimeMillis()+ganimedModel.getEditorModel().getAnimDelay();
				if(index==0) {
					sleep+=ganimedModel.getEditorModel().getAnimEndDelay();
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
		logger.debug("shutdown");
	}

}
