package spectra.events;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

public class LineOpen{
    int lineCode;
    String dateTime;
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String date) {
		this.dateTime = date;
	}
	public int getLineCode() {
		return lineCode;
	}
	public void setLineCode(int lineCode) {
		this.lineCode = lineCode;
	}    
	public LineOpen(JSONObject details,int date,int time){
       try{
		   setLineCode(details.getInt("Code"));
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   Calendar c = Calendar.getInstance();
		   c.setTime(sdf.parse("1997-01-01 00:00"));
		   c.add(Calendar.DATE, date); 
		   c.add(Calendar.HOUR, time/60);
		   c.add(Calendar.MINUTE, time%60);
		   c.add(Calendar.SECOND, 00);

		   SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   setDateTime(sdf2.format(sdf2.parse(sdf.format(c.getTime()))));

		   System.out.println(getDateTime());
       }catch(Exception e){
    	   System.out.println(e);
       }
	}
}