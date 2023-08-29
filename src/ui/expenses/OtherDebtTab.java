package ui.expenses;

import java.util.ArrayList;

import javax.swing.JComponent;

import file.XMLManager;
import ref.Constants;
import sheet.SheetManager;

public class OtherDebtTab extends ExpenseTab {
	public OtherDebtTab(SheetManager sheetManager, JComponent panelExpense) {
    	super(sheetManager, panelExpense);
    }
	
	@Override
	public void init() {
		super.init();
		createSection("Student Loans");
		createSection("Credit Card");
	}
	
	@Override
	protected void saveToXml() {
		ArrayList<String> saveXml = getExpenseSectionXml();
		
		saveXml = XMLManager.encapsulateInTag(Constants.OTHER_DEBT, saveXml);
		
		sheetManager.replaceSection(Constants.OTHER_DEBT, saveXml);
	}
	
	@Override
	protected void loadFromXml() {
		loadExpenseSectionFromXml(Constants.OTHER_DEBT);
	}
}
