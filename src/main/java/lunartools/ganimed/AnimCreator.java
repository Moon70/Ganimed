package lunartools.ganimed;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface AnimCreator {

	public void addImage(BufferedImage bufferedImage, int delay, boolean loop) throws IOException;

	public byte[] toByteArray() throws IOException;

}
