package calc;

public class NumberFormatter {
	public static String formatDoubleToCurrency(double number) {
		String numberStr= ""+number;
		StringBuilder currency = new StringBuilder();
		
		boolean decimalPassed = false;
		int digitsSinceDecimal = 0;
		for(int i=0; i<numberStr.length(); i++) {
			char c = numberStr.charAt(i);
			if(c == '.') {
				decimalPassed = true;
			}
			else if(decimalPassed) {
				digitsSinceDecimal++;
			}
			if(digitsSinceDecimal>2) {
				break;
			}
			currency.append(c);
		}
		if(digitsSinceDecimal == 1) {
			currency.append('0');
		}
		addCommas(currency.toString());
		
		return addCommas(currency.toString());
	}
	
	private static String addCommas(String numberStr) {
		StringBuilder currency = new StringBuilder(numberStr);
		String[] numberSplit = numberStr.split("\\.");
		
		if(numberSplit.length > 0) {
			String preDecimal = numberSplit[0];
			if(preDecimal.length() > 3) {
				currency = new StringBuilder();
				for(int i=preDecimal.length()-1; i>=0; i--) {
					currency.append(preDecimal.charAt(i));
					int diff = preDecimal.length() - i;
					
					if(diff%3==0 && i>0) {
						currency.append(",");
					}
				}
				currency.reverse();
				currency.append(".");
				currency.append(numberSplit[1]);
			}
		}
		
		return currency.toString();
	}
}
