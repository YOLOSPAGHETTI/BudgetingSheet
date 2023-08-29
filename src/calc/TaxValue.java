package calc;

public class TaxValue extends IncomeValue {
	private double percentage;
	
	public TaxValue(double hourlyValue, double dailyValue, double weeklyValue, double biweeklyValue, double monthlyValue, double yearlyValue, double percentage) {
		super(hourlyValue, dailyValue, weeklyValue, biweeklyValue, monthlyValue, yearlyValue);
		this.percentage = percentage;
	}
	
	public TaxValue(double percentValue, int hoursPerDay, String convertFrom, double incomeValue, boolean isPercent) {
		super(percentValue, hoursPerDay, convertFrom);
		if(isPercent) {
			percentage = percentValue;
			double flatPercentValue = incomeValue*(percentage/100);
			
			hourlyValue = flatPercentValue;
			dailyValue = BudgetCalculator.convertHourlyToDaily(flatPercentValue, hoursPerDay);
			weeklyValue = BudgetCalculator.convertHourlyToWeekly(flatPercentValue, hoursPerDay);
			biweeklyValue = BudgetCalculator.convertHourlyToBiWeekly(flatPercentValue, hoursPerDay);
			monthlyValue = BudgetCalculator.convertHourlyToMonthly(flatPercentValue, hoursPerDay);
			yearlyValue = BudgetCalculator.convertHourlyToYearly(flatPercentValue, hoursPerDay);
		}
		else {
			percentage = percentValue/incomeValue;
		}
	}
	
	public double getPercentageValue() {
		return percentage;
	}
}
