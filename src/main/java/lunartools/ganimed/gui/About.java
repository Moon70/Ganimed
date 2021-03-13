package lunartools.ganimed.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import lunartools.FileTools;

public class About {

	public static void showAboutDialog(JFrame jframe) {
		try {
			InputStream inputStream = About.class.getResourceAsStream("/About_Ganimed.html");
			StringBuffer html=FileTools.getStringBufferFromInputStream(inputStream, "iso-8859-1");
			JEditorPane editorPane = new JEditorPane("text/html", html.toString());

			editorPane.addHyperlinkListener(new HyperlinkListener(){
				@Override
				public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent){
					if (hyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
						try {
							Desktop.getDesktop().browse(hyperlinkEvent.getURL().toURI());
						} catch (IOException | URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			});
			editorPane.setEditable(false);
			editorPane.setBackground(jframe.getBackground());

			JOptionPane.showMessageDialog(jframe, editorPane, "About Ganimed",JOptionPane.INFORMATION_MESSAGE,FileTools.createImageIcon("/icons/ProgramIcon.png",new About()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
