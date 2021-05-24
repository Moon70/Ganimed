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

	public ImageData(AnimationData animationData,File file) {
		this.animationData=animationData;
		this.file=file;
	}

	public File getFile() {
		return file;
	}

	public BufferedImage getBufferedImage() {
		if(bufferedImage==null) {
			try {
				bufferedImage=ImageIO.read(file);
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
		int width=model.getEditorModel().getCropRight()-model.getEditorModel().getCropLeft();
		int height=model.getEditorModel().getCropBottom()-model.getEditorModel().getCropTop();
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
				model.getEditorModel().getCropLeft(), model.getEditorModel().getCropTop(),
				model.getEditorModel().getCropRight(),model.getEditorModel().getCropBottom(),
				null);
		return bufferedImage;
	}

}
