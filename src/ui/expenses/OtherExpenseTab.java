package ui.expenses;

import java.util.ArrayList;

import javax.swing.JComponent;

import file.XMLManager;
import ref.Constants;
import sheet.SheetManager;

public class OtherExpenseTab extends ExpenseTab {
	public OtherExpenseTab(SheetManager sheetManager, JComponent panelExpense) {
    	super(sheetManager, panelExpense);
    }
	
	@Override
	public void init() {
		super.init();
		createSection("Food");
	}
	
	@Override
	protected void saveToXml() {
		ArrayList<String> saveXml = getExpenseSectionXml();
		
		saveXml = XMLManager.encapsulateInTag(Constants.OTHER_EXPENSES, saveXml);
		
		sheetManager.replaceSection(Constants.OTHER_EXPENSES, saveXml);
	}
	
	@Override
	protected void loadFromXml() {
		loadExpenseSectionFromXml(Constants.OTHER_EXPENSES);
	}
}
