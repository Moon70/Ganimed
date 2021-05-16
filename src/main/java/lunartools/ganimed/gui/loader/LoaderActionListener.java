package lunartools.ganimed.gui.loader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoaderActionListener implements ActionListener{
	private LoaderView loaderView;
	
	public LoaderActionListener(LoaderView loaderView) {
		this.loaderView=loaderView;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source=actionEvent.getSource();
		if(source.equals(loaderView.button)) {
			loaderView.selectImageFolder();
		}
	}
	
}
