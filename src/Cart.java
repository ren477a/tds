import java.util.ArrayList;

public class Cart {

	private ArrayList<Ticket> tickets;
	private ArrayList<Integer> quantity;
	private ArrayList<String> summary;
	private double total;
	
	public Cart() {
		 tickets = new ArrayList<Ticket>();
		 quantity = new ArrayList<Integer>();
		 summary = new ArrayList<String>();
	}
	
	public void addToCart(Ticket t, int qty) {
		tickets.add(t);
		quantity.add(qty);
		summary.add(qty + t.getTicketInfo());
		total += t.getPrice() * qty;
	}
	
	public boolean removeItem(String item) {
		int index = summary.indexOf(item);
		if(index==-1)
			return false;
		else {
			total -= tickets.get(index).getPrice()*quantity.get(index);
			tickets.remove(index);
			quantity.remove(index);
			summary.remove(index);
			
			return true;
		}
			
	}
	
	public double getTotal() {
		return total;
	}
	
	public boolean isEmpty() {
		if(tickets.size()>0)
			return false;
		else
			return true;
	}
	
	
	public String getLastDisplaySummary() {
		return summary.get(summary.size()-1);
	}
	
	public String generateReceipt() {
		StringBuilder sb = new StringBuilder();
		sb.append("RECEIPT:\n");
		for(int i = 0; i < tickets.size(); i++) {
			sb.append(quantity.get(i));
			sb.append(" : ");
			sb.append(tickets.get(i).getType());
			sb.append(" : ");
			sb.append(tickets.get(i).getEventCode());
			sb.append(" :\t");
			sb.append(tickets.get(i).getPrice());
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
