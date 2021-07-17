package lunartools.ganimed.gui.selection;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.selection.model.ImageSelectionModel;

public class ImageSelectionController {
	private GanimedModel ganimedModel;
	private ImageSelectionModel imageSelectionModel;

	public ImageSelectionController(GanimedModel ganimedModel,ImageSelectionModel imageSelectionModel) {
		this.ganimedModel=ganimedModel;
		this.imageSelectionModel=ganimedModel.getImageSelectionModel();
	}

	public boolean isCaptureButtonEnabled() {
		return ganimedModel.getAnimationData()==null && imageSelectionModel.getCaptureRegion()!=null;
	}

	public boolean isStopButtonEnabled() {
		return isCaptureButtonEnabled();
	}

}
