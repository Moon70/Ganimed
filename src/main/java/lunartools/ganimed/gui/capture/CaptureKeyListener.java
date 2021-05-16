package lunartools.ganimed.gui.capture;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import lunartools.ganimed.gui.capture.model.CaptureModel;
import lunartools.ganimed.gui.loader.model.LoaderModel;

@SuppressWarnings("unused")
public class CaptureKeyListener implements KeyListener{
	private CaptureModel loaderModel;
	private CaptureView loaderView;

	public CaptureKeyListener(CaptureModel captureModel,CaptureView captureView) {
		this.loaderModel=captureModel;
		this.loaderView=captureView;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_ENTER){
			Object source=event.getSource();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
