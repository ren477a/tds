import java.sql.*;

public class Ticket {
	private int id;
	private double price;
	
	private Database db;
	public Ticket(int id) {
		db = new Database();
		this.id = id;
		this.price = db.getTicketPrice(id);
	}
	
	public int getId() {
		return id;
	}
	
	public double getPrice() {
		return price;
	}
}
