package lunartools.ganimed.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import abyss.lunarengine.tools.ObservableJFrame;
import lunartools.FileTools;
import lunartools.ScreenTools;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.imagetype.ImageType;

public class GanimedView extends ObservableJFrame implements Observer{
	protected JTextField textfieldFolderpath;
	protected JTextField textfieldImagesFps;
	protected JTextField textfieldAnimFps;
	protected JTextField textfieldAnimDelay;
	protected JTextField textfieldAnimEndDelay;
	protected Scrollbar scrollbarAnimFps;
	protected Scrollbar scrollbarAnimDelay;
	protected Scrollbar scrollbarAnimEndDelay;
	protected JTextField textfieldCropLeft;
	protected JTextField textfieldCropTop;
	protected Scrollbar scrollbarCropLeft;
	protected Scrollbar scrollbarCropTop;
	protected JTextField textfieldCropRight;
	protected JTextField textfieldCropBottom;
	protected Scrollbar scrollbarCropRight;
	protected Scrollbar scrollbarCropBottom;
	protected JTextField textfieldResize;
	protected Scrollbar scrollbarResize;
	private JLabel labelStatus;
	private JProgressBar progressBar;
	protected JButton button;
	
	public static final double SECTIOAUREA=1.6180339887;
	public static final int WINDOW_MINIMUM_WIDTH=860;
	public static final int WINDOW_MINIMUM_HEIGHT=(int)(WINDOW_MINIMUM_WIDTH/SECTIOAUREA);
	
	private GanimedModel model;
	
	private MenubarController menubarController;

	public GanimedView(GanimedModel gameModel){
		super.setTitle(GanimedModel.PROGRAMNAME+" "+GanimedModel.determineProgramVersion());
		setBounds(10,10,WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT);
		setMinimumSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
		setResizable(true);
		this.model=gameModel;
		this.model.addObserver(this);
		
		this.setLayout(null);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				sendMessage(SimpleEvents.EXIT);
			}
		});
		
		KeyListener keyListener=new GanimedKeyListener(model,this);
		AdjustmentListener adjustmentlistener=new GanimedAdjustmentListener(model, this);
		
		int xLabel1=12;
		int xField1=90;
		int xScrollbar1=120;
		int xLabel2=440;
		int xField2=440+78;
		int xScrollbar2=470+78;
		int y=0;
		int scrollHeight=14;
		int lineHight=18;
		int lineDistance=22;
		int lineDistance2=30;
		
		JLabel label=new JLabel("Image folder:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);

		textfieldFolderpath=new JTextField(200);
		textfieldFolderpath.setBounds(xField1,y,300,lineHight);
		textfieldFolderpath.addKeyListener(keyListener);
		add(textfieldFolderpath);
		button=new JButton();
		button.setBounds(xField1+300+0, y, 16, lineHight);
		button.setIcon(FileTools.createImageIcon("/icons/Open16.gif",this));
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.addActionListener(new GanimedViewActionListener(this));
		add(button);

		label=new JLabel("Images FPS:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldImagesFps=new JTextField(200);
		textfieldImagesFps.setBounds(xField2,y,32,lineHight);
		textfieldImagesFps.addKeyListener(keyListener);
		add(textfieldImagesFps);

		y+=lineDistance2;
		
		label=new JLabel("Crop top:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldCropTop=new JTextField(200);
		textfieldCropTop.setBounds(xField1,y,32,lineHight);
		textfieldCropTop.addKeyListener(keyListener);
		add(textfieldCropTop);
		scrollbarCropTop=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropTop.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarCropTop.setBackground(Color.DARK_GRAY);
		scrollbarCropTop.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropTop);

		label=new JLabel("Crop bottom:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldCropBottom=new JTextField(200);
		textfieldCropBottom.setBounds(xField2,y,32,lineHight);
		textfieldCropBottom.addKeyListener(keyListener);
		add(textfieldCropBottom);
		scrollbarCropBottom=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropBottom.setBounds(xScrollbar2,y+4,255+16+16,scrollHeight);
		scrollbarCropBottom.setBackground(Color.DARK_GRAY);
		scrollbarCropBottom.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropBottom);

		y+=lineDistance;
		
		label=new JLabel("Crop left:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldCropLeft=new JTextField(200);
		textfieldCropLeft.setBounds(xField1,y,32,lineHight);
		textfieldCropLeft.addKeyListener(keyListener);
		add(textfieldCropLeft);
		scrollbarCropLeft=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropLeft.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarCropLeft.setBackground(Color.DARK_GRAY);
		scrollbarCropLeft.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropLeft);

		label=new JLabel("Crop right:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldCropRight=new JTextField(200);
		textfieldCropRight.setBounds(xField2,y,32,lineHight);
		textfieldCropRight.addKeyListener(keyListener);
		add(textfieldCropRight);
		scrollbarCropRight=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropRight.setBounds(xScrollbar2,y+4,255+16+16,scrollHeight);
		scrollbarCropRight.setBackground(Color.DARK_GRAY);
		scrollbarCropRight.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropRight);

		y+=lineDistance;
		
		label=new JLabel("Resize %:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldResize=new JTextField(200);
		textfieldResize.setBounds(xField1,y,32,lineHight);
		textfieldResize.addKeyListener(keyListener);
		add(textfieldResize);
		scrollbarResize=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarResize.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarResize.setBackground(Color.DARK_GRAY);
		scrollbarResize.addAdjustmentListener(adjustmentlistener);
		add(scrollbarResize);

		y+=lineDistance2;
		
		label=new JLabel("Anim FPS:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldAnimFps=new JTextField(200);
		textfieldAnimFps.setBounds(xField1,y,32,lineHight);
		textfieldAnimFps.addKeyListener(keyListener);
		add(textfieldAnimFps);
		scrollbarAnimFps=new Scrollbar(Scrollbar.HORIZONTAL,model.getAnimFps(),1,1,0);
		scrollbarAnimFps.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarAnimFps.setBackground(Color.DARK_GRAY);
		scrollbarAnimFps.setForeground(Color.BLUE);
		scrollbarAnimFps.addAdjustmentListener(adjustmentlistener);
		add(scrollbarAnimFps);

		label=new JLabel("Anim delay:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldAnimDelay=new JTextField(4);
		textfieldAnimDelay.setBounds(xField2,y,32,lineHight);
		textfieldAnimDelay.setText(""+model.getAnimDelay());
		textfieldAnimDelay.addKeyListener(keyListener);
		add(textfieldAnimDelay);
		scrollbarAnimDelay=new Scrollbar(Scrollbar.HORIZONTAL,model.getAnimDelay(),1,16,1001);
		scrollbarAnimDelay.setBounds(xScrollbar2,y+4,255+16+16,scrollHeight);
		scrollbarAnimDelay.setBackground(Color.DARK_GRAY);
		scrollbarAnimDelay.addAdjustmentListener(adjustmentlistener);
		add(scrollbarAnimDelay);

		y+=lineDistance;
		
		label=new JLabel("End delay:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldAnimEndDelay=new JTextField(4);
		textfieldAnimEndDelay.setBounds(xField1,y,32,lineHight);
		textfieldAnimEndDelay.addKeyListener(keyListener);
		add(textfieldAnimEndDelay);
		scrollbarAnimEndDelay=new Scrollbar(Scrollbar.HORIZONTAL,model.getAnimEndDelay(),1,0,5001);
		scrollbarAnimEndDelay.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarAnimEndDelay.setBackground(Color.DARK_GRAY);
		scrollbarAnimEndDelay.addAdjustmentListener(adjustmentlistener);
		add(scrollbarAnimEndDelay);

		y+=lineDistance;
		
		label=new JLabel("Status:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		labelStatus=new JLabel();
		labelStatus.setBounds(xField1,y,750,lineHight);
		labelStatus.setVisible(true);
		add(labelStatus);

		progressBar = new JProgressBar(0, 200);
		progressBar.setBounds(xField1, y, 730, lineHight);
		progressBar.setValue(10);
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		add(progressBar);
		
		y+=lineDistance;

		this.menubarController=new MenubarController(this.model,this);
		setMenuBar(this.menubarController.createMenubar());

		try {
			this.setIconImage(FileTools.createImage("/icons/ProgramIcon.png",this));
		} catch (IOException e) {
			System.err.println("error loading frame icon");
			e.printStackTrace();
		}
		refreshGui();
	}

	@Override
	public void update(Observable observable,Object object){
		if(observable instanceof GanimedModel) {
			if(object==SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED) {
				refreshAnimPlaybackValues();
			}else if(object==SimpleEvents.MODEL_IMAGESIZECHANGED) {
				refreshGui();
			}else if(object.equals(SimpleEvents.MODEL_IMAGESCHANGED)) {
				menubarController.menuItem_SaveAs.setEnabled(true);
				refreshGui();
				resizeFrame();
			}
		}
	}
	
	private void resizeFrame() {
		int requiredWidth=model.getImageWidth()+20;
		int requiredHeight=model.getImageHeight()+250;
		Rectangle rectangle=getBounds();
		if(rectangle.width<requiredWidth) {
			rectangle.width=requiredWidth;
		}
		if(rectangle.height<requiredHeight) {
			rectangle.height=requiredHeight;
		}
		setBounds(ScreenTools.getInstance().optimizeBounds(getBounds(),rectangle));
	}
	
	private void refreshAnimPlaybackValues() {
		textfieldImagesFps.setText(""+model.getImagesFps());
		textfieldAnimFps.setText(""+model.getAnimFps());
		scrollbarAnimFps.setMaximum(model.getAnimFpsMax());
		scrollbarAnimFps.setValue(model.getAnimFps());
		textfieldAnimDelay.setText(""+model.getAnimDelay());
		scrollbarAnimDelay.setValue(model.getAnimDelay());
		textfieldAnimEndDelay.setText(""+model.getAnimEndDelay());
		scrollbarAnimEndDelay.setMaximum(model.getAnimEndDelayMax());
		scrollbarAnimEndDelay.setValue(model.getAnimEndDelay());
	}
	
	private void refreshGui() {
		if(model.getImageFolder()==null) {
			textfieldImagesFps.setEnabled(false);
			textfieldCropLeft.setEnabled(false);
			scrollbarCropLeft.setEnabled(false);
			textfieldCropRight.setEnabled(false);
			scrollbarCropRight.setEnabled(false);
			textfieldCropTop.setEnabled(false);
			scrollbarCropTop.setEnabled(false);
			textfieldCropBottom.setEnabled(false);
			scrollbarCropBottom.setEnabled(false);
			textfieldResize.setEnabled(false);
			scrollbarResize.setEnabled(false);
			textfieldAnimFps.setEnabled(false);
			scrollbarAnimFps.setEnabled(false);
			textfieldAnimDelay.setEnabled(false);
			scrollbarAnimDelay.setEnabled(false);
			textfieldAnimEndDelay.setEnabled(false);
			scrollbarAnimEndDelay.setEnabled(false);
			
			setStatusInfo("READY, select an image folder");
		}else {
			textfieldImagesFps.setEnabled(true);
			textfieldCropLeft.setEnabled(true);
			scrollbarCropLeft.setEnabled(true);
			textfieldCropRight.setEnabled(true);
			scrollbarCropRight.setEnabled(true);
			textfieldCropTop.setEnabled(true);
			scrollbarCropTop.setEnabled(true);
			textfieldCropBottom.setEnabled(true);
			scrollbarCropBottom.setEnabled(true);
			textfieldResize.setEnabled(true);
			scrollbarResize.setEnabled(true);
			textfieldAnimFps.setEnabled(true);
			scrollbarAnimFps.setEnabled(true);
			textfieldAnimDelay.setEnabled(true);
			scrollbarAnimDelay.setEnabled(true);
			textfieldAnimEndDelay.setEnabled(true);
			scrollbarAnimEndDelay.setEnabled(true);
			
		}
		scrollbarCropLeft.setMinimum(model.getCropLeftMin());
		scrollbarCropLeft.setMaximum(model.getCropLeftMax());
		scrollbarCropTop.setMinimum(model.getCropTopMin());
		scrollbarCropTop.setMaximum(model.getCropTopMax());
		
		scrollbarCropRight.setMinimum(model.getCropRightMin());
		scrollbarCropRight.setMaximum(model.getCropRightMax());
		scrollbarCropBottom.setMinimum(model.getCropBottomMin());
		scrollbarCropBottom.setMaximum(model.getCropBottomMax());
		
		scrollbarResize.setMinimum(model.getResizeMin());
		scrollbarResize.setMaximum(model.getResizeMax());
		
		textfieldCropLeft.setText(""+model.getCropLeft());
		scrollbarCropLeft.setValue(model.getCropLeft());
		textfieldCropTop.setText(""+model.getCropTop());
		scrollbarCropTop.setValue(model.getCropTop());
		textfieldCropRight.setText(""+model.getCropRight());
		scrollbarCropRight.setValue(model.getCropRight());
		textfieldCropBottom.setText(""+model.getCropBottom());
		scrollbarCropBottom.setValue(model.getCropBottom());
		textfieldResize.setText(""+model.getResizePercent());
		scrollbarResize.setValue(model.getResizePercent());

	}
	
	public void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public void showMessageboxAbout(){
		About.showAboutDialog(this);
	}
	
	public void selectImageFolder() {
		final JFileChooser fileChooser= new JFileChooser() {
			 public void updateUI() {
                 putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
                 super.updateUI();
             }
		};
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(model.getImageFolder()!=null) {
			fileChooser.setCurrentDirectory(model.getImageFolder());
		}else {
			fileChooser.setCurrentDirectory(model.getLastImageFolder());
		}
		if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			File file=fileChooser.getSelectedFile();
			textfieldFolderpath.setText(file.getAbsolutePath());
			selectedImageFolder(file);
		}else {
			setStatusInfo("folder selection canceled");
		}
	}
	
	public void saveAs() {
		final JFileChooser fileChooser= new JFileChooser() {
			 public void updateUI() {
                 putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
                 super.updateUI();
             }
		};
		fileChooser.setAcceptAllFileFilterUsed(false);
		for(ImageType imageType:model.getImageTypes()) {
			FileFilter fileFilter=new ImageFileFilter(imageType.getName(),imageType.getFileExtension());
			fileChooser.addChoosableFileFilter(fileFilter);
			if(imageType == model.getImageType()) {
				fileChooser.setFileFilter(fileFilter);
			}
		}
		fileChooser.setCurrentDirectory(model.getAnimFolder());
		File lastfile=model.getAnimFile();
		if(lastfile!=null && lastfile.getName().endsWith(model.getImageType().getFileExtension())) {
			fileChooser.setSelectedFile(lastfile);
		}
		int rc=fileChooser.showSaveDialog(this);
		if(rc==JFileChooser.APPROVE_OPTION) {
			File fileAnim=fileChooser.getSelectedFile();
			if(!fileAnim.getName().contains(".")) {
				fileAnim=new File(fileAnim.getParentFile(),fileAnim.getName()+model.getImageType().getFileExtension());
			}
			if(fileAnim.exists()) {
				String imageTypename=model.getImageType().getName();
				if(JOptionPane.showOptionDialog(this, "File already exists, OK to overwrite?\n"+fileAnim.getAbsolutePath(), "Ganimed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null)!=JOptionPane.OK_OPTION) {
					setStatusInfo("save "+imageTypename+" animation canceled");
					return;
				}
			}
			model.setAnimFile(fileAnim);
			model.setAnimFolder(fileChooser.getSelectedFile().getParentFile().getAbsolutePath());
			sendMessage(SimpleEvents.SAVEAS);
		}
	}

	protected void selectedImageFolder(File file) {
		if(file.exists()) {
			model.setImageFolder(file);
		}else {
			setStatusError("Folder does not exist: "+file.getAbsolutePath());
		}
	}
	
	public void setStatusInfo(String message) {
		labelStatus.setText(message);
		labelStatus.setForeground(Color.BLUE);
	}
	
	public void setStatusError(String message) {
		labelStatus.setText(message);
		labelStatus.setForeground(Color.RED);
	}
	
	public void setProgressBarValue(int value) {
		progressBar.setValue(value);
	}
	
	public void setProgressBarValue(int value, String message) {
		progressBar.setValue(value);
		progressBar.setString(message);
	}
	
	public void enableProgressBar(int steps) {
		labelStatus.setVisible(false);
		progressBar.setValue(0);
		progressBar.setMaximum(steps);
		progressBar.setVisible(true);
		
	}

	public void disableProgressBar() {
		labelStatus.setVisible(true);
		progressBar.setVisible(false);
	}
	
}
