import java.util.ArrayList;

public class Cart {

	private ArrayList<Ticket> tickets;
	private ArrayList<Integer> quantity;
	
	public Cart() {
		 tickets = new ArrayList<Ticket>();
		 quantity = new ArrayList<Integer>();
	}
	
	public void addToCart(Ticket t, int qty) {
		tickets.add(t);
		quantity.add(qty);
	}
	
	public double getTotalPrice() {
		double total = 0;
		for(int i = 0; i < tickets.size(); i++) {
			total += (tickets.get(i).getPrice()*quantity.get(i));
		}
		return total;
	}
	
	public String generateReceipt() {
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
	
}
