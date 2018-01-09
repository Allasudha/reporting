package spectra.events;

import org.json.JSONObject;

public class VehicleDuty{
    int vehicleCode;
    int dutyCode;
    String deviceNumber;
	
	public int getVehicleCode() {
		return vehicleCode;
	}
	public void setVehicleCode(int vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
	public int getDutyCode() {
		return dutyCode;
	}
	public void setDutyCode(int dutyCode) {
		this.dutyCode = dutyCode;
	}    
	public VehicleDuty(JSONObject details){
       try{
		   setVehicleCode(details.getInt("Vehicle"));
		   setDutyCode(details.getInt("Duty"));
		  
       }catch(Exception e){
    	   System.out.println(e);
       }
	}
}