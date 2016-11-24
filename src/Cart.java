import java.util.ArrayList;

public class Cart {
	
	private ArrayList<Ticket> tickets = new ArrayList<Ticket>();
	
	public Cart() {
		
	}
	
	public void addToCart(Ticket t) {
		tickets.add(t);
	}
	
	public double getTotalPrice() {
		double total = 0;
		for(int i = 0; i < tickets.size(); i++) {
			total += tickets.get(i).getPrice();
		}
		return total;
	}
	
	public String generateReceipt() {
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
	
}
