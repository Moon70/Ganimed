package lunartools.ganimed.gui.editor;

import java.awt.Color;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.gui.GanimedView;
import lunartools.ganimed.gui.editor.model.EditorModel;

public class EditorView extends JPanel{
	private GanimedModel ganimedModel;
	private GanimedView ganimedView;
	private EditorModel editorModel;

	JTextField textfieldCutLeft;
	Scrollbar scrollbarCutLeft;
	Scrollbar scrollbarCutRight;
	JTextField textfieldCutRight;
	JTextField textfieldCropTop;
	Scrollbar scrollbarCropTop;
	JTextField textfieldCropBottom;
	Scrollbar scrollbarCropBottom;
	JTextField textfieldCropLeft;
	Scrollbar scrollbarCropLeft;
	JTextField textfieldCropRight;
	Scrollbar scrollbarCropRight;
	JTextField textfieldResize;
	Scrollbar scrollbarResize;
	JTextField textfieldAnimFps;
	Scrollbar scrollbarAnimFps;
	JTextField textfieldAnimDelay;
	Scrollbar scrollbarAnimDelay;
	JTextField textfieldAnimEndDelay;
	Scrollbar scrollbarAnimEndDelay;

	public EditorView(GanimedModel ganimedModel, GanimedView view) {
		this.ganimedModel=ganimedModel;
		this.ganimedView=view;
		this.editorModel=this.ganimedModel.getEditorModel();
		this.setLayout(null);

		KeyListener keyListener=new EditorKeyListener(editorModel,this);
		AdjustmentListener adjustmentlistener=new EditorAdjustmentListener(editorModel,this);
		EditorActionListener editorActionListener=new EditorActionListener(this);

		int xLabel1=12;
		int xField1=90;
		int xScrollbar1=120;
		int xLabel2=440;
		int xField2=440+78;
		int xScrollbar2=470+78;
		int y=4;
		int scrollHeight=14;
		int lineHight=18;
		int lineDistance=21;
		int lineDistance2=30;

		JLabel label=new JLabel("Cut left:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldCutLeft=new JTextField(200);
		textfieldCutLeft.setBounds(xField1,y,32,lineHight);
		textfieldCutLeft.addKeyListener(keyListener);
		add(textfieldCutLeft);
		scrollbarCutLeft=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCutLeft.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarCutLeft.setBackground(Color.DARK_GRAY);
		scrollbarCutLeft.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCutLeft);

		label=new JLabel("Cut right:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldCutRight=new JTextField(200);
		textfieldCutRight.setBounds(xField2,y,32,lineHight);
		textfieldCutRight.addKeyListener(keyListener);
		add(textfieldCutRight);
		scrollbarCutRight=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCutRight.setBounds(xScrollbar2,y+4,255+16+16,scrollHeight);
		scrollbarCutRight.setBackground(Color.DARK_GRAY);
		scrollbarCutRight.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCutRight);

		y+=lineDistance2;

		label=new JLabel("Crop top:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldCropTop=new JTextField(200);
		textfieldCropTop.setBounds(xField1,y,32,lineHight);
		textfieldCropTop.addKeyListener(keyListener);
		add(textfieldCropTop);
		scrollbarCropTop=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropTop.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarCropTop.setBackground(Color.DARK_GRAY);
		scrollbarCropTop.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropTop);

		label=new JLabel("Crop bottom:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldCropBottom=new JTextField(200);
		textfieldCropBottom.setBounds(xField2,y,32,lineHight);
		textfieldCropBottom.addKeyListener(keyListener);
		add(textfieldCropBottom);
		scrollbarCropBottom=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropBottom.setBounds(xScrollbar2,y+4,255+16+16,scrollHeight);
		scrollbarCropBottom.setBackground(Color.DARK_GRAY);
		scrollbarCropBottom.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropBottom);

		y+=lineDistance;

		label=new JLabel("Crop left:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldCropLeft=new JTextField(200);
		textfieldCropLeft.setBounds(xField1,y,32,lineHight);
		textfieldCropLeft.addKeyListener(keyListener);
		add(textfieldCropLeft);
		scrollbarCropLeft=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropLeft.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarCropLeft.setBackground(Color.DARK_GRAY);
		scrollbarCropLeft.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropLeft);

		label=new JLabel("Crop right:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldCropRight=new JTextField(200);
		textfieldCropRight.setBounds(xField2,y,32,lineHight);
		textfieldCropRight.addKeyListener(keyListener);
		add(textfieldCropRight);
		scrollbarCropRight=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarCropRight.setBounds(xScrollbar2,y+4,255+16+16,scrollHeight);
		scrollbarCropRight.setBackground(Color.DARK_GRAY);
		scrollbarCropRight.addAdjustmentListener(adjustmentlistener);
		add(scrollbarCropRight);

		y+=lineDistance;

		label=new JLabel("Resize %:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldResize=new JTextField(200);
		textfieldResize.setBounds(xField1,y,32,lineHight);
		textfieldResize.addKeyListener(keyListener);
		add(textfieldResize);
		scrollbarResize=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,0);
		scrollbarResize.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarResize.setBackground(Color.DARK_GRAY);
		scrollbarResize.addAdjustmentListener(adjustmentlistener);
		add(scrollbarResize);

		y+=lineDistance2;

		label=new JLabel("Anim FPS:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldAnimFps=new JTextField(200);
		textfieldAnimFps.setBounds(xField1,y,32,lineHight);
		textfieldAnimFps.addKeyListener(keyListener);
		add(textfieldAnimFps);
		scrollbarAnimFps=new Scrollbar(Scrollbar.HORIZONTAL,editorModel.getAnimFps(),1,1,0);
		scrollbarAnimFps.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarAnimFps.setBackground(Color.DARK_GRAY);
		scrollbarAnimFps.setForeground(Color.BLUE);
		scrollbarAnimFps.addAdjustmentListener(adjustmentlistener);
		add(scrollbarAnimFps);

		label=new JLabel("Anim delay:");
		label.setBounds(xLabel2,y,100,lineHight);
		add(label);
		textfieldAnimDelay=new JTextField(4);
		textfieldAnimDelay.setBounds(xField2,y,32,lineHight);
		textfieldAnimDelay.setText(""+editorModel.getAnimDelay());
		textfieldAnimDelay.addKeyListener(keyListener);
		add(textfieldAnimDelay);
		scrollbarAnimDelay=new Scrollbar(Scrollbar.HORIZONTAL,editorModel.getAnimDelay(),1,16,1001);
		scrollbarAnimDelay.setBounds(xScrollbar2,y+4,255+16+16,scrollHeight);
		scrollbarAnimDelay.setBackground(Color.DARK_GRAY);
		scrollbarAnimDelay.addAdjustmentListener(adjustmentlistener);
		add(scrollbarAnimDelay);

		y+=lineDistance;

		label=new JLabel("End delay:");
		label.setBounds(xLabel1,y,100,lineHight);
		add(label);
		textfieldAnimEndDelay=new JTextField(4);
		textfieldAnimEndDelay.setBounds(xField1,y,32,lineHight);
		textfieldAnimEndDelay.addKeyListener(keyListener);
		add(textfieldAnimEndDelay);
		scrollbarAnimEndDelay=new Scrollbar(Scrollbar.HORIZONTAL,editorModel.getAnimEndDelay(),1,0,5001);
		scrollbarAnimEndDelay.setBounds(xScrollbar1,y+4,255+16+16,scrollHeight);
		scrollbarAnimEndDelay.setBackground(Color.DARK_GRAY);
		scrollbarAnimEndDelay.addAdjustmentListener(adjustmentlistener);
		add(scrollbarAnimEndDelay);

		y+=lineDistance;

		setBounds(0,0,860,178);
		//System.out.println("EditorPanel y: "+y);
	}

	public void refreshGui() {
		boolean animationDataIsAvailable=ganimedModel.isAnimationDataAvailable();
		textfieldCutLeft.setEnabled(animationDataIsAvailable);
		scrollbarCutLeft.setEnabled(animationDataIsAvailable);
		textfieldCutRight.setEnabled(animationDataIsAvailable);
		scrollbarCutRight.setEnabled(animationDataIsAvailable);
		textfieldCropLeft.setEnabled(animationDataIsAvailable);
		scrollbarCropLeft.setEnabled(animationDataIsAvailable);
		textfieldCropRight.setEnabled(animationDataIsAvailable);
		scrollbarCropRight.setEnabled(animationDataIsAvailable);
		textfieldCropTop.setEnabled(animationDataIsAvailable);
		scrollbarCropTop.setEnabled(animationDataIsAvailable);
		textfieldCropBottom.setEnabled(animationDataIsAvailable);
		scrollbarCropBottom.setEnabled(animationDataIsAvailable);
		textfieldResize.setEnabled(animationDataIsAvailable);
		scrollbarResize.setEnabled(animationDataIsAvailable);
		textfieldAnimFps.setEnabled(animationDataIsAvailable);
		scrollbarAnimFps.setEnabled(animationDataIsAvailable);
		textfieldAnimDelay.setEnabled(animationDataIsAvailable);
		scrollbarAnimDelay.setEnabled(animationDataIsAvailable);
		textfieldAnimEndDelay.setEnabled(animationDataIsAvailable);
		scrollbarAnimEndDelay.setEnabled(animationDataIsAvailable);
		scrollbarCutLeft.setMinimum(editorModel.getCutLeftMin());
		scrollbarCutLeft.setMaximum(editorModel.getCutLeftMax()+1);
		scrollbarCutRight.setMinimum(editorModel.getCutRightMin());
		scrollbarCutRight.setMaximum(editorModel.getCutRightMax());
		scrollbarCropLeft.setMinimum(editorModel.getCropLeftMin());
		scrollbarCropLeft.setMaximum(editorModel.getCropLeftMax());
		scrollbarCropTop.setMinimum(editorModel.getCropTopMin());
		scrollbarCropTop.setMaximum(editorModel.getCropTopMax()+1);

		scrollbarCropRight.setMinimum(editorModel.getCropRightMin());
		scrollbarCropRight.setMaximum(editorModel.getCropRightMax()+1);
		scrollbarCropBottom.setMinimum(editorModel.getCropBottomMin());
		scrollbarCropBottom.setMaximum(editorModel.getCropBottomMax()+1);

		scrollbarResize.setMinimum(editorModel.getResizeMin());
		scrollbarResize.setMaximum(editorModel.getResizeMax());

		textfieldCutLeft.setText(""+editorModel.getCutLeft());
		scrollbarCutLeft.setValue(editorModel.getCutLeft());
		textfieldCutRight.setText(""+editorModel.getCutRight());
		scrollbarCutRight.setValue(editorModel.getCutRight());

		textfieldCropLeft.setText(""+editorModel.getCropLeft());
		scrollbarCropLeft.setValue(editorModel.getCropLeft());
		textfieldCropTop.setText(""+editorModel.getCropTop());
		scrollbarCropTop.setValue(editorModel.getCropTop());
		textfieldCropRight.setText(""+editorModel.getCropRight());
		scrollbarCropRight.setValue(editorModel.getCropRight());
		textfieldCropBottom.setText(""+editorModel.getCropBottom());
		scrollbarCropBottom.setValue(editorModel.getCropBottom());
		textfieldResize.setText(""+editorModel.getResizePercent());
		scrollbarResize.setValue(editorModel.getResizePercent());
		if(animationDataIsAvailable) {
			textfieldAnimFps.setText(""+editorModel.getAnimFps());
			scrollbarAnimFps.setMaximum(editorModel.getAnimFpsMax());
			scrollbarAnimFps.setValue(editorModel.getAnimFps());
			textfieldAnimDelay.setText(""+editorModel.getAnimDelay());
			scrollbarAnimDelay.setValue(editorModel.getAnimDelay());
			textfieldAnimEndDelay.setText(""+editorModel.getAnimEndDelay());
			scrollbarAnimEndDelay.setMaximum(editorModel.getAnimEndDelayMax());
			scrollbarAnimEndDelay.setValue(editorModel.getAnimEndDelay());
		}
	}

}
