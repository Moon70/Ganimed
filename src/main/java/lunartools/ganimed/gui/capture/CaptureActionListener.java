package lunartools.ganimed.gui.capture;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("unused")
public class CaptureActionListener implements ActionListener{
	private CaptureView captureView;
	
	public CaptureActionListener(CaptureView loaderView) {
		this.captureView=loaderView;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source=actionEvent.getSource();
	}
	
}
