package ui.savings;

import java.util.ArrayList;

import javax.swing.JComponent;

import calc.NumberFormatter;
import file.XMLManager;
import ref.Constants;
import sheet.SheetManager;
import ui.BudgetTab;
import ui.ValidatedTextBox;

public class SavingsTab extends BudgetTab {
	private ArrayList<SavingsSection> savingsSections = new ArrayList<SavingsSection>();
	
	public SavingsTab(SheetManager sheetManager, JComponent panelExpense) {
    	super(sheetManager, Constants.ACCOUNT, Constants.SAVINGS, panelExpense);
    }
	
	@Override
	public void init() {
		initPanels();
		
		initAddSection();
		
		initEditSection();
	}
	
	@Override
	protected void createSectionFromFields() {
		String account = nameBox.checkNotEmpty();
		if(!account.equals("")) {
			boolean newSource = true;
			for(SavingsSection section : savingsSections) {
				if(section.getName().equals(account)) {
					newSource = false;
				}
			}
			
			if(newSource) {
				nameBox.hideErrorText();
				double savingsValue = valueBox.checkDoubleValueInField();
				if(savingsValue != -1) {
					createSection(account, savingsValue);
				}
			}
			else {
				nameBox.displayError("Cannot have a duplicate account name.");
			}
		}
	}
	
	protected void refreshTotal() {
		double total = 0;
		for(SavingsSection section : savingsSections) {
        	double value = section.getSavingsValue();
        	total = total + value;
        }
		totalHeader.setTotal(NumberFormatter.formatDoubleToCurrency(total));
	}
	
	public void createSection(String name, double value) {
		SavingsSection section = new SavingsSection(this, savingsSections.size(), name, value, scrollPanel);
		section.init();
		savingsSections.add(section);
		
		refresh();
	}
	
	@Override
	protected void updateSections() {		
		boolean errored = false;
		ArrayList<String> names = new ArrayList<String>();
		for(SavingsSection section : savingsSections) {
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
				currentNameBox.displayError("Cannot have a duplicate account name.");
				break;
			}
			else {
				currentNameBox.hideErrorText();
				names.add(name);
			}
		}
		
		if(!errored) {
			saveToXml();
		}
	}
	
	@Override
	public void removeAllSections() {
		for(SavingsSection section : savingsSections) {
			section.remove();
		}
		savingsSections.clear();
	}
	
	@Override
	public void removeSection(int sequence) {
		savingsSections.remove(sequence);
		for(int i=0; i<savingsSections.size(); i++) {
			savingsSections.get(i).setSequence(i);
		}
		
		refresh();
	}
	
	@Override
	protected void saveToXml() {
		ArrayList<String> saveXml = new ArrayList<String>();
		for(SavingsSection savingsSection : savingsSections) {
			ArrayList<String> savingsSectionXml = new ArrayList<String>();
			savingsSectionXml.add(XMLManager.buildXMLLine(Constants.ACCOUNT, savingsSection.getName()));
			savingsSectionXml.add(XMLManager.buildXMLLine(Constants.SAVINGS_VALUE, savingsSection.getSavingsValue()));
			savingsSectionXml = XMLManager.encapsulateInTag(Constants.SAVINGS_SECTION, savingsSectionXml);
			
			saveXml.addAll(savingsSectionXml);
		}
		saveXml = XMLManager.encapsulateInTag(Constants.SAVINGS, saveXml);
		
		sheetManager.replaceSection(Constants.SAVINGS, saveXml);
	}
	
	@Override
	protected void loadFromXml() {
		// Remove all expense sections
		removeAllSections();
		
		// Add new ones from xml
		ArrayList<String> saveXml = sheetManager.getCurrentSheet().getSaveXml();
		ArrayList<String> savingsXml = XMLManager.getEncapsulatedXML(saveXml, Constants.SAVINGS);
		ArrayList<String> savingsSectionXml = XMLManager.getEncapsulatedXML(savingsXml, Constants.SAVINGS_SECTION);
		
		while(!savingsSectionXml.isEmpty()) {
			String account = XMLManager.getSpecificValueFromXMLFile(savingsSectionXml, Constants.ACCOUNT);
			double savingsValue = XMLManager.getDoubleValueFromXMLFile(savingsSectionXml, Constants.SAVINGS_VALUE);
			
			createSection(account, savingsValue);
			
			savingsXml = XMLManager.removeSections(savingsXml, Constants.SAVINGS_SECTION, true);
			savingsSectionXml = XMLManager.getEncapsulatedXML(savingsXml, Constants.SAVINGS_SECTION);
		}
	}
	
	@Override
	public void refresh() {
		if(savingsSections.size() > 0) {
			refreshTotal();
			refreshSaveButton();
		}
		else {
			totalHeader.setTotal("0.00");
		}
		super.refresh();
	}
}
