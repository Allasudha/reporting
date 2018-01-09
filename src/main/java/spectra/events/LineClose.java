package spectra.events;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

public class LineClose{
    int journeyDistance;
	String dateTime;
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String string) {
		this.dateTime = string;
	}
	public int getJourneyDistance() {
		return journeyDistance;
	}
	public void setJourneyDistance(int journeyDistance) {
		this.journeyDistance = journeyDistance;
	}
	  
	public LineClose(JSONObject details,int date,int time){
       try{
		   setJourneyDistance(details.getInt("JourneyDistance"));
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   Calendar c = Calendar.getInstance();
		   c.setTime(sdf.parse("1997-01-01 00:00"));
		   c.add(Calendar.DATE, date); 
		   c.add(Calendar.HOUR, time/60);
		   c.add(Calendar.MINUTE, time%60);
		   c.add(Calendar.SECOND, 00);

		   SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		   setDateTime(sdf2.format(sdf2.parse(sdf.format(c.getTime()))));
       }catch(Exception e){
    	   System.out.println(e);
       }
	}
}