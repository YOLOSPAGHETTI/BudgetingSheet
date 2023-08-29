package ui.income;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ref.Constants;
import ui.ValidatedTextBox;

public class TaxBox extends ValidatedTextBox {
	private JComboBox<String> comboTaxType;
	public TaxBox(JComponent tabPanel, String labelValue) {
		super(tabPanel, labelValue);
	}
	
	public TaxBox(JComponent tabPanel, String labelValue, String textFieldDefault) {
		super(tabPanel, labelValue, textFieldDefault);
	}
	
	@Override
	public void init() {
		textBoxPanel = new JPanel(false);
		textBoxPanel.setLayout(new BoxLayout(textBoxPanel, BoxLayout.X_AXIS));
		
		addLeftBuffer(Constants.SMALL_WIDTH+Constants.BUFFER);
		
		super.init();
		
		comboTaxType = new JComboBox<String>();
		comboTaxType.setFont(new Font("Serif", Font.PLAIN, 16));
		comboTaxType.addItem(Constants.FLAT);
		comboTaxType.addItem(Constants.PERCENT);
		comboTaxType.setMaximumSize(new Dimension(Constants.SMALL_WIDTH, Constants.HEIGHT));
		textBoxPanel.add(Box.createRigidArea(new Dimension(0,Constants.BUFFER)));
		textBoxPanel.add(comboTaxType);
		
		textBoxPanel.add(Box.createRigidArea(new Dimension(Constants.BUFFER,0)));
		textBoxPanel.add(labelErrorText);
	}
	
	public boolean isPercent() {
		if (comboTaxType.getSelectedItem() != null){
			String taxType = comboTaxType.getSelectedItem().toString();
			if(taxType.equals(Constants.PERCENT)) {
				return true;
			}
		}
		return false;
	}
}
