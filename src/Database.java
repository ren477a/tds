import java.awt.Component;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
	
	public int getLatestTransacID() {
		try {
			rs = stmt.executeQuery("SELECT MAX(transac_id) FROM transactions");
			rs.next();
			int maxID = rs.getInt("MAX(transac_id)");
			return maxID;
		} catch (SQLException e) {
			System.out.println("error");
			return -1;
		}
	}
	
	public String getTicketInfo(int tkt_id) {
		try {
			rs = stmt.executeQuery("SELECT event_id, ticket_type FROM tickets WHERE ticket_id="+tkt_id);
			rs.next();
			int eventID = rs.getInt("event_id");
			String type = rs.getString("ticket_type");
			rs = stmt.executeQuery("SELECT * FROM events WHERE event_id="+eventID);
			rs.next();
			StringBuilder sb = new StringBuilder();
			sb.append("Event: \n");
			sb.append(rs.getString("event_name"));
			sb.append("\n\nTicket Type: \n");
			sb.append(type);
			sb.append("\n\nVenue: \n");
			sb.append(rs.getString("venue"));
			sb.append("\n\nDate: \n");
			sb.append(rs.getDate("event_date").toString());
			sb.append("\n\nTime: \n");
			sb.append(rs.getTime("event_time").toString());
			if(rs.getString("event_description")!=null) {
				sb.append("\n\nDescription: \n");
				sb.append(rs.getString("event_description"));
			}
			
			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean deleteEntry(int id, String tableName) {
		try {
			String colName = "";
			if(tableName.equals("events"))
				colName = "event_id";
			else if(tableName.equals("tickets"))
				colName = "ticket_id";
			else if(tableName.equals("transactions"))
				colName = "transac_id";
			stmt.executeUpdate("DELETE FROM "+tableName+" WHERE "+colName+"="+id);
			return true;
		} catch(SQLException e) {
			return false;
		}
		
	}
	
	public void recordTransaction(int trn_id, int tkt_id, int qty, double price, double cash) {
		try {
			stmt.executeUpdate("UPDATE tickets "
					+ "SET ticket_stock=ticket_stock-" + qty
					+ " WHERE ticket_id=" + tkt_id);
			
			stmt.executeUpdate("INSERT INTO transactions "
					+ "VALUES(null, " + trn_id
					+ ", DEFAULT, " + tkt_id
					+ ", " + qty
					+ ", " + price
					+ ", " + cash
					+ ", " + (cash-price) + ")");
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public void refreshTable(JTable table, String query, String[] cols) {
		try {
			table.setModel(buildTableModel(stmt.executeQuery(query), cols));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//resultset to table
	public JTable createTable(String query, String[] cols) {
		JTable table = null;
		try {
			rs = stmt.executeQuery(query);
			table = new JTable(buildTableModel(rs, cols)){
//			    @Override
//			       public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//			           Component component = super.prepareRenderer(renderer, row, column);
//			           int rendererWidth = component.getPreferredSize().width;
//			           TableColumn tableColumn = getColumnModel().getColumn(column);
//			           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
//			           return component;
//			        }
			    	public boolean isCellEditable(int row, int column) {                
			    		return false;    
			    	}
			    };
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);;
			return table;
		} catch (SQLException e) {
			return table;
		}
	}
	
	private static DefaultTableModel buildTableModel(ResultSet rs, String[] columns)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    Vector<String> columnNames = new Vector<String>(Arrays.asList(columns));
	    int columnCount = metaData.getColumnCount();
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
