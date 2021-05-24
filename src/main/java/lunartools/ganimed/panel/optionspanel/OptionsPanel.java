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

	public OptionsPanel(GanimedModel ganimedModel, GanimedView ganimedView) {
		this.ganimedModel=ganimedModel;
		this.ganimdeView=ganimedView;
		this.setLayout(null);

		int xLabel1=12;
		int sPanelWidth;
		int y=0;
		gifOptionsPanel=new OptionsGifView(ganimedModel, ganimedView);
		gifOptionsPanel.setBounds(xLabel1, 0, gifOptionsPanel.getWidth(), gifOptionsPanel.getHeight());
		gifOptionsPanel.setVisible(false);
		pngOptionsPanel=new OptionsPngView(ganimedModel, ganimedView);
		pngOptionsPanel.setBounds(xLabel1, 0, pngOptionsPanel.getWidth(), pngOptionsPanel.getHeight());
		pngOptionsPanel.setVisible(false);
		add((JPanel)gifOptionsPanel);
		add((JPanel)pngOptionsPanel);

		sPanelWidth=Math.max(gifOptionsPanel.getWidth(),pngOptionsPanel.getWidth());
		y+=Math.max(gifOptionsPanel.getHeight(),pngOptionsPanel.getHeight());

		setBounds(0,0,sPanelWidth,y);
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
