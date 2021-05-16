package lunartools.ganimed.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lunartools.ganimed.gui.GanimedView;

@SuppressWarnings("unused")
public class EditorActionListener implements ActionListener{
	private EditorView editorView;
	
	public EditorActionListener(EditorView editorView) {
		this.editorView=editorView;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source=actionEvent.getSource();
	}

}
