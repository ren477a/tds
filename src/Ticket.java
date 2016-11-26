import java.sql.*;

public class Ticket {
	private int id;
	private double price;
	private String eventCode;
	private String type;
	
	private Database db;
	public Ticket(int id) {
		db = new Database();
		this.id = id;
		this.price = db.getTicketPrice(id);
		this.eventCode = db.getEventCode(id);
		this.type = db.getTicketType(id);
	}
	
	public int getId() {
		return id;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getEventCode() {
		return eventCode;
	}
	public String getType() {
		return type;
	}
	
	public String getTicketInfo() {
		System.out.println();
		return " : " + type + " : " + eventCode + " : " + price;
	}
}
