package lunartools.ganimed.gui.capture.service;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.ScreenTools;

public class SelectCaptureRegionService {
	private static Logger logger = LoggerFactory.getLogger(SelectCaptureRegionService.class);
	private Thread thread;
	private JFrame[] jFrame;
	private Thread[] threadSelectRegion;
	private volatile Rectangle rectangle;
	private GraphicsDevice graphicsDeviceSelection;
	private int numberOfGraphicsDevices;

	public void selectScreenRegion() throws AWTException {
		this.thread=Thread.currentThread();
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();  
		GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		this.numberOfGraphicsDevices=graphicsDevices.length;
		logger.trace("number of GraphicsDevices: "+numberOfGraphicsDevices);
		for(int i=0;i<numberOfGraphicsDevices;i++){
			GraphicsDevice graphicsDevice=graphicsDevices[i];
			Rectangle graphicsDeviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
			logger.trace("GraphicsDevice "+i+" bounds: "+graphicsDeviceBounds);
		} 
		jFrame=new JFrame[numberOfGraphicsDevices];
		threadSelectRegion=new Thread[numberOfGraphicsDevices];
		for(int i=0;i<numberOfGraphicsDevices;i++){
			jFrame[i]=new SelectCaptureRegionJFrame(this,graphicsDevices[i]);
			threadSelectRegion[i]=new Thread((Runnable)jFrame[i]);
			threadSelectRegion[i].start();
			jFrame[i].setVisible(true);

		}

		synchronized(this) {
			boolean active=true;
			do {
				try {
					active=false;
					this.wait(500);
				} catch (InterruptedException e) {}
				for(int i=0;i<numberOfGraphicsDevices;i++){
					active|=(jFrame[i]!=null);
				}
			}while(active);

		}
		logger.trace("Selected screen region: "+this.rectangle);
	}

	public Rectangle getSelectedScreenRegion() {
		return this.rectangle;
	}

	public GraphicsDevice getSelectedGraphicsDevice() {
		return graphicsDeviceSelection;
	}

	void exit() {
		logger.trace("exit screen region selection");
		for(int i=0;i<numberOfGraphicsDevices;i++){
			if(threadSelectRegion[i]!=null) {
				threadSelectRegion[i].interrupt();
				threadSelectRegion[i]=null;
			}
			if(jFrame[i]!=null) {
				((SelectCaptureRegionJFrame)jFrame[i]).interrupt();
				jFrame[i].setVisible(false);
				jFrame[i].dispose();
				jFrame[i]=null;
			}
		}
		this.thread.interrupt();
	}

	void selected(GraphicsDevice graphicsDevice,int x1, int y1, int x2, int y2) {
		this.graphicsDeviceSelection=graphicsDevice;
		if(x1>x2) {
			int x=x1;
			x1=x2;
			x2=x;
		}
		if(y1>y2) {
			int y=y1;
			y1=y2;
			y2=y;
		}

		Rectangle graphicsDeviceBounds = graphicsDevice.getDefaultConfiguration().getBounds();
		x1+=graphicsDeviceBounds.x;
		x2+=graphicsDeviceBounds.x;
		y1+=graphicsDeviceBounds.y;
		y2+=graphicsDeviceBounds.y;
		this.rectangle=new Rectangle(x1,y1,x2-x1,y2-y1);
		exit();
	}

	void mousePressed(SelectCaptureRegionJFrame selectCaptureRegionJFrame) {
		for(int i=0;i<numberOfGraphicsDevices;i++){
			if(jFrame[i]!=selectCaptureRegionJFrame) {
				threadSelectRegion[i].interrupt();
				threadSelectRegion[i]=null;
				((SelectCaptureRegionJFrame)jFrame[i]).interrupt();
				jFrame[i].setVisible(false);
				jFrame[i].dispose();
				jFrame[i]=null;
			}
		}

	}

	void mouseExited(int x, int y) {
		for(int i=0;i<numberOfGraphicsDevices;i++){
			if(jFrame[i]!=null && ScreenTools.isInBounds(jFrame[i].getBounds(),x,y)) {
				jFrame[i].setVisible(true);
				jFrame[i].repaint();
			}
		}
	}

}
