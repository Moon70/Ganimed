package lunartools.ganimed;

import java.io.IOException;

import lunartools.apng.ApngBuilder;
import lunartools.apng.ApngBuilder.DitheringAlgorithm;
import lunartools.apng.ApngBuilder.QuantizerAlgorithm;
import lunartools.apng.Png;
import lunartools.apng.ProgressCallback;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;
import lunartools.ganimed.gui.optionspng.model.OptionsPngModel;
import lunartools.ganimed.gui.optionspng.model.PngEncoderType;

public class PngAnimCreator implements AnimCreator{
	private GanimedController ganimedController;
	private ProgressCallback progressCallback;
	private Png apng;
	private ApngBuilder apngBuilder;
	private ImageData previousImageData;
	private Png previousPng;
	private byte[] baPNG;
	
	public PngAnimCreator(OptionsPngModel optionsPngModel,GanimedController ganimedController,ProgressCallback progressCallback) {
		this.ganimedController=ganimedController;
		this.progressCallback=progressCallback;

		apngBuilder=new ApngBuilder();
		apngBuilder.enablePngEncoder(optionsPngModel.getPngEncoderType()==PngEncoderType.Ganimed);
		apngBuilder.enableReencodePngFiles(optionsPngModel.isPngReencodingEnabled());
		apngBuilder.setNumberOfTruecolourBits(optionsPngModel.getTrueColourBits().getValue());
		if(optionsPngModel.isTransparentPixelEnabled()) {
			apngBuilder.setMinimumNumberOfTransparentPixel(optionsPngModel.getNumberOfTransparentPixel().getValue());
			if(optionsPngModel.isConvertToPaletteEnabled()) {
				apngBuilder.setMaximumNumberOfColours(255);
			}else {
				apngBuilder.setMaximumNumberOfColours(0);
			}
		}else {
			apngBuilder.setMinimumNumberOfTransparentPixel(0);
			if(optionsPngModel.isConvertToPaletteEnabled()) {
				apngBuilder.setMaximumNumberOfColours(256);
			}else {
				apngBuilder.setMaximumNumberOfColours(0);
			}
		}
		ColourReductionModel colourReductionModel=optionsPngModel.getColourReductionModel();
		switch(colourReductionModel.getQuantizerAlgorithm()) {
		case MEDIAN_CUT:
			apngBuilder.setQuantizerAlgorithm(QuantizerAlgorithm.MEDIAN_CUT);
			break;
		default:
			throw new RuntimeException("colour quantizer algorithm not supported: "+colourReductionModel.getQuantizerAlgorithm());
		}
		switch(colourReductionModel.getDitheringAlgorithm()) {
		case NO_DITHERING:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.NO_DITHERING);
			break;
			//case SIMPLE_DITHERING1:
			//	apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.SIMPLE_DITHERING1);
			//	break;
		case FLOYD_STEINBERG:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.FLOYD_STEINBERG);
			break;
		case JARVIS_JUDICE_NINKE:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.JARVIS_JUDICE_NINKE);
			break;
		case STUCKI:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.STUCKI);
			break;
		case ATKINSON:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.ATKINSON);
			break;
		case BURKES:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.BURKES);
			break;
		case SIERRA:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.SIERRA);
			break;
		case TWO_ROW_SIERRA:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.TWO_ROW_SIERRA);
			break;
		case SIERRA_LITE:
			apngBuilder.setDitheringAlgorithm(DitheringAlgorithm.SIERRA_LITE);
			break;
		default:
			throw new RuntimeException("dithering algorithm not supported: "+colourReductionModel.getDitheringAlgorithm());
		}
	}

	@Override
	public void addImage(ImageData imageData, int delay, boolean loop) throws IOException {
		if(imageData==previousImageData) {
			previousPng.setDelay(previousPng.getDelay()+delay);
			return;
		}
		previousImageData=imageData;
		Png png;
		if(!imageData.isImagedataChanged() && imageData.getFile()!=null) {
			png=apngBuilder.buildPng(imageData.getFile());
		}else {
			png=apngBuilder.buildPng(imageData.getResultBufferedImage());
		}
		png.setDelay(delay);
		previousPng=png;
		if(apng==null) {
			apng=png;
		}else {
			apng.addPng(png);
		}
	}

	@Override
	public byte[] toByteArray() throws IOException {
		if(baPNG==null) {
			apng.addText("Software", "created with "+GanimedModel.PROGRAMNAME+" "+GanimedModel.determineProgramVersion(),false);
			baPNG=apng.toByteArray(progressCallback);
		}
		return baPNG;
	}

}
