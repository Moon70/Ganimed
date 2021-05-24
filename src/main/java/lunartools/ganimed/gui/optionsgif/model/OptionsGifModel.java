package lunartools.ganimed.gui.optionsgif.model;

import java.util.Observable;

import lunartools.ganimed.IOptionsModel;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;
import lunartools.ganimed.gui.optionscolourreduction.model.IColourReductionParentModel;

public class OptionsGifModel extends Observable implements IOptionsModel,IColourReductionParentModel{
	private ColourReductionModel colourReductionModel;

	public OptionsGifModel() {
		colourReductionModel=new ColourReductionModel();
	}

	private void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	@Override
	public ColourReductionModel getColourReductionModel() {
		return colourReductionModel;
	}

	@Override
	public boolean isQuantizerSelectionEnabled() {
		return true;
	}

	@Override
	public boolean isDitheringSelectionEnabled() {
		return true;
	}

}
