package lunartools.ganimed.gui.editor;

import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import lunartools.ganimed.gui.editor.model.EditorModel;

public class EditorAdjustmentListener implements AdjustmentListener{
	private EditorModel editorModel;
	private EditorView panel;

	public EditorAdjustmentListener(EditorModel editorModel,EditorView panel) {
		this.editorModel=editorModel;
		this.panel=panel;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Scrollbar scrollbar=(Scrollbar)e.getSource();
		if(scrollbar==panel.scrollbarCutLeft) {
			editorModel.setCutLeft(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarCutRight) {
			editorModel.setCutRight(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarCropTop) {
			editorModel.setCropTop(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarCropBottom) {
			editorModel.setCropBottom(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarCropLeft) {
			editorModel.setCropLeft(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarCropRight) {
			editorModel.setCropRight(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarResize) {
			editorModel.setResizePercent(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarAnimFps) {
			editorModel.setAnimFps(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarAnimDelay) {
			editorModel.setAnimDelay(scrollbar.getValue());
		}else if(scrollbar==panel.scrollbarAnimEndDelay) {
			editorModel.setAnimEndDelay(scrollbar.getValue());
		}
	}

	
}
