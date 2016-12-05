import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class TellerFrame extends JFrame {
		private JList<String> listCart;
		private DefaultListModel<String> mdlCart;
		private JLabel lblSelTickets, lblCart, lblT, lblTotal, lblCashPay;
		private JTextField tfCash;
		private JButton btnViewMore, btnAdd, btnRemove, btnSubmit, btnLogout;
		private Database db;
		private JTable tblTickets;
		private final String[] tblTktCols = {"ID", "Type", "Event", "Available", "Price"};;
		private final String tblTktsQuery = "SELECT tickets.ticket_id AS ID, tickets.ticket_type AS TYPE, events.event_name AS EVENT, "
				+ "tickets.ticket_stock AS AVAILABLE, tickets.ticket_price AS PRICE "
				+"FROM tickets " 
				+"INNER JOIN events " 
				+"ON tickets.event_id=events.event_id "
				+"ORDER BY tickets.event_id, tickets.ticket_price DESC";
		private ButtonListener btnL;
		private Main main;
		private Cart cart;
		
		public TellerFrame(Database db, Main main) {
			this.main = main;
			cart = new Cart();
			this.db = db;
			btnL = new ButtonListener();
			tblTickets = db.createTable(tblTktsQuery ,tblTktCols);
			tblTickets.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			tblTickets.setAlignmentX(Component.LEFT_ALIGNMENT);
			mdlCart = new DefaultListModel<String>();
			listCart = new JList<String>(mdlCart);
			listCart.setLayoutOrientation(JList.VERTICAL);
			listCart.setVisibleRowCount(3);
			JScrollPane scrCart = new JScrollPane(listCart);
			scrCart.setAlignmentX(Component.CENTER_ALIGNMENT);
			JScrollPane scrTickets = new JScrollPane(tblTickets);
			scrTickets.setAlignmentX(Component.LEFT_ALIGNMENT);
			lblSelTickets = new JLabel("Select Tickets:");
			lblCart = new JLabel("Cart:");
			lblT = new JLabel("Total:");
			lblT.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblTotal = new JLabel("0");
			lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblCashPay = new JLabel("Cash Payment:");
			lblCashPay.setAlignmentX(Component.CENTER_ALIGNMENT);

			tfCash = new JTextField();
			tfCash.setColumns(15);
			
			btnAdd = new JButton("Add to Cart");
			btnAdd.addActionListener(btnL);
			btnViewMore = new JButton("More Details");
			btnViewMore.addActionListener(btnL);
			btnRemove = new JButton("Remove from Cart");
			btnRemove.addActionListener(btnL);
			btnSubmit = new JButton("Submit");
			btnSubmit.addActionListener(btnL);
			btnLogout = new JButton("Logout");
			btnLogout.addActionListener(btnL);
			
			
			JPanel pnlBtnLeft = new JPanel(new FlowLayout());
			pnlBtnLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
			pnlBtnLeft.add(btnAdd);
			pnlBtnLeft.add(btnViewMore);
			JPanel pnlTop = new JPanel();
			pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.PAGE_AXIS));
			pnlTop.setBorder(new EmptyBorder(20,20,20,20));
			pnlTop.add(lblSelTickets);
			pnlTop.add(Box.createRigidArea(new Dimension(0, 5)));
			pnlTop.add(scrTickets);
			pnlTop.add(pnlBtnLeft);
			pnlTop.add(lblCart);
			pnlTop.add(Box.createRigidArea(new Dimension(0, 5)));
			JPanel pnlBtnRight = new JPanel(new FlowLayout());
			pnlBtnRight.setAlignmentX(Component.CENTER_ALIGNMENT);
			pnlBtnRight.add(btnRemove);
			pnlBtnRight.add(btnSubmit);
			pnlBtnRight.add(btnLogout);
			JPanel pnlBtm = new JPanel();
			pnlBtm.setAlignmentX(Component.LEFT_ALIGNMENT);
			pnlBtm.setLayout(new BoxLayout(pnlBtm, BoxLayout.PAGE_AXIS));
			pnlBtm.add(scrCart);
			pnlBtm.add(Box.createRigidArea(new Dimension(0, 5)));
			JPanel pnlTotal = new JPanel(new FlowLayout());
			pnlTotal.add(lblT);
			pnlTotal.add(lblTotal);
			pnlBtm.add(pnlTotal);
			pnlBtm.add(Box.createRigidArea(new Dimension(0, 5)));
			JPanel pnlCash = new JPanel(new FlowLayout());
			pnlCash.add(lblCashPay);
			pnlCash.add(tfCash);
			pnlBtm.add(pnlCash);
			pnlBtm.add(pnlBtnRight);
			
			pnlTop.add(pnlBtm);

			add(pnlTop);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setTitle("TDS: Teller");
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
						} catch (NumberFormatException e) {

						}
					} 
				} else if(ae.getSource().equals(btnRemove)) {
					String selected = listCart.getSelectedValue();
					if(selected!=null) {
						mdlCart.remove(listCart.getSelectedIndex());
						cart.removeItem(selected);
						lblTotal.setText(Double.toString(cart.getTotal()));
					}
					
				} else if(ae.getSource().equals(btnViewMore)) {
					if(tblTickets.getSelectedRow()!=-1) {
						int id = (int) tblTickets.getModel().getValueAt(tblTickets.getSelectedRow(), 0);
						String details = db.getTicketInfo(id);
						JOptionPane.showMessageDialog(null, details, "More Info", JOptionPane.PLAIN_MESSAGE);
					} 
				} else if(ae.getSource().equals(btnSubmit)) {
					try {
						double cash = Double.parseDouble(tfCash.getText());
						if(cash < cart.getTotal())
							JOptionPane.showMessageDialog(null, "Insufficient payment!");
						else {
							String receipt = cart.generateReceipt(cash);
							JOptionPane.showMessageDialog(null, receipt, "Transaction Completed", JOptionPane.PLAIN_MESSAGE);
							cart.submitPurchase(db, cash, receipt);
							tfCash.setText("");
							mdlCart.removeAllElements();
							lblTotal.setText("0");
							db.refreshTable(tblTickets, tblTktsQuery, tblTktCols);
							
						}
					} catch (NumberFormatException e) {
						
					}
				}	else if(ae.getSource().equals(btnLogout)) {
					TellerFrame.this.setVisible(false);
					main.setVisible(true);
					main.clear();
				}
			}
		}
}
