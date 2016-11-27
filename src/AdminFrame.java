import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class AdminFrame {
	JButton btnAdd, btnRemove, btnEdit;
	JTable table;
	ButtonListener btnL;

	
	public AdminFrame() {
		
		btnL = new ButtonListener();
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(btnL);
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(btnL);
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(btnL);
		JPanel pnlBtns = new JPanel(new FlowLayout());
		pnlBtns.add(btnAdd);
		pnlBtns.add(btnRemove);
		pnlBtns.add(btnEdit);
		
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if(ae.getSource().equals(btnAdd)) {
				
			} else if(ae.getSource().equals(btnRemove)) {
				
			} else if(ae.getSource().equals(btnEdit)) {
				
			}
		}
	}
}
