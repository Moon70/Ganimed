package lunartools;

public class PixelTools {

	public static int[] createPixelCountArray(int[] pixeldata) {
		int[] pixelcount=new int[0x1000000];
		for(int i=0;i<pixeldata.length;i++) {
			pixelcount[pixeldata[i]&0xffffff]++;
		}
		return pixelcount;
	}

}
