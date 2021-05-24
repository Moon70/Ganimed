package lunartools.ganimed.gui.capture;

import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.capture.model.CaptureModel;

@SuppressWarnings("unused")
public class CaptureView extends JPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private CaptureModel captureModel;

	public JTextField textfieldFolderpath;
	JButton button;
	JTextField textfieldImagesFps;

	public CaptureView(GanimedModel ganimedModel, GanimedView ganimedView) {
		this.ganimedModel=ganimedModel;
		this.ganimedView=ganimedView;
		this.captureModel=ganimedModel.getCaptureModel();
		this.setLayout(null);

		KeyListener keyListener=new CaptureKeyListener(captureModel,this);

		int y=4;
		int xLabel1=12;
		int xField1=90;
		int lineHight=18;
		int lineDistance=21;

		JLabel label=new JLabel("Animation capture not implemented yet");
		label.setBounds(xLabel1,y,300,lineHight);
		add(label);

		y+=lineDistance;

		//TODO: size of CaptureView
		setBounds(0,0,860,y);
		//System.out.println("CaptureView y: "+y);
	}

	public void refreshGui() {
	}

}
