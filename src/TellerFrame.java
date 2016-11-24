import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class TellerFrame extends JFrame {
		private JList listTickets, listCart;
		private DefaultListModel<String> mdlTickets, mdlCart;
		private JLabel lblSelTickets, lblCart;
		private JButton btnNewT, btnAdd, btnRemove;
		private Database db;
		ButtonListener btnL;
		
		Cart cart;
		
		public TellerFrame() {
			cart = new Cart();
			db = new Database();
			btnL = new ButtonListener();
			mdlTickets = new DefaultListModel<String>();
			db.loadTicketsData(mdlTickets);
			listTickets = new JList(mdlTickets);
			if(!mdlTickets.isEmpty()) listTickets.setSelectedIndex(0);
			listTickets.setLayoutOrientation(JList.VERTICAL);
			listTickets.setVisibleRowCount(3);
			mdlCart = new DefaultListModel<String>();
			listCart = new JList(mdlCart);
			listCart.setLayoutOrientation(JList.VERTICAL);
			listCart.setVisibleRowCount(3);
			JScrollPane scrCart = new JScrollPane(listCart);
			JScrollPane scrTickets = new JScrollPane(listTickets);
			lblSelTickets = new JLabel("Select Tickets:");
			lblCart = new JLabel("Cart:");

			btnAdd = new JButton("Add to Cart");
			btnAdd.addActionListener(btnL);
			btnRemove = new JButton("Remove from Cart");
			btnRemove.addActionListener(btnL);
			
			JPanel pnlTeller = new JPanel();
			pnlTeller.setLayout(new BoxLayout(pnlTeller, BoxLayout.PAGE_AXIS));
			pnlTeller.add(lblSelTickets);
			pnlTeller.add(scrTickets);
			pnlTeller.add(btnAdd);
			pnlTeller.add(lblCart);
			pnlTeller.add(scrCart);
			pnlTeller.add(btnRemove);
			
			add(pnlTeller);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setTitle("Loan Management");
			setSize(400, 400);
			setLocationRelativeTo(null);
			setVisible(true);
			
		}
		
		private class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				if(ae.getSource().equals(btnAdd)) {
					int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
					String selected = listTickets.getSelectedValue().toString();
					int id = Integer.parseInt(selected.substring(0, selected.indexOf(" ")));
					cart.addToCart(new Ticket(id), qty);
				}
			}
		}
}
