package lunartools.ganimed.gui.loader;

import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lunartools.ImageTools;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.loader.model.LoaderModel;

public class LoaderView extends JPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private LoaderModel loaderModel;
	
	public JTextField textfieldFolderpath;
	JButton button;
	JTextField textfieldImagesFps;

	public LoaderView(GanimedModel ganimedModel, GanimedView ganimedView) {
		this.ganimedModel=ganimedModel;
		this.ganimedView=ganimedView;
		this.loaderModel=ganimedModel.getLoaderModel();
		this.setLayout(null);
		
		KeyListener keyListener=new LoaderKeyListener(loaderModel,this);
		
		int y=4;
		int xLabel1=12;
		int xField1=90;
		int lineHight=18;
		int lineDistance=22;
		
		JLabel label=new JLabel("Image folder:");
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
		button.addActionListener(new LoaderActionListener(this));
		add(button);

		y+=lineDistance;

		label=new JLabel("Images FPS:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldImagesFps=new JTextField(200);
		textfieldImagesFps.setBounds(xField1,y,32,lineHight);
		textfieldImagesFps.addKeyListener(keyListener);
		add(textfieldImagesFps);

		y+=lineDistance;

		//TODO: size of LoaderView
		setBounds(0,0,860,y);
		//System.out.println("LoaderView y: "+y);
	}
	
	public void refreshGui() {
		File folder=loaderModel.getImageFolder();
		if(folder==null) {
			textfieldFolderpath.setText("");
		}else {
			textfieldFolderpath.setText(folder.getAbsolutePath());
		}
		textfieldImagesFps.setText(""+loaderModel.getImagesFps());
	}

	public void selectImageFolder() {
		final JFileChooser fileChooser= new JFileChooser() {
			 public void updateUI() {
                 putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
                 super.updateUI();
             }
		};
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(loaderModel.getImageFolder()!=null) {
			fileChooser.setCurrentDirectory(loaderModel.getImageFolder());
		}else {
			fileChooser.setCurrentDirectory(loaderModel.getSavedImageFolder());
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
			loaderModel.setImageFolder(file);
		}else {
			ganimedView.setStatusError("Folder does not exist: "+file.getAbsolutePath());
		}
	}
	
}
