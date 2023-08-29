package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ref.Constants;

public class TotalHeader {
	private JComponent tabPanel;
	private String headerText;
	protected JComponent headerPanel;
	protected JLabel labelHeader;
	protected JLabel labelTotal;
	
	public TotalHeader(JComponent tabPanel, String headerText) {
		this.tabPanel = tabPanel;
		this.headerText = headerText; 
	}
	
	public void init() {
		headerPanel = new JPanel(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		
		labelHeader = new JLabel(headerText);
		labelHeader.setFont(new Font("Serif", Font.BOLD, Constants.HEADER));
		labelHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
    	
		labelTotal = new JLabel("Total: ");
		labelTotal.setFont(new Font("Serif", Font.BOLD, Constants.HEADER));
		
		headerPanel.add(labelHeader);
		headerPanel.add(Box.createRigidArea(new Dimension(Constants.BUFFER,0)));
		headerPanel.add(labelTotal);
    	
		tabPanel.add(Box.createRigidArea(new Dimension(0,Constants.BUFFER)));
		tabPanel.add(headerPanel);
		
		setTotal("0.00");
	}
	
	public void setVisible(boolean visible) {
		if(visible) {
			headerPanel.setVisible(true);
		}
		else {
			headerPanel.setVisible(false);
		}
	}
	
	public void setTotal(String total) {
		labelTotal.setText("Total: " + total);
	}
}
