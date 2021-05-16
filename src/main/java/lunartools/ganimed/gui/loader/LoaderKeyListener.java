package lunartools.ganimed.gui.loader;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import lunartools.ganimed.ErrorMessage;
import lunartools.ganimed.gui.SimpleEvents;
import lunartools.ganimed.gui.loader.model.LoaderModel;

public class LoaderKeyListener implements KeyListener{
	private LoaderModel loaderModel;
	private LoaderView loaderView;

	public LoaderKeyListener(LoaderModel loaderModel,LoaderView loaderView) {
		this.loaderModel=loaderModel;
		this.loaderView=loaderView;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_ENTER){
			Object source=event.getSource();
			if(source==loaderView.textfieldFolderpath) {
				File file=new File(loaderView.textfieldFolderpath.getText());
				loaderView.selectedImageFolder(file);
			}else if(source==loaderView.textfieldImagesFps) {
				try {
					loaderModel.setImagesFps(Integer.parseInt(loaderView.textfieldImagesFps.getText()));
				} catch (NumberFormatException e) {
					loaderModel.sendMessage(SimpleEvents.MODEL_REFRESH_SELECTION_GUI);
					loaderModel.sendMessage(new ErrorMessage("Illegal input for image fps!"));
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
