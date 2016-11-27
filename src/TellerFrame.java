import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class TellerFrame extends JFrame {
		private JList<String> listTickets, listCart;
		private DefaultListModel<String> mdlTickets, mdlCart;
		private JLabel lblSelTickets, lblCart, lblT, lblTotal, lblCashPay;
		private JTextField tfCash;
		private JButton btnViewMore, btnAdd, btnRemove, btnSubmit;
		private Database db;
		private JTable tblTickets;
		private final String[] tblTktCols = {"ID", "Type", "Event", "Available", "Price"};;
		private final String tblTktsQuery = "SELECT tickets.ticket_id AS ID, tickets.ticket_type AS TYPE, events.event_name AS EVENT, "
				+ "tickets.ticket_stock AS AVAILABLE, tickets.ticket_price AS PRICE "
				+"FROM tickets " 
				+"INNER JOIN events " 
				+"ON tickets.event_id=events.event_id "
				+"ORDER BY tickets.event_id, tickets.ticket_price DESC";;
		private ButtonListener btnL;
		
		private Cart cart;
		
		public TellerFrame() {
			cart = new Cart();
			db = new Database();
			btnL = new ButtonListener();
			mdlTickets = new DefaultListModel<String>();
			tblTickets = db.createTable(tblTktsQuery ,tblTktCols);
			tblTickets.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			mdlCart = new DefaultListModel<String>();
			listCart = new JList<String>(mdlCart);
			listCart.setLayoutOrientation(JList.VERTICAL);
			listCart.setVisibleRowCount(3);
			JScrollPane scrCart = new JScrollPane(listCart);
			JScrollPane scrTickets = new JScrollPane(tblTickets);
			lblSelTickets = new JLabel("Select Tickets:");
			lblCart = new JLabel("Cart:");
			lblT = new JLabel("Total:");
			lblTotal = new JLabel("0");
			lblCashPay = new JLabel("Cash Payment:");

			tfCash = new JTextField();
			
			btnAdd = new JButton("Add to Cart");
			btnAdd.addActionListener(btnL);
			btnViewMore = new JButton("More Details");
			btnViewMore.addActionListener(btnL);
			btnRemove = new JButton("Remove from Cart");
			btnRemove.addActionListener(btnL);
			btnSubmit = new JButton("Submit");
			btnSubmit.addActionListener(btnL);
			btnSubmit.setEnabled(false);
			
			JPanel pnlTeller = new JPanel();
			pnlTeller.setLayout(new BoxLayout(pnlTeller, BoxLayout.PAGE_AXIS));
			pnlTeller.setBorder(new EmptyBorder(20,20,20,20));
			pnlTeller.add(lblSelTickets);
			pnlTeller.add(scrTickets);
			pnlTeller.add(btnAdd);
			pnlTeller.add(btnViewMore);
			pnlTeller.add(lblCart);
			pnlTeller.add(scrCart);
			pnlTeller.add(lblT);
			pnlTeller.add(lblTotal);
			pnlTeller.add(lblCashPay);
			pnlTeller.add(tfCash);
			pnlTeller.add(btnRemove);
			pnlTeller.add(btnSubmit);
			
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
					if(tblTickets.getSelectedRow()!=-1) {
						try {
							int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
							int id = (int) tblTickets.getModel().getValueAt(tblTickets.getSelectedRow(), 0);
							cart.addToCart(new Ticket(id), qty);
							mdlCart.addElement(cart.getLastDisplaySummary());
							lblTotal.setText(Double.toString(cart.getTotal()));
							btnSubmit.setEnabled(true);
						} catch (NumberFormatException e) {

						}
					} 
				} else if(ae.getSource().equals(btnRemove)) {
					String selected = listCart.getSelectedValue();
					if(selected!=null) {
						mdlCart.remove(listCart.getSelectedIndex());
						cart.removeItem(selected);
						lblTotal.setText(Double.toString(cart.getTotal()));
						if(cart.isEmpty())
							btnSubmit.setEnabled(false);
					}
					
				} else if(ae.getSource().equals(btnViewMore)) {
					if(tblTickets.getSelectedRow()!=-1) {
						int id = (int) tblTickets.getModel().getValueAt(tblTickets.getSelectedRow(), 0);
						JOptionPane.showMessageDialog(null, "message", "Details", JOptionPane.OK_OPTION);
					} 
				} else if(ae.getSource().equals(btnSubmit)) {
					try {
						double cash = Double.parseDouble(tfCash.getText());
						if(cash < cart.getTotal())
							JOptionPane.showMessageDialog(null, "Insufficient payment!");
						else {
							cart.submitPurchase(db, cash);
							
							JOptionPane.showMessageDialog(null, cart.generateReceipt(cash), "Transaction Completed", JOptionPane.PLAIN_MESSAGE);
							db.refreshTable(tblTickets, tblTktsQuery, tblTktCols);
							
						}
					} catch (NumberFormatException e) {
						
					}
				}
			}
		}
}
