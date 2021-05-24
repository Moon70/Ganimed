package lunartools.ganimed.gui.optionscolourreduction.model;

public class ColourReductionModel {
	private QuantizerAlgorithm quantizerAlgorithm;
	private DitheringAlgorithm ditheringAlgorithm;

	public ColourReductionModel() {
		quantizerAlgorithm=QuantizerAlgorithm.MEDIAN_CUT;
		ditheringAlgorithm=DitheringAlgorithm.SIERRA;
	}

	public void setQuantizerAlgorithm(QuantizerAlgorithm quantizerAlgorithm) {
		this.quantizerAlgorithm=quantizerAlgorithm;
	}

	public QuantizerAlgorithm getQuantizerAlgorithm() {
		return quantizerAlgorithm;
	}

	public void setDitheringAlgorithm(DitheringAlgorithm ditheringAlgorithm) {
		this.ditheringAlgorithm=ditheringAlgorithm;
	}

	public DitheringAlgorithm getDitheringAlgorithm() {
		return ditheringAlgorithm;
	}

}
