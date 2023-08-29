package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

import ref.Constants;

public class Section {
	private Tab tab;
	private int sequence;
	
	private JComponent panel;
	private JButton buttonRemove;
	private Component bottomBuffer;
	
	public Section(Tab tab, int sequence, JComponent panel) {
		this.tab = tab;
		this.sequence = sequence;
		this.panel = panel;
	}
	
	public void init() {
		buttonRemove = new JButton("Remove");
		buttonRemove.addActionListener(new buttonRemove_Action());
		
		panel.add(buttonRemove);
		bottomBuffer = Box.createRigidArea(new Dimension(0,Constants.BUFFER));
		panel.add(bottomBuffer);
	}
	
	public Tab getTab() {
		return tab;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	protected void remove() {
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
