package lunartools.ganimed.gui.selection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lunartools.ganimed.gui.SimpleEvents;

public class ImageSelectionActionListener implements ActionListener{
	private ImageSelectionView imageSelectionView;

	public ImageSelectionActionListener(ImageSelectionView imageSelectionView) {
		this.imageSelectionView=imageSelectionView;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source=actionEvent.getSource();
		if(source.equals(imageSelectionView.button)) {
			imageSelectionView.selectImageFolder();
		}else if(source.equals(imageSelectionView.buttonSelectCaptureRegion)) {
			imageSelectionView.sendMessage(SimpleEvents.SELECT_CAPTURE_REGION);
		}else if(source.equals(imageSelectionView.buttonStartCapture)) {
			imageSelectionView.sendMessage(SimpleEvents.START_CAPTURE);
		}else if(source.equals(imageSelectionView.buttonStopCapture)) {
			imageSelectionView.sendMessage(SimpleEvents.STOP_CAPTURE);
		}
	}

}
