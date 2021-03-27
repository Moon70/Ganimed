package lunartools.ganimed.gui;

import java.awt.CheckboxMenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Vector;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.imagetype.ImageType;

public class CheckboxMenuItemGroup implements ItemListener{
	private GanimedModel model;
	@SuppressWarnings("unused")
	private GanimedView view;

	private Vector<CheckboxMenuItem> checkboxMenuItems=new Vector<CheckboxMenuItem>();

	private HashMap<String, ImageType> imageTypes=new HashMap<String, ImageType>();

	public CheckboxMenuItemGroup(GanimedModel model,GanimedView view) {
		this.model=model;
		this.view=view;
	}

	public void addCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem,ImageType imageType) {
		checkboxMenuItem.addItemListener(this);
		checkboxMenuItems.add(checkboxMenuItem);
		imageTypes.put(checkboxMenuItem.getActionCommand(),imageType);
	}

	@Override
	public void itemStateChanged(ItemEvent itemEvent) {
		CheckboxMenuItem selectedCheckboxMenuItem=(CheckboxMenuItem)itemEvent.getSource();
		selectedCheckboxMenuItem.setState(true);
		for(CheckboxMenuItem checkboxMenuItem:checkboxMenuItems){
			checkboxMenuItem.setState(checkboxMenuItem==selectedCheckboxMenuItem);
		}
		if(itemEvent.getStateChange()==ItemEvent.SELECTED) {
			CheckboxMenuItem checkboxMenuItem=(CheckboxMenuItem)itemEvent.getSource();
			String actionCommand=checkboxMenuItem.getActionCommand();
			model.setImageType(imageTypes.get(actionCommand));
		}
	}

}
