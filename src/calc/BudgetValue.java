package calc;

import ref.Constants;

public class BudgetValue {
	protected double dailyValue;
	protected double weeklyValue;
	protected double biweeklyValue;
	protected double monthlyValue;
	protected double yearlyValue;
	
	public BudgetValue(double dailyValue, double weeklyValue, double biweeklyValue, double monthlyValue, double yearlyValue) {
		this.dailyValue = dailyValue;
		this.weeklyValue = weeklyValue;
		this.biweeklyValue = biweeklyValue;
		this.monthlyValue = monthlyValue;
		this.yearlyValue = yearlyValue;
	}
	
	public BudgetValue(double value, String convertFrom) {
		if(convertFrom.equals(Constants.DAILY)) {
			dailyValue = value;
			weeklyValue = BudgetCalculator.convertDailyToWeekly(value);
			biweeklyValue = BudgetCalculator.convertDailyToBiWeekly(value);
			monthlyValue = BudgetCalculator.convertDailyToMonthly(value);
			yearlyValue = BudgetCalculator.convertDailyToYearly(value);
		}
		else if(convertFrom.equals(Constants.WEEKLY)) {
			dailyValue = BudgetCalculator.convertWeeklyToDaily(value);;
			weeklyValue = value;
			biweeklyValue = BudgetCalculator.convertWeeklyToBiWeekly(value);
			monthlyValue = BudgetCalculator.convertWeeklyToMonthly(value);
			yearlyValue = BudgetCalculator.convertWeeklyToYearly(value);
		}
		else if(convertFrom.equals(Constants.BIWEEKLY)) {
			dailyValue = BudgetCalculator.convertBiWeeklyToDaily(value);
			weeklyValue = BudgetCalculator.convertBiWeeklyToWeekly(value);
			biweeklyValue = value;
			monthlyValue = BudgetCalculator.convertBiWeeklyToMonthly(value);
			yearlyValue = BudgetCalculator.convertBiWeeklyToYearly(value);
		}
		else if(convertFrom.equals(Constants.MONTHLY)) {
			dailyValue = BudgetCalculator.convertMonthlyToDaily(value);;
			weeklyValue = BudgetCalculator.convertMonthlyToWeekly(value);
			biweeklyValue = BudgetCalculator.convertMonthlyToBiWeekly(value);
			monthlyValue = value;
			yearlyValue = BudgetCalculator.convertMonthlyToYearly(value);
		}
		else if(convertFrom.equals(Constants.YEARLY)) {
			dailyValue = BudgetCalculator.convertYearlyToDaily(value);
			weeklyValue = BudgetCalculator.convertYearlyToWeekly(value);
			biweeklyValue = BudgetCalculator.convertYearlyToBiWeekly(value);
			monthlyValue = BudgetCalculator.convertYearlyToMonthly(value);
			yearlyValue = value;
		}
	}
	
	public double getValueForTimePeriod(String timePeriod) {
		double value = 0;
		if(timePeriod.equals(Constants.DAILY)) {
			value = dailyValue;
		}
		else if(timePeriod.equals(Constants.WEEKLY)) {
			value = weeklyValue;
		}
		else if(timePeriod.equals(Constants.BIWEEKLY)) {
			value = biweeklyValue;
		}
		else if(timePeriod.equals(Constants.MONTHLY)) {
			value = monthlyValue;
		}
		else if(timePeriod.equals(Constants.YEARLY)) {
			value = yearlyValue;
		}
		return value;
	}
	
	public double getDailyValue() {
		return dailyValue;
	}
	
	public double getWeeklyValue() {
		return weeklyValue;
	}
	
	public double getBiWeeklyValue() {
		return biweeklyValue;
	}
	
	public double getMonthlyValue() {
		return monthlyValue;
	}
	
	public double getYearlyValue() {
		return yearlyValue;
	}
}
