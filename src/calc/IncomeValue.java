package calc;

import ref.Constants;

public class IncomeValue extends BudgetValue {
	protected double hourlyValue;
	
	public IncomeValue(double hourlyValue, double dailyValue, double weeklyValue, double biweeklyValue, double monthlyValue, double yearlyValue) {
		super(dailyValue, weeklyValue, biweeklyValue, monthlyValue, yearlyValue);
		this.hourlyValue = hourlyValue;
	}
	
	public IncomeValue(double value, int hoursPerDay, String convertFrom) {
		super(value, convertFrom);
		if(convertFrom.equals(Constants.HOURLY)) {
			hourlyValue = value;
			dailyValue = BudgetCalculator.convertHourlyToDaily(value, hoursPerDay);
			weeklyValue = BudgetCalculator.convertHourlyToWeekly(value, hoursPerDay);
			biweeklyValue = BudgetCalculator.convertHourlyToBiWeekly(value, hoursPerDay);
			monthlyValue = BudgetCalculator.convertHourlyToMonthly(value, hoursPerDay);
			yearlyValue = BudgetCalculator.convertHourlyToYearly(value, hoursPerDay);
		}
		else if(convertFrom.equals(Constants.DAILY)) {
			hourlyValue = BudgetCalculator.convertDailyToHourly(value, hoursPerDay);
		}
		else if(convertFrom.equals(Constants.WEEKLY)) {
			hourlyValue = BudgetCalculator.convertWeeklyToHourly(value, hoursPerDay);
		}
		else if(convertFrom.equals(Constants.BIWEEKLY)) {
			hourlyValue = BudgetCalculator.convertBiWeeklyToHourly(value, hoursPerDay);
		}
		else if(convertFrom.equals(Constants.MONTHLY)) {
			hourlyValue = BudgetCalculator.convertMonthlyToHourly(value, hoursPerDay);
		}
		else if(convertFrom.equals(Constants.YEARLY)) {
			hourlyValue = BudgetCalculator.convertYearlyToHourly(value, hoursPerDay);
		}
	}
	
	@Override
	public double getValueForTimePeriod(String timePeriod) {
		double value = super.getValueForTimePeriod(timePeriod);
		if(timePeriod.equals(Constants.HOURLY)) {
			value = hourlyValue;
		}
		return value;
	}
	
	public double getHourlyValue() {
		return hourlyValue;
	}
}
