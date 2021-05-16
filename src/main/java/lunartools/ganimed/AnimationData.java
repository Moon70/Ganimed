package lunartools.ganimed;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class AnimationData {
	private GanimedModel ganimedModel;
	private Vector<ImageData> imageDatas=new Vector<ImageData>();
	private int minWidth=Integer.MAX_VALUE;
	private int maxWidth=Integer.MIN_VALUE;
	private int minHeight=Integer.MAX_VALUE;
	private int maxHeight=Integer.MIN_VALUE;
	
	public AnimationData(GanimedModel ganimedModel) {
		this.ganimedModel=ganimedModel;
	}
	
	public GanimedModel getGanimedModel() {
		return ganimedModel;
	}
	
	public void addFile(File file) throws IOException {
		ImageData imageData=new ImageData(this,file);
		imageDatas.add(imageData);
		BufferedImage bufferedImage=imageData.getBufferedImage();
		int width=bufferedImage.getWidth();
		int height=bufferedImage.getHeight();
		if(minWidth>width) {
			minWidth=width;
		}
		if(maxWidth<width) {
			maxWidth=width;
		}
		if(minHeight>height) {
			minHeight=height;
		}
		if(maxHeight<height) {
			maxHeight=height;
		}
	}
	
	public int getWidth() {
		return maxWidth;
	}
	
	public int getHeight() {
		return maxHeight;
	}
	
	public int size() {
		return imageDatas.size();
	}
	
	public ImageData getImageData(int index) {
		return imageDatas.get(index);
	}
	
	@Deprecated
	public ImageData[] getAsArray() {
		return imageDatas.toArray(new ImageData[0]);
	}
}
