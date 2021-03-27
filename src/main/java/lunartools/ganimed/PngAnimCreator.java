package lunartools.ganimed;

import java.awt.image.BufferedImage;
import java.io.IOException;

import lunartools.apng.ApngBuilder;
import lunartools.apng.Png;

public class PngAnimCreator implements AnimCreator{
	private Png apng;

	@Override
	public void addImage(BufferedImage bufferedImage, int delay, boolean loop) throws IOException {
		if(apng==null) {
			apng=ApngBuilder.createPng(bufferedImage, delay);
		}else {
			Png png=ApngBuilder.createPng(bufferedImage, delay);
			apng.addPng(png);
		}
	}

	@Override
	public byte[] toByteArray() throws IOException {
		return apng.toByteArray();
	}

}
