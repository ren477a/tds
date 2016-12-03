import java.io.*;
public class Reports {
	
	public Reports() {
	}
	
	//generate transaction report
	public boolean generateTR(int id) {
		int trNumber = id;
		Database db = new Database();
		//Get all distinct events
		//IN: trNumber
		//OUT: Array of event names
		//SELECT DISTINCT event_name FROM transactions tr JOIN tickets t ON tr.ticket_id=t.ticket_id JOIN events e ON e.event_id=t.event_id WHERE transac_id=
		String[] events = db.getEventNames(id);
		for (int i = 0; i < events.length; i++) {
			
		}
		//Make a table for each event
		
		//Event name
		//	ticketID
		//	ticketType
		//	ticketPrice
		//	quantity
		//	total(price*quantity)
		// SELECT ticket_id, ticket_type, ticket_price, quantity, ticket_price*quantity as total FROM transactions a JOIN tickets b ON a.ticket_id=b.ticket_id
		
		Double[] summary = db.getTRSummary(id);
		//IN: trNumber
		//OUT: Array of values below
		//GrandTotal
		//Cash
		//Change
		
		return true;
	}
}
