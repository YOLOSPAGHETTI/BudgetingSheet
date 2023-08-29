package ui.savings;

import javax.swing.JComponent;

import calc.NumberFormatter;
import ref.Constants;
import ui.BudgetSection;
import ui.ValidatedTextBox;

public class SavingsSection extends BudgetSection {
	private double savingsValue;
	
	public SavingsSection(SavingsTab tab, int sequence, String name, double value, JComponent panel) {
		super(tab, sequence, name, panel);
		this.savingsValue = value;
	}
	
	@Override 
	public void init() {
		nameBox = new ValidatedTextBox(getPanel(), Constants.ACCOUNT+":", name);
		nameBox.init();
		nameBox.addBottomBuffer(Constants.BUFFER);
		
		valueBox = new ValidatedTextBox(getPanel(), Constants.SAVINGS+":", ""+savingsValue);
		valueBox.init();
		valueBox.addBottomBuffer(Constants.BUFFER);
		
		super.init();
		
		String currency = NumberFormatter.formatDoubleToCurrency(savingsValue);
		valueBox.setText(currency);
	}
	
	@Override
	public void updateValuesToMatchTextFields(String timePeriod) {
		name = nameBox.getText();
		savingsValue = Double.parseDouble(valueBox.getText().replace(",", ""));
		valueBox.setText(NumberFormatter.formatDoubleToCurrency(savingsValue));
	}
	
	protected double getSavingsValue() {
		return savingsValue;
	}
}
