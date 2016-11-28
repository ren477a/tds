import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class EditEntry extends JFrame {
	private JTextField[] tf;
	private JButton btnSubmit, btnCancel;
	private ButtonListener btnL;
	private AdminFrame parent;
	private String table;
	
	public EditEntry(String[] lblNames, AdminFrame parent, int id) {
		if(lblNames[0].equals("Event ID"))
			table = "events";
		else if(lblNames[0].equals("Ticket ID"))
			table = "tickets";
		else if(lblNames[0].equals("ID"))
			table = "transactions";
		
		
		this.parent = parent;
		this.parent.setEnabled(false);
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
		tf[0].setEditable(false);
		Database db = new Database();
		db.loadEntry(tf, table, id);
		
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
		setTitle("Edit Entry");
		setSize(400, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			String[] elements = new String[tf.length];
			for(int i = 0; i < tf.length; i++) {
				elements[i] = tf[i].getText();
			}
			Database db = new Database();
			
			if(ae.getSource().equals(btnSubmit)) {
				if(!db.update(table, elements, Integer.parseInt(tf[0].getText()))) {
					JOptionPane.showMessageDialog(null, "Invalid input!\nNote: \nDate format is (YYYY-MM-DD)\nTime format is (HH-MM-SS)");
				} else {
					parent.setEnabled(true);
					parent.refresh();
					EditEntry.this.setVisible(false);
				}
			} else if(ae.getSource().equals(btnCancel)) {
				parent.setEnabled(true);
				EditEntry.this.setVisible(false);
			}
		}
	}
}
