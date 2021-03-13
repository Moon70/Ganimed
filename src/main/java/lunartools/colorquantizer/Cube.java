package lunartools.colorquantizer;

import java.util.ArrayList;

public class Cube {
	public ArrayList<Color> colors=new ArrayList<Color>();
	
	private int minR=Integer.MAX_VALUE;
	private int maxR=Integer.MIN_VALUE;
	private int minG=Integer.MAX_VALUE;
	private int maxG=Integer.MIN_VALUE;
	private int minB=Integer.MAX_VALUE;
	private int maxB=Integer.MIN_VALUE;
	
	private long totalR;
	private long totalG;
	private long totalB;
	
	private Cube childCubeHi;
	private Cube childCubeLo;
	
	public void addColor(Color color) {
		colors.add(color);
		totalR+=color.red;
		totalG+=color.green;
		totalB+=color.blue;
		
		if(minR>color.red) {
			minR=color.red;
		}
		if(maxR<color.red) {
			maxR=color.red;
		}
		
		if(minG>color.green) {
			minG=color.green;
		}
		if(maxG<color.green) {
			maxG=color.green;
		}
		
		if(minB>color.blue) {
			minB=color.blue;
		}
		if(maxB<color.blue) {
			maxB=color.blue;
		}
	}
	
	public Cube getChildCubeHi() {
		if(childCubeHi==null) {
			split();
		}
		return childCubeHi;
	}
	
	public Cube getChildCubeLo() {
		if(childCubeLo==null) {
			split();
		}
		return childCubeLo;
	}

	public int getAveragePixel() {
		int r=(int)(totalR/colors.size());
		int g=(int)(totalG/colors.size());
		int b=(int)(totalB/colors.size());
		return ((r<<16)|(g<<8)|b);
	}

	private void split() {
		childCubeHi=new Cube();
		childCubeLo=new Cube();
		
		int lenR=maxR-minR;
		int lenG=maxG-minG;
		int lenB=maxB-minB;
		
		if(lenR>lenG) {
			if(lenR>=lenB) {
				splitR(minR+(lenR>>1));
			}else {
				if(lenG>=lenB) {
					splitG(minG+(lenG>>1));
				}else {
					splitB(minB+(lenB>>1));
				}
			}
		}else {
			if(lenG>=lenB) {
				splitG(minG+(lenG>>1));
			}else {
				splitB(minB+(lenB>>1));
			}
		}
	}

	private void splitR(int cutR) {
		for(int i=0;i<colors.size();i++) {
			Color color=colors.get(i);
			if(color.red<cutR) {
				childCubeHi.addColor(color);
			}else {
				childCubeLo.addColor(color);
			}
		}
	}

	private void splitG(int cutG) {
		for(int i=0;i<colors.size();i++) {
			Color color=colors.get(i);
			if(color.green<cutG) {
				childCubeHi.addColor(color);
			}else {
				childCubeLo.addColor(color);
			}
		}
	}

	private void splitB(int cutB) {
		for(int i=0;i<colors.size();i++) {
			Color color=colors.get(i);
			if(color.blue<cutB) {
				childCubeHi.addColor(color);
			}else {
				childCubeLo.addColor(color);
			}
		}
	}
	
}
