package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import calc.BudgetValue;
import calc.NumberFormatter;
import ref.Constants;
import sheet.SheetManager;
import ui.BudgetSection;

public class BudgetTab extends Tab {
	private JButton buttonAdd, buttonSave;
	private Component saveBufferTop;
	private Component saveBufferBottom;
	protected JComboBox<String> comboTimePeriod;
	
	protected ValidatedTextBox nameBox;
	protected ValidatedTextBox valueBox;
	protected TotalHeader totalHeader;
	
	private String nameLabel, valueLabel;
	protected ArrayList<BudgetSection> sections = new ArrayList<BudgetSection>();
	
	public BudgetTab(SheetManager sheetManager, String nameLabel, String valueLabel, JComponent tabPanel) {
		super(sheetManager, tabPanel);
		this.nameLabel = nameLabel;
		this.valueLabel = valueLabel;
	}
	
	public void init() {
		initPanels();
		
		initComboBox();
		
		initAddSection();
		
		initEditSection();
	}
	
	public void initPanels() {
		super.init();
	}
	
	public void initComboBox() {
		// Add time period combo
		comboTimePeriod = new JComboBox<String>();
		comboTimePeriod.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		comboTimePeriod.addItem(Constants.DAILY);
		comboTimePeriod.addItem(Constants.WEEKLY);
		comboTimePeriod.addItem(Constants.BIWEEKLY);
		comboTimePeriod.addItem(Constants.MONTHLY);
		comboTimePeriod.addItem(Constants.YEARLY);
		comboTimePeriod.setMaximumSize(new Dimension(Constants.MED_WIDTH, Constants.HEIGHT));
		addDefaultYBuffer(scrollPanel);
		scrollPanel.add(comboTimePeriod);
		
		comboTimePeriod.addActionListener(new comboTimePeriod_Action());
	}
	
	public void initAddSection() {
		// Add button
		buttonAdd = new JButton ("Add " + nameLabel);
		buttonAdd.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		addDefaultYBuffer(scrollPanel);
		scrollPanel.add(buttonAdd);
		buttonAdd.addActionListener(new buttonAdd_Action());
		
		// Name box
		addDefaultYBuffer(scrollPanel);
		nameBox = new ValidatedTextBox(scrollPanel, nameLabel+":");
		nameBox.init();
				
		// Value box
		addDefaultYBuffer(scrollPanel);
		valueBox = new ValidatedTextBox(scrollPanel, valueLabel+":");
		valueBox.init();
	}
	
	public void initEditSection() {
		totalHeader = new TotalHeader(scrollPanel, nameLabel+"s");
		totalHeader.init();
		addDefaultYBuffer(scrollPanel);
		
		saveBufferTop = addDefaultYBuffer(scrollPanel);
		buttonSave = new JButton (Constants.SAVE);
		buttonSave.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		saveBufferBottom = addDefaultYBuffer(scrollPanel);
		scrollPanel.add(buttonSave);
		buttonSave.addActionListener(new buttonSave_Action());
	}
	
	public ArrayList<BudgetSection> getSections() {
		return sections;
	}
	
	public String getTimePeriod() {
		return comboTimePeriod.getSelectedItem().toString();
	}
	
	public class buttonSave_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            updateSections();
        }
    }
	
	public class buttonAdd_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
        	createSectionFromFields();
        }
    }
	
	public class comboTimePeriod_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
        	if (comboTimePeriod.getSelectedItem() != null){
                String timePeriod = comboTimePeriod.getSelectedItem().toString();
                refreshTotal(timePeriod);
            }
        }
    }
	
	protected void refreshTotal(String timePeriod) {
		double total = 0;
		for(BudgetSection section : sections) {
        	section.setFieldsToTimePeriod(timePeriod);
        	double value = section.getValue().getValueForTimePeriod(timePeriod);
        	total = total + value;
        }
		totalHeader.setTotal(NumberFormatter.formatDoubleToCurrency(total));
	}
	
	protected void createSectionFromFields() {
		String name = nameBox.checkNotEmpty();
		if(!name.equals("")) {
			boolean newSource = true;
			for(BudgetSection section : sections) {
				if(section.getName().equals(name)) {
					newSource = false;
				}
			}
			
			if(newSource) {
				nameBox.hideErrorText();
				double rawValue = valueBox.checkDoubleValueInField();
				if(rawValue != -1) {
					String timePeriod = comboTimePeriod.getSelectedItem().toString();
					BudgetValue value = new BudgetValue(rawValue, timePeriod);
					createSection(name, timePeriod, value);
				}
			}
			else {
				nameBox.displayError("Cannot have a duplicate " + nameLabel.toLowerCase() + " name.");
			}
		}
	}
	
	public void createSection(String name, String timePeriod, BudgetValue value) {
		BudgetSection section = new BudgetSection(this, sections.size(), name, value, scrollPanel);
		section.init(timePeriod, name, value.getValueForTimePeriod(timePeriod)+"");
		sections.add(section);
		
		refreshForTimePeriod(timePeriod);
	}
	
	protected void createSection(String name) {
		String timePeriod = comboTimePeriod.getSelectedItem().toString();
		BudgetSection section = new BudgetSection(this, sections.size(), name, scrollPanel);
		section.init(timePeriod, nameLabel, valueLabel);
		sections.add(section);
		
		refreshForTimePeriod(timePeriod);
	}
	
	protected void updateSections() {
		String timePeriod = comboTimePeriod.getSelectedItem().toString();
		
		boolean errored = false;
		ArrayList<String> names = new ArrayList<String>();
		for(BudgetSection section : sections) {
			ValidatedTextBox currentValueBox = section.getValueBox();
			double rawIncome = currentValueBox.checkDoubleValueInField();
			if(rawIncome == -1) {
				errored = true;
				break;
			}
			ValidatedTextBox currentNameBox = section.getNameBox();
			String name = currentNameBox.getText();
			if(names.contains(name)) {
				errored = true;
				currentNameBox.displayError("Cannot have a duplicate " + nameLabel.toLowerCase() + " name.");
				break;
			}
			else {
				currentNameBox.hideErrorText();
				names.add(name);
			}
		}
		
		if(!errored) {
			for(BudgetSection section : sections) {
				section.updateValuesToMatchTextFields(timePeriod);
			}
			saveToXml();
		}
	}
	
	public void removeAllSections() {
		for(BudgetSection section : sections) {
			section.remove();
		}
		sections.clear();
	}
	
	public void removeSection(int sequence) {
		sections.remove(sequence);
		for(int i=0; i<sections.size(); i++) {
			sections.get(i).setSequence(i);
		}
		
		String timePeriod = comboTimePeriod.getSelectedItem().toString();
		refreshForTimePeriod(timePeriod);
	}
	
	protected void refreshForTimePeriod(String timePeriod) {
		if(sections.size() > 0) {
			refreshTotal(timePeriod);
			/*ArrayList<BudgetValue> values = new ArrayList<BudgetValue>();
			for(BudgetSection section : sections) {
				values.add(section.getValue());
			}
			String total = NumberFormatter.formatDoubleToCurrency(BudgetCalculator.sumValues(values, timePeriod));
			totalHeader.setTotal(total);*/
			
			refreshSaveButton();
		}
		else {
			totalHeader.setTotal("0.00");
		}
		super.refresh();
	}
	
	protected void refreshSaveButton() {
		scrollPanel.remove(saveBufferTop);
		scrollPanel.remove(buttonSave);
		scrollPanel.remove(saveBufferBottom);
		saveBufferTop = addDefaultYBuffer(scrollPanel);
		scrollPanel.add(buttonSave);
		saveBufferBottom = addDefaultYBuffer(scrollPanel);
	}
	
	protected void saveToXml() {
		// Override
	}
	
	protected void loadFromXml() {
		// Override
	}
}
