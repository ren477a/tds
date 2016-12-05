import java.awt.Component;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextField;
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
	
	public boolean loadEntry(JTextField[] tf, String table, int id) {
		try {
			String idCol = "";
			if(table.equals("events"))
				idCol = "event_id";
			else if(table.equals("tickets"))
				idCol = "ticket_id";
			else if(table.equals("transactions"))
				idCol = "id";
			System.out.println("SELECT * FROM " + table + " WHERE "+idCol+"="+id);
			rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE "+idCol+"="+id);
			rs.next();
			if(table.equals("events")) {
				tf[0].setText(Integer.toString(rs.getInt("event_id")));
				tf[1].setText(rs.getString("code"));
				tf[2].setText(rs.getString("event_name"));
				tf[3].setText(rs.getString("event_description"));
				tf[4].setText(rs.getString("venue"));
				tf[5].setText(rs.getString("event_date"));
				tf[6].setText(rs.getString("event_time"));
			} else if(table.equals("tickets")) {
				tf[0].setText(Integer.toString(rs.getInt("ticket_id")));
				tf[1].setText(rs.getString("event_id"));
				tf[2].setText(rs.getString("ticket_type"));
				tf[3].setText(rs.getString("ticket_price"));
				tf[4].setText(rs.getString("ticket_stock"));
			} else if(table.equals("transactions")) {
				tf[0].setText(Integer.toString(rs.getInt("id")));
				tf[1].setText(rs.getString("transac_id"));
				tf[2].setText(rs.getString("date_of_purchase"));
				tf[3].setText(rs.getString("ticket_id"));
				tf[4].setText(Integer.toString(rs.getInt("quantity")));
				tf[5].setText(Double.toString(rs.getDouble("total_price")));
				tf[6].setText(Double.toString(rs.getDouble("payment")));
				tf[7].setText(Double.toString(rs.getDouble("change")));
			}
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
	
	
	public boolean insertInto(String table, String[] elements) {
		try {
			String query = "";
			if(table.equals("events")) {
				query = "INSERT INTO events VALUES(null, '"+elements[1]+"', '"+elements[2]+"', '"+elements[3]+"', '"+elements[4]+"', '"+elements[5]+"', '"+elements[6]+"')";
			} else if(table.equals("tickets"))
				query = "INSERT INTO tickets VALUES(null, "+elements[1]+", '"+elements[2]+"', "+elements[3]+", "+elements[4]+")";
			else if(table.equals("transactions")) {
				query = "INSERT INTO transactions VALUES(null, "+elements[1]+", '"+elements[2]+"', "+elements[3]+", "+elements[4]+", "+elements[5]+", "+elements[6]+", "+elements[7]+")";
			
			}
			stmt.executeUpdate(query);
			return true;
		} catch(SQLException e) {
			return false;
		}
	}
	
	public boolean update(String table, String[] elements, int id) {
		try {
			String query = "";
			if(table.equals("events")) {
				query = "UPDATE events SET code='"+elements[1]+"', event_name='"+elements[2]+"', event_description= '"+elements[3]+"', venue='"+elements[4]+"', event_date= '"+elements[5]+"', event_time= '"+elements[6]+"' WHERE event_id="+id;
			} else if(table.equals("tickets"))
				query = "UPDATE tickets SET event_id="+elements[1]+", ticket_type='"+elements[2]+"', ticket_price= "+elements[3]+", ticket_stock="+elements[4]+" WHERE ticket_id="+id;
			else if(table.equals("transactions")) {
				query = "UPDATE transactions SET transac_id="+elements[1]+", date_of_purchase='"+elements[2]+"', ticket_id="+elements[3]+", quantity="+elements[4]+", total_price="+elements[5]+", payment="+elements[6]+", change="+elements[7]+" WHERE id="+id;
			
			}
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
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
	
	//reports
	public String[] getEventNames(int transacID) {
		ArrayList<String> res = new ArrayList<String>();
		String q = "SELECT DISTINCT event_name "
				+ "FROM transactions tr "
				+ "JOIN tickets t ON tr.ticket_id=t.ticket_id "
				+ "JOIN events e ON e.event_id=t.event_id "
				+ "WHERE transac_id="+transacID;
		try {
			rs = stmt.executeQuery(q);
			while (rs.next()) {
				res.add(rs.getString("event_name"));
			}
		} catch (SQLException e) {
			return null;
		}
		String[] out = new String[res.size()];
		for (int i = 0; i < res.size(); i++) {
			out[i]=res.get(i);
		}
		
		
		return out;
	}
	
	public double[] getTRSummary(int transacID) {
		double[] res = new double[3];
		String q = "SELECT * "
				+ "FROM transactions WHERE transac_id="+transacID;
		try {
			rs = stmt.executeQuery(q);
			rs.next();
			res[0] = rs.getDouble("total_price");
			res[1] = rs.getDouble("payment");
			res[2] = rs.getDouble("change");
		} catch (SQLException e) {
			return null;
		}
		return res;
	}
	
	public String[] getTicketDetailsTR(int transacID, String eventName) {
		String[] res = null;
		ArrayList<String> r = new ArrayList<String>();
		String q= "SELECT ticket_type, ticket_price, quantity, ticket_price*quantity as total "
					+ "FROM transactions a JOIN tickets b ON a.ticket_id=b.ticket_id "
					+ "JOIN events e ON e.event_id=b.event_id "
					+ "WHERE transac_id="+transacID+" AND event_name='"+eventName+"'";
		try {
			rs = stmt.executeQuery(q);
			while(rs.next()) {
				r.add(rs.getString("ticket_type"));
				r.add(Double.toString(rs.getDouble("ticket_price")));
				r.add(Integer.toString(rs.getInt("quantity")));
				r.add(Double.toString(rs.getDouble("total")));
			}
			res = new String[r.size()];
			for (int i = 0; i < res.length; i++) {
				res[i] = r.get(i);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String getTRDate(int transacID) {
		try {
			rs = stmt.executeQuery("SELECT date_of_purchase FROM transactions WHERE transac_id="+transacID);
			rs.next();
			return rs.getString("date_of_purchase");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	

}
