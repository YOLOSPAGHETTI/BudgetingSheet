package ui.load;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ref.Constants;
import sheet.SheetManager;

public class LoadBox {
	private JComponent tabPanel;
	private String sheetName;
	private JButton buttonLoad, buttonEdit;
	private JTextField textFieldEdit;
	protected JComponent textBoxPanel;
	protected JLabel labelErrorText;
	protected Component buttonBuffer;
	protected Component fieldBuffer;
	protected Component leftBuffer;
	protected Component bottomBuffer;
	protected Component errorTextBuffer;
	private SheetManager sheetManager;
	
	public LoadBox(SheetManager sheetManager, JComponent tabPanel, String sheetName) {
		this.sheetManager = sheetManager;
		this.tabPanel = tabPanel;
		this.sheetName = sheetName;
		
		textBoxPanel = new JPanel(false);
		textBoxPanel.setLayout(new BoxLayout(textBoxPanel, BoxLayout.X_AXIS));
	}
	
	public void init() {
		buttonLoad = new JButton(sheetName);
		buttonLoad.setFont(new Font("Serif", Font.PLAIN, Constants.HEADER));
		buttonLoad.addActionListener(new buttonLoad_Action());
		
		buttonBuffer = Box.createRigidArea(new Dimension(Constants.BUFFER,0));
		
		buttonEdit = new JButton(Constants.EDIT);
		buttonEdit.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		buttonEdit.addActionListener(new buttonEdit_Action());
    	
		fieldBuffer = Box.createRigidArea(new Dimension(Constants.BUFFER,0));
		
		textFieldEdit = new JTextField("");
		textFieldEdit.setFont(new Font("Serif", Font.PLAIN, Constants.REGULAR));
		textFieldEdit.setMaximumSize(new Dimension(Constants.LARGE_WIDTH, Constants.HEIGHT));
		textFieldEdit.setVisible(false);
		
		errorTextBuffer = Box.createRigidArea(new Dimension(Constants.BUFFER,0));
		
		labelErrorText = new JLabel();
		labelErrorText.setForeground(Color.RED);
		labelErrorText.setVisible(false);
		
		addLeftBuffer(0);
		textBoxPanel.add(buttonLoad);
		textBoxPanel.add(buttonBuffer);
		textBoxPanel.add(buttonEdit);
		textBoxPanel.add(fieldBuffer);
		textBoxPanel.add(textFieldEdit);
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
	
	public void displayError(String errorMessage) {
		labelErrorText.setText(errorMessage);
		labelErrorText.setVisible(true);
	}
	
	public void hideErrorText() {
		labelErrorText.setVisible(false);
	}
	
	public class buttonLoad_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            loadTab(sheetName);
        }
    }
	
	public class buttonEdit_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
        	editSheetName();
        }
    }
	
	private void loadTab(String sheetName) {
		sheetManager.setCurrentSheet(sheetName);
	}
	
	private void editSheetName() {
		String buttonText = buttonEdit.getText();
		if(buttonText.equals(Constants.EDIT)) {
			buttonEdit.setText(Constants.SAVE);
	    	textFieldEdit.setVisible(true);
		}
		else {
			String newSheetName = textFieldEdit.getText();
			if(!newSheetName.equals("")) {
				if(sheetManager.getSheetByName(newSheetName) == null) {
					sheetManager.editSheetName(sheetName, newSheetName);
					sheetName = newSheetName;
					buttonLoad.setText(newSheetName);
				}
				else {
					displayError("This sheet name is already in use.");
				}
			}
			
			buttonEdit.setText(Constants.EDIT);
			textFieldEdit.setVisible(false);
		}
	}
}
