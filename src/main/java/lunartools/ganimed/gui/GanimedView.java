package lunartools.ganimed.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ImageTools;
import lunartools.ObservableJFrame;
import lunartools.ScreenTools;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.editor.EditorView;
import lunartools.ganimed.panel.optionspanel.ImageType;
import lunartools.ganimed.panel.optionspanel.OptionsPanel;
import lunartools.ganimed.panel.selectionpanel.SelectionPanel;

public class GanimedView extends ObservableJFrame implements Observer{
	private static Logger logger = LoggerFactory.getLogger(GanimedView.class);
	private SelectionPanel jPanelSelect;
	private EditorView jPanelEditor;
	private OptionsPanel jPanelOptions;
	private JLabel labelStatus;
	private JProgressBar progressBar;

	public static final double SECTIOAUREA=1.6180339887;
	private static final int tabWidth=100;
	public static final int WINDOW_MINIMUM_WIDTH=760+tabWidth;
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
		this.model.getLoaderModel().addObserver(this);
		this.model.getEditorModel().addObserver(this);
		this.model.getOptionsGifModel().addObserver(this);
		this.model.getOptionsPngModel().addObserver(this);
		this.setLayout(null);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				sendMessage(SimpleEvents.EXIT);
			}
		});

		jPanelSelect=new SelectionPanel(model,this);
		jPanelEditor=new EditorView(model,this);
		jPanelOptions=new OptionsPanel(model,this);

		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		tabs.setSize(jPanelEditor.getWidth()+tabWidth,jPanelEditor.getHeight());
		//add(jPanelEditor);

		tabs.addTab("Select images", jPanelSelect);
		tabs.addTab("Editor", jPanelEditor);
		tabs.addTab("Options", jPanelOptions);
		//		add(tabs);
		getContentPane().add(tabs);

		//		KeyListener keyListener=new GanimedKeyListener(model,this);
		//		AdjustmentListener adjustmentlistener=new GanimedAdjustmentListener(model, this);
		//		
		int xLabel1=12;
		int xField1=90;
		int y=178;
		int lineHight=18;
		int lineDistance=21;

		JLabel label=new JLabel("Status:");
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
			this.setIconImage(ImageTools.createImage("/icons/ProgramIcon.png"));
		} catch (IOException e) {
			System.err.println("error loading frame icon");
			e.printStackTrace();
		}
		refreshGui();
	}

	@Override
	public void update(Observable observable,Object object){
		if(object==SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED) {
			refreshGui();
		}else if(object==SimpleEvents.MODEL_IMAGESIZECHANGED) {
			refreshGui();
		}else if(object.equals(SimpleEvents.MODEL_IMAGETYPECHANGED)) {
			menubarController.imageTypeChanged();
			jPanelOptions.refreshGui();
		}else if(object.equals(SimpleEvents.MODEL_IMAGESCHANGED)) {
			menubarController.menuItem_SaveAs.setEnabled(true);
			refreshGui();
			resizeFrame();
		}else if(object.equals(SimpleEvents.MODEL_REFRESH_SELECTION_GUI)) {
			logger.trace("received: "+SimpleEvents.MODEL_REFRESH_SELECTION_GUI);
			refreshSelectionGui();
		}else if(object.equals(SimpleEvents.MODEL_PNGOPTIONCHANGED)) {
			logger.trace("received: "+object);
			refreshOptionsGui();
		}
	}

	private void resizeFrame() {
		int requiredWidth=model.getImageWidth()+20;
		int requiredHeight=model.getImageHeight()+250+30;
		Rectangle rectangle=getBounds();
		if(rectangle.width<requiredWidth) {
			rectangle.width=requiredWidth;
		}
		if(rectangle.height<requiredHeight) {
			rectangle.height=requiredHeight;
		}
		setBounds(ScreenTools.getInstance().optimizeBounds(getBounds(),rectangle));
	}

	private void refreshGui() {
		jPanelSelect.refreshGui();
		jPanelEditor.refreshGui();
		jPanelOptions.refreshGui();
		clearStatus();
	}

	private void refreshSelectionGui() {
		jPanelSelect.refreshGui();
		clearStatus();
	}

	private void refreshOptionsGui() {
		jPanelOptions.refreshGui();
		clearStatus();
	}

	public void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public void showMessageboxAbout(){
		About.showAboutDialog(this);
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

	public void clearStatus() {
		labelStatus.setText("ready");
		labelStatus.setForeground(Color.BLUE);
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

	public SelectionPanel getjPanelSelect() {
		return jPanelSelect;
	}

	public EditorView getjPanelEditor() {
		return jPanelEditor;
	}

	public OptionsPanel getjPanelOptions() {
		return jPanelOptions;
	}

}
