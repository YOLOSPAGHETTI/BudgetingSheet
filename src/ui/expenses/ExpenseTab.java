package ui.expenses;

import java.util.ArrayList;

import javax.swing.JComponent;

import calc.BudgetValue;
import file.XMLManager;
import ref.Constants;
import sheet.SheetManager;
import ui.BudgetSection;
import ui.BudgetTab;

public class ExpenseTab extends BudgetTab {	
	public ExpenseTab(SheetManager sheetManager, JComponent panelExpense) {
    	super(sheetManager, "Expense", "Payment", panelExpense);
    }
	
	@Override
	public void init() {
		super.init();
		comboTimePeriod.setSelectedItem(Constants.MONTHLY);
	}
	
	protected ArrayList<String> getExpenseSectionXml() {
		ArrayList<String> saveXml = new ArrayList<String>();
		for(BudgetSection expenseSection : getSections()) {
			ArrayList<String> expenseSectionXml = new ArrayList<String>();
			BudgetValue payment = expenseSection.getValue();
			expenseSectionXml.add(XMLManager.buildXMLLine(Constants.EXPENSE, expenseSection.getName()));
			expenseSectionXml.add(XMLManager.buildXMLLine(Constants.PAYMENT_DAILY, payment.getDailyValue()));
			expenseSectionXml.add(XMLManager.buildXMLLine(Constants.PAYMENT_WEEKLY, payment.getWeeklyValue()));
			expenseSectionXml.add(XMLManager.buildXMLLine(Constants.PAYMENT_BIWEEKLY, payment.getBiWeeklyValue()));
			expenseSectionXml.add(XMLManager.buildXMLLine(Constants.PAYMENT_MONTHLY, payment.getMonthlyValue()));
			expenseSectionXml.add(XMLManager.buildXMLLine(Constants.PAYMENT_YEARLY, payment.getYearlyValue()));
			expenseSectionXml = XMLManager.encapsulateInTag(Constants.EXPENSE_SECTION, expenseSectionXml);
			
			saveXml.addAll(expenseSectionXml);
		}
		
		return saveXml;
	}
	
	protected void loadExpenseSectionFromXml(String tag) {
		// Remove all expense sections
		removeAllSections();
		
		// Add new ones from xml
		ArrayList<String> saveXml = sheetManager.getCurrentSheet().getSaveXml();
		ArrayList<String> expenseXml = XMLManager.getEncapsulatedXML(saveXml, tag);
		ArrayList<String> expenseSectionXml = XMLManager.getEncapsulatedXML(expenseXml, Constants.EXPENSE_SECTION);
		
		while(!expenseSectionXml.isEmpty()) {
			String expense = XMLManager.getSpecificValueFromXMLFileFirst(expenseSectionXml, Constants.EXPENSE);
			double paymentDaily = XMLManager.getDoubleValueFromXMLFileFirst(expenseSectionXml, Constants.PAYMENT_DAILY);
			double paymentWeekly = XMLManager.getDoubleValueFromXMLFileFirst(expenseSectionXml, Constants.PAYMENT_WEEKLY);
			double paymentBiweekly = XMLManager.getDoubleValueFromXMLFileFirst(expenseSectionXml, Constants.PAYMENT_BIWEEKLY);
			double paymentMonthly = XMLManager.getDoubleValueFromXMLFileFirst(expenseSectionXml, Constants.PAYMENT_MONTHLY);
			double paymentYearly = XMLManager.getDoubleValueFromXMLFileFirst(expenseSectionXml, Constants.PAYMENT_YEARLY);
			
			String timePeriod = getTimePeriod();
			BudgetValue payment = new BudgetValue(paymentDaily, paymentWeekly, paymentBiweekly, paymentMonthly, paymentYearly);
			createSection(expense, timePeriod, payment);
			
			expenseXml = XMLManager.removeSections(expenseXml, Constants.EXPENSE_SECTION, true);
			expenseSectionXml = XMLManager.getEncapsulatedXML(expenseXml, Constants.EXPENSE_SECTION);
		}
	}
}
