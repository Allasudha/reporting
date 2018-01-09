package spectra.events;

import org.json.JSONObject;

public class StopOpen{
    int stopCode;
	public int getStopCode() {
		return stopCode;
	}
	public void setStopCode(int stopCode) {
		this.stopCode = stopCode;
	}    
	public StopOpen(JSONObject details){
       try{
		   setStopCode(details.getInt("Code"));
       }catch(Exception e){
    	   System.out.println(e);
       }
	}
}