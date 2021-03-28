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
			}else if(event.getSource()==view.textfieldCutLeft) {
				model.setCutLeft(Integer.parseInt(view.textfieldCutLeft.getText()));
			}else if(event.getSource()==view.textfieldCutRight) {
				model.setCutRight(Integer.parseInt(view.textfieldCutRight.getText()));
			}else if(event.getSource()==view.textfieldCropLeft) {
				model.setCropLeft(Integer.parseInt(view.textfieldCropLeft.getText()));
			}else if(event.getSource()==view.textfieldCropRight) {
				model.setCropRight(Integer.parseInt(view.textfieldCropRight.getText()));

			}else if(event.getSource()==view.textfieldResize) {
				model.setResizePercent(Integer.parseInt(view.textfieldResize.getText()));
			}else if(event.getSource()==view.textfieldAnimFps) {
				model.setAnimFps(Integer.parseInt(view.textfieldAnimFps.getText()));
			}else if(event.getSource()==view.textfieldAnimDelay) {
				model.setAnimDelay(Integer.parseInt(view.textfieldAnimDelay.getText()));
			}else if(event.getSource()==view.textfieldAnimEndDelay) {
				model.setAnimEndDelay(Integer.parseInt(view.textfieldAnimEndDelay.getText()));
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
