package lunartools.ganimed.imagetype;

public class ImageTypeGif implements ImageType{
	private static ImageTypeGif instance;

	private ImageTypeGif() {}

	public static ImageTypeGif getInstance() {
		if(instance==null) {
			instance=new ImageTypeGif();
		}
		return instance;
	}

	@Override
	public String getFileExtension() {
		return ".gif";
	}

	@Override
	public String getName() {
		return "GIF";
	}

}
