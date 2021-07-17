package lunartools.ganimed;

import javax.swing.UIManager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class MainGanimed{
//	static {
//		ConsoleAppender console = new ConsoleAppender();
//		console.setLayout(new PatternLayout("[%-5p] %c - %m%n")); 
//		console.setThreshold(Level.ALL);
//		console.activateOptions();
//		Logger.getRootLogger().addAppender(console);
//		Logger.getRootLogger().setLevel(Level.ALL);
//	}

	/*
	 * Ganimed
	 * Creates a GIF/PNG from a set of single image files.
	 * 
	 * Written by Thomas Mattel in 2021.
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
