package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ref.Constants;

public class ValidatedTextBox {
	protected JComponent tabPanel;
	private String labelValue;
	private String textFieldDefault;
	protected JComponent textBoxPanel;
	protected JLabel labelField;
	protected JTextField textField;
	protected JLabel labelErrorText;
	protected Component leftBuffer;
	protected Component bottomBuffer;
	protected Component fieldBuffer;
	protected Component errorTextBuffer;
	
	public ValidatedTextBox(JComponent tabPanel, String labelValue) {
		this.tabPanel = tabPanel;
		this.labelValue = labelValue; 
		this.textFieldDefault = "";
		
		textBoxPanel = new JPanel(false);
		textBoxPanel.setLayout(new BoxLayout(textBoxPanel, BoxLayout.X_AXIS));
	}
	
	public ValidatedTextBox(JComponent tabPanel, String labelValue, String textFieldDefault) {
		this.tabPanel = tabPanel;
		this.labelValue = labelValue; 
		this.textFieldDefault = textFieldDefault;
		
		textBoxPanel = new JPanel(false);
		textBoxPanel.setLayout(new BoxLayout(textBoxPanel, BoxLayout.X_AXIS));
	}
	
	public void init() {
		labelField = new JLabel(labelValue);
		labelField.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		
		fieldBuffer = Box.createRigidArea(new Dimension(Constants.BUFFER,0));
		
		textField = new JTextField(textFieldDefault);
		textField.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		textField.setMaximumSize(new Dimension(Constants.LARGE_WIDTH, Constants.HEIGHT));
    	
		errorTextBuffer = Box.createRigidArea(new Dimension(Constants.BUFFER,0));
		
		labelErrorText = new JLabel();
		labelErrorText.setForeground(Color.RED);
		labelErrorText.setVisible(false);
		
		addLeftBuffer(0);
		textBoxPanel.add(labelField);
		textBoxPanel.add(fieldBuffer);
		textBoxPanel.add(textField);
		textBoxPanel.add(errorTextBuffer);
		tabPanel.add(textBoxPanel);
		tabPanel.add(labelErrorText);
	}
	
	public void remove() {
		tabPanel.remove(labelErrorText);
		tabPanel.remove(textBoxPanel);
		if(bottomBuffer != null) {
			tabPanel.remove(bottomBuffer);
		}
	}
	
	protected void addLeftBuffer(int width) {
		leftBuffer = Box.createRigidArea(new Dimension(width,0));
		textBoxPanel.add(leftBuffer);
	}
	
	public void addBottomBuffer(int height) {
		bottomBuffer = Box.createRigidArea(new Dimension(0,height));
		tabPanel.add(bottomBuffer);
	}
	
	public int checkIntegerValueInField() {
		String text = textField.getText().replace(",", "");
		int number = 0;
		if(!text.equals("")) {
			try {
				number = Integer.parseInt(text);
				hideErrorText();
			}
			catch(NumberFormatException e) {
				number = -1;
				displayError("This field requires an integer value.");
			}
		}
		else {
			textField.setText("0");
		}
		return number;
	}
	
	public double checkDoubleValueInField() {
		String text = textField.getText().replace(",", "");
		double number = 0;
		if(!text.equals("")) {
			try {
				number = Double.parseDouble(text);
				hideErrorText();
			}
			catch(NumberFormatException e) {
				number = -1;
				displayError("This field requires a decimal value.");
			}
		}
		else {
			textField.setText("0");
		}
		return number;
	}
	
	public String checkNotEmpty() {
		String text = textField.getText();
		if(text.isEmpty()) {
			displayError("This is a required field.");
		}
		else {
			hideErrorText();
		}
		return text;
	}
	
	public JTextField getTextField() {
		return textField;
	}
	
	public String getText() {
		return textField.getText();
	}
	
	public void setText(String text) {
		textField.setText(text);
	}
	
	public void displayError(String errorMessage) {
		labelErrorText.setText(errorMessage);
		labelErrorText.setVisible(true);
	}
	
	public void hideErrorText() {
		labelErrorText.setVisible(false);
	}
}
