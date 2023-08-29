package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import calc.NumberFormatter;
import ref.Constants;
import sheet.SheetManager;

public class CalendarTab extends Tab {
	protected JComponent monthPanel;
	protected JComponent yearPanel;
	private Dimension screenSize;
	private JLabel labelMonth, labelYear;
	private JButton buttonPrev, buttonNext;
    private JTable tableCalendar;
    private JComboBox<String> comboYear;
    private DefaultTableModel modelTableCalendar; //Table model
    private int realYear, realMonth, realDay, currentYear, currentMonth;
    
    public CalendarTab(SheetManager sheetManager, JComponent panelCalendar, Dimension screenSize) {
    	super(sheetManager, panelCalendar);
    	this.screenSize = screenSize;
    }
    
    @Override
    public void init() {
        //Look and feel
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
        
        super.init();
        
        //Set border
        scrollPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
        
        // Month panel
        monthPanel = new JPanel(false);
        monthPanel.setLayout(new BoxLayout(monthPanel, BoxLayout.X_AXIS));
        
        labelMonth = new JLabel ("January");
        labelMonth.setFont(new Font("Serif", Font.PLAIN, Constants.HEADER));
        buttonPrev = new JButton ("<");
        buttonPrev.setFont(new Font("Serif", Font.PLAIN, Constants.HEADER));
        buttonNext = new JButton (">");
        buttonNext.setFont(new Font("Serif", Font.PLAIN, Constants.HEADER));
        buttonPrev.addActionListener(new buttonPrev_Action());
        buttonNext.addActionListener(new buttonNext_Action());
        
        monthPanel.add(buttonPrev);
        addDefaultXBuffer(monthPanel);
        monthPanel.add(labelMonth);
        addDefaultXBuffer(monthPanel);
        monthPanel.add(buttonNext);
        scrollPanel.add(monthPanel);
        addDefaultYBuffer(scrollPanel);
        
        // Year panel
        yearPanel = new JPanel(false);
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.X_AXIS));
        
        labelYear = new JLabel ("Change year:");
        labelYear.setFont(new Font("Serif", Font.PLAIN, Constants.HEADER));
        comboYear = new JComboBox<String>();
        comboYear.setFont(new Font("Serif", Font.PLAIN, Constants.HEADER));
        comboYear.setMaximumSize(new Dimension(Constants.MED_WIDTH, Constants.HEIGHT));
        comboYear.addActionListener(new comboYear_Action());
        
        yearPanel.add(labelYear);
        addDefaultXBuffer(yearPanel);
        yearPanel.add(comboYear);
        scrollPanel.add(yearPanel);
        addDefaultYBuffer(scrollPanel);
        
        // Calendar
        modelTableCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        tableCalendar = new JTable(modelTableCalendar);
        
        scrollPanel.add(tableCalendar);
        
        //Get real month/year
        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;
        
        //Add headers
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i=0; i<7; i++){
            modelTableCalendar.addColumn(headers[i]);
        }
        
        tableCalendar.getParent().setBackground(tableCalendar.getBackground()); //Set background
        
        //No resize/reorder
        tableCalendar.getTableHeader().setResizingAllowed(false);
        tableCalendar.getTableHeader().setReorderingAllowed(false);
        
        //Single cell selection
        tableCalendar.setColumnSelectionAllowed(true);
        tableCalendar.setRowSelectionAllowed(true);
        tableCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Set row/column count
        tableCalendar.setRowHeight((screenSize.height-Constants.BUFFER*12)/6);
        tableCalendar.setFont(new Font("Serif", Font.PLAIN, Constants.LARGE));
        modelTableCalendar.setColumnCount(7);
        modelTableCalendar.setRowCount(6);
        
        //Populate table
        for (int i=realYear-100; i<=realYear+100; i++){
            comboYear.addItem(String.valueOf(i));
        }
        
        //Refresh calendar
        refreshCalendar (realMonth, realYear); //Refresh calendar
    }
    
    public void refreshCalendar(int month, int year){
        //Variables
        String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int nod, som; //Number Of Days, Start Of Month
        
        //Allow/disallow buttons
        buttonPrev.setEnabled(true);
        buttonNext.setEnabled(true);
        if (month == 0 && year <= realYear-10){buttonPrev.setEnabled(false);} //Too early
        if (month == 11 && year >= realYear+100){buttonNext.setEnabled(false);} //Too late
        labelMonth.setText(months[month]); //Refresh the month label (at the top)
        labelMonth.setBounds(160-labelMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
        comboYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
        
        //Clear table
        for (int i=0; i<6; i++){
            for (int j=0; j<7; j++){
                modelTableCalendar.setValueAt(null, i, j);
            }
        }
        
        //Get first day of month and number of days
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);
        
        sheetManager.getCurrentSheet().calculateSavings();
        sheetManager.getCurrentSheet().calculateDailyTotal();
        
        //Draw calendar
        for (int i=1; i<=nod; i++){
            int row = new Integer((i+som-2)/7);
            int column  =  (i+som-2)%7;
            long daysAfterToday = getDaysAfterToday(i);
            if(daysAfterToday < 0) {
            	modelTableCalendar.setValueAt(i, row, column);
            }
            else {
            	modelTableCalendar.setValueAt(i + ":   " + getTotalForDay(daysAfterToday), row, column);
            }
        }
        
        //Apply renderers
        tableCalendar.setDefaultRenderer(tableCalendar.getColumnClass(0), new tableCalendarRenderer());
    }

    public void refreshCalendar() {
    	refreshCalendar(currentMonth, currentYear);
    }
    
    public class tableCalendarRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 0 || column == 6){ //Week-end
                setBackground(new Color(255, 220, 220));
            }
            else{ //Week
                setBackground(new Color(255, 255, 255));
            }
            if (value != null){
            	String currentDayString = value.toString();
            	if(currentDayString.contains(":")) {
            		currentDayString = value.toString().substring(0, value.toString().indexOf(":"));
            	}
            	int currentDay = Integer.parseInt(currentDayString);
                if (currentDay == realDay && currentMonth == realMonth && currentYear == realYear){ //Today
                    setBackground(new Color(220, 220, 255));
                }
            }
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }
    
    public class buttonPrev_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (currentMonth == 0){ //Back one year
                currentMonth = 11;
                currentYear -= 1;
            }
            else{ //Back one month
                currentMonth -= 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }
    
    public class buttonNext_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (currentMonth == 11){ //Foward one year
                currentMonth = 0;
                currentYear += 1;
            }
            else{ //Foward one month
                currentMonth += 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }
    
    public class comboYear_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (comboYear.getSelectedItem() != null){
                String b = comboYear.getSelectedItem().toString();
                currentYear = Integer.parseInt(b);
                refreshCalendar(currentMonth, currentYear);
            }
        }
    }
    
    private String getTotalForDay(long daysAfterToday) {
    	double savings = sheetManager.getCurrentSheet().getSavings();
    	double dailyTotal = sheetManager.getCurrentSheet().getDailyTotal();
    	double total = savings + dailyTotal*daysAfterToday;
    	//System.out.println("Savings: " + savings);
    	//System.out.println("dailyTotal: " + dailyTotal);
    	
    	return NumberFormatter.formatDoubleToCurrency(total);
    }
    
    private long getDaysAfterToday(int day) {
    	long daysAfterToday = 0;
    	try {
	    	LocalDate startDate = LocalDate.parse(realYear+"-"+parseDateValue(realMonth)+"-"+parseDateValue(realDay));
	    	LocalDate endDate   = LocalDate.parse(currentYear+"-"+parseDateValue(currentMonth)+"-"+parseDateValue(day));
	    	daysAfterToday = ChronoUnit.DAYS.between(startDate,endDate);
    	}
    	catch(DateTimeParseException e) {
    		e.printStackTrace();
    	}
    	
    	return daysAfterToday;
    }
    
    private String parseDateValue(int value) {
    	String valueString = value+"";
    	if(valueString.length() < 2) {
    		valueString = "0"+valueString;
    	}
    	return valueString;
    }
}
