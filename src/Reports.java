import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class Reports {
	
	public Reports() {
	}
	
	//generate transaction report
	public boolean generateTR(int id) {
		File temp = new File("templates/transaction.html");
		int trNumber = id;
		String html="";
		try {
			BufferedReader br = new BufferedReader(new FileReader("templates/transaction.html"));
			try {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    String everything = sb.toString();
			    html+=everything;
			} finally {
			    br.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database db = new Database();
		StringBuilder sb = new StringBuilder();
		String[] events = db.getEventNames(id);
		for (int i = 0; i < events.length; i++) {
			sb.append("<h3>Event Name: "+events[i]+"</h3>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<table border='1' align='center'>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<tr>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<td>Ticket Type</td>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<td>Ticket Price</td>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<td>Quantity</td>");
			sb.append(System.getProperty("line.separator"));
			sb.append("<td>Total</td>");
			sb.append(System.getProperty("line.separator"));
			sb.append("</tr>");
			sb.append(System.getProperty("line.separator"));
			String[] out = db.getTicketDetailsTR(id, events[i]);
			for (int j = 0; j < out.length; j+=4) {
				sb.append("<tr>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<td>"+out[j]+"</td>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<td>"+out[j+1]+"</td>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<td>"+out[j+2]+"</td>");
				sb.append(System.getProperty("line.separator"));
				sb.append("<td>"+out[j+3]+"</td>");
				sb.append(System.getProperty("line.separator"));
				sb.append("</tr>");
			}
			sb.append("</table>");
			sb.append(System.getProperty("line.separator"));
		}
		String table = sb.toString();
		
		double[] summary = db.getTRSummary(id);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		html = html.replaceAll("[$]table", table);
		html = html.replaceAll("[$]trNum", Integer.toString(trNumber));
		html = html.replaceAll("[$]grandTotal", Double.toString(summary[0]));
		html = html.replaceAll("[$]cash", Double.toString(summary[1]));
		html = html.replaceAll("[$]change", Double.toString(summary[2]));
		html = html.replaceAll("[$]currDate", dateFormat.format(date));
		html = html.replaceAll("[$]trDate", db.getTRDate(id));

		BufferedWriter output;
		String outFileName = "reports/TR"+id+".html";
		try {
	        output = new BufferedWriter(new FileWriter(outFileName));
	        output.write(html);
	        output.close();
			return true;
	    } catch (IOException ex) {
	    	System.out.println("File IO Error opening " + outFileName);
	    	ex.printStackTrace();
	    	return false;
	    }	    
	}
	
	public boolean generateIR() {
		File temp = new File("templates/inventory.html");
		String html="";
		try {
			BufferedReader br = new BufferedReader(new FileReader(temp));
			try {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    String everything = sb.toString();
			    html+=everything;
			} finally {
			    br.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database db = new Database();
		StringBuilder sb = new StringBuilder();
		html = html.replaceAll("[$]aTickets", db.getAvailableItems(true));
		html = html.replaceAll("[$]soTickets", db.getAvailableItems(false));
		
		//all transactions where transaction date > currDate-24hours;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = dateFormat.format(date);
		html = html.replaceAll("[$]last", db.getTransactionsWithinTheLastDay(d));
		html = html.replaceAll("[$]currDate", d);
		BufferedWriter output;
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String outFileName = "reports/IR"+df.format(date)+".html";
		try {
	        output = new BufferedWriter(new FileWriter(outFileName));
	        output.write(html);
	        output.close();
			return true;
	    } catch (IOException ex) {
	    	System.out.println("File IO Error opening " + outFileName);
	    	ex.printStackTrace();
	    	return false;
	    }	    
	}
}
