import java.awt.Component;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.DefaultListModel;
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
