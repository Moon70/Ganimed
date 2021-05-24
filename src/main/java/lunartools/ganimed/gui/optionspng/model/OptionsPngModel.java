package lunartools.ganimed.gui.optionspng.model;

import java.util.Observable;

import lunartools.ganimed.IOptionsModel;
import lunartools.ganimed.gui.SimpleEvents;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;
import lunartools.ganimed.gui.optionscolourreduction.model.IColourReductionParentModel;

public class OptionsPngModel extends Observable implements IOptionsModel,IColourReductionParentModel{
	private ColourReductionModel colourReductionModel;
	private PngEncoderType pngEncoderType;
	private boolean reencodePngEnabled;
	private boolean convertToPaletteEnabled;
	private TruecolourBitsCount trueColourBits;
	private boolean transparentPixelEnabled;
	private TransparentPixelCount numberOfTransparentPixel;

	public OptionsPngModel() {
		colourReductionModel=new ColourReductionModel();
		pngEncoderType=PngEncoderType.Ganimed;
		reencodePngEnabled=true;
		convertToPaletteEnabled=false;
		trueColourBits=TruecolourBitsCount.BIT24;
		transparentPixelEnabled=true;
		numberOfTransparentPixel=TransparentPixelCount.PIXEL3;
	}

	private void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public PngEncoderType getPngEncoderType() {
		return pngEncoderType;
	}

	public void setPngEncoderType(PngEncoderType pngEncoder) {
		if(this.pngEncoderType==pngEncoder) {
			return;
		}
		this.pngEncoderType=pngEncoder;
		sendMessage(SimpleEvents.MODEL_PNGOPTIONCHANGED);
	}

	public boolean isPngReencodingEnabled() {
		return reencodePngEnabled;
	}

	public void enablePngReencoding(boolean reencodePngEnabled) {
		this.reencodePngEnabled = reencodePngEnabled;
	}

	public boolean isConvertToPaletteEnabled() {
		return convertToPaletteEnabled;
	}

	public void enableConvertToPalette(boolean convertToPaletteEnabled) {
		this.convertToPaletteEnabled = convertToPaletteEnabled;
		sendMessage(SimpleEvents.MODEL_PNGOPTIONCHANGED);
	}

	public TruecolourBitsCount getTrueColourBits() {
		return trueColourBits;
	}

	public void setTrueColourBits(TruecolourBitsCount trueColourBits) {
		this.trueColourBits = trueColourBits;
	}

	public TransparentPixelCount getNumberOfTransparentPixel() {
		return numberOfTransparentPixel;
	}

	public void setNumberOfTransparentPixel(TransparentPixelCount numberOfTransparentPixel) {
		if(this.numberOfTransparentPixel==numberOfTransparentPixel) {
			return;
		}
		this.numberOfTransparentPixel = numberOfTransparentPixel;
		sendMessage(SimpleEvents.MODEL_PNGOPTIONCHANGED);
	}

	public void enableTransparentPixel(boolean transparentPixelEnabled) {
		if(this.transparentPixelEnabled==transparentPixelEnabled) {
			return;
		}
		this.transparentPixelEnabled = transparentPixelEnabled;
		sendMessage(SimpleEvents.MODEL_PNGOPTIONCHANGED);
	}

	public boolean isTransparentPixelEnabled() {
		return transparentPixelEnabled;
	}

	public boolean isCheckboxUseTransparentPixelEnabled() {
		return getPngEncoderType()==PngEncoderType.Ganimed;
	}

	public boolean isComboBoxTransparentPixelQuantityEnabled() {
		return isCheckboxUseTransparentPixelEnabled() && isTransparentPixelEnabled();
	}

	@Override
	public ColourReductionModel getColourReductionModel() {
		return colourReductionModel;
	}

	@Override
	public boolean isQuantizerSelectionEnabled() {
		return isConvertToPaletteEnabled();
	}

	@Override
	public boolean isDitheringSelectionEnabled() {
		return isConvertToPaletteEnabled();
	}
}
