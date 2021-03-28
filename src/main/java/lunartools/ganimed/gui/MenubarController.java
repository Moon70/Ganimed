package lunartools.ganimed.gui;

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.imagetype.ImageType;

public class MenubarController implements ActionListener{
	private static final String ACTIONCOMMAND_SELECTFOLDER = "selectfolder";
	private static final String ACTIONCOMMAND_SAVEAS = "saveas";
	private static final String ACTIONCOMMAND_ABOUT = "about";
	private static final String ACTIONCOMMAND_EXIT = "exit";

	@SuppressWarnings("unused")
	private GanimedModel model;
	private GanimedView view;

	private MenuItem menuItem_SelectFolder;
	protected MenuItem menuItem_SaveAs;

	private CheckboxMenuItemGroup checkboxMenuItemGroupImageType;

	public MenubarController(GanimedModel model,GanimedView view) {
		this.model=model;
		this.view=view;
		checkboxMenuItemGroupImageType=new CheckboxMenuItemGroup(model,view);
	}

	public MenuBar createMenubar() {
		MenuBar menuBar=new MenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createOptionsMenu());
		menuBar.add(createHelpMenu());
		return menuBar;
	}

	private Menu createFileMenu(){
		Menu menu=new Menu("File");
		menu.addActionListener(this);

		menuItem_SelectFolder=new MenuItem("Select Image Folder");
		menuItem_SelectFolder.setActionCommand(ACTIONCOMMAND_SELECTFOLDER);
		menuItem_SelectFolder.setEnabled(true);
		menu.add(menuItem_SelectFolder);

		menuItem_SaveAs=new MenuItem("Save As");
		menuItem_SaveAs.setActionCommand(ACTIONCOMMAND_SAVEAS);
		menuItem_SaveAs.setEnabled(false);
		menu.add(menuItem_SaveAs);

		MenuItem menuItem=new MenuItem("Exit");
		menuItem.setActionCommand(ACTIONCOMMAND_EXIT);
		menu.add(menuItem);

		return menu;
	}

	private Menu createOptionsMenu() {
		Menu menuOptions=new Menu("Options");
		menuOptions.addActionListener(this);

		Menu menuItemImageType=new Menu("Image Type");
		menuOptions.add(menuItemImageType);

		for(ImageType imageType:model.getImageTypes()) {
			final String imageTypeName=imageType.getName();
			CheckboxMenuItem checkboxMenuItem=new CheckboxMenuItem(imageTypeName);
			checkboxMenuItem.setActionCommand(imageTypeName);
			menuItemImageType.add(checkboxMenuItem);
			checkboxMenuItem.setState(imageType==model.getImageType());
			checkboxMenuItemGroupImageType.addCheckboxMenuItem(checkboxMenuItem,imageType);

		}
		return menuOptions;
	}

	private Menu createHelpMenu(){
		Menu menu=new Menu("?");
		MenuItem menuItem=new MenuItem("About "+GanimedModel.PROGRAMNAME);
		menuItem.setActionCommand(ACTIONCOMMAND_ABOUT);
		menu.add(menuItem);
		menu.addActionListener(this);
		return menu;
	}

	@Override
	public void actionPerformed(ActionEvent event){
		String actionCommand=event.getActionCommand();
		if(actionCommand.equals(ACTIONCOMMAND_EXIT)){
			view.sendMessage(SimpleEvents.EXIT);
		}else if(actionCommand.equals(ACTIONCOMMAND_SELECTFOLDER)){
			view.selectImageFolder();
		}else if(actionCommand.equals(ACTIONCOMMAND_SAVEAS)){
			view.saveAs();
		}else if(actionCommand.equals(ACTIONCOMMAND_ABOUT)){
			view.showMessageboxAbout();
		}
	}

	public void imageTypeChanged() {
		checkboxMenuItemGroupImageType.imageTypeChanged();
	}

}
