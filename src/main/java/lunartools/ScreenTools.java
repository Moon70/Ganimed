package lunartools;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class ScreenTools {
	private static ScreenTools instance;
	private Rectangle[] bounds;
	
	private ScreenTools() {
		GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices=graphicsEnvironment.getScreenDevices();
		bounds=new Rectangle[graphicsDevices.length];
		for(int i=0;i<graphicsDevices.length;i++) {
			bounds[i]=graphicsDevices[i].getDefaultConfiguration().getBounds();
		}
	}

	public static ScreenTools getInstance() {
		if(instance==null) {
			createInstance();
		}
		return instance;
	}
	
	private static synchronized void createInstance() {
		if(instance==null) {
			instance=new ScreenTools();
		}
	}
	
	public Rectangle getScreenBounds(Rectangle rectangleFrame) {
		int centerFrameX=rectangleFrame.x+(rectangleFrame.width>>1);
		int centerFrameY=rectangleFrame.y+(rectangleFrame.height>>1);
		Rectangle rectangle;
		for(int i=0;i<bounds.length;i++) {
			rectangle=bounds[i];
			if(
					centerFrameX>=rectangle.x &&
					centerFrameX<=rectangle.x+rectangle.width &&
					centerFrameY>=rectangle.y &&
					centerFrameY<=rectangle.y+rectangle.height
					) {
				return rectangle;
			}
		}
		return bounds[0];
	}
	
	public Rectangle optimizeBounds(Rectangle rectangleOldBounds,Rectangle rectangleNewBounds) {
		Rectangle screenBounds=getScreenBounds(rectangleOldBounds);
		int deltaX=screenBounds.x+screenBounds.width-rectangleNewBounds.x-rectangleNewBounds.width;
		int deltaY=screenBounds.y+screenBounds.height-rectangleNewBounds.y-rectangleNewBounds.height;
		if(deltaX<0) {
			rectangleNewBounds.x+=deltaX;
			if(rectangleNewBounds.x<screenBounds.x) {
				rectangleNewBounds.x=screenBounds.x;
			}
		}
		if(deltaY<0) {
			rectangleNewBounds.y+=deltaY;
			if(rectangleNewBounds.y<screenBounds.y) {
				rectangleNewBounds.y=screenBounds.y;
			}
		}
		return rectangleNewBounds;
	}
	
}
