package lunartools.ganimed.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GanimedViewActionListener implements ActionListener{
	private GanimedView view;
	
	public GanimedViewActionListener(GanimedView view) {
		this.view=view;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source=actionEvent.getSource();
		if(source.equals(view.button)) {
			view.selectImageFolder();
		}
	}

}
