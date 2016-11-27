import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;


public class NewEntry extends JFrame {
	
	private JTextField[] tf;
	private JButton btnSubmit, btnCancel;
	private ButtonListener btnL;
	private AdminFrame parent;
	private String table;
	
	public NewEntry(String[] lblNames, AdminFrame parent) {
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
				String[] elements = new String[tf.length];
				for(int i = 0; i < tf.length; i++) {
					elements[i] = tf[i].getText();
				}
				Database db = new Database();
				if(!db.insertInto(table, elements)) {
					JOptionPane.showMessageDialog(null, "Invalid input!\nNote: \nDate format is (YYYY-MM-DD)\nTime format is (HH-MM-SS)");
				} else {
					parent.setEnabled(true);
					parent.refresh();
					NewEntry.this.setVisible(false);
				}
			} else if(ae.getSource().equals(btnCancel)) {
				parent.setEnabled(true);
				NewEntry.this.setVisible(false);
			}
		}
	}
	
}
