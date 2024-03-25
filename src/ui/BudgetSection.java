package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

import calc.BudgetValue;
import calc.NumberFormatter;
import ref.Constants;

public class BudgetSection {
	private BudgetTab tab;
	private int sequence;
	
	private JComponent panel;
	private JButton buttonRemove;
	private Component bottomBuffer;
	
	protected String name;
	protected BudgetValue value;
	
	protected ValidatedTextBox nameBox;
	protected ValidatedTextBox valueBox;
	
	public BudgetSection(BudgetTab tab, int sequence, String name, BudgetValue value, JComponent panel) {
		this.tab = tab;
		this.sequence = sequence;
		this.name = name;
		this.value = value;
		this.panel = panel;
	}
	
	public BudgetSection(BudgetTab tab, int sequence, String name, JComponent panel) {
		this.tab = tab;
		this.sequence = sequence;
		this.name = name;
		this.value = new BudgetValue(0, Constants.DAILY);
		this.panel = panel;
	}
	
	public BudgetSection(BudgetTab tab, int sequence, JComponent panel) {
		this.tab = tab;
		this.sequence = sequence;
		this.panel = panel;
	}
	
	public void init() {
		buttonRemove = new JButton("Remove");
		buttonRemove.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		buttonRemove.addActionListener(new buttonRemove_Action());
		
		panel.add(buttonRemove);
		bottomBuffer = Box.createRigidArea(new Dimension(0,Constants.BUFFER));
		panel.add(bottomBuffer);
	}
	
	public void init(String timePeriod, String nameLabel, String valueLabel) {
		nameBox = new ValidatedTextBox(getPanel(), nameLabel+":", name);
		nameBox.init();
		nameBox.addBottomBuffer(Constants.BUFFER);
		
		double rawValue = value.getValueForTimePeriod(timePeriod);
		valueBox = new ValidatedTextBox(getPanel(), valueLabel+":", ""+rawValue);
		valueBox.init();
		valueBox.addBottomBuffer(Constants.BUFFER);
		
		init(timePeriod);
	}
	
	public void init(String timePeriod) {		
		init();
		
		setFieldsToTimePeriod(timePeriod);
	}
	
	public BudgetTab getTab() {
		return tab;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public String getName() {
		return name;
	}
	
	public BudgetValue getValue() {
		return value;
	}
	
	public ValidatedTextBox getNameBox() {
		return nameBox;
	}
	
	public ValidatedTextBox getValueBox() {
		return valueBox;
	}
	
	public void setFieldsToTimePeriod(String timePeriod) {
		String currency = NumberFormatter.formatDoubleToCurrency(value.getValueForTimePeriod(timePeriod));
		if(valueBox != null) {
			valueBox.setText(currency);
		}
	}
	
	public void updateValuesToMatchTextFields(String timePeriod) {
		name = nameBox.getText();
		double rawValue = Double.parseDouble(valueBox.getText().replace(",", ""));
		value = new BudgetValue(rawValue, timePeriod);
		valueBox.setText(NumberFormatter.formatDoubleToCurrency(rawValue));
	}
	
	public void remove() {
		if(nameBox != null) {
			nameBox.remove();
		}
		
		if(valueBox != null) {
			valueBox.remove();
		}
		
		
		panel.remove(buttonRemove);
		panel.remove(bottomBuffer);
	}
	
	public JComponent getPanel() {
		return panel;
	}
	
	public class buttonRemove_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
        	remove();
        	tab.removeSection(sequence);
        }
    }
}
