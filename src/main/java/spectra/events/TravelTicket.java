package spectra.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class TravelTicket{

	int sessionId;
	String dateTime;
	int receiptNumber;
	int productCode;
	int persons;
	int distance;
	int totalPrice;
	int lastStop;
	
	public int getLastStop() {
		return lastStop;
	}
	public void setLastStop(int lastStop) {
		this.lastStop = lastStop;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public int getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(int receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	public int getProductCode() {
		return productCode;
	}
	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}
	public int getPersons() {
		return persons;
	}
	public void setPersons(int persons) {
		this.persons = persons;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public TravelTicket(JSONObject details,int date,int time,int sessionId,ArrayList<Double> dists,int startStop) throws JSONException, ParseException{
		setReceiptNumber(details.getInt("ReceiptNumber"));
		setProductCode(details.getInt("ProductCode"));
		setPersons(details.getInt("Persons"));
		setDistance(details.getInt("Distance"));
		setTotalPrice(details.getInt("TotalPrice"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   Calendar c = Calendar.getInstance();
		   c.setTime(sdf.parse("1997-01-01 00:00"));
		   c.add(Calendar.DATE, date); 
		   c.add(Calendar.HOUR, time/60);
		   c.add(Calendar.MINUTE, time%60);
		   c.add(Calendar.SECOND, 00);
		   setLastStop(findLastStop(startStop,dists,details.getInt("Distance")));
           SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   setDateTime(sdf2.format(sdf2.parse(sdf.format(c.getTime()))));
		   setSessionId(sessionId);
	}
	private int findLastStop(int startStop,ArrayList<Double> dists,int ticketDistance) {
		double dist=0;
		int i=1;
		for(i=startStop;(double)ticketDistance>dist;i++){
			if(i>dists.size()-1) {++i;break;};
			dist=dist+dists.get(i);
		}
		return i;
	}
}