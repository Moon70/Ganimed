package lunartools.ganimed.gui.optionspng;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.optionscolourreduction.ColourReductionPanel;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;
import lunartools.ganimed.gui.optionspng.model.OptionsPngModel;
import lunartools.ganimed.gui.optionspng.model.PngEncoderType;
import lunartools.ganimed.gui.optionspng.model.TransparentPixelCount;
import lunartools.ganimed.gui.optionspng.model.TruecolourBitsCount;

public class OptionsPngView extends JPanel{
	private GanimedModel ganimedModel;
	private OptionsPngModel optionsPngModel;
	private ColourReductionModel colourReductionModel;
	private GanimedView ganimedView;
	private ColourReductionPanel colourReductionPanel;

	ButtonGroup buttonGroup;
	JRadioButton jRadioButtonPngEncoder;
	JRadioButton jRadioButtonImageIO;

	JCheckBox checkboxReEncodePng;
	JCheckBox checkboxConvertToPalette;
	JComboBox<TruecolourBitsCount> comboBoxTruecolourOptions;
	JCheckBox checkboxUseTransparentPixel;
	JComboBox<TransparentPixelCount> comboBoxTransparentPixelQuantity;

	public OptionsPngView(GanimedModel ganimdeModel, GanimedView ganimdeView) {
		this.ganimedModel=ganimdeModel;
		this.optionsPngModel=this.ganimedModel.getOptionsPngModel();
		this.colourReductionModel=optionsPngModel.getColourReductionModel();
		this.ganimedView=ganimdeView;
		this.setLayout(null);

		Font fontCurrent=this.getFont();
		Font fontBold=new Font(fontCurrent.getName(),Font.BOLD,fontCurrent.getSize());

		ActionListener actionListener=new PngOptionsActionListener(ganimdeModel,this);
		KeyListener keyListener=new PngOptionsKeyListener(optionsPngModel,this);

		int y=0;
		int xLabel1=0;
		int xField1=78;
		int xColumn2=338;
		int xOptions1Column1=98;
		int lineHight=18;
		int lineDistance=21;

		JLabel label=new JLabel("PNG options:");
		label.setBounds(xLabel1,y,100,lineHight);
		label.setFont(fontBold);
		add(label);

		y+=lineDistance;

		label=new JLabel("Truecolour:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		Vector<TruecolourBitsCount> trueColourOptions=new Vector<TruecolourBitsCount>();
		trueColourOptions.add(TruecolourBitsCount.BIT24);
		trueColourOptions.add(TruecolourBitsCount.BIT21);
		trueColourOptions.add(TruecolourBitsCount.BIT18);
		trueColourOptions.add(TruecolourBitsCount.BIT15);
		trueColourOptions.add(TruecolourBitsCount.BIT12);
		trueColourOptions.add(TruecolourBitsCount.BIT9);
		trueColourOptions.add(TruecolourBitsCount.BIT6);
		trueColourOptions.add(TruecolourBitsCount.BIT3);
		comboBoxTruecolourOptions = new JComboBox<TruecolourBitsCount>(trueColourOptions);
		comboBoxTruecolourOptions.setBounds(xField1,y,100,lineHight);
		comboBoxTruecolourOptions.setMaximumRowCount(6);
		comboBoxTruecolourOptions.addActionListener(actionListener);
		add(comboBoxTruecolourOptions);

		label=new JLabel("Colours:");
		label.setBounds(xColumn2+xLabel1,y,100,lineHight);
		add(label);
		checkboxConvertToPalette = new JCheckBox("convert to 256 colour palette");
		checkboxConvertToPalette.setBounds(xColumn2+xOptions1Column1,y,300,lineHight);
		checkboxConvertToPalette.addActionListener(actionListener);
		add(checkboxConvertToPalette);

		y+=lineDistance;

		label=new JLabel("Encoder:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		buttonGroup=new ButtonGroup();
		jRadioButtonPngEncoder=new JRadioButton("Ganimed",true);
		jRadioButtonPngEncoder.setBounds(xField1,y,70,lineHight);
		buttonGroup.add(jRadioButtonPngEncoder);
		jRadioButtonPngEncoder.addActionListener(actionListener);
		add(jRadioButtonPngEncoder);

		jRadioButtonImageIO=new JRadioButton("Java",false);
		jRadioButtonImageIO.setBounds(xField1+70,y,70,lineHight);
		buttonGroup.add(jRadioButtonImageIO);
		jRadioButtonImageIO.addActionListener(actionListener);
		add(jRadioButtonImageIO);

		colourReductionPanel=new ColourReductionPanel(ganimdeModel,colourReductionModel);
		colourReductionPanel.setBounds(xColumn2+xLabel1, y,colourReductionPanel.getWidth(),colourReductionPanel.getHeight());
		add((JPanel)colourReductionPanel);
		int sPanelHeight=xColumn2+xLabel1+colourReductionPanel.getHeight();

		y+=lineDistance;


		checkboxReEncodePng = new JCheckBox("re-encode PNG files");
		checkboxReEncodePng.setBounds(xField1,y,250,lineHight);
		checkboxReEncodePng.addActionListener(actionListener);
		add(checkboxReEncodePng);

		y+=lineDistance;

		checkboxUseTransparentPixel = new JCheckBox("use transparent pixel to optimize filesize");
		checkboxUseTransparentPixel.setBounds(xField1,y,250,lineHight);
		checkboxUseTransparentPixel.addActionListener(actionListener);
		add(checkboxUseTransparentPixel);

		y+=lineDistance;

		Vector<TransparentPixelCount> transparentPixelQuantity=new Vector<TransparentPixelCount>();
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL1);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL2);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL3);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL4);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL5);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL6);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL7);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL8);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL9);
		transparentPixelQuantity.add(TransparentPixelCount.PIXEL10);
		comboBoxTransparentPixelQuantity = new JComboBox<TransparentPixelCount>(transparentPixelQuantity);
		comboBoxTransparentPixelQuantity.setBounds(xField1+20,y,80,lineHight);
		comboBoxTransparentPixelQuantity.setMaximumRowCount(3);
		comboBoxTransparentPixelQuantity.addActionListener(actionListener);
		add(comboBoxTransparentPixelQuantity);

		y+=lineDistance;

		int sPanelWidth=xColumn2+xLabel1+colourReductionPanel.getWidth();
		setBounds(0,0,sPanelWidth,sPanelHeight);
	}

	public void refreshGui() {
		jRadioButtonPngEncoder.setEnabled(true);
		jRadioButtonImageIO.setEnabled(true);
		checkboxReEncodePng.setEnabled(true);
		checkboxConvertToPalette.setEnabled(true);
		comboBoxTruecolourOptions.setEnabled(true);

		jRadioButtonPngEncoder.setSelected(optionsPngModel.getPngEncoderType()==PngEncoderType.Ganimed);
		jRadioButtonImageIO.setSelected(optionsPngModel.getPngEncoderType()==PngEncoderType.Java);
		checkboxReEncodePng.setSelected(optionsPngModel.isPngReencodingEnabled());
		checkboxConvertToPalette.setSelected(optionsPngModel.isConvertToPaletteEnabled());
		comboBoxTruecolourOptions.setSelectedItem(optionsPngModel.getTrueColourBits());

		if(optionsPngModel.isCheckboxUseTransparentPixelEnabled()) {
			checkboxUseTransparentPixel.setEnabled(true);
			checkboxUseTransparentPixel.setSelected(optionsPngModel.isTransparentPixelEnabled());
		}else {
			checkboxUseTransparentPixel.setEnabled(false);
		}

		if(optionsPngModel.isComboBoxTransparentPixelQuantityEnabled()) {
			comboBoxTransparentPixelQuantity.setEnabled(true);
			comboBoxTransparentPixelQuantity.setSelectedItem(optionsPngModel.getNumberOfTransparentPixel());
		}else {
			comboBoxTransparentPixelQuantity.setEnabled(false);
		}

		colourReductionPanel.refreshGui();
	}

}
