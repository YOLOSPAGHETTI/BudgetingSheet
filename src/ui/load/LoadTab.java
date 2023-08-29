package ui.load;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import ref.Constants;
import sheet.SheetManager;
import ui.Tab;
import ui.ValidatedTextBox;

import java.util.ArrayList;

public class LoadTab extends Tab {
	private ValidatedTextBox sheetNameBox;
	private JButton buttonAdd;
	private ArrayList<LoadSection> loadSections;
	private JLabel labelHeader;
	private JLabel labelLoaded;
	
	public LoadTab(SheetManager sheetManager, JComponent panelLoad) {
    	super(sheetManager, panelLoad);
    }
	
	@Override
	public void init() {
		super.init();
		
		// Add sheet button
		buttonAdd = new JButton ("Add Sheet");
		buttonAdd.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		addDefaultYBuffer(scrollPanel);
		scrollPanel.add(buttonAdd);
		
		buttonAdd.addActionListener(new buttonAdd_Action());
    	
    	// Add new sheet field and label
		addDefaultYBuffer(scrollPanel);
		sheetNameBox = new ValidatedTextBox(scrollPanel, "Sheet Name:");
		sheetNameBox.init();
    	
		// Load sheets header
		addDefaultYBuffer(scrollPanel);
		labelHeader = new JLabel("Load A Saved Sheet");
		labelHeader.setFont(new Font("Serif", Font.BOLD, Constants.HEADER));
		scrollPanel.add(labelHeader);
		
		// Load sheets and create buttons for each
		addDefaultYBuffer(scrollPanel);
    	loadSections = new ArrayList<LoadSection>();
    	ArrayList<String> sheetNames = sheetManager.getSheetNames();
    	for(String sheetName : sheetNames) {
    		createLoadSection(sheetName);
    	}
		
    	// Info text to say when a sheet is loaded
		labelLoaded = new JLabel("Successfully loaded a sheet");
		labelLoaded.setForeground(Color.BLUE);
		labelLoaded.setVisible(false);
	}
	
	public class buttonAdd_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
            createNewSaveFile();
        }
	}
	
	private void createNewSaveFile() {
		String sheetName = sheetNameBox.checkNotEmpty();
		boolean duplicate = sheetManager.addSheet(sheetName);
		
		if(!sheetName.isEmpty()) {			
			if(duplicate) {
				sheetNameBox.displayError("This sheet name is already in use.");
			}
			else {
				sheetNameBox.hideErrorText();
				
				createLoadSection(sheetName);
			}
		}
	}
	
	private void createLoadSection(String sheetName) {
		LoadSection loadSection = new LoadSection(this, loadSections.size(), sheetManager, scrollPanel, sheetName);
		loadSection.init();
		loadSections.add(loadSection);
		
		refresh();
	}
	
	@Override
	public void removeSection(int sequence) {
		if(loadSections.get(sequence).wasRemoved()) {
			loadSections.remove(sequence);
			for(int i=0; i<loadSections.size(); i++) {
				loadSections.get(i).setSequence(i);
			}
			
			refresh();
		}
	}
}
