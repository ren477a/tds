import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TellerFrame extends JFrame {
		private JList listTickets, listCart;
		private DefaultListModel<String> mdlTickets, mdlCart;
		private JLabel lblSelTickets, lblCart;
		private JButton btnNewT;
		
		public TellerFrame() {
			mdlTickets = new DefaultListModel<String>();
			listTickets = new JList(mdlTickets);
			listTickets.setLayoutOrientation(JList.VERTICAL);
			listTickets.setVisibleRowCount(3);
			mdlCart = new DefaultListModel<String>();
			listCart = new JList(mdlCart);
			listCart.setLayoutOrientation(JList.VERTICAL);
			listCart.setVisibleRowCount(3);
			JScrollPane scrTickets = new JScrollPane(listTickets);
			lblSelTickets = new JLabel("Select Tickets:");
			lblCart = new JLabel("Cart:");
			
			
			
			
		}
}
