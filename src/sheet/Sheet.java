package sheet;

import java.io.File;
import java.util.ArrayList;

import calc.BudgetCalculator;
import file.FileManager;
import file.XMLManager;
import ref.Constants;

public class Sheet {
	private String name;
	private File file;
	private ArrayList<String> saveXml;
	private double dailyTotal;
	private double savings;
	
	public Sheet(String name, File file, ArrayList<String> saveXml) {
		this.name = name;
		this.file = file;
		this.saveXml = saveXml;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	protected String getDir() {
		return file.getAbsolutePath();
	}
	
	protected File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public ArrayList<String> getSaveXml() {
		return saveXml;
	}
	
	public double getSavings() {
		return savings;
	}
	
	public double getDailyTotal() {
		return dailyTotal;
	}
	
	protected void overwriteXml(ArrayList<String> saveXml) {
		this.saveXml = saveXml;
		String dir = file.getAbsolutePath();
		FileManager.clear(dir);
		FileManager.write(saveXml, dir);
	}
	
	public void calculateSavings() {
		ArrayList<Double> savingsValues = getValues(saveXml, Constants.SAVINGS, Constants.SAVINGS_SECTION, Constants.SAVINGS_VALUE);
		
		savings = BudgetCalculator.sumValues(savingsValues);;
	}
	
	public void calculateDailyTotal() {
    	ArrayList<Double> addValues = new ArrayList<Double>();
    	ArrayList<Double> subtractValues = new ArrayList<Double>();
    	
    	ArrayList<Double> incomeValues = getValues(saveXml, Constants.INCOME, Constants.INCOME_SECTION, Constants.INCOME_DAILY);
    	addValues.addAll(incomeValues);
    	
    	ArrayList<Double> incomeTaxValues = getValues(saveXml, Constants.INCOME, Constants.INCOME_SECTION, Constants.TAX_DAILY);
    	ArrayList<Double> carExpenseValues = getValues(saveXml, Constants.CAR_EXPENSES, Constants.EXPENSE_SECTION, Constants.PAYMENT_DAILY);
    	ArrayList<Double> houseExpenseValues = getValues(saveXml, Constants.HOUSING_EXPENSES, Constants.EXPENSE_SECTION, Constants.PAYMENT_DAILY);
    	ArrayList<Double> otherExpenseValues = getValues(saveXml, Constants.OTHER_EXPENSES, Constants.EXPENSE_SECTION, Constants.PAYMENT_DAILY);
    	ArrayList<Double> otherDebtValues = getValues(saveXml, Constants.OTHER_DEBT, Constants.EXPENSE_SECTION, Constants.PAYMENT_DAILY);
    	subtractValues.addAll(incomeTaxValues);
    	subtractValues.addAll(carExpenseValues);
    	subtractValues.addAll(houseExpenseValues);
    	subtractValues.addAll(otherExpenseValues);
    	subtractValues.addAll(otherDebtValues);
    	
    	dailyTotal = BudgetCalculator.sumValues(addValues, subtractValues);
	}
	
    private ArrayList<Double> getValues(ArrayList<String> saveXml, String tabTag, String sectionTag, String tag) {
    	ArrayList<Double> values = new ArrayList<Double>();
    	
    	ArrayList<String> tabXml = XMLManager.getEncapsulatedXML(saveXml, tabTag);
    	ArrayList<String> sectionXml = XMLManager.getEncapsulatedXML(tabXml, sectionTag);
    	
    	while(!sectionXml.isEmpty()) {
			String valueString = XMLManager.getSpecificValueFromXMLFile(sectionXml, tag);
			double value = Double.valueOf(valueString);
			values.add(value);
			
			tabXml = XMLManager.removeSections(tabXml, sectionTag, true);
			sectionXml = XMLManager.getEncapsulatedXML(tabXml, sectionTag);
		}
    	return values;
    }
}
