package lunartools.ganimed.panel.optionspanel;

import javax.swing.JPanel;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.optionsgif.OptionsGifView;
import lunartools.ganimed.gui.optionspng.OptionsPngView;

public class OptionsPanel extends JPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimdeView;
	private OptionsGifView gifOptionsPanel;
	private OptionsPngView pngOptionsPanel;
	
	public OptionsPanel(GanimedModel model, GanimedView view) {
		this.ganimedModel=model;
		this.ganimdeView=view;
		this.setLayout(null);
		
		this.setLayout(null);
		gifOptionsPanel=new OptionsGifView(model, view);
		gifOptionsPanel.setVisible(false);
		pngOptionsPanel=new OptionsPngView(model, view);
		pngOptionsPanel.setVisible(false);
		add((JPanel)gifOptionsPanel);
		add((JPanel)pngOptionsPanel);
		
		//TODO: size of OptionsPanel
		setBounds(0,0,860,178);
	}
	
	public void refreshGui() {
		if(ganimedModel.getImageType()==ImageType.GIF) {
			pngOptionsPanel.setVisible(false);
			gifOptionsPanel.setVisible(true);
			gifOptionsPanel.refreshGui();
		}else if(ganimedModel.getImageType()==ImageType.PNG) {
			gifOptionsPanel.setVisible(false);
			pngOptionsPanel.setVisible(true);
			pngOptionsPanel.refreshGui();
		}else {
			pngOptionsPanel.setVisible(false);
			gifOptionsPanel.setVisible(false);
			throw new RuntimeException("unexpected image type");
		}
	}

}
