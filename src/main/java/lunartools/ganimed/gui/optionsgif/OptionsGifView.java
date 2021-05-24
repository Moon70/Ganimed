package lunartools.ganimed.gui.optionsgif;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.optionscolourreduction.ColourReductionPanel;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;

public class OptionsGifView extends JPanel{
	private GanimedModel ganimedModel;
	private ColourReductionModel colourReductionModel;
	private GanimedView ganimedView;
	private ColourReductionPanel colourReductionPanel;

	public OptionsGifView(GanimedModel ganimdeModel, GanimedView ganimdeView) {
		this.ganimedModel=ganimdeModel;
		this.colourReductionModel=ganimedModel.getOptionsGifModel().getColourReductionModel();
		this.ganimedView=ganimdeView;
		this.setLayout(null);

		Font fontCurrent=this.getFont();
		Font fontBold=new Font(fontCurrent.getName(),Font.BOLD,fontCurrent.getSize());

		//ActionListener actionListener=new GifOptionsActionListener(ganimdeModel,this);

		int y=0;
		int xLabel1=0;
		int lineHight=18;
		int lineDistance=21;

		JLabel label=new JLabel("GIF options:");
		label.setBounds(xLabel1,y,100,lineHight);
		label.setFont(fontBold);
		add(label);

		y+=lineDistance;

		colourReductionPanel=new ColourReductionPanel(ganimdeModel,colourReductionModel);
		colourReductionPanel.setBounds(xLabel1,y,colourReductionPanel.getWidth(),colourReductionPanel.getHeight());
		add((JPanel)colourReductionPanel);

		y+=colourReductionPanel.getHeight();

		int sPanelWidth=colourReductionPanel.getWidth();
		setBounds(0,0,sPanelWidth,y);
	}

	public void refreshGui() {
		colourReductionPanel.refreshGui();
	}

}
