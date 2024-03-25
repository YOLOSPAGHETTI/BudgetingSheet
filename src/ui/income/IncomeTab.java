package ui.income;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import calc.IncomeValue;
import calc.NumberFormatter;
import calc.TaxValue;
import file.XMLManager;
import ref.Constants;
import sheet.SheetManager;
import ui.ValidatedTextBox;
import ui.BudgetTab;

public class IncomeTab extends BudgetTab {
	private TaxBox taxBox;
	private ValidatedTextBox hoursWorkedBox;
	
	protected ArrayList<IncomeSection> incomeSections = new ArrayList<IncomeSection>();
	
	public IncomeTab(SheetManager sheetManager, JComponent panelIncome) {
    	super(sheetManager, "Source", "Income", panelIncome);
    }
	
	@Override
	public void init() {
		initPanels();
		
		// Add time period combo
		comboTimePeriod = new JComboBox<String>();
		comboTimePeriod.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		comboTimePeriod.addItem(Constants.HOURLY);
		comboTimePeriod.addItem(Constants.DAILY);
		comboTimePeriod.addItem(Constants.WEEKLY);
		comboTimePeriod.addItem(Constants.BIWEEKLY);
		comboTimePeriod.addItem(Constants.MONTHLY);
		comboTimePeriod.addItem(Constants.YEARLY);
		comboTimePeriod.setMaximumSize(new Dimension(Constants.MED_WIDTH, Constants.HEIGHT));
		addDefaultYBuffer(scrollPanel);
		scrollPanel.add(comboTimePeriod);
		
		comboTimePeriod.addActionListener(new comboTimePeriod_Action());
		
		initAddSection();
		
		// Tax box
		addDefaultYBuffer(scrollPanel);
		taxBox = new TaxBox(scrollPanel, "Tax:");
		taxBox.init();
		
		// Hours worked box
		addDefaultYBuffer(scrollPanel);
		hoursWorkedBox = new ValidatedTextBox(scrollPanel, "Hours Worked Weekly:", "40");
		hoursWorkedBox.init();
		
		initEditSection();
	}
	
	@Override
	protected void refreshTotal(String timePeriod) {
		double total = 0;
		for(IncomeSection section : incomeSections) {
        	section.setFieldsToTimePeriod(timePeriod);
        	double value = section.getValue().getValueForTimePeriod(timePeriod);
        	total = total + value;
        }
		totalHeader.setTotal(NumberFormatter.formatDoubleToCurrency(total));
	}
	
	@Override
	protected void createSectionFromFields() {
		String source = nameBox.checkNotEmpty();
		if(!source.equals("")) {
			boolean newSource = true;
			for(IncomeSection section : incomeSections) {
				if(section.getName().equals(source)) {
					newSource = false;
				}
			}
			
			if(newSource) {
				nameBox.hideErrorText();
				int hoursWorked = hoursWorkedBox.checkIntegerValueInField();
				double rawIncome = valueBox.checkDoubleValueInField();
				double rawTax = taxBox.checkDoubleValueInField();
				
				if(hoursWorked != -1 && rawIncome != -1 && rawTax != -1) {
					String timePeriod = comboTimePeriod.getSelectedItem().toString();
					IncomeValue income = new IncomeValue(rawIncome, hoursWorked, timePeriod);
					TaxValue tax = new TaxValue(rawTax, hoursWorked, timePeriod, rawIncome, taxBox.isPercent());
					createSection(source, timePeriod, hoursWorked, income, tax);
				}
			}
			else {
				nameBox.displayError("Cannot have a duplicate source name.");
			}
		}
	}
	
	private void createSection(String source, String timePeriod, int hoursWorked, IncomeValue income, TaxValue tax) {
		IncomeSection incomeSection = new IncomeSection(this, incomeSections.size(), source, income, tax, hoursWorked, scrollPanel);
		incomeSection.init(timePeriod);
		incomeSections.add(incomeSection);
		
		refreshForTimePeriod(timePeriod);
	}
	
	@Override
	protected void updateSections() {
		String timePeriod = comboTimePeriod.getSelectedItem().toString();
		
		boolean errored = false;
		ArrayList<String> sources = new ArrayList<String>();
		for(IncomeSection section : incomeSections) {
			ValidatedTextBox currentIncomeBox = section.getValueBox();
			double rawIncome = currentIncomeBox.checkDoubleValueInField();
			if(rawIncome == -1) {
				errored = true;
				break;
			}
			ValidatedTextBox currentSourceBox = section.getNameBox();
			String source = currentSourceBox.getText();
			if(sources.contains(source)) {
				errored = true;
				currentSourceBox.displayError("Cannot have a duplicate source name.");
				break;
			}
			else {
				currentSourceBox.hideErrorText();
				sources.add(source);
			}
		}
		
		if(!errored) {
			for(IncomeSection section : incomeSections) {
				int currentHoursWorked = section.getHoursWorkedBox().checkIntegerValueInField();
				if(currentHoursWorked != -1) {
					section.updateValuesToMatchTextFields(currentHoursWorked, timePeriod);
				}
			}
		}
		
		saveToXml();
	}
	
	@Override
	public void removeAllSections() {
		for(IncomeSection section : incomeSections) {
			section.remove();
		}
		incomeSections.clear();
	}
	
	@Override
	public void removeSection(int sequence) {
		incomeSections.remove(sequence);
		for(int i=0; i<incomeSections.size(); i++) {
			incomeSections.get(i).setSequence(i);
		}
		
		String timePeriod = comboTimePeriod.getSelectedItem().toString();
		refreshForTimePeriod(timePeriod);
	}
	
	@Override
	protected void refreshForTimePeriod(String timePeriod) {
		if(incomeSections.size() > 0) {
			refreshTotal(timePeriod);
			
			refreshSaveButton();
		}
		else {
			totalHeader.setTotal("0.00");
		}
		super.refresh();
	}
	
	@Override
	protected void saveToXml() {
		ArrayList<String> saveXml = new ArrayList<String>();
		for(IncomeSection incomeSection : incomeSections) {
			ArrayList<String> incomeSectionXml = new ArrayList<String>();
			IncomeValue income = incomeSection.getIncome();
			TaxValue tax = incomeSection.getTax();
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.SOURCE, incomeSection.getName()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.INCOME_HOURLY, income.getHourlyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.INCOME_DAILY, income.getDailyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.INCOME_WEEKLY, income.getWeeklyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.INCOME_BIWEEKLY, income.getBiWeeklyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.INCOME_MONTHLY, income.getMonthlyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.INCOME_YEARLY, income.getYearlyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.TAX_HOURLY, tax.getHourlyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.TAX_DAILY, tax.getDailyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.TAX_WEEKLY, tax.getWeeklyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.TAX_BIWEEKLY, tax.getBiWeeklyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.TAX_MONTHLY, tax.getMonthlyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.TAX_YEARLY, tax.getYearlyValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.TAX_PERCENT, tax.getPercentageValue()));
			incomeSectionXml.add(XMLManager.buildXMLLine(Constants.HOURS_WORKED, incomeSection.getHoursWorked()));
			incomeSectionXml = XMLManager.encapsulateInTag(Constants.INCOME_SECTION, incomeSectionXml);
			
			saveXml.addAll(incomeSectionXml);
		}
		saveXml = XMLManager.encapsulateInTag(Constants.INCOME, saveXml);
		
		sheetManager.replaceSection(Constants.INCOME, saveXml);
	}
	
	@Override
	protected void loadFromXml() {
		// Remove all income sections
		removeAllSections();
		
		// Add new ones from xml
		ArrayList<String> saveXml = sheetManager.getCurrentSheet().getSaveXml();
		ArrayList<String> incomeXml = XMLManager.getEncapsulatedXML(saveXml, Constants.INCOME);
		ArrayList<String> incomeSectionXml = XMLManager.getEncapsulatedXML(incomeXml, Constants.INCOME_SECTION);
		
		while(!incomeSectionXml.isEmpty()) {
			String source = XMLManager.getSpecificValueFromXMLFileFirst(incomeSectionXml, Constants.SOURCE);
			double incomeHourly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.INCOME_HOURLY);
			double incomeDaily = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.INCOME_DAILY);
			double incomeWeekly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.INCOME_WEEKLY);
			double incomeBiweekly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.INCOME_BIWEEKLY);
			double incomeMonthly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.INCOME_MONTHLY);
			double incomeYearly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.INCOME_YEARLY);
			double taxHourly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.TAX_HOURLY);
			double taxDaily = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.TAX_DAILY);
			double taxWeekly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.TAX_WEEKLY);
			double taxBiweekly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.TAX_BIWEEKLY);
			double taxMonthly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.TAX_MONTHLY);
			double taxYearly = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.TAX_YEARLY);
			double taxPercent = XMLManager.getDoubleValueFromXMLFileFirst(incomeSectionXml, Constants.TAX_PERCENT);
			int hoursWorked = XMLManager.getIntegerValueFromXMLFileFirst(incomeSectionXml, Constants.HOURS_WORKED);
			
			String timePeriod = comboTimePeriod.getSelectedItem().toString();
			IncomeValue income = new IncomeValue(incomeHourly, incomeDaily, incomeWeekly, incomeBiweekly, incomeMonthly, incomeYearly);
			TaxValue tax = new TaxValue(taxHourly, taxDaily, taxWeekly, taxBiweekly, taxMonthly, taxYearly, taxPercent);
			createSection(source, timePeriod, hoursWorked, income, tax);
			
			incomeXml = XMLManager.removeSections(incomeXml, Constants.INCOME_SECTION, true);
			incomeSectionXml = XMLManager.getEncapsulatedXML(incomeXml, Constants.INCOME_SECTION);
			//System.out.println(incomeXml);
			//System.out.println(incomeSectionXml);
		}
	}
}
