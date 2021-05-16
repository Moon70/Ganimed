package lunartools.ganimed;

import java.io.IOException;

import lunartools.apng.ApngBuilder;
import lunartools.apng.Png;
import lunartools.apng.ProgressCallback;
import lunartools.ganimed.gui.optionspng.model.OptionsPngModel;
import lunartools.ganimed.gui.optionspng.model.PngEncoderType;

public class PngAnimCreator implements AnimCreator, ProgressCallback{
	private GanimedController controller;
	private Png apng;
	private ApngBuilder apngBuilder;
	private OptionsPngModel pngSettings;
	
	public PngAnimCreator(OptionsPngModel pngSettings,GanimedController controller) {
		this.pngSettings=pngSettings;
		this.controller=controller;
	}
	
	@Override
	public void addImage(ImageData imageData, int delay, boolean loop) throws IOException {
		if(apngBuilder==null) {
			apngBuilder=new ApngBuilder();
			apngBuilder.enablePngEncoder(pngSettings.getPngEncoderType()==PngEncoderType.Ganimed);
			apngBuilder.enableReencodePngFiles(pngSettings.isPngReencodingEnabled());
			apngBuilder.setNumberOfTruecolourBits(pngSettings.getTrueColourBits().getValue());
			if(pngSettings.isTransparentPixelEnabled()) {
				apngBuilder.setMinimumNumberOfTransparentPixel(pngSettings.getNumberOfTransparentPixel().getValue());
				if(pngSettings.isConvertToPaletteEnabled()) {
					apngBuilder.setMaximumNumberOfColours(255);
				}
			}else {
				apngBuilder.setMinimumNumberOfTransparentPixel(0);
				if(pngSettings.isConvertToPaletteEnabled()) {
					//TODO: Set to 256 colours
					apngBuilder.setMaximumNumberOfColours(255);
				}
			}
		}
		Png png;
		png=apngBuilder.buildPng(imageData.getResultBufferedImage());
		png.setDelay(delay);
		
		if(apng==null) {
			apng=png;
		}else {
			apng.addPng(png);
		}
		
	}

	@Override
	public byte[] toByteArray() throws IOException {
		return apng.toByteArray(this);
	}

	@Override
	public void setProgressStep(int step) {
		controller.setProgressBarValue(step,"creating PNG...");
	}

}
