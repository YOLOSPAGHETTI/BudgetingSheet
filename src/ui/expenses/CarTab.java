package ui.expenses;

import java.util.ArrayList;

import javax.swing.JComponent;

import calc.IncomeValue;
import calc.TaxValue;
import file.XMLManager;
import ref.Constants;
import sheet.SheetManager;

public class CarTab extends ExpenseTab {
	public CarTab(SheetManager sheetManager, JComponent panelExpense) {
    	super(sheetManager, panelExpense);
    }
	
	@Override
	public void init() {
		super.init();
		createSection("Loan");
		createSection("Insurance");
		createSection("Gas");
	}
	
	@Override
	protected void saveToXml() {
		ArrayList<String> saveXml = getExpenseSectionXml();
		
		saveXml = XMLManager.encapsulateInTag(Constants.CAR_EXPENSES, saveXml);
		
		sheetManager.replaceSection(Constants.CAR_EXPENSES, saveXml);
	}
	
	@Override
	protected void loadFromXml() {
		loadExpenseSectionFromXml(Constants.CAR_EXPENSES);
	}
}
