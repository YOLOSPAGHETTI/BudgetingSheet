package ui.expenses;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import file.XMLManager;
import ref.Constants;
import sheet.SheetManager;

public class HousingTab extends ExpenseTab {
	private JComboBox<String> comboHousingType;
	
	public HousingTab(SheetManager sheetManager, JComponent panelExpense) {
    	super(sheetManager, panelExpense);
    }
	
	@Override
	public void init() {
		initPanels();
		initComboBox();
		initAddSection();
		
		comboHousingType = new JComboBox<String>();
		comboHousingType.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		comboHousingType.addItem(Constants.RENT);
		comboHousingType.addItem(Constants.OWN);
		comboHousingType.setMaximumSize(new Dimension(Constants.MED_WIDTH, Constants.HEIGHT));
		addDefaultYBuffer(scrollPanel);
		scrollPanel.add(comboHousingType);
		
		comboHousingType.addActionListener(new comboHousingType_Action());
		
		initEditSection();
		
		comboTimePeriod.setSelectedItem(Constants.MONTHLY);
	}
	
	public class comboHousingType_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
        	if (comboHousingType.getSelectedItem() != null){
                String housingType = comboHousingType.getSelectedItem().toString();
                addExpenseSectionsForHousingType(housingType);
            }
        }
    }
	
	public void addExpenseSectionsForHousingType(String housingType) {
		removeAllSections();
		if(housingType.equals(Constants.RENT)) {
			createSection("Rent");
			createSection("Insurance");
			createSection("Gas");
			createSection("Electric");
			createSection("Internet");
			createSection("TV");
        }
        else if(housingType.equals(Constants.OWN)) {
        	createSection("Mortgage");
        	createSection("HOA");
        	createSection("Insurance");
        	createSection("Income Tax");
        	createSection("Gas");
        	createSection("Electric");
        	createSection("Internet");
        	createSection("TV");
        	createSection("Water");
        	createSection("Sewage");
        }
	}
	
	@Override
	protected void saveToXml() {
		ArrayList<String> saveXml = getExpenseSectionXml();
		
		saveXml = XMLManager.encapsulateInTag(Constants.HOUSING_EXPENSES, saveXml);
		
		sheetManager.replaceSection(Constants.HOUSING_EXPENSES, saveXml);
	}
	
	@Override
	protected void loadFromXml() {
		loadExpenseSectionFromXml(Constants.HOUSING_EXPENSES);
	}
}
