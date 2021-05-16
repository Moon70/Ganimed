package lunartools.ganimed.gui.optionspng.model;

public class OptionsPngModel {
	private PngEncoderType pngEncoderType;
	private boolean reencodePngEnabled;
	private boolean convertToPaletteEnabled;
	private TruecolourBitsEnum trueColourBits;
	private boolean transparentPixelEnabled;
	private TransparentPixelEnum numberOfTransparentPixel;
	
	public OptionsPngModel() {
		pngEncoderType=PngEncoderType.Ganimed;
		reencodePngEnabled=true;
		convertToPaletteEnabled=false;
		trueColourBits=TruecolourBitsEnum.BIT24;
		transparentPixelEnabled=true;
		numberOfTransparentPixel=TransparentPixelEnum.PIXEL3;
	}
	
	public PngEncoderType getPngEncoderType() {
		return pngEncoderType;
	}
	
	public void setPngEncoderType(PngEncoderType pngEncoder) {
		this.pngEncoderType=pngEncoder;
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
	}

	public TruecolourBitsEnum getTrueColourBits() {
		return trueColourBits;
	}
	
	public void setTrueColourBits(TruecolourBitsEnum trueColourBits) {
		this.trueColourBits = trueColourBits;
	}
	
	public TransparentPixelEnum getNumberOfTransparentPixel() {
		return numberOfTransparentPixel;
	}
	
	public void setNumberOfTransparentPixel(TransparentPixelEnum numberOfTransparentPixel) {
		this.numberOfTransparentPixel = numberOfTransparentPixel;
	}
	
	public void enableTransparentPixel(boolean transparentPixelEnabled) {
		this.transparentPixelEnabled = transparentPixelEnabled;
	}
	
	public boolean isTransparentPixelEnabled() {
		return transparentPixelEnabled;
	}
}
