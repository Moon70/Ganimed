package lunartools.ganimed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import lunartools.ganimed.gui.SimpleEvents;
import lunartools.ganimed.gui.capture.model.CaptureModel;
import lunartools.ganimed.gui.editor.model.EditorModel;
import lunartools.ganimed.gui.loader.model.LoaderModel;
import lunartools.ganimed.gui.optionsgif.model.OptionsGifModel;
import lunartools.ganimed.gui.optionspng.model.OptionsPngModel;
import lunartools.ganimed.panel.optionspanel.ImageType;
import lunartools.ganimed.panel.selectionpanel.SelectionType;

public class GanimedModel extends Observable{
	public static final String PROGRAMNAME = "Ganimed";

	private SelectionType selectionType;
	private ImageType imageType;

	private File fileAnimFolder;
	private File fileAnim;

	private double numberOfImagesToSkip;

	private int imageWidth;
	private int imageHeight;

	private AnimationData bufferedImages;
	private byte[] bytearrayAnim;
	private static String versionProgram;
	private ArrayList<ImageType> imageTypes;

	private LoaderModel loaderModel;
	private CaptureModel captureModel;
	private EditorModel editorModel;
	private OptionsPngModel optionsPngModel;
	private OptionsGifModel optionsGifModel;

	public GanimedModel() {
		loaderModel=new LoaderModel(this);
		captureModel=new CaptureModel(this);
		editorModel=new EditorModel(this);
		setSelectionType(SelectionType.LOAD);
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
		this.bufferedImages=animationData;
		if(animationData!=null) {
			this.imageWidth=animationData.getWidth();
			this.imageHeight=animationData.getHeight();
			//			cutLeft=1;
			//			cutLeftMin=1;
			//			cutLeftMax=animationData.size()-MINIMUM_ANIM_FRAMECOUNT;
			//			cutRight=animationData.size();
			//			cutRightMin=1+MINIMUM_ANIM_FRAMECOUNT;
			//			cutRightMax=animationData.size();
			editorModel.reset(animationData);
			//			cropLeft=0;
			//			cropLeftMin=0;
			//			cropLeftMax=this.imageWidth-MINIMUM_ANIM_SIZE;
			//			cropTop=0;
			//			cropTopMin=0;
			//			cropTopMax=this.imageHeight-MINIMUM_ANIM_SIZE;
			//			cropRight=this.imageWidth;
			//			cropRightMin=MINIMUM_ANIM_SIZE;
			//			cropRightMax=this.imageWidth;
			//			cropBottom=this.imageHeight;
			//			cropBottomMin=0;
			//			cropBottomMax=this.imageHeight-MINIMUM_ANIM_SIZE;
			//			resizePercent=100;
			//			resizeMin=1;
			//			resizeMax=101;
			//			if(animFps==0) {
			//				setAnimFps(loaderModel.getImagesFps()>>1);
			//			}
			sendMessage(SimpleEvents.MODEL_IMAGESCHANGED);
		}
	}

	public AnimationData getAnimationData() {
		return bufferedImages;
	}

	@Deprecated
	public ImageData[] getImageDataArray() {
		return bufferedImages==null?null:bufferedImages.getAsArray();
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

	public SelectionType getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(SelectionType selectionType) {
		if(this.selectionType==selectionType) {
			return;
		}
		this.selectionType = selectionType;
		sendMessage(SimpleEvents.MODEL_SELECTIONTYPECHANGED);
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
		return loaderModel.getImageFolder()!=null;
	}

	public LoaderModel getLoaderModel() {
		return loaderModel;
	}

	public CaptureModel getCaptureModel() {
		return captureModel;
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
			setNumberOfImagesToSkip(1.0*getLoaderModel().getImagesFps()/animFps);
		}
		sendMessage(SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED);
	}

}
