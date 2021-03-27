package lunartools.ganimed.imagetype;

public class ImageTypeApng implements ImageType{
	private static ImageTypeApng instance;

	private ImageTypeApng() {}

	public static ImageTypeApng getInstance() {
		if(instance==null) {
			instance=new ImageTypeApng();
		}
		return instance;
	}

	@Override
	public String getFileExtension() {
		return ".png";
	}

	@Override
	public String getName() {
		return "APNG";
	}

}
