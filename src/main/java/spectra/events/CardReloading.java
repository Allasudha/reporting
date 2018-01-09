package spectra.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class CardReloading{

	int sessionId;
	String dateTime;
	int receiptNumber;
	String cardNumber;
	int totalPrice;
	
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
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String string) {
		this.cardNumber = string;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public CardReloading(JSONObject details,int date,int time,int sessionId) throws JSONException, ParseException{
		setReceiptNumber(details.getInt("ReceiptNumber"));
		setCardNumber(details.getString("CardNumber"));
		setTotalPrice(details.getInt("TotalPrice"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   Calendar c = Calendar.getInstance();
		   c.setTime(sdf.parse("1997-01-01 00:00"));
		   c.add(Calendar.DATE, date); 
		   c.add(Calendar.HOUR, time/60);
		   c.add(Calendar.MINUTE, time%60);
		   c.add(Calendar.SECOND, 00);

		   SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   setDateTime(sdf2.format(sdf2.parse(sdf.format(c.getTime()))));
		setSessionId(sessionId);
	}
}