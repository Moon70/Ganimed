package lunartools.ganimed.gui.selection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import lunartools.ganimed.ErrorMessage;
import lunartools.ganimed.gui.SimpleEvents;
import lunartools.ganimed.gui.selection.model.ImageSelectionModel;

public class ImageSelectionKeyListener implements KeyListener{
	private ImageSelectionModel imageSelectionModel;
	private ImageSelectionView imageSelectionView;

	public ImageSelectionKeyListener(ImageSelectionModel imageSelectionModel,ImageSelectionView imageSelectionView) {
		this.imageSelectionModel=imageSelectionModel;
		this.imageSelectionView=imageSelectionView;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_ENTER){
			Object source=event.getSource();
			if(source==imageSelectionView.textfieldFolderpath) {
				File file=new File(imageSelectionView.textfieldFolderpath.getText());
				imageSelectionView.selectedImageFolder(file);
			}else if(source==imageSelectionView.textfieldImagesFps) {
				try {
					imageSelectionModel.setImagesFps(Integer.parseInt(imageSelectionView.textfieldImagesFps.getText()));
				} catch (NumberFormatException e) {
					imageSelectionModel.sendMessage(SimpleEvents.MODEL_REFRESH_SELECTION_GUI);
					imageSelectionModel.sendMessage(new ErrorMessage("Illegal input for image fps!"));
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
