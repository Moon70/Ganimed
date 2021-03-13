package lunartools.ganimed.gui;

import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import lunartools.ganimed.GanimedModel;

public class GanimedAdjustmentListener implements AdjustmentListener{
	private GanimedModel model;
	private GanimedView view;

	public GanimedAdjustmentListener(GanimedModel model,GanimedView view) {
		this.model=model;
		this.view=view;
	}
	
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Scrollbar scrollbar=(Scrollbar)e.getSource();
		if(scrollbar==view.scrollbarGifFps) {
			model.setGifFps(scrollbar.getValue());
		}else if(scrollbar==view.scrollbarGifDelay) {
			model.setGifDelay(scrollbar.getValue());
		}else if(scrollbar==view.scrollbarGifEndDelay) {
			model.setGifEndDelay(scrollbar.getValue());
		}else if(scrollbar==view.scrollbarCropLeft) {
			model.setCropLeft(scrollbar.getValue());
		}else if(scrollbar==view.scrollbarCropTop) {
			model.setCropTop(scrollbar.getValue());
		}else if(scrollbar==view.scrollbarCropRight) {
			model.setCropRight(scrollbar.getValue());
		}else if(scrollbar==view.scrollbarCropBottom) {
			model.setCropBottom(scrollbar.getValue());
		}else if(scrollbar==view.scrollbarResize) {
			model.setResizePercent(scrollbar.getValue());
		}
	}

}
