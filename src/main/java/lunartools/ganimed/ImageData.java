package lunartools.ganimed;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageData {
	private AnimationData animationData;
	private File file;
	private BufferedImage bufferedImage;
	private int count=1;

	public ImageData(AnimationData animationData,File file) {
		this.animationData=animationData;
		this.file=file;
	}

	ImageData(AnimationData animationData,BufferedImage bufferedImage) {
		this.animationData=animationData;
		this.bufferedImage=bufferedImage;
	}

	public File getFile() {
		return file;
	}

	public BufferedImage getBufferedImage() {
		if(bufferedImage==null) {
			try {
				bufferedImage=ImageIO.read(file);
//				BufferedImage bufferedImageTemp=ImageIO.read(file);
//				bufferedImage=new BufferedImage(bufferedImageTemp.getWidth(),bufferedImageTemp.getHeight(),BufferedImage.TYPE_INT_RGB);
//				bufferedImage.getGraphics().drawImage(bufferedImageTemp,0,0,null);
//				bufferedImageTemp.flush();
			} catch (IOException e) {
				throw new RuntimeException("Error loading file: "+file,e);
			}
		}
		if(bufferedImage==null) {
			throw new RuntimeException("Error loading file: "+file);
		}
		return bufferedImage;
	}

	public BufferedImage getResultBufferedImage() {
		GanimedModel model=animationData.getGanimedModel();
		int width=model.getEditorModel().getCropRight()-model.getEditorModel().getCropLeft()+1;
		int height=model.getEditorModel().getCropBottom()-model.getEditorModel().getCropTop()+1;
		int resizePercent=model.getEditorModel().getResizePercent();
		if(resizePercent!=100) {
			width=width*resizePercent/100;
			height=height*resizePercent/100;
		}
		BufferedImage bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics graphicsImage=bufferedImage.getGraphics();
		graphicsImage.drawImage(getBufferedImage(),
				0, 0,
				width,height,
				model.getEditorModel().getCropLeft()-1, model.getEditorModel().getCropTop()-1,
				model.getEditorModel().getCropRight(),model.getEditorModel().getCropBottom(),
				null);
		return bufferedImage;
	}

	public int getCount() {
		return count;
	}

	public void increaseCount() {
		count++;
	}
	
}
