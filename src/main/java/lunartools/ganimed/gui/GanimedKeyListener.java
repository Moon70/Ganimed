package lunartools.ganimed.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import lunartools.ganimed.GanimedModel;

public class GanimedKeyListener implements KeyListener{
	private GanimedModel model;
	private GanimedView view;
	
	public GanimedKeyListener(GanimedModel model,GanimedView view) {
		this.model=model;
		this.view=view;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_ENTER){
			if(event.getSource()==view.textfieldFolderpath) {
				File file=new File(view.textfieldFolderpath.getText());
				view.selectedImageFolder(file);
			}else if(event.getSource()==view.textfieldImagesFps) {
				model.setImagesFps(Integer.parseInt(view.textfieldImagesFps.getText()));
			}else if(event.getSource()==view.textfieldCropTop) {
				model.setCropTop(Integer.parseInt(view.textfieldCropTop.getText()));
			}else if(event.getSource()==view.textfieldCropBottom) {
				model.setCropBottom(Integer.parseInt(view.textfieldCropBottom.getText()));
			}else if(event.getSource()==view.textfieldCropLeft) {
				model.setCropLeft(Integer.parseInt(view.textfieldCropLeft.getText()));
			}else if(event.getSource()==view.textfieldCropRight) {
				model.setCropRight(Integer.parseInt(view.textfieldCropRight.getText()));

			}else if(event.getSource()==view.textfieldResize) {
				model.setResizePercent(Integer.parseInt(view.textfieldResize.getText()));
			}else if(event.getSource()==view.textfieldGifFps) {
				model.setGifFps(Integer.parseInt(view.textfieldGifFps.getText()));
			}else if(event.getSource()==view.textfieldGifDelay) {
				model.setGifDelay(Integer.parseInt(view.textfieldGifDelay.getText()));
			}else if(event.getSource()==view.textfieldGifEndDelay) {
				model.setGifEndDelay(Integer.parseInt(view.textfieldGifEndDelay.getText()));
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
