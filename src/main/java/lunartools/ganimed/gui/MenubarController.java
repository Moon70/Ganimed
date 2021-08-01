package lunartools.ganimed.gui;

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lunartools.ganimed.GanimedModel;
import lunartools.ganimed.panel.optionspanel.ImageType;

public class MenubarController implements ActionListener{
	private static final String ACTIONCOMMAND_SELECTFOLDER = "selectfolder";
	private static final String ACTIONCOMMAND_SAVEAS = "saveas";
	private static final String ACTIONCOMMAND_SAVEIMAGES = "saveframes";
	private static final String ACTIONCOMMAND_SAVERAWIMAGES = "saveRawFrames";
	private static final String ACTIONCOMMAND_CLEARIMAGES = "closeImages";
	private static final String ACTIONCOMMAND_ABOUT = "about";
	private static final String ACTIONCOMMAND_EXIT = "exit";

	private GanimedModel ganimedModel;
	private GanimedView ganimedView;

	private MenuItem menuItem_SelectFolder;
	protected MenuItem menuItem_SaveAs;
	protected MenuItem menuItem_SaveRawFrames;
	protected MenuItem menuItem_SaveFrames;
	protected MenuItem menuItem_Clear;

	private CheckboxMenuItemGroup checkboxMenuItemGroupImageType;

	public MenubarController(GanimedModel ganimdeModel,GanimedView ganimdedView) {
		this.ganimedModel=ganimdeModel;
		this.ganimedView=ganimdedView;
		checkboxMenuItemGroupImageType=new CheckboxMenuItemGroup(ganimdeModel,ganimdedView);
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
		
		menuItem_SaveRawFrames=new MenuItem("Save Raw Images");
		menuItem_SaveRawFrames.setActionCommand(ACTIONCOMMAND_SAVERAWIMAGES);
		menu.add(menuItem_SaveRawFrames);

		menuItem_SaveFrames=new MenuItem("Save Images");
		menuItem_SaveFrames.setActionCommand(ACTIONCOMMAND_SAVEIMAGES);
		menu.add(menuItem_SaveFrames);

		menuItem_Clear=new MenuItem("Clear Images");
		menuItem_Clear.setActionCommand(ACTIONCOMMAND_CLEARIMAGES);
		menuItem_Clear.setEnabled(true);
		menu.add(menuItem_Clear);

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

		for(ImageType imageType:ganimedModel.getImageTypes()) {
			final String imageTypeName=imageType.getName();
			CheckboxMenuItem checkboxMenuItem=new CheckboxMenuItem(imageTypeName);
			checkboxMenuItem.setActionCommand(imageTypeName);
			menuItemImageType.add(checkboxMenuItem);
			checkboxMenuItem.setState(imageType==ganimedModel.getImageType());
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
			ganimedView.sendMessage(SimpleEvents.EXIT);
		}else if(actionCommand.equals(ACTIONCOMMAND_SELECTFOLDER)){
			ganimedView.getImageSelectionView().selectImageFolder();
		}else if(actionCommand.equals(ACTIONCOMMAND_SAVEAS)){
			ganimedView.saveAs();
		}else if(actionCommand.equals(ACTIONCOMMAND_SAVEIMAGES)){
			ganimedView.saveImages();
		}else if(actionCommand.equals(ACTIONCOMMAND_SAVERAWIMAGES)){
			ganimedView.saveRawImages();
		}else if(actionCommand.equals(ACTIONCOMMAND_CLEARIMAGES)){
			ganimedView.clearImages();
		}else if(actionCommand.equals(ACTIONCOMMAND_ABOUT)){
			ganimedView.showMessageboxAbout();
		}
	}

	public void imageTypeChanged() {
		checkboxMenuItemGroupImageType.imageTypeChanged();
	}

}
