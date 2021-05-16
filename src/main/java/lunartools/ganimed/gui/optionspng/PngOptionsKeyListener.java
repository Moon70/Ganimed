package lunartools.ganimed.gui.optionspng;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import lunartools.ganimed.gui.optionspng.model.OptionsPngModel;

@SuppressWarnings("unused")
public class PngOptionsKeyListener implements KeyListener{
	private OptionsPngModel optionsPngModel;
	private OptionsPngView optionsPngView;

	public PngOptionsKeyListener(OptionsPngModel optionsPngModel,OptionsPngView optionsPngView) {
		this.optionsPngModel=optionsPngModel;
		this.optionsPngView=optionsPngView;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_ENTER){
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
