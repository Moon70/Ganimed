package lunartools.ganimed;

import java.io.IOException;

public interface AnimCreator {

	public void addImage(ImageData imageData, int delay, boolean loop) throws IOException;

	public byte[] toByteArray() throws IOException;

}
