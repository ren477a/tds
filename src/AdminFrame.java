import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame {
	private JButton btnAdd, btnRemove, btnEdit, btnLoad;
	private JTable table;
	private ButtonListener btnL;
	private final String eventsQ = "SELECT * FROM events";
	private final String ticketsQ = "SELECT * FROM tickets";
	private final String transactionsQ = "SELECT * FROM transactions ORDER BY transac_id";
	private final String[] eventsC = {"Event ID", "Code", "Event", "Description", "Venue", "Date", "Time"};
	private final String[] ticketsC = {"Ticket ID", "Event ID", "Type", "Price", "Stock"};
	private final String[] transactionsC = {"ID", "Transaction #", "Date of Purchase", "Ticket ID", "Quantity", "Total", "Cash", "Change"};
	private JComboBox<String> cb;
	private Database db;
	
	private enum State {
		EVENTS, TICKETS, TRANSACTIONS
	}
	
	State state;
	
	public AdminFrame(Database db) {
		state = State.EVENTS;
		this.db = db;
		table = db.createTable(eventsQ, eventsC);
		JScrollPane scrlTbl = new JScrollPane(table);
		
		cb = new JComboBox<String>(new String[] {"Events", "Tickets", "Transactions"});
		
		btnL = new ButtonListener();
		
		btnLoad = new JButton("Load Table");
		btnLoad.addActionListener(btnL);
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
		pnlBtns.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel pnlTop = new JPanel(new FlowLayout());
		pnlTop.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlTop.add(cb);
		pnlTop.add(btnLoad);
		
		JPanel pnlBody = new JPanel();
		pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.PAGE_AXIS));
		pnlBody.setBorder(new EmptyBorder(20, 20, 20, 20));
		pnlBody.add(pnlTop);
		pnlBody.add(scrlTbl);
		pnlBody.add(pnlBtns);
		
		add(pnlBody);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("TDS: Admin");
		setSize(950, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void refresh() {
		switch(state) {
		case EVENTS:
			db.refreshTable(table, eventsQ, eventsC);
			break;
		case TICKETS:
			db.refreshTable(table, ticketsQ, ticketsC);
			break;
		case TRANSACTIONS:
			db.refreshTable(table, transactionsQ, transactionsC);
			break;
		}
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if(ae.getSource().equals(btnAdd)) {
				NewEntry ne;
				switch(state) {
				case EVENTS:
					ne = new NewEntry(eventsC, AdminFrame.this);
					db.refreshTable(table, eventsQ, eventsC);
					break;
				case TICKETS:
					ne = new NewEntry(ticketsC, AdminFrame.this);
					db.refreshTable(table, ticketsQ, ticketsC);
					break;
				case TRANSACTIONS:
					ne = new NewEntry(transactionsC, AdminFrame.this);
					db.refreshTable(table, transactionsQ, transactionsC);
					break;
				}
			} else if(ae.getSource().equals(btnRemove)) {
				int id = (int) table.getModel().getValueAt(table.getSelectedRow(), 0);
				switch(state) {
				case EVENTS:
					db.deleteEntry(id, "events");
					db.refreshTable(table, eventsQ, eventsC);
					break;
				case TICKETS:
					db.deleteEntry(id, "tickets");
					db.refreshTable(table, ticketsQ, ticketsC);
					break;
				case TRANSACTIONS:
					id = (int) table.getModel().getValueAt(table.getSelectedRow(), 1);
					db.deleteEntry(id, "transactions");
					db.refreshTable(table, transactionsQ, transactionsC);
					break;
				}
				
			} else if(ae.getSource().equals(btnEdit)) {
				switch(state) {
				case EVENTS:
					break;
				case TICKETS:
					break;
				case TRANSACTIONS:
					break;
				}
			} else if(ae.getSource().equals(btnLoad)) {
				if(cb.getSelectedIndex()==0) {
					db.refreshTable(table, eventsQ, eventsC);
					setSize(950, 700);
					setLocationRelativeTo(null);
					state = State.EVENTS;
				}
				else if(cb.getSelectedIndex()==1) {
					db.refreshTable(table, ticketsQ, ticketsC); 
					setSize(420, 700);
					setLocationRelativeTo(null);
					state = State.TICKETS;
				}
				else if(cb.getSelectedIndex()==2) {
					db.refreshTable(table, transactionsQ, transactionsC);
					setSize(700, 700);
					setLocationRelativeTo(null);
					state = State.TRANSACTIONS;
				}
			}
		}
	}
}
