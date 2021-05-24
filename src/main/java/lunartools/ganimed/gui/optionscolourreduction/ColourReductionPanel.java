package lunartools.ganimed.gui.optionscolourreduction;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;
import lunartools.ganimed.gui.optionscolourreduction.model.DitheringAlgorithm;
import lunartools.ganimed.gui.optionscolourreduction.model.IColourReductionParentModel;
import lunartools.ganimed.gui.optionscolourreduction.model.QuantizerAlgorithm;

public class ColourReductionPanel extends JPanel{
	private GanimedModel ganimedModel;
	private ColourReductionModel colourReductionModel;

	private JLabel labelQuantizer;
	private ButtonGroup buttonGroup;
	JRadioButton jRadioButtonQuantizerAlgorithm;

	HashMap<JRadioButton, DitheringAlgorithm> hashmapButtonToDitheringAlgorithm=new HashMap<JRadioButton, DitheringAlgorithm>();
	private JLabel labelDithering;
	private ButtonGroup buttonGroup2;

	public ColourReductionPanel(GanimedModel ganimdeModel,ColourReductionModel colourReductionModel) {
		this.ganimedModel=ganimdeModel;
		this.colourReductionModel=colourReductionModel;
		this.setLayout(null);

		ActionListener actionListener=new ColourReductionActionListener(ganimdeModel,this,colourReductionModel);

		int y=0;
		int xLabel1=0;
		int xField1=98;
		int xOptions1Column1=98;
		int xOptions1Column2=278;
		int xOptionsWidth=180;
		int sPanelWidth=xOptions1Column2+xOptionsWidth;
		int lineHight=18;
		int lineDistance=21;
		int lineDistance3=17;

		labelQuantizer=new JLabel("Colour quantizer:");
		labelQuantizer.setBounds(xLabel1,y,100,lineHight);
		add(labelQuantizer);
		buttonGroup=new ButtonGroup();
		jRadioButtonQuantizerAlgorithm=new JRadioButton("Median Cut",true);
		jRadioButtonQuantizerAlgorithm.setBounds(xField1,y,80,lineHight);
		buttonGroup.add(jRadioButtonQuantizerAlgorithm);
		jRadioButtonQuantizerAlgorithm.addActionListener(actionListener);
		add(jRadioButtonQuantizerAlgorithm);

		y+=lineDistance;

		labelDithering=new JLabel("Dithering:");
		labelDithering.setBounds(xLabel1,y,100,lineHight);
		add(labelDithering);
		buttonGroup2=new ButtonGroup();
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.NO_DITHERING,			actionListener,new Rectangle(xOptions1Column1,y,xOptionsWidth,lineHight));
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.BURKES,				actionListener,new Rectangle(xOptions1Column2,y,xOptionsWidth,lineHight));
		y+=lineDistance3;
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.FLOYD_STEINBERG,		actionListener,new Rectangle(xOptions1Column1,y,xOptionsWidth,lineHight));
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.SIERRA,				actionListener,new Rectangle(xOptions1Column2,y,xOptionsWidth,lineHight));
		y+=lineDistance3;
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.JARVIS_JUDICE_NINKE,	actionListener,new Rectangle(xOptions1Column1,y,xOptionsWidth,lineHight));
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.TWO_ROW_SIERRA,		actionListener,new Rectangle(xOptions1Column2,y,xOptionsWidth,lineHight));
		y+=lineDistance3;
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.STUCKI,				actionListener,new Rectangle(xOptions1Column1,y,xOptionsWidth,lineHight));
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.SIERRA_LITE,			actionListener,new Rectangle(xOptions1Column2,y,xOptionsWidth,lineHight));
		y+=lineDistance3;
		addDitheringAlgorithmOptionButton(DitheringAlgorithm.ATKINSON,				actionListener,new Rectangle(xOptions1Column1,y,xOptionsWidth,lineHight));
		//addDitheringAlgorithmOptionButton(DitheringAlgorithm.SIMPLE_DITHERING1,		actionListener,new Rectangle(xOptions1Column2,y,xOptionsWidth,lineHight));
		y+=lineDistance3;
		y++;
		setBounds(0,0,sPanelWidth,y);
	}

	private void addDitheringAlgorithmOptionButton(DitheringAlgorithm ditheringAlgorithm,ActionListener actionListener,Rectangle bounds) {
		JRadioButton jRadioButton2=new JRadioButton(ditheringAlgorithm.toString(),false);
		jRadioButton2.setBounds(bounds);
		buttonGroup2.add(jRadioButton2);
		jRadioButton2.addActionListener(actionListener);
		add(jRadioButton2);
		hashmapButtonToDitheringAlgorithm.put(jRadioButton2, ditheringAlgorithm);
	}

	public void refreshGui() {
		IColourReductionParentModel colourReductionParentModel=(IColourReductionParentModel)ganimedModel.getCurrentOptionsModel();
		if(colourReductionParentModel.isQuantizerSelectionEnabled()) {
			labelQuantizer.setEnabled(true);
			jRadioButtonQuantizerAlgorithm.setEnabled(true);
			jRadioButtonQuantizerAlgorithm.setSelected(colourReductionParentModel.getColourReductionModel().getQuantizerAlgorithm()==QuantizerAlgorithm.MEDIAN_CUT);
		}else {
			labelQuantizer.setEnabled(false);
			jRadioButtonQuantizerAlgorithm.setEnabled(false);
		}
		if(colourReductionParentModel.isDitheringSelectionEnabled()) {
			labelDithering.setEnabled(true);
			DitheringAlgorithm ditheringAlgorithm=colourReductionParentModel.getColourReductionModel().getDitheringAlgorithm();
			for(Entry<JRadioButton, DitheringAlgorithm> entry:hashmapButtonToDitheringAlgorithm.entrySet()) {
				entry.getKey().setEnabled(true);
				entry.getKey().setSelected(entry.getValue()==ditheringAlgorithm);
			}
		}else {
			labelDithering.setEnabled(false);
			for(Entry<JRadioButton, DitheringAlgorithm> entry:hashmapButtonToDitheringAlgorithm.entrySet()) {
				entry.getKey().setEnabled(false);
			}
		}
	}

}
