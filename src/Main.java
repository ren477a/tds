import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame{
	
	JPanel pnlMain, pnlBtns;
	JLabel lblBanner;
	JButton btnAdmin, btnTeller, btnExit;
	ButtonListener btnL;
	public Main() {
		pnlMain = new JPanel(new BorderLayout());
		pnlBtns = new JPanel();
		
		btnL = new ButtonListener();
		btnAdmin = new JButton("Admin");
		btnAdmin.addActionListener(btnL);
		btnTeller = new JButton("Teller");
		btnTeller.addActionListener(btnL);
		btnExit = new JButton("Exit");
		btnExit.addActionListener(btnL);
		
		lblBanner = new JLabel("TDS", JLabel.CENTER);

		pnlBtns.add(btnAdmin);
		pnlBtns.add(btnTeller);
		pnlBtns.add(btnExit);
		pnlMain.add(lblBanner);
		pnlMain.add(pnlBtns, BorderLayout.SOUTH);
		add(pnlMain);
		
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if(ae.getSource().equals(btnTeller)) {
				TellerFrame tf = new TellerFrame();
				setVisible(false);
			} else if(ae.getSource().equals(btnAdmin)) {
				
			}
		}
	}
	
	public static void createShowGUI() {
		Main app = new Main();
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setTitle("Ticket Master");
		app.setSize(400, 400);
		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}
	
	

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createShowGUI();
            }
        });

	}

}
