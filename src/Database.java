import java.sql.*;

import javax.swing.DefaultListModel;

public class Database {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private final String dbUser = "root", dbPass = "",
			dbUrl = "jdbc:mysql://localhost/td_system?autoReconnect=true&useSSL=false",
			dbDriver = "com.mysql.jdbc.Driver";
	
	public Database() {
		
		try {
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public double getTicketPrice(int ticket_id) {
		try {
			rs = stmt.executeQuery("SELECT * FROM tickets WHERE ticket_id="+ticket_id);
			rs.next();
			return rs.getDouble("ticket_price");
		} catch (SQLException e) {
			return 0.0;
		}
	}
	
	public void loadTicketsData(DefaultListModel<String> mdlTickets) {
		mdlTickets.removeAllElements();
		try {
			rs = stmt.executeQuery("SELECT tickets.ticket_id, tickets.ticket_type, tickets.event_id, events.event_name, tickets.ticket_price "
									+"FROM tickets " 
									+"INNER JOIN events " 
									+"ON tickets.event_id=events.event_id "
									+"ORDER BY tickets.event_id, tickets.ticket_price DESC");
			while(rs.next()){
				int ticketId = rs.getInt("tickets.ticket_id");
				String name = rs.getString("events.event_name");
				double price = rs.getDouble("tickets.ticket_price");
				String type = rs.getString("tickets.ticket_type");
				System.out.println(ticketId + " " + name + " " + type + " " + price);
				String element = ticketId + " " + name + " " + type + " " + price;
				mdlTickets.addElement(element);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
