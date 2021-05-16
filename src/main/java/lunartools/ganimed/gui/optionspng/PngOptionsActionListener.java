package lunartools.ganimed.gui.optionspng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.optionspng.model.PngEncoderType;
import lunartools.ganimed.gui.optionspng.model.TransparentPixelEnum;
import lunartools.ganimed.gui.optionspng.model.TruecolourBitsEnum;

public class PngOptionsActionListener implements ActionListener{
	private GanimedModel ganimedModel;
	private OptionsPngView optionsPngView;
	
	public PngOptionsActionListener(GanimedModel ganimedModel,OptionsPngView optionsPngView) {
		this.ganimedModel=ganimedModel;
		this.optionsPngView=optionsPngView;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source=actionEvent.getSource();
		if(source.equals(optionsPngView.checkboxConvertToPalette)) {
			ganimedModel.getOptionsPngModel().enableConvertToPalette(((JCheckBox)source).isSelected());
		}else if(source.equals(optionsPngView.comboBoxTruecolourOptions)) {
			@SuppressWarnings("unchecked")
			JComboBox<TruecolourBitsEnum> jComboBox=(JComboBox<TruecolourBitsEnum>)source;
			TruecolourBitsEnum truecolourBitsEnum=(TruecolourBitsEnum)jComboBox.getSelectedItem();
			ganimedModel.getOptionsPngModel().setTrueColourBits(truecolourBitsEnum);
		}else if(source.equals(optionsPngView.checkboxUseTransparentPixel)) {
			ganimedModel.getOptionsPngModel().enableTransparentPixel(((JCheckBox)source).isSelected());
		}else if(source.equals(optionsPngView.comboBoxTransparentPixelQuantity)) {
			@SuppressWarnings("unchecked")
			JComboBox<TransparentPixelEnum> jComboBox=(JComboBox<TransparentPixelEnum>)source;
			TransparentPixelEnum transparentPixelEnum=(TransparentPixelEnum)jComboBox.getSelectedItem();
			ganimedModel.getOptionsPngModel().setNumberOfTransparentPixel(transparentPixelEnum);
		}else if(source.equals(optionsPngView.jRadioButtonPngEncoder)) {
			ganimedModel.getOptionsPngModel().setPngEncoderType(PngEncoderType.Ganimed);
		}else if(source.equals(optionsPngView.jRadioButtonImageIO)) {
			ganimedModel.getOptionsPngModel().setPngEncoderType(PngEncoderType.Java);
		}else if(source.equals(optionsPngView.checkboxReEncodePng)) {
			ganimedModel.getOptionsPngModel().enablePngReencoding(((JCheckBox)source).isSelected());
		}
	}

}