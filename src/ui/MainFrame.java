package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import sheet.SheetManager;
import ui.BudgetTab;
import ui.CalendarTab;
import ui.load.LoadTab;
import ui.savings.SavingsTab;
import ui.income.IncomeTab;
import ui.TabManager;
import ui.expenses.CarTab;
import ui.expenses.HousingTab;
import ui.expenses.OtherDebtTab;
import ui.expenses.OtherExpenseTab;

public class MainFrame extends JPanel {
	private static JFrame frame;
	private static Dimension screenSize;
	
	// Tabs
	private static CalendarTab calendarTab;
	private static LoadTab loadTab;
	private static IncomeTab incomeTab;
	private static SavingsTab savingsTab;
	private static HousingTab housingTab;
	private static CarTab carTab;
	private static OtherExpenseTab otherExpenseTab;
	private static OtherDebtTab otherDebtTab;
	
	private static TabManager tabManager;
	private static SheetManager sheetManager;
	
	public MainFrame() {
        super(new GridLayout(1, 1));
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        init();
    }
	
	private void init() {
		tabManager = new TabManager();
		sheetManager = new SheetManager(tabManager);
		
		JTabbedPane tabbedPane = new JTabbedPane();
        
		JComponent panel1 = addTab(tabbedPane, "Add/Load Sheets", 1);
        loadTab = new LoadTab(sheetManager, panel1);
        initAndAddToManager(loadTab);
		
        JComponent panel2 = addTab(tabbedPane, "Calendar", 2);
        calendarTab = new CalendarTab(sheetManager, panel2, screenSize);
        initAndAddToManager(calendarTab);
        tabManager.setCaldendarTab(calendarTab);
        calendarTab.refresh();
        
        JComponent panel3 = addTab(tabbedPane, "Income", 3);
        incomeTab = new IncomeTab(sheetManager, panel3);
        initAndAddToManager(incomeTab);
        
        JComponent panel4 = addTab(tabbedPane, "Savings", 4);
        savingsTab = new SavingsTab(sheetManager, panel4);
        initAndAddToManager(savingsTab);
        
        JComponent panel5 = addTab(tabbedPane, "Housing Expenses/Debts", 5);
        housingTab = new HousingTab(sheetManager, panel5);
        initAndAddToManager(housingTab);
        
        JComponent panel6 = addTab(tabbedPane, "Car Expenses/Debts", 6);
        carTab = new CarTab(sheetManager, panel6);
        initAndAddToManager(carTab);
        
        JComponent panel7 = addTab(tabbedPane, "Other Regular Expenses", 7);
        otherExpenseTab = new OtherExpenseTab(sheetManager, panel7);
        initAndAddToManager(otherExpenseTab);
        
        JComponent panel8 = addTab(tabbedPane, "Other Debts", 8);
        otherDebtTab = new OtherDebtTab(sheetManager, panel8);
        initAndAddToManager(otherDebtTab);
        
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	private JComponent addTab(JTabbedPane tabbedPane, String title, int sequence) {
		JComponent panel = new JPanel(false);
        tabbedPane.addTab(title, panel);
        tabbedPane.setMnemonicAt(sequence - 1, 48 + sequence);
        
        return panel;
	}
	
	private void initAndAddToManager(Tab tab) {
		tab.init();
		tabManager.addTab(tab);
	}
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Budget");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Maximize the frame
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //Add content to the window.
        frame.add(new MainFrame(), BorderLayout.CENTER);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });
    }
}
