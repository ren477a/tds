import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class TellerFrame extends JFrame {
		private JList<String> listTickets, listCart;
		private DefaultListModel<String> mdlTickets, mdlCart;
		private JLabel lblSelTickets, lblCart;
		private JButton btnNewT, btnAdd, btnRemove;
		private Database db;
		private  JTable tblTickets;
		ButtonListener btnL;
		
		Cart cart;
		
		public TellerFrame() {
			cart = new Cart();
			db = new Database();
			btnL = new ButtonListener();
			mdlTickets = new DefaultListModel<String>();
//			db.loadTicketsData(mdlTickets);
//			listTickets = new JList<String>(mdlTickets);
//			if(!mdlTickets.isEmpty()) listTickets.setSelectedIndex(0);
//			listTickets.setLayoutOrientation(JList.VERTICAL);
//			listTickets.setVisibleRowCount(3);
			String[] tblTklCols = {"ID", "Type", "Event", "Price", "Available"};
			tblTickets = db.createTable("SELECT tickets.ticket_id AS ID, tickets.ticket_type AS TYPE, events.event_name AS EVENT, "
					+ "tickets.ticket_price AS PRICE, tickets.ticket_stock AS AVAILABLE "
					+"FROM tickets " 
					+"INNER JOIN events " 
					+"ON tickets.event_id=events.event_id "
					+"ORDER BY tickets.event_id, tickets.ticket_price DESC",tblTklCols);
			tblTickets.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			mdlCart = new DefaultListModel<String>();
			listCart = new JList<String>(mdlCart);
			listCart.setLayoutOrientation(JList.VERTICAL);
			listCart.setVisibleRowCount(3);
			JScrollPane scrCart = new JScrollPane(listCart);
			JScrollPane scrTickets = new JScrollPane(tblTickets);
			lblSelTickets = new JLabel("Select Tickets:");
			lblCart = new JLabel("Cart:");

			btnAdd = new JButton("Add to Cart");
			btnAdd.addActionListener(btnL);
			btnRemove = new JButton("Remove from Cart");
			btnRemove.addActionListener(btnL);
			
			JPanel pnlTeller = new JPanel();
			pnlTeller.setLayout(new BoxLayout(pnlTeller, BoxLayout.PAGE_AXIS));
			pnlTeller.setBorder(new EmptyBorder(20,20,20,20));
			pnlTeller.add(lblSelTickets);
			pnlTeller.add(scrTickets);
			pnlTeller.add(btnAdd);
			pnlTeller.add(lblCart);
			pnlTeller.add(scrCart);
			pnlTeller.add(btnRemove);
			
			add(pnlTeller);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setTitle("Ticket Master");
			setSize(600, 600);
			setLocationRelativeTo(null);
			setVisible(true);
			
		}
		
		private class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				if(ae.getSource().equals(btnAdd)) {
					System.out.println(tblTickets.getSelectedRow());
					if(tblTickets.getSelectedRow()!=-1) {
						int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
						int id = (int) tblTickets.getModel().getValueAt(tblTickets.getSelectedRow(), 0);
						cart.addToCart(new Ticket(id), qty);
						mdlCart.addElement(cart.getLastDisplaySummary());
					} 
				} else if(ae.getSource().equals(btnRemove)) {
					String selected = listCart.getSelectedValue();
					if(selected!=null) {
						mdlCart.remove(listCart.getSelectedIndex());
						cart.removeItem(selected);
					}
					
				}
			}
		}
}
