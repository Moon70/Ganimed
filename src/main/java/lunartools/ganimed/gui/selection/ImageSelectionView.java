package lunartools.ganimed.gui.selection;

import java.awt.Color;
import java.awt.Scrollbar;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import lunartools.ImageTools;
import lunartools.ObservableJPanel;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.selection.model.ImageSelectionModel;

public class ImageSelectionView extends ObservableJPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private ImageSelectionModel imageSelectionModel;
	private ImageSelectionController imageSelectionController;
	
	JTextField textfieldImagesFps;
	Scrollbar scrollbarImagesFps;
	public JTextField textfieldFolderpath;
	JButton button;
	JButton buttonSelectCaptureRegion;
	JButton buttonStartCapture;
	JButton buttonStopCapture;

	public ImageSelectionView(GanimedModel ganimedModel, GanimedView ganimedView) {
		this.ganimedModel=ganimedModel;
		this.ganimedView=ganimedView;
		this.imageSelectionModel=ganimedModel.getImageSelectionModel();
		this.imageSelectionController=new ImageSelectionController(ganimedModel,imageSelectionModel);
		this.setLayout(null);

		KeyListener keyListener=new ImageSelectionKeyListener(imageSelectionModel,this);
		AdjustmentListener adjustmentlistener=new ImageSelectionAdjustmentListener(imageSelectionModel,this);
		ActionListener actionListener=new ImageSelectionActionListener(this);
		
		int y=4;
		int xLabel1=12;
		int wLabel1=90;
		int xField1=90;
		int lineHight=18;
		int lineDistance=21;
		int lineDistance2=27;
		int xScrollbar1=120;
		int sScrollbar1=300;
		int scrollHeight=14;
		int hButton=30;

		JLabel label=new JLabel("Images FPS:");
		label.setBounds(xLabel1,y,wLabel1,lineHight);
		add(label);
		textfieldImagesFps=new JTextField(200);
		textfieldImagesFps.setBounds(xField1,y,32,lineHight);
		textfieldImagesFps.addKeyListener(keyListener);
		add(textfieldImagesFps);
		scrollbarImagesFps=new Scrollbar(Scrollbar.HORIZONTAL,imageSelectionModel.getImagesFps(),1,1,0);
		scrollbarImagesFps.setBounds(xScrollbar1,y+4,sScrollbar1,scrollHeight);
		scrollbarImagesFps.setBackground(Color.DARK_GRAY);
		scrollbarImagesFps.setForeground(Color.BLUE);
		scrollbarImagesFps.addAdjustmentListener(adjustmentlistener);
		add(scrollbarImagesFps);

		y+=lineDistance;

		label=new JLabel("Images folder:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);

		textfieldFolderpath=new JTextField(200);
		textfieldFolderpath.setBounds(xField1,y,300,lineHight);
		textfieldFolderpath.addKeyListener(keyListener);
		add(textfieldFolderpath);
		button=new JButton();
		button.setBounds(xField1+300+4, y, 16, lineHight);
		button.setIcon(ImageTools.createImageIcon("/icons/Open16.gif"));
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.addActionListener(actionListener);
		add(button);

		y+=lineDistance2;
		
		buttonSelectCaptureRegion=new JButton("Select screen region");
		buttonSelectCaptureRegion.setBounds(xLabel1, y, 200,hButton);
		buttonSelectCaptureRegion.setIcon(ImageTools.createImageIcon("/icons/SelectCaptureRegion.gif"));
		buttonSelectCaptureRegion.setToolTipText("Select screen region to capture, ESC aborts selection");
		buttonSelectCaptureRegion.addActionListener(actionListener);
		add(buttonSelectCaptureRegion);

		buttonStartCapture=new JButton("Capture");
		buttonStartCapture.setBounds(xLabel1+200, y, 100,hButton);
		buttonStartCapture.setIcon(ImageTools.createImageIcon("/icons/StartCapture.gif"));
		buttonStartCapture.setToolTipText("Starts capturing");
		buttonStartCapture.addActionListener(actionListener);
		add(buttonStartCapture);

		buttonStopCapture=new JButton("Stop");
		buttonStopCapture.setBounds(xLabel1+200+100, y, 100,hButton);
		buttonStopCapture.setIcon(ImageTools.createImageIcon("/icons/StopCapture.gif"));
		buttonStopCapture.addActionListener(actionListener);
		add(buttonStopCapture);
	}

	public void refreshGui() {
		File folder=imageSelectionModel.getImageFolder();
		if(folder==null) {
			textfieldFolderpath.setText("");
		}else {
			textfieldFolderpath.setText(folder.getAbsolutePath());
		}
		
		textfieldImagesFps.setText(""+imageSelectionModel.getImagesFps());
		scrollbarImagesFps.setMinimum(imageSelectionModel.getImagesFpsMin());
		scrollbarImagesFps.setMaximum(imageSelectionModel.getImagesFpsMax()+1);
		scrollbarImagesFps.setValue(imageSelectionModel.getImagesFps());

		buttonStartCapture.setEnabled(imageSelectionController.isCaptureButtonEnabled());
		buttonStopCapture.setEnabled(imageSelectionController.isStopButtonEnabled());
	}

	public void selectImageFolder() {
		final JFileChooser fileChooser= new JFileChooser() {
			public void updateUI() {
				putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
				super.updateUI();
			}
		};
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(imageSelectionModel.getImageFolder()!=null) {
			fileChooser.setCurrentDirectory(imageSelectionModel.getImageFolder());
		}else {
			fileChooser.setCurrentDirectory(imageSelectionModel.getSavedImageFolder());
		}
		if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			File file=fileChooser.getSelectedFile();
			selectedImageFolder(file);
		}else {
			ganimedView.setStatusInfo("folder selection canceled");
		}
	}

	void selectedImageFolder(File file) {
		if(file.exists()) {
			imageSelectionModel.setImageFolder(file);
		}else {
			ganimedView.setStatusError("Folder does not exist: "+file.getAbsolutePath());
		}
	}

	public void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

}
