package calc;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import ref.Constants;

public class BudgetCalculator {

	public static double convertFromDateStrings(double value, int hoursPerWeek, String convertFrom, String convertTo) {
		double returnValue = 0;
		if(convertFrom.equals(Constants.HOURLY)) {
			if(convertTo.equals(Constants.DAILY)) {
				returnValue = convertHourlyToDaily(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.WEEKLY)) {
				returnValue = convertHourlyToWeekly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.BIWEEKLY)) {
				returnValue = convertHourlyToBiWeekly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.MONTHLY)) {
				returnValue = convertHourlyToMonthly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.YEARLY)) {
				returnValue = convertHourlyToYearly(value, hoursPerWeek);
			}
		}
		else if(convertFrom.equals(Constants.DAILY)) {
			if(convertTo.equals(Constants.HOURLY)) {
				returnValue = convertDailyToHourly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.WEEKLY)) {
				returnValue = convertDailyToWeekly(value);
			}
			else if(convertTo.equals(Constants.BIWEEKLY)) {
				returnValue = convertDailyToBiWeekly(value);
			}
			else if(convertTo.equals(Constants.MONTHLY)) {
				returnValue = convertDailyToMonthly(value);
			}
			else if(convertTo.equals(Constants.YEARLY)) {
				returnValue = convertDailyToYearly(value);
			}
		}
		else if(convertFrom.equals(Constants.WEEKLY)) {
			if(convertTo.equals(Constants.HOURLY)) {
				returnValue = convertWeeklyToHourly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.DAILY)) {
				returnValue = convertWeeklyToDaily(value);
			}
			else if(convertTo.equals(Constants.BIWEEKLY)) {
				returnValue = convertWeeklyToBiWeekly(value);
			}
			else if(convertTo.equals(Constants.MONTHLY)) {
				returnValue = convertWeeklyToMonthly(value);
			}
			else if(convertTo.equals(Constants.YEARLY)) {
				returnValue = convertWeeklyToYearly(value);
			}
		}
		else if(convertFrom.equals(Constants.BIWEEKLY)) {
			if(convertTo.equals(Constants.HOURLY)) {
				returnValue = convertBiWeeklyToHourly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.DAILY)) {
				returnValue = convertBiWeeklyToDaily(value);
			}
			else if(convertTo.equals(Constants.WEEKLY)) {
				returnValue = convertBiWeeklyToWeekly(value);
			}
			else if(convertTo.equals(Constants.MONTHLY)) {
				returnValue = convertBiWeeklyToMonthly(value);
			}
			else if(convertTo.equals(Constants.YEARLY)) {
				returnValue = convertBiWeeklyToYearly(value);
			}
		}
		else if(convertFrom.equals(Constants.MONTHLY)) {
			if(convertTo.equals(Constants.HOURLY)) {
				returnValue = convertMonthlyToHourly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.DAILY)) {
				returnValue = convertMonthlyToDaily(value);
			}
			else if(convertTo.equals(Constants.WEEKLY)) {
				returnValue = convertMonthlyToWeekly(value);
			}
			else if(convertTo.equals(Constants.BIWEEKLY)) {
				returnValue = convertMonthlyToBiWeekly(value);
			}
			else if(convertTo.equals(Constants.YEARLY)) {
				returnValue = convertMonthlyToYearly(value);
			}
		}
		else if(convertFrom.equals(Constants.YEARLY)) {
			if(convertTo.equals(Constants.HOURLY)) {
				returnValue = convertYearlyToHourly(value, hoursPerWeek);
			}
			else if(convertTo.equals(Constants.DAILY)) {
				returnValue = convertYearlyToDaily(value);
			}
			else if(convertTo.equals(Constants.WEEKLY)) {
				returnValue = convertYearlyToWeekly(value);
			}
			else if(convertTo.equals(Constants.BIWEEKLY)) {
				returnValue = convertYearlyToBiWeekly(value);
			}
			else if(convertTo.equals(Constants.MONTHLY)) {
				returnValue = convertYearlyToMonthly(value);
			}
		}
		
		
		return returnValue;
	}
	
	// Hours
	public static double convertHourlyToDaily(double hourlyValue, int hoursPerWeek) {
		return (hourlyValue*hoursPerWeek)/7;
	}
	
	public static double convertHourlyToWeekly(double hourlyValue, int hoursPerWeek) {
		return hourlyValue*hoursPerWeek;
	}
	
	public static double convertHourlyToBiWeekly(double hourlyValue, int hoursPerWeek) {
		return hourlyValue*hoursPerWeek*2;
	}
	
	public static double convertHourlyToMonthly(double hourlyValue, int hoursPerWeek) {
		return (hourlyValue*hoursPerWeek*getDaysInMonth())/7;
	}
	
	public static double convertHourlyToYearly(double hourlyValue, int hoursPerWeek) {
		return (hourlyValue*hoursPerWeek*getDaysInYear())/7;
	}
	
	// Days
	public static double convertDailyToHourly(double dailyValue, int hoursPerWeek) {
		return dailyValue/(hoursPerWeek/7);
	}
	
	public static double convertDailyToWeekly(double dailyValue) {
		return dailyValue*7;
	}
	
	public static double convertDailyToBiWeekly(double dailyValue) {
		return dailyValue*14;
	}
	
	public static double convertDailyToMonthly(double dailyValue) {
		return dailyValue*getDaysInMonth();
	}
	
	public static double convertDailyToYearly(double dailyValue) {
		return dailyValue*getDaysInYear();
	}
	
	// Weeks
	public static double convertWeeklyToHourly(double weeklyValue, int hoursPerWeek) {
		return weeklyValue/hoursPerWeek;
	}
	
	public static double convertWeeklyToDaily(double weeklyValue) {
		return weeklyValue/7;
	}
	
	public static double convertWeeklyToBiWeekly(double weeklyValue) {
		return weeklyValue*2;
	}
	
	public static double convertWeeklyToMonthly(double weeklyValue) {
		return weeklyValue*(getDaysInMonth()/7);
	}
	
	public static double convertWeeklyToYearly(double weeklyValue) {
		return weeklyValue*(getDaysInYear()/7);
	}
	
	// Bi-Weekly
	public static double convertBiWeeklyToHourly(double biweeklyValue, int hoursPerWeek) {
		return biweeklyValue/(hoursPerWeek*2);
	}
	
	public static double convertBiWeeklyToDaily(double biweeklyValue) {
		return biweeklyValue/14;
	}
	
	public static double convertBiWeeklyToWeekly(double biweeklyValue) {
		return biweeklyValue/2;
	}
	
	public static double convertBiWeeklyToMonthly(double biweeklyValue) {
		return biweeklyValue*(getDaysInMonth()/14);
	}
	
	public static double convertBiWeeklyToYearly(double biweeklyValue) {
		return biweeklyValue*(getDaysInYear()/14);
	}
	
	// Monthly
	public static double convertMonthlyToHourly(double monthlyValue, int hoursPerWeek) {
		return monthlyValue/((hoursPerWeek*getDaysInMonth())/7);
	}
	
	public static double convertMonthlyToDaily(double monthlyValue) {
		return monthlyValue/getDaysInMonth();
	}
	
	public static double convertMonthlyToWeekly(double monthlyValue) {
		return monthlyValue/(getDaysInMonth()/7);
	}
	
	public static double convertMonthlyToBiWeekly(double monthlyValue) {
		return monthlyValue/(getDaysInMonth()/14);
	}
	
	public static double convertMonthlyToYearly(double monthlyValue) {
		return monthlyValue*12;
	}
	
	// Yearly
	public static double convertYearlyToHourly(double yearlyValue, int hoursPerWeek) {
		return yearlyValue/((hoursPerWeek*getDaysInYear())/7);
	}
	
	public static double convertYearlyToDaily(double yearlyValue) {
		return yearlyValue/getDaysInYear();
	}
	
	public static double convertYearlyToWeekly(double yearlyValue) {
		return yearlyValue/(getDaysInYear()/7);
	}
	
	public static double convertYearlyToBiWeekly(double yearlyValue) {
		return yearlyValue/(getDaysInYear()/14);
	}
	
	public static double convertYearlyToMonthly(double yearlyValue) {
		return yearlyValue/12;
	}
	
	private static int getDaysInMonth() {
		GregorianCalendar cal = new GregorianCalendar();
        return cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
	}
	
	private static int getDaysInYear() {
		GregorianCalendar cal = new GregorianCalendar();
        return cal.getActualMaximum(GregorianCalendar.DAY_OF_YEAR);
	}
	
	public static double sumValues(ArrayList<BudgetValue> values, String timePeriod) {
		double total = 0;
		
		for(BudgetValue budgetValue : values) {
			double value = budgetValue.getValueForTimePeriod(timePeriod);
			total = total + value;
		}
		
		return total;
	}
	
	public static double sumValues(ArrayList<Double> addValues) {
		double total = 0;
		
		for(Double value : addValues) {
			total = total + value;
		}
		
		return total;
	}
	
	public static double sumValues(ArrayList<Double> addValues, ArrayList<Double> subtractValues) {
		double total = 0;
		
		for(Double value : addValues) {
			total = total + value;
		}
		
		for(Double value : subtractValues) {
			total = total - value;
		}
		
		return total;
	}
}
