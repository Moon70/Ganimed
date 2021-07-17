package lunartools.ganimed;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class AnimationData {
	private GanimedModel ganimedModel;
	private Vector<ImageData> imageDatas=new Vector<ImageData>();
	private ArrayList<Integer> imageDataIndex=new ArrayList<Integer>();
	private int minWidth=Integer.MAX_VALUE;
	private int maxWidth=Integer.MIN_VALUE;
	private int minHeight=Integer.MAX_VALUE;
	private int maxHeight=Integer.MIN_VALUE;
	private boolean isCaptured;

	public AnimationData(GanimedModel ganimedModel) {
		this.ganimedModel=ganimedModel;
	}

	public AnimationData(GanimedModel ganimedModel, boolean captured) {
		this(ganimedModel);
		this.isCaptured=captured;
	}

	public GanimedModel getGanimedModel() {
		return ganimedModel;
	}

	public void addFile(File file) throws IOException {
		ImageData imageDataFile=new ImageData(this,file);
		BufferedImage bufferedImage=imageDataFile.getBufferedImage();
		int imageDatasLastIndex=imageDatas.size()-1;
		if(imageDatasLastIndex>=0) {
			ImageData imageData=imageDatas.get(imageDatasLastIndex);
			if(isSame(bufferedImage,imageData.getBufferedImage())) {
				imageData.increaseCount();
				imageDataIndex.add(imageDatasLastIndex);
				return;
			}
		}
		imageDatas.add(imageDataFile);
		imageDataIndex.add(++imageDatasLastIndex);
		processSize(bufferedImage);
	}

	public void addImage(BufferedImage bufferedImage) {
		ImageData imageData;
		int imageDatasLastIndex=imageDatas.size()-1;
		if(imageDatasLastIndex>=0) {
			imageData=imageDatas.get(imageDatasLastIndex);
			if(isSame(bufferedImage,imageData.getBufferedImage())) {
				imageData.increaseCount();
				imageDataIndex.add(imageDatasLastIndex);
				return;
			}
		}
		imageData=new ImageData(this,bufferedImage);
		imageDatas.add(imageData);
		imageDataIndex.add(++imageDatasLastIndex);
		processSize(bufferedImage);
	}

	private void processSize(BufferedImage bufferedImage) {
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

	private boolean isSame(BufferedImage bufferedImageCurrentFrame,BufferedImage bufferedImagePreviousFrame) {
		if(bufferedImagePreviousFrame==null) {
			return false;
		}
		DataBuffer databuffer1=bufferedImagePreviousFrame.getRaster().getDataBuffer();
		DataBuffer databuffer2=bufferedImageCurrentFrame.getRaster().getDataBuffer();
		if(databuffer1 instanceof DataBufferInt) {
			int[] intImagedata1=((DataBufferInt)databuffer1).getData();
			int[] intImagedata2=((DataBufferInt)databuffer2).getData();
			return Arrays.equals(intImagedata1, intImagedata2);
		}else if(databuffer1 instanceof DataBufferByte) {
			byte[] intImagedata1=((DataBufferByte)databuffer1).getData();
			byte[] intImagedata2=((DataBufferByte)databuffer2).getData();
			return Arrays.equals(intImagedata1, intImagedata2);
		}else {
			throw new RuntimeException("databuffer not supported: "+databuffer1.getClass().getName());
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

	public int logicalSize() {
		return imageDataIndex.size();
	}

	public ImageData getPhysicalImageData(int index) {
		return imageDatas.get(index);
	}

	public ImageData getLogicalImageData(int index) {
		int pIndex=imageDataIndex.get(index);
		return imageDatas.get(pIndex);

	}

	public boolean isCaptured() {
		return isCaptured;
	}

	public String toString() {
		return AnimationData.class.getName()+": physical images="+size()+", logical images="+logicalSize();
	}
}
