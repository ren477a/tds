import java.awt.Component;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
			rs = stmt.executeQuery("SELECT ticket_price FROM tickets WHERE ticket_id="+ticket_id);
			rs.next();
			return rs.getDouble("ticket_price");
		} catch (SQLException e) {
			return 0.0;
		}
	}
	
	public String getTicketType(int ticket_id) {
		try {
			rs = stmt.executeQuery("SELECT ticket_type FROM tickets WHERE ticket_id="+ticket_id);
			rs.next();
			return rs.getString("ticket_type");
		} catch (SQLException e) {
			return "ERR";
		}
	}
	
	public String getEventCode(int ticket_id) {
		try {
			rs = stmt.executeQuery("SELECT * FROM tickets WHERE ticket_id="+ticket_id);
			rs.next();
			int event_id = rs.getInt("event_id");
			rs = stmt.executeQuery("SELECT code FROM events WHERE event_id="+event_id);
			rs.next();
			return rs.getString("code");
		} catch (SQLException e) {
			return "ERR";
		}
	}
	
//	public void loadTicketsData(DefaultListModel<String> mdlTickets) {
//		mdlTickets.removeAllElements();
//		try {
//			rs = stmt.executeQuery("SELECT tickets.ticket_id, tickets.ticket_type, tickets.event_id, events.event_name, "
//									+ "tickets.ticket_price, tickets.ticket_stock "
//									+"FROM tickets " 
//									+"INNER JOIN events " 
//									+"ON tickets.event_id=events.event_id "
//									+"ORDER BY tickets.event_id, tickets.ticket_price DESC");
//			while(rs.next()){
//				int ticketId = rs.getInt("tickets.ticket_id");
//				String name = rs.getString("events.event_name");
//				double price = rs.getDouble("tickets.ticket_price");
//				String type = rs.getString("tickets.ticket_type");
//				int stock = rs.getInt("ticket_stock");
//				String available = Integer.toString(stock);
//				if(stock==0)
//					available = "SOLD OUT";
//				String element = ticketId + " " + name + " " + type + " " + price + " :: Available: " + available;
//				mdlTickets.addElement(element);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	//resultset to table
	public JTable createTable(String query, String[] cols) {
		JTable table = null;
		try {
			rs = stmt.executeQuery(query);
			table = new JTable(buildTableModel(rs, cols)){
			    @Override
			       public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			           Component component = super.prepareRenderer(renderer, row, column);
			           int rendererWidth = component.getPreferredSize().width;
			           TableColumn tableColumn = getColumnModel().getColumn(column);
			           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
			           return component;
			        }
			    };
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);;
			return table;
		} catch (SQLException e) {
			return table;
		}
	}
	
	private static DefaultTableModel buildTableModel(ResultSet rs, String[] columns)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>(Arrays.asList(columns));
	    int columnCount = metaData.getColumnCount();
//	    for (int column = 1; column <= columnCount; column++) {
//	        columnNames.add(metaData.getColumnName(column));
//	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}

}
