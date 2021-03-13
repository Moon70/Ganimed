package lunartools.ganimed;

import javax.swing.UIManager;

public class MainGanimed{

	/*
	 * Ganimed
	 * Creates a GIF from a set of single imager files.
	 * 
	 * Written by Thomas Mattel in february 2021.
	 * https://github.com/Moon70/Ganimed
	 */
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new GanimedController().openGUI();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
