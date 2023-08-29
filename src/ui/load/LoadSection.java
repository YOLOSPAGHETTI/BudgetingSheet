package ui.load;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import ref.Constants;
import sheet.SheetManager;
import ui.Section;

public class LoadSection extends Section {
	private String sheetName;
	private boolean removed;
	
	private SheetManager sheetManager;
	private LoadBox loadBox;
	
	public LoadSection(LoadTab loadTab, int sequence, SheetManager sheetManager, JComponent panel, String sheetName) {
		super(loadTab, sequence, panel);
		this.sheetManager = sheetManager;
		this.sheetName = sheetName;
		removed = false;
	}
	
	public void init() {
		loadBox = new LoadBox(sheetManager, getPanel(), sheetName);
		loadBox.init();
		loadBox.addBottomBuffer(Constants.BUFFER);
		
		super.init();
	}
	
	@Override
	protected void remove() {
		int a=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the sheet?");  
		if(a==JOptionPane.YES_OPTION){  
			loadBox.remove();
			super.remove();
			sheetManager.deleteSheet(sheetName);
			removed = true;
		}
	}
	
	public boolean wasRemoved() {
		return removed;
	}
}
