package ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ref.Constants;
import sheet.SheetManager;

public class Tab {
	protected SheetManager sheetManager;
	protected JComponent tabPanel;
	protected JComponent scrollPanel;
	protected JScrollPane scrollpane;
	
	public Tab(SheetManager sheetManager, JComponent tabPanel) {
		this.sheetManager = sheetManager;
		this.tabPanel = tabPanel;
	}
	
	public void init() {
		tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
		
		scrollPanel = new JPanel(false);
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
		
		scrollpane = new JScrollPane(scrollPanel);
		tabPanel.add(scrollpane);
	}
	
	protected Component addDefaultXBuffer(JComponent panel) {
		Component buffer = Box.createRigidArea(new Dimension(Constants.BUFFER,0));
		panel.add(buffer);
		
		return buffer;
	}
	
	protected Component addDefaultYBuffer(JComponent panel) {
		Component buffer = Box.createRigidArea(new Dimension(0,Constants.BUFFER));
		panel.add(buffer);
		
		return buffer;
	}
	
	public void removeSection(int sequence) {
		refresh();
	}
	
	public void refresh() {
		scrollPanel.setVisible(false);
		scrollPanel.setVisible(true);
	}
}
