package ui.income;

import javax.swing.JComponent;

import calc.BudgetValue;
import calc.IncomeValue;
import calc.NumberFormatter;
import calc.TaxValue;
import ref.Constants;
import ui.BudgetSection;
import ui.ValidatedTextBox;

public class IncomeSection extends BudgetSection {
	private IncomeValue income;
	private TaxValue tax;
	private int hoursWorked;
	
	private TaxBox taxBox;
	private ValidatedTextBox hoursWorkedBox;
	
	public IncomeSection(IncomeTab incomeTab, int sequence, String source, IncomeValue income, TaxValue tax, int hoursWorked, JComponent panel) {
		super(incomeTab, sequence, source, income, panel);
		this.income = income;
		this.tax = tax;
		this.hoursWorked = hoursWorked;
	}
	
	@Override
	public void init(String timePeriod) {
		nameBox = new ValidatedTextBox(getPanel(), "Source:", name);
		nameBox.init();
		nameBox.addBottomBuffer(Constants.BUFFER);
		
		double incomeValue = value.getValueForTimePeriod(timePeriod);
		valueBox = new ValidatedTextBox(getPanel(), "Income:", ""+incomeValue);
		valueBox.init();
		valueBox.addBottomBuffer(Constants.BUFFER);
		
		double taxValue = tax.getValueForTimePeriod(timePeriod);
		taxBox = new TaxBox(getPanel(), "Tax:", ""+taxValue);
		taxBox.init();
		taxBox.addBottomBuffer(Constants.BUFFER);
		
		hoursWorkedBox = new ValidatedTextBox(getPanel(), "Hours Worked Weekly:", ""+hoursWorked);
		hoursWorkedBox.init();
		hoursWorkedBox.addBottomBuffer(Constants.BUFFER);
		
		super.init(timePeriod);
	}
	
	public IncomeValue getIncome() {
		return income;
	}
	
	public TaxValue getTax() {
		return tax;
	}
	
	public int getHoursWorked() {
		return hoursWorked;
	}
	
	public TaxBox getTaxBox() {
		return taxBox;
	}
	
	public ValidatedTextBox getHoursWorkedBox() {
		return hoursWorkedBox;
	}
	
	@Override
	public void setFieldsToTimePeriod(String timePeriod) {
		super.setFieldsToTimePeriod(timePeriod);
		
		String currency = NumberFormatter.formatDoubleToCurrency(tax.getValueForTimePeriod(timePeriod));
		taxBox.setText(currency);
	}
	
	public void updateValuesToMatchTextFields(int hoursWorked, String timePeriod) {
		super.updateValuesToMatchTextFields(timePeriod);
		
		double rawValue = Double.parseDouble(valueBox.getText().replace(",", ""));
		income = new IncomeValue(rawValue, hoursWorked, timePeriod);
		
		double rawTax = Double.parseDouble(taxBox.getText());
		tax = new TaxValue(rawTax, hoursWorked, timePeriod, rawValue, taxBox.isPercent());
		taxBox.setText(NumberFormatter.formatDoubleToCurrency(rawTax));
		
		this.hoursWorked = hoursWorked;
	}
	
	@Override
	public void remove() {
		taxBox.remove();
		hoursWorkedBox.remove();
		super.remove();
	}
}
