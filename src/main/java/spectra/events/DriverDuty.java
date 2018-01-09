package spectra.events;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

public class DriverDuty{
    int driverCode;
    int dutyCode;
    String datetime;
    int time;
    public String getDateTime() {
		return datetime;
	}
	public void setDateTime(String string) {
		this.datetime = string;
	}
	
	String deviceNumber;
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public int getDriverCode() {
		return driverCode;
	}
	public void setDriverCode(int driverCode) {
		this.driverCode = driverCode;
	}
	public int getDutyCode() {
		return dutyCode;
	}
	public void setDutyCode(int dutyCode) {
		this.dutyCode = dutyCode;
	}    
	public DriverDuty(JSONObject details,String deviceNumber,int date, int time){
       try{
		   setDriverCode(details.getInt("Driver"));
		   setDutyCode(details.getInt("Duty"));
		   setDeviceNumber(deviceNumber);
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