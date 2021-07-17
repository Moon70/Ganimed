package lunartools.ganimed.gui.editor.model;

import java.util.Observable;

import lunartools.ganimed.AnimationData;
import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.SimpleEvents;

public class EditorModel extends Observable{
	private static final int MINIMUM_ANIM_FRAMECOUNT=1;
	private static final int MINIMUM_ANIM_SIZE=16;
	private GanimedModel ganimedModel;
	private int cutLeft;
	private int cutLeftMin;
	private int cutLeftMax;
	private int cutRight;
	private int cutRightMin;
	private int cutRightMax;
	private int cropTop;
	private int cropTopMin;
	private int cropTopMax;
	private int cropBottom;
	private int cropBottomMin;
	private int cropBottomMax;
	private int cropLeft;
	private int cropLeftMin;
	private int cropLeftMax;
	private int cropRight;
	private int cropRightMin;
	private int cropRightMax;
	private int resizePercent;
	private int resizeMin;
	private int resizeMax;
	private int animFps;
	private int animDelay;
	private int animEndDelay;
	private final int animEndDelayMaxDefault=5000;
	private final int animEndDelayMaxMax=216000;
	private int animEndDelayMax=animEndDelayMaxDefault;

	public EditorModel(GanimedModel ganimedModel) {
		this.ganimedModel=ganimedModel;
	}

	public void sendMessage(Object message) {
		setChanged();
		notifyObservers(message);
	}

	public void setCutLeft(int cutLeft) {
		if(cutLeft==this.cutLeft) {
			return;
		}else if(cutLeft<cutLeftMin) {
			this.cutLeft = cutLeftMin;
		}else if(cutLeft>cutLeftMax) {
			this.cutLeft=cutLeftMax;
		}else {
			this.cutLeft = cutLeft;
		}
		cutRightMin=cutLeft+MINIMUM_ANIM_FRAMECOUNT;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getCutLeft() {
		return cutLeft;
	}

	public int getCutLeftMin() {
		return cutLeftMin;
	}

	public int getCutLeftMax() {
		return cutLeftMax;
	}

	public void setCutRight(int cutRight) {
		if(cutRight==this.cutRight) {
			return;
		}else if(cutRight<cutRightMin) {
			this.cutRight = cutRightMin;
		}else if(cutRight>cutRightMax) {
			this.cutRight=cutRightMax;
		}else {
			this.cutRight = cutRight;
		}
		cutLeftMax=cutRight-MINIMUM_ANIM_FRAMECOUNT;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getCutRight() {
		return cutRight;
	}

	public int getCutRightMin() {
		return cutRightMin;
	}

	public int getCutRightMax() {
		return cutRightMax;
	}

	//TODO: refactor
	public void reset(AnimationData animationData) {
		cutLeft=1;
		cutLeftMin=1;
		cutLeftMax=animationData.logicalSize()-MINIMUM_ANIM_FRAMECOUNT;
		cutRight=animationData.logicalSize();
		cutRightMin=1+MINIMUM_ANIM_FRAMECOUNT;
		cutRightMax=animationData.logicalSize()+1;
		
		cropTop=1;
		cropTopMin=1;
		cropTopMax=animationData.getHeight()-MINIMUM_ANIM_SIZE;
		cropBottom=animationData.getHeight();
		cropBottomMin=MINIMUM_ANIM_SIZE;
		cropBottomMax=animationData.getHeight();
		cropLeft=1;
		cropLeftMin=1;
		cropLeftMax=animationData.getWidth()-MINIMUM_ANIM_SIZE;
		cropRight=animationData.getWidth();
		cropRightMin=MINIMUM_ANIM_SIZE;
		cropRightMax=animationData.getWidth();
		resizePercent=100;
		resizeMin=1;
		resizeMax=101;
		if(animFps==0) {
			setAnimFps(ganimedModel.getImageSelectionModel().getImagesFps()>>1);
		}
	}

	public void setCropTop(int cropTop) {
		if(cropTop==this.cropTop) {
			return;
		}else if(cropTop<cropTopMin) {
			this.cropTop = cropTopMin;
		}else if(cropTop>cropTopMax) {
			this.cropTop=cropTopMax;
		}else {
			this.cropTop = cropTop;
		}
		cropBottomMin=cropTop+MINIMUM_ANIM_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getCropTop() {
		return cropTop;
	}

	public int getCropTopMin() {
		return cropTopMin;
	}

	public int getCropTopMax() {
		return cropTopMax;
	}

	public void setCropBottom(int cropBottom) {
		if(cropBottom==this.cropBottom) {
			return;
		}else if(cropBottom<cropBottomMin) {
			this.cropBottom = cropBottomMin;
		}else if(cropBottom>cropBottomMax) {
			this.cropBottom=cropBottomMax;
		}else {
			this.cropBottom = cropBottom;
		}
		cropTopMax=cropBottom-MINIMUM_ANIM_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getCropBottom() {
		return cropBottom;
	}

	public int getCropBottomMin() {
		return cropBottomMin;
	}

	public int getCropBottomMax() {
		return cropBottomMax;
	}

	public void setCropLeft(int cropLeft) {
		if(cropLeft==this.cropLeft) {
			return;
		}else if(cropLeft<cropLeftMin) {
			this.cropLeft = cropLeftMin;
		}else if(cropLeft>cropLeftMax) {
			this.cropLeft=cropLeftMax;
		}else {
			this.cropLeft = cropLeft;
		}
		cropRightMin=cropLeft+MINIMUM_ANIM_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getCropLeft() {
		return cropLeft;
	}

	public int getCropLeftMin() {
		return cropLeftMin;
	}

	public int getCropLeftMax() {
		return cropLeftMax;
	}

	public void setCropRight(int cropRight) {
		if(cropRight==this.cropRight) {
			return;
		}else if(cropRight<cropRightMin) {
			this.cropRight = cropRightMin;
		}else if(cropRight>cropRightMax) {
			this.cropRight=cropRightMax;
		}else {
			this.cropRight = cropRight;
		}
		cropLeftMax=cropRight-MINIMUM_ANIM_SIZE;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getCropRight() {
		return cropRight;
	}

	public int getCropRightMin() {
		return cropRightMin;
	}

	public int getCropRightMax() {
		return cropRightMax;
	}

	public void setResizePercent(int resizePercent) {
		if(resizePercent==this.resizePercent) {
			return;
		}
		if(resizePercent<1) {
			resizePercent=1;
		}
		if(resizePercent>100) {
			resizePercent=100;
		}
		this.resizePercent = resizePercent;
		sendMessage(SimpleEvents.MODEL_IMAGESIZECHANGED);
	}

	public int getResizePercent() {
		return resizePercent;
	}

	public int getResizeMin() {
		return resizeMin;
	}

	public int getResizeMax() {
		return resizeMax;
	}

	public void setAnimFps(int fps) {
		if(this.animFps==fps) {
			return;
		}
		if(fps<1) {
			fps=1;
		}
		int imagesFps=ganimedModel.getImageSelectionModel().getImagesFps();
		if(fps>imagesFps) {
			fps=imagesFps;
		}
		this.animFps=fps;
		//TODO: refactor
		ganimedModel.calculateAnimPlaybackParameter();
	}

	public int getAnimFps() {
		return animFps;
	}

	public int getAnimFpsMin() {
		return 1;
	}

	public int getAnimFpsMax() {
		return ganimedModel.getImageSelectionModel().getImagesFps()+1;
	}

	public void setAnimDelay(int delay) {
		if(this.animDelay==delay) {
			return;
		}
		if(delay<16) {
			delay=16;
		}
		if(delay>1000) {
			delay=1000;
		}
		this.animDelay=delay;
		sendMessage(SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED);
	}

	public int getAnimDelay() {
		return animDelay;
	}
	//TODO: getAnimDelayMin()

	//TODO: getAnimDelayMax()


	public void setAnimEndDelay(int delay) {
		if(this.animEndDelay==delay) {
			return;
		}
		if(delay<0) {
			delay=0;
		}
		if(delay>animEndDelayMaxMax) {
			delay=animEndDelayMaxMax;
		}
		this.animEndDelay=delay;
		sendMessage(SimpleEvents.MODEL_ANIMPLAYBACKVALUESCHANGED);
	}

	public int getAnimEndDelay() {
		return animEndDelay;
	}

	//TODO: getAnimEndDelayMin()

	public int getAnimEndDelayMax() {
		return animEndDelayMax;
	}

}
