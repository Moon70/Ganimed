package lunartools.ganimed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ganimed.gui.SimpleEvents;
import lunartools.ganimed.gui.editor.model.EditorModel;
import lunartools.ganimed.gui.optionsgif.model.OptionsGifModel;
import lunartools.ganimed.gui.optionspng.model.OptionsPngModel;
import lunartools.ganimed.gui.selection.model.ImageSelectionModel;
import lunartools.ganimed.panel.optionspanel.ImageType;

public class GanimedModel extends Observable{
	private static Logger logger = LoggerFactory.getLogger(GanimedModel.class);
	public static final String PROGRAMNAME = "Ganimed";

	private ImageType imageType;

	private File fileAnimFolder;
	private File fileAnim;
	private File fileRawImages;
	private File fileImages;

	private double numberOfImagesToSkip;

	private int imageWidth;
	private int imageHeight;

	private AnimationData animationData;
	private byte[] bytearrayAnim;
	private static String versionProgram;
	private ArrayList<ImageType> imageTypes;

	private ImageSelectionModel imageSelectionModel;
	private EditorModel editorModel;
	private OptionsPngModel optionsPngModel;
	private OptionsGifModel optionsGifModel;

	public GanimedModel() {
		imageSelectionModel=new ImageSelectionModel(this);
		editorModel=new EditorModel(this);
		setImageType(ImageType.GIF);
		optionsPngModel=new OptionsPngModel();
		optionsGifModel=new OptionsGifModel();
	}

	public ArrayList<ImageType> getImageTypes(){
		if(imageTypes==null) {
			imageTypes=new ArrayList<ImageType>();
			imageTypes.add(ImageType.GIF);
			imageTypes.add(ImageType.PNG);
		}
		return imageTypes;
	}

	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
	}

	public void setAnimFolder(String path) {
		if(path!=null) {
			this.fileAnimFolder=new File(path);
		}
	}

	public File getAnimFolder() {
		return fileAnimFolder;
	}

	public void setAnimFile(File file) {
		this.fileAnim=file;
		int p=file.getName().lastIndexOf('.');
		if(p!=-1) {
			String fileextension=file.getName().substring(p);
			for(ImageType imageType:imageTypes) {
				if(imageType.getFileExtension().equalsIgnoreCase(fileextension)) {
					setImageType(imageType);
					break;
				}
			}
		}
	}

	public File getRawImagesFile() {
		return fileRawImages;
	}

	public void setRawImagesFile(File file) {
		this.fileRawImages=file;
	}

	public File getImagesFile() {
		return fileImages;
	}

	public void setImagesFile(File file) {
		this.fileImages=file;
	}

	public File getAnimFile() {
		return fileAnim;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	private void setNumberOfImagesToSkip(double number) {
		if(this.numberOfImagesToSkip==number) {
			return;
		}
		this.numberOfImagesToSkip=number;
		sendMessage(SimpleEvents.MODEL_ANIMNUMBEROFIMAGESCHANGED);
	}

	public double getNumberOfImagesToSkip() {
		return numberOfImagesToSkip;
	}

	public void setImageData(AnimationData animationData) {
		this.animationData=animationData;
		logger.trace("setAnimationData: "+this.animationData);
		if(animationData!=null) {
			this.imageWidth=animationData.getWidth();
			this.imageHeight=animationData.getHeight();
			editorModel.reset(animationData);
			sendMessage(SimpleEvents.MODEL_IMAGESCHANGED);
		}
	}

	public AnimationData getAnimationData() {
		return animationData;
	}

	private void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public byte[] getBytearrayAnim() {
		return bytearrayAnim;
	}

	public void setBytearrayAnim(byte[] bytearrayAnim) {
		this.bytearrayAnim = bytearrayAnim;
		sendMessage(SimpleEvents.MODEL_ANIMBYTEARRAYCHANGED);
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		if(this.imageType==imageType) {
			return;
		}
		this.imageType = imageType;
		sendMessage(SimpleEvents.MODEL_IMAGETYPECHANGED);
	}

	public static String determineProgramVersion() {
		if(versionProgram==null) {
			versionProgram="";
			Properties properties = new Properties();
			InputStream inputStream=GanimedModel.class.getClassLoader().getResourceAsStream("project.properties");
			if(inputStream==null) {
				System.err.println("project.properties not found");
			}else {
			}
			try {
				properties.load(inputStream);
				versionProgram=properties.getProperty("version");
			} catch (IOException e) {
				System.err.println("error loading project.properties");
				e.printStackTrace();
			}
			if("${project.version}".equals(versionProgram)) {
				versionProgram="";
			}
		}
		return versionProgram;
	}

	public IOptionsModel getCurrentOptionsModel() {
		if(imageType==ImageType.GIF) {
			return optionsGifModel;
		}else if(imageType==ImageType.PNG) {
			return optionsPngModel;
		}else {
			return null;
		}
	}

	public OptionsPngModel getOptionsPngModel() {
		return optionsPngModel;
	}

	public OptionsGifModel getOptionsGifModel() {
		return optionsGifModel;
	}

	//TODO: ask AnimationData if animation data is available, or better move this method to AnimationData...
	public boolean isAnimationDataAvailable() {
		return animationData!=null && animationData.size()>0;
	}

	public ImageSelectionModel getImageSelectionModel() {
		return imageSelectionModel;
	}

	public EditorModel getEditorModel() {
		return editorModel;
	}

	public void calculateAnimPlaybackParameter() {
		int animFps=editorModel.getAnimFps();
		if(animFps==0) {
			numberOfImagesToSkip=0;
			editorModel.setAnimDelay(0);
		}else {
			editorModel.setAnimDelay((int)(1000.0/animFps+0.5));
			setNumberOfImagesToSkip(1.0*getImageSelectionModel().getImagesFps()/animFps);
		}
		sendMessage(SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED);
	}

}
