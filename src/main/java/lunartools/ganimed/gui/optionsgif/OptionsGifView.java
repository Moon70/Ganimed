package lunartools.ganimed.gui.optionsgif;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.optionsgif.model.OptionsGifModel;

@SuppressWarnings("unused")
public class OptionsGifView extends JPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private OptionsGifModel optionsGifModel;
	
	public OptionsGifView(GanimedModel ganimdeModel, GanimedView ganimdeView) {
		this.ganimedModel=ganimdeModel;
		this.ganimedView=ganimdeView;
		this.optionsGifModel=this.ganimedModel.getOptionsGifModel();
		this.setLayout(null);
		
		int y=4;
		int xLabel1=12;
		int xField1=90;
		int xLabel2=440;
		int xField2=440+78;
		int lineHight=18;
		int lineDistance=22;
		int lineDistance2=30;
		
		JLabel label=new JLabel("GIF options:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);

		y+=lineDistance2;
		
		label=new JLabel("...no GIF options at the moment - work in progress");
		label.setBounds(xLabel1,y,500,lineHight);
		add(label);
		
		y+=lineDistance;
		
		//TODO: size of OptionsGifView
		setBounds(0,0,860,178);
		//System.out.println("OptionsGifView y: "+y);
	}
	
	public void refreshGui() {
	}

}
