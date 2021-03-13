package lunartools.colorquantizer;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import lunartools.PixelTools;

/*
 * GPAC - Gif Piece A Change
 * A color quantizer, based on the 'Median Cut' algorithm
 */
public class GPAC_experimental {
	private ArrayList<Cube> cubes=new ArrayList<Cube>();
	private int[] pixeldata;
	private int width;
	private int height;
	private Color[] palette;

	public void quantizeColors(BufferedImage bufferedImage,int paletteSize) {
		this.width=bufferedImage.getWidth();
		this.height=bufferedImage.getHeight();
		pixeldata=((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
		for(int i=0;i<pixeldata.length;i++) {
			pixeldata[i]=pixeldata[i]&0xffffff;
		}

		Cube cube=new Cube();
		int[] pixelcount=PixelTools.createPixelCountArray(pixeldata);
		for(int i=0;i<pixelcount.length;i++) {
			if(pixelcount[i]>0) {
				cube.addColor(new Color(i));
			}
		}
		if(cube.colors.size()<=paletteSize) {
			return;
		}
		
		recursion(cube, paletteSize>>1);
		
		while(cubes.size()<paletteSize) {
			int maxCols=0;
			Cube cubeSplit=null;
			for(int i=0;i<cubes.size();i++) {
				if(cubes.get(i)!=null && cubes.get(i).colors.size()>maxCols) {
					cubeSplit=cubes.get(i);
					maxCols=cubeSplit.colors.size();
				}
			}
			Cube cube1=cubeSplit.getChildCubeHi();
			Cube cube2=cubeSplit.getChildCubeLo();
			cubes.remove(cubeSplit);
			cubes.add(cube1);
			cubes.add(cube2);
		}
		
		palette=calcPalette(cubes);
		int[] pixelHash1=new int[0x1000000];
		int[] pixelHash2=new int[0x1000000];
		for(int i=0;i<cubes.size();i++) {
			cube=cubes.get(i);
			replaceCubeColorsHash(cube,pixelHash1,pixelHash2);
		}

		int index=0;
		for(int y=0;y<height;y++) {
			if((y&1)==0) {
				for(int x=0;x<width>>1;x++) {
					pixeldata[index]=pixelHash1[pixeldata[index++]];
					pixeldata[index]=pixelHash2[pixeldata[index++]];
				}
			}else {
				for(int x=0;x<width>>1;x++) {
					pixeldata[index]=pixelHash2[pixeldata[index++]];
					pixeldata[index]=pixelHash1[pixeldata[index++]];
				}
			}
		}
	}
	
	private volatile int colorReplaceIndex;
	
	public synchronized Cube getNextCubeToReplaceColors() {
		if(colorReplaceIndex==cubes.size()) {
			return null;
		}
		return cubes.get(colorReplaceIndex++);
	}
	
	
	private Color[] calcPalette(ArrayList<Cube> cubes) {
		Color[] colorsPalette=new Color[cubes.size()];
		for(int i=0;i<cubes.size();i++) {
			colorsPalette[i]=new Color(cubes.get(i).getAveragePixel());
		}
		return colorsPalette;
	}

	private void recursion(Cube cube,int level) {
		int mincolors=100;
		if(cube.colors.size()<=mincolors) {
			cubes.add(cube);
			return;
		}
		Cube cubeHi=cube.getChildCubeHi();
		Cube cubeLo=cube.getChildCubeLo();
		if(level==1) {
			if(cubeHi!=null) {
				cubes.add(cubeHi);
			}
			if(cubeLo!=null) {
				cubes.add(cubeLo);
			}
		}else {
			recursion(cubeHi, level>>1);
			recursion(cubeLo, level>>1);
		}
	}

	public void replaceCubeColorsHash(Cube cube,int[] pixelcount1,int[] pixelcount2) {
		TreeSet<Color> set=new TreeSet<Color>(new Comparator<Color>() {
			@Override
			public int compare(Color color1, Color color2) {
				return color1.grey>color2.grey?1:-1;
			}
		});
		for(int x=0;x<cube.colors.size();x++) {
			set.add(cube.colors.get(x));
		}
		
		int pixelCenter=cube.getAveragePixel();
		int pixelHi;
		int pixelLo;
		
		int drittel=set.size()/3;
		int counter=0;

		Iterator<Color> iterator=set.descendingIterator();
		while(iterator.hasNext()) {
			Color color=iterator.next();
			int pixelToReplace=color.color;
			color.calcBestFriends(palette);
			int indexHi=color.leftFriendIndex;
			if(indexHi==-1) {
				pixelHi=pixelCenter;
			}else {
				pixelHi=palette[indexHi].color;
			}
			int indexLo=color.rightFriendIndex;
			if(indexLo==-1) {
				pixelLo=pixelCenter;
			}else {
				pixelLo=palette[indexLo].color;
			}
			
			if(counter<=drittel) {
				pixelcount1[pixelToReplace]=pixelHi;
				pixelcount2[pixelToReplace]=pixelCenter;
			}else if(counter>=set.size()-drittel) {
				pixelcount1[pixelToReplace]=pixelCenter;
				pixelcount2[pixelToReplace]=pixelLo;
			}else {
				pixelcount1[pixelToReplace]=pixelCenter;
				pixelcount2[pixelToReplace]=pixelCenter;
			}
		}
	}

}
