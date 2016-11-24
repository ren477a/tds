import java.sql.*;

public class Ticket {
	private int id;
	private double price;
	
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private final String dbUser = "root", dbPass = "",
			dbUrl = "jdbc:mysql://localhost/td_system?autoReconnect=true&useSSL=false",
			dbDriver = "com.mysql.jdbc.Driver";
	
	public Ticket(int id) {
		this.id = id;
		
		try {
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM tickets WHERE ticket_id="+id);
			rs.next();
			price = rs.getDouble("ticket_price");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return id;
	}
	
	public double getPrice() {
		return price;
	}
}
