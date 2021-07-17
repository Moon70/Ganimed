package lunartools.ganimed.gui.selection;

import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import lunartools.ganimed.gui.selection.model.ImageSelectionModel;

public class ImageSelectionAdjustmentListener implements AdjustmentListener{
	private ImageSelectionModel imageSelectionModel;
	private ImageSelectionView imageSelectionView;

	public ImageSelectionAdjustmentListener(ImageSelectionModel imageSelectionModel,ImageSelectionView imageSelectionView) {
		this.imageSelectionModel=imageSelectionModel;
		this.imageSelectionView=imageSelectionView;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Scrollbar scrollbar=(Scrollbar)e.getSource();
		if(scrollbar==imageSelectionView.scrollbarImagesFps) {
			imageSelectionModel.setImagesFps(scrollbar.getValue());
		}
	}

}
