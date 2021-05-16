package lunartools.ganimed.gui.capture.model;

import java.util.Observable;

import lunartools.ganimed.GanimedModel;

public class CaptureModel extends Observable{
	private GanimedModel ganimedModel;

	public CaptureModel(GanimedModel ganimedModel) {
		this.ganimedModel=ganimedModel;
	}
	
	public void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

}
