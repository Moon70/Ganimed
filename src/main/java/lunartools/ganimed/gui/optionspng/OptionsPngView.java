package lunartools.ganimed.gui.optionspng;

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
import lunartools.ganimed.gui.optionspng.model.OptionsPngModel;
import lunartools.ganimed.gui.optionspng.model.PngEncoderType;
import lunartools.ganimed.gui.optionspng.model.TransparentPixelEnum;
import lunartools.ganimed.gui.optionspng.model.TruecolourBitsEnum;

public class OptionsPngView extends JPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private OptionsPngModel optionsPngModel;
	
	ButtonGroup buttonGroup;
	JRadioButton jRadioButtonPngEncoder;
	JRadioButton jRadioButtonImageIO;
	
	JCheckBox checkboxReEncodePng;
	JCheckBox checkboxConvertToPalette;
	JComboBox<TruecolourBitsEnum> comboBoxTruecolourOptions;
	JCheckBox checkboxUseTransparentPixel;
	JComboBox<TransparentPixelEnum> comboBoxTransparentPixelQuantity;
	
	public OptionsPngView(GanimedModel ganimdeModel, GanimedView ganimdeView) {
		this.ganimedModel=ganimdeModel;
		this.ganimedView=ganimdeView;
		this.optionsPngModel=this.ganimedModel.getOptionsPngModel();
		this.setLayout(null);
		
		ActionListener actionListener=new PngOptionsActionListener(ganimdeModel,this);
		KeyListener keyListener=new PngOptionsKeyListener(optionsPngModel,this);
		
		int y=4;
		int xLabel1=12;
		int xField1=90;
		int xLabel2=440;
		int xField2=440+78;
		int lineHight=18;
		int lineDistance=22;
		int lineDistance2=30;
		
		JLabel label=new JLabel("PNG options:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);

		y+=lineDistance2;

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
		
		y+=lineDistance;
		
		checkboxReEncodePng = new JCheckBox("re-encode PNG files");
		checkboxReEncodePng.setBounds(xLabel1,y,300,lineHight);
		checkboxReEncodePng.addActionListener(actionListener);
		add(checkboxReEncodePng);

		y+=lineDistance;

		checkboxUseTransparentPixel = new JCheckBox("use transparent pixel to optimize filesize");
		checkboxUseTransparentPixel.setBounds(xLabel1,y,300,lineHight);
		checkboxUseTransparentPixel.addActionListener(actionListener);
		add(checkboxUseTransparentPixel);
		label=new JLabel("Quantity:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		Vector<TransparentPixelEnum> transparentPixelQuantity=new Vector<TransparentPixelEnum>();
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL1);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL2);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL3);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL4);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL5);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL6);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL7);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL8);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL9);
		transparentPixelQuantity.add(TransparentPixelEnum.PIXEL10);
		comboBoxTransparentPixelQuantity = new JComboBox<TransparentPixelEnum>(transparentPixelQuantity);
		comboBoxTransparentPixelQuantity.setBounds(xField2,y,100,lineHight);
		comboBoxTransparentPixelQuantity.setMaximumRowCount(6);
		comboBoxTransparentPixelQuantity.addActionListener(actionListener);
		add(comboBoxTransparentPixelQuantity);
		
		y+=lineDistance;

		label=new JLabel("Truecolour:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		Vector<TruecolourBitsEnum> trueColourOptions=new Vector<TruecolourBitsEnum>();
		trueColourOptions.add(TruecolourBitsEnum.BIT24);
		trueColourOptions.add(TruecolourBitsEnum.BIT21);
		trueColourOptions.add(TruecolourBitsEnum.BIT18);
		trueColourOptions.add(TruecolourBitsEnum.BIT15);
		trueColourOptions.add(TruecolourBitsEnum.BIT12);
		trueColourOptions.add(TruecolourBitsEnum.BIT9);
		trueColourOptions.add(TruecolourBitsEnum.BIT6);
		trueColourOptions.add(TruecolourBitsEnum.BIT3);
		comboBoxTruecolourOptions = new JComboBox<TruecolourBitsEnum>(trueColourOptions);
		comboBoxTruecolourOptions.setBounds(xField1,y,100,lineHight);
		comboBoxTruecolourOptions.setMaximumRowCount(5);
		comboBoxTruecolourOptions.addActionListener(actionListener);
		add(comboBoxTruecolourOptions);
		
		y+=lineDistance;
		
		checkboxConvertToPalette = new JCheckBox("convert truecolour to 256 colour palette image");
		checkboxConvertToPalette.setBounds(xLabel1,y,300,lineHight);
		checkboxConvertToPalette.addActionListener(actionListener);
		add(checkboxConvertToPalette);

		y+=lineDistance;
		
		y+=lineDistance2;
		
		y+=lineDistance;
		
		y+=lineDistance;
		
		//TODO: size of OptionsPngView
		setBounds(0,0,860,178);
		//setBackground(Color.BLUE);
		//System.out.println("OptionsPngView y: "+y);
	}
	
	public void refreshGui() {
		boolean animationDataIsAvailable=ganimedModel.isAnimationDataAvailable();
		jRadioButtonPngEncoder.setEnabled(animationDataIsAvailable);
		jRadioButtonImageIO.setEnabled(animationDataIsAvailable);
		checkboxReEncodePng.setEnabled(animationDataIsAvailable);
		checkboxConvertToPalette.setEnabled(animationDataIsAvailable);
		comboBoxTruecolourOptions.setEnabled(true);
		checkboxUseTransparentPixel.setEnabled(animationDataIsAvailable);
		comboBoxTransparentPixelQuantity.setEnabled(true);
		if(animationDataIsAvailable) {
			OptionsPngModel pngSettings=ganimedModel.getOptionsPngModel();
			jRadioButtonPngEncoder.setSelected(pngSettings.getPngEncoderType()==PngEncoderType.Ganimed);
			jRadioButtonImageIO.setSelected(pngSettings.getPngEncoderType()==PngEncoderType.Java);
			checkboxReEncodePng.setSelected(pngSettings.isPngReencodingEnabled());
			checkboxConvertToPalette.setSelected(pngSettings.isConvertToPaletteEnabled());
			comboBoxTruecolourOptions.setSelectedItem(pngSettings.getTrueColourBits());
			checkboxUseTransparentPixel.setSelected(pngSettings.isTransparentPixelEnabled());
			comboBoxTransparentPixelQuantity.setSelectedItem(pngSettings.getNumberOfTransparentPixel());
		}
	}

}
