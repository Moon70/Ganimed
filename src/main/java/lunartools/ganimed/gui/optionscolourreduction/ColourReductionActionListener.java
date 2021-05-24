package lunartools.ganimed.gui.optionscolourreduction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;
import lunartools.ganimed.gui.optionscolourreduction.model.QuantizerAlgorithm;

public class ColourReductionActionListener implements ActionListener{
	private GanimedModel ganimedModel;
	private ColourReductionModel colourReductionModel;
	private ColourReductionPanel colourReductionPanel;

	public ColourReductionActionListener(GanimedModel ganimedModel,ColourReductionPanel colourReductionPanel,ColourReductionModel colourReductionModel) {
		this.ganimedModel=ganimedModel;
		this.colourReductionModel=colourReductionModel;
		this.colourReductionPanel=colourReductionPanel;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source=actionEvent.getSource();
		if(source.equals(colourReductionPanel.jRadioButtonQuantizerAlgorithm)) {
			colourReductionModel.setQuantizerAlgorithm(QuantizerAlgorithm.MEDIAN_CUT);
		}else if(colourReductionPanel.hashmapButtonToDitheringAlgorithm.containsKey(source)) {
			colourReductionModel.setDitheringAlgorithm(colourReductionPanel.hashmapButtonToDitheringAlgorithm.get(source));
		}
	}

}
