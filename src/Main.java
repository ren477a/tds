import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame{
	
	JPanel pnlMain, pnlBtns;
	JLabel lblBanner;
	JTextField tfUser;
	JPasswordField tfPass;
	JButton btnAdmin, btnTeller, btnExit;
	ButtonListener btnL;
	Database db;
	
	private String userAdmin = "admin";
	private String userTeller = "teller";
	private String passAdmin = "123";
	private String passTeller = "123";
	
	public Main() {
		db = new Database();
		
		pnlMain = new JPanel(new BorderLayout());
		pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
		pnlBtns = new JPanel();
		
		tfUser = new JTextField();
		tfPass = new JPasswordField();
		JPanel pnlCenter = new JPanel(new GridLayout(4, 1));
		pnlCenter.setBorder(new EmptyBorder(20, 20, 20, 20));
		pnlCenter.add(new JLabel("User:"));
		pnlCenter.add(tfUser);
		pnlCenter.add(new JLabel("Pass:"));
		pnlCenter.add(tfPass);
		
		
		btnL = new ButtonListener();
		btnAdmin = new JButton("Admin Login");
		btnAdmin.addActionListener(btnL);
		btnTeller = new JButton("Teller Login");
		btnTeller.addActionListener(btnL);
		btnExit = new JButton("Exit");
		btnExit.addActionListener(btnL);
		
		lblBanner = new JLabel("TDS", JLabel.CENTER);

		pnlBtns.add(btnAdmin);
		pnlBtns.add(btnTeller);
		pnlBtns.add(btnExit);
		pnlMain.add(lblBanner, BorderLayout.NORTH);
		pnlMain.add(pnlCenter);
		pnlMain.add(pnlBtns, BorderLayout.SOUTH);
		add(pnlMain);
		
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if(ae.getSource().equals(btnTeller)) {
				String pass = new String(tfPass.getPassword());
				if(tfUser.getText().equals(userTeller) && pass.equals(passTeller)) {
					TellerFrame tf = new TellerFrame(db);
					setVisible(false);
				}
				
			} else if(ae.getSource().equals(btnAdmin)) {
				String pass = new String(tfPass.getPassword());
				if(tfUser.getText().equals(userAdmin) && pass.equals(passAdmin)) {
					AdminFrame af = new AdminFrame(db);
					setVisible(false);
				}
				setVisible(false);
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
