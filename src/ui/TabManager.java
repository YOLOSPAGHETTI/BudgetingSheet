package ui;

import java.util.ArrayList;

public class TabManager {
	private ArrayList<Tab> tabs;
	private CalendarTab calendarTab;
	
	public TabManager() {
		tabs = new ArrayList<Tab>();
	}
	
	public void addTab(Tab tab) {
		tabs.add(tab);
	}
	
	public void loadTabs() {
		for(Tab tab : tabs) {
			if(tab instanceof BudgetTab) {
				BudgetTab budgetTab = (BudgetTab)tab;
				budgetTab.loadFromXml();
			}
		}
		refreshCaldendarTab();
	}
	
	public void setCaldendarTab(CalendarTab calendarTab) {
		this.calendarTab = calendarTab;
	}
	
	public void refreshCaldendarTab() {
		calendarTab.refreshCalendar();
	}
}
