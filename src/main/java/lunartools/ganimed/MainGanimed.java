package lunartools.ganimed;

import javax.swing.UIManager;

public class MainGanimed{

	/*
	 * Ganimed
	 * Creates a GIF/PNG from a set of single image files.
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
