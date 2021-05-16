package lunartools.ganimed.panel.selectionpanel;

import javax.swing.JPanel;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.capture.CaptureView;
import lunartools.ganimed.gui.loader.LoaderView;

public class SelectionPanel extends JPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private LoaderView loaderView;
	private CaptureView captureView;
	
	public SelectionPanel(GanimedModel ganimdeModel, GanimedView ganimedView) {
		this.ganimedModel=ganimdeModel;
		this.ganimedView=ganimedView;
		this.setLayout(null);
		
		this.setLayout(null);
		loaderView=new LoaderView(ganimdeModel, ganimedView);
		loaderView.setVisible(false);
		captureView=new CaptureView(ganimdeModel, ganimedView);
		captureView.setVisible(false);
		add((JPanel)loaderView);
		add((JPanel)captureView);
		
		//TODO: size of SelectionPanel
		setBounds(0,0,860,178);
	}
	
	public void refreshGui() {
		if(ganimedModel.getSelectionType()==SelectionType.LOAD) {
			loaderView.setVisible(false);
			loaderView.setVisible(true);
			loaderView.refreshGui();
		}else if(ganimedModel.getSelectionType()==SelectionType.CAPTURE) {
			captureView.setVisible(false);
			captureView.setVisible(true);
			captureView.refreshGui();
		}else {
			loaderView.setVisible(false);
			captureView.setVisible(false);
			throw new RuntimeException("unexpected selection type");
		}
	}

	public LoaderView getLoaderView() {
		return loaderView;
	}
	
}
