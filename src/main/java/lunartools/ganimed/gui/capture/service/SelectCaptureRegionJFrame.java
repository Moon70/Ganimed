package lunartools.ganimed.gui.capture.service;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SelectCaptureRegionJFrame extends JFrame implements MouseListener,MouseMotionListener,KeyListener, Runnable{
	private static Logger logger = LoggerFactory.getLogger(SelectCaptureRegionJFrame.class);
	private SelectCaptureRegionService selectCaptureRegionService;
	private GraphicsDevice graphicsDevice;
	private volatile boolean animationThreadEnabled = true;
	private Image imageScreenshot;
	private int currentMouseX;
	private int currentMouseY;
	private int rangeX1;
	private int rangeY1;
	private int rangeX2;
	private int rangeY2;
	private Color[] colourTable=new Color[0x1000];
	private boolean mouseEntered;
	private boolean mouseButton1Pressed;
	private boolean selectionChanged;
	private Rectangle rectangleScreensize;
	private Robot robot;
	private int[] colourTableRed;
	private int[] colourTableGreen;
	private int[] colourTableBlue;
	private int colourIndexRed;
	private int colourIndexGreen;
	private int colourIndexBlue;
	
	SelectCaptureRegionJFrame(SelectCaptureRegionService selectCaptureRegionService,GraphicsDevice graphicsDevice) throws AWTException{
		this.selectCaptureRegionService=selectCaptureRegionService;
		this.graphicsDevice=graphicsDevice;
		this.rectangleScreensize = graphicsDevice.getDefaultConfiguration().getBounds();
		setBounds(this.rectangleScreensize);
		setUndecorated(true);
		setType(Type.UTILITY);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		createColourTable();
		robot=new Robot(graphicsDevice);
	}
	
	private void createColourTable() {
		colourTableRed=new int[32];
		colourTableGreen=new int[32];
		colourTableBlue=new int[32];
		for(int i=0;i<16;i++) {
			colourTableRed[i]=colourTableRed[31-i]=i<<8;
			colourTableGreen[i]=colourTableGreen[31-i]=i<<4;
			colourTableBlue[i]=colourTableBlue[31-i]=i;
		}
		for(int i=0;i<0x1000;i++) {
			int r=(i&0b111100000000)<<12;
			int g=(i&0b000011110000)<<8;
			int b=(i&0b000000001111)<<4;
			colourTable[i]=new Color(r|g|b);
		}
	}
	
	private void drawColourLine(Graphics graphics,int width,int x1,int y1, int x2, int y2) {
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
		int indexLoopRed=colourIndexRed>>1;
		int indexLoopGreen=colourIndexGreen>>1;
		int indexLoopBlue=colourIndexBlue>>1;
		final int step=8;
		if(y1==y2) {
			for(int xs=x1;xs<x2;xs+=step) {
				indexLoopRed=(indexLoopRed+29)%colourTableRed.length;
				indexLoopGreen=(indexLoopGreen+31)%colourTableGreen.length;
				indexLoopBlue=(indexLoopBlue+30)%colourTableBlue.length;
				int colour=colourTableRed[indexLoopRed] | colourTableGreen[indexLoopGreen] | colourTableBlue[indexLoopBlue];
				int xd=xs+step;
				if(xd>x2) {
					xd=x2;
				}
				graphics.setColor(colourTable[colour]);
				graphics.drawLine(xs,		y1, 	xd,		y2);
				graphics.drawLine(xs,		y1+1,	xd,		y2+1);
			}
		}else {
			for(int ys=y1;ys<y2;ys+=step) {
				indexLoopRed=(indexLoopRed+29)%colourTableRed.length;
				indexLoopGreen=(indexLoopGreen+31)%colourTableGreen.length;
				indexLoopBlue=(indexLoopBlue+30)%colourTableBlue.length;
				int colour=colourTableRed[indexLoopRed] | colourTableGreen[indexLoopGreen] | colourTableBlue[indexLoopBlue];
				int yd=ys+step;
				if(yd>y2) {
					yd=y2;
				}
				graphics.setColor(colourTable[colour]);
				graphics.drawLine(x1,		ys,		x2,		yd);
				graphics.drawLine(x1+1,		ys,		x2+1,	yd);
			}
		}
	}
	
	@Override
	public void paint(Graphics g){
		colourIndexRed=(colourIndexRed+2)%(colourTableRed.length<<1);
		colourIndexGreen=(colourIndexGreen+63)%(colourTableGreen.length<<1);
		colourIndexBlue=(colourIndexBlue+1)%(colourTableBlue.length<<1);
		if(selectionChanged) {
			g.drawImage(imageScreenshot,0,0,null);
			selectionChanged=false;
		}
		if(mouseEntered){
			if(!mouseButton1Pressed) {
				drawColourLine(g,rectangleScreensize.width,0,				currentMouseY,	rectangleScreensize.width,	currentMouseY);
				drawColourLine(g,rectangleScreensize.width,currentMouseX,	0,				currentMouseX,				rectangleScreensize.width);
			}else {
				drawColourLine(g,rectangleScreensize.width,rangeX1,			rangeY1,		currentMouseX,				rangeY1);
				drawColourLine(g,rectangleScreensize.width,rangeX1,			rangeY1,		rangeX1,					currentMouseY);
				drawColourLine(g,rectangleScreensize.width,rangeX1,			currentMouseY,	currentMouseX,				currentMouseY);
				drawColourLine(g,rectangleScreensize.width,currentMouseX,	rangeY1,		currentMouseX,				currentMouseY);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e){}

	@Override
	public void mousePressed(MouseEvent e){
		if(e.isPopupTrigger()) {
			return;
		}
		if(e.getButton()==MouseEvent.BUTTON1){
			rangeX1=currentMouseX;
			rangeY1=currentMouseY;
			rangeX2=0;
			rangeY2=0;
			mouseButton1Pressed=true;
			selectCaptureRegionService.mousePressed(this);
		}else{
			logger.trace("!LMB");
			selectCaptureRegionService.exit();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e){
		if(e.getButton()==MouseEvent.BUTTON1 && !e.isPopupTrigger()){
			rangeX2=currentMouseX;
			rangeY2=currentMouseY;
			mouseButton1Pressed=false;
			selectCaptureRegionService.selected(graphicsDevice,rangeX1,rangeY1,rangeX2,rangeY2);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e){
		this.setVisible(true);
		selectionChanged=true;
		this.mouseEntered=true;
	}

	@Override
	public void mouseExited(MouseEvent e){
		if(!mouseButton1Pressed) {
			this.setVisible(false);
			this.mouseEntered=false;
			selectionChanged=true;
			selectCaptureRegionService.mouseExited(e.getXOnScreen(),e.getYOnScreen());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e){
		currentMouseX=e.getX();
		currentMouseY=e.getY();
		selectionChanged=true;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e){
		currentMouseX=e.getX();
		currentMouseY=e.getY();
		selectionChanged=true;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e){
		if(e.getKeyChar()==27) {
			logger.trace("ESC");
			selectCaptureRegionService.exit();
		}
	}

	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyReleased(KeyEvent e){}

	@Override
	public void setVisible(boolean b) {
		if(b & !mouseButton1Pressed) {
			this.imageScreenshot=robot.createScreenCapture(this.rectangleScreensize);
		}
		super.setVisible(b);
	}

	@Override
	public void run() {
		while(animationThreadEnabled) {
			if(isVisible()) {
				repaint();
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void interrupt() {
		this.animationThreadEnabled=false;
	}
	
}
