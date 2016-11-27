import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;


public class NewEntry extends JFrame {
	
	private JTextField[] tf;
	private JButton btnSubmit, btnCancel;
	private ButtonListener btnL;
	
	public NewEntry(String[] lblNames, JFrame parent) {
		
		
		tf = new JTextField[lblNames.length];
		JPanel pnl = new JPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.PAGE_AXIS));
		pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
		for(int i = 0; i < lblNames.length; i++) {
			pnl.add(new JLabel(lblNames[i]+":"));
			pnl.add(Box.createRigidArea(new Dimension(0, 5)));
			tf[i] = new JTextField();
			pnl.add(tf[i]);
			pnl.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		btnL = new ButtonListener();
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(btnL);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(btnL);
		JPanel pnlBtns = new JPanel(new FlowLayout());
		pnlBtns.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlBtns.add(btnSubmit);
		pnlBtns.add(btnCancel);
		
		pnl.add(pnlBtns);
		
		add(pnl);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("New Entry");
		setSize(400, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if(ae.getSource().equals(btnSubmit)) {
				
			} else if(ae.getSource().equals(btnCancel)) {
				
			}
		}
	}
	
}
