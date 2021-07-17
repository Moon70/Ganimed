package lunartools.ganimed.gui.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import lunartools.ganimed.gui.editor.model.EditorModel;

public class EditorKeyListener implements KeyListener{
	private EditorModel editorModel;
	private EditorView panel;

	public EditorKeyListener(EditorModel editorModel,EditorView editorView) {
		this.editorModel=editorModel;
		this.panel=editorView;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_ENTER){
			if(event.getSource()==panel.textfieldCropTop) {
				editorModel.setCropTop(Integer.parseInt(panel.textfieldCropTop.getText()));
			}else if(event.getSource()==panel.textfieldCropBottom) {
				editorModel.setCropBottom(Integer.parseInt(panel.textfieldCropBottom.getText()));
			}else if(event.getSource()==panel.textfieldCutLeft) {
				editorModel.setCutLeft(Integer.parseInt(panel.textfieldCutLeft.getText()));
			}else if(event.getSource()==panel.textfieldCutRight) {
				editorModel.setCutRight(Integer.parseInt(panel.textfieldCutRight.getText()));
			}else if(event.getSource()==panel.textfieldCropLeft) {
				editorModel.setCropLeft(Integer.parseInt(panel.textfieldCropLeft.getText()));
			}else if(event.getSource()==panel.textfieldCropRight) {
				editorModel.setCropRight(Integer.parseInt(panel.textfieldCropRight.getText()));

			}else if(event.getSource()==panel.textfieldResize) {
				editorModel.setResizePercent(Integer.parseInt(panel.textfieldResize.getText()));
			}else if(event.getSource()==panel.textfieldAnimFps) {
				editorModel.setAnimFps(Integer.parseInt(panel.textfieldAnimFps.getText()));
			}else if(event.getSource()==panel.textfieldAnimDelay) {
				editorModel.setAnimDelay(Integer.parseInt(panel.textfieldAnimDelay.getText()));
			}else if(event.getSource()==panel.textfieldAnimEndDelay) {
				editorModel.setAnimEndDelay(Integer.parseInt(panel.textfieldAnimEndDelay.getText()));
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	
}
