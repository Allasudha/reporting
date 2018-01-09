package spectra.avro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

import spectra.database.DatabaseConnect;
import spectra.events.CardReloading;
import spectra.events.CardValidation;
import spectra.events.DriverDuty;
import spectra.events.LineClose;
import spectra.events.LineOpen;
import spectra.events.StopOpen;
import spectra.events.VehicleDuty;
import spectra.events.TravelTicket;


public class EventParser 
{
    public static void main( String[] args ) throws IOException, JSONException, NumberFormatException, SQLException, ParseException, InterruptedException
    {
    	//avro to json
    	/*try {
            String[] command = {"cmd.exe", "/C","start", "/wait" , "D:\\Database\\Avro\\Avro_Tools\\testfiles\\batch.bat"};
            Process p =  Runtime.getRuntime().exec(command);   
            p.waitFor();
        } catch (IOException ex) {
        }*/
    	try{
    	File directory = new File("D:/Database/Avro/Avro_Tools/testfiles/");
        File[] fList = directory.listFiles();
         for (File onefile : fList){
        	 System.out.println(FilenameUtils.getExtension(onefile.getAbsolutePath()));
        	 if(FilenameUtils.getExtension(onefile.getAbsolutePath()).equals("json")) 
        	 parseEachFile(onefile.getAbsolutePath(),onefile.getName());
        }
    	}catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
        }
    	
    }

	private static void parseEachFile(String filepath,String filename) throws IOException, JSONException, NumberFormatException, SQLException, ParseException {
		String deviceNumber = filename.split("\\_(?=[^\\_]+$)")[1].split("\\.(?=[^\\.]+$)")[0];
		FileInputStream fstream = new FileInputStream(filepath);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine= null;
        DriverDuty dd= null;
        VehicleDuty vehicleDuty= null;
        LineOpen lineopen= null;
        LineClose lineclose = null;
        TravelTicket travelTicket = null;
        StopOpen stopOpen =null;
        CardReloading cardReloading = null; 
        CardValidation cardValidation = null;
        int sessionID = 0;
        ArrayList<Double> stopsdifferences = new ArrayList<Double>();
        while ((strLine = br.readLine()) != null)   {
    	  JSONObject json = new JSONObject(strLine);
    	  JSONObject content = new JSONObject(json.getString("Content"));
    	  JSONObject events = new JSONObject(content.toString());
    	  String evtn = events.keys().next().toString();
    	  if(evtn.equals("POS.SessionLog.DriverDuty")){ dd = new DriverDuty(new JSONObject(events.getString("POS.SessionLog.DriverDuty")),deviceNumber,Integer.parseInt(json.getString("Date")),Integer.parseInt(json.getString("Time")));}
    	  if(evtn.equals("POS.SessionLog.VehicleDuty")){ vehicleDuty = new VehicleDuty(new JSONObject(events.getString("POS.SessionLog.VehicleDuty")));}
    	  if(evtn.equals("POS.SessionLog.LineOpen")){ 
    		  stopsdifferences.clear();
    		 if((new JSONObject(events.getString("POS.SessionLog.LineOpen")))!=null){
    		  lineopen = new LineOpen(new JSONObject(events.getString("POS.SessionLog.LineOpen")),Integer.parseInt(json.getString("Date")),Integer.parseInt(json.getString("Time")));
    		  DatabaseConnect db = new DatabaseConnect();
    	  	  sessionID =  db.sessionInsert(dd.getDateTime(),dd.getDeviceNumber(),dd.getDriverCode(),vehicleDuty.getVehicleCode(),lineopen.getLineCode(),0,lineopen.getDateTime(),"");
              ArrayList<Integer> distancesdb = new ArrayList<Integer>();
    	      distancesdb = db.getStopDistances(lineopen.getLineCode());
    	      for(int i=0;i<=distancesdb.size()-2;i++){
    	    	  stopsdifferences.add((((double)distancesdb.get(i+1)-(double)distancesdb.get(i))/1000.00));
    	      }
    		 }else {continue;}
    	  }if(evtn.equals("POS.SessionLog.StopOpen")){ 
    		  stopOpen = new StopOpen(new JSONObject(events.getString("POS.SessionLog.StopOpen")));
    		  }
    	  if(evtn.equals("POS.SessionLog.LineClose")){ 
    		  lineclose = new LineClose(new JSONObject(events.getString("POS.SessionLog.LineClose")),Integer.parseInt(json.getString("Date")),Integer.parseInt(json.getString("Time")));
    		  DatabaseConnect db = new DatabaseConnect();
    	  	  db.sessionUpdate(lineclose.getJourneyDistance(),lineclose.getDateTime(),sessionID);
    	  }
    	  if(evtn.equals("POS.SessionLog.TravelTicket")){ 
    		  travelTicket = new TravelTicket(new JSONObject(events.getString("POS.SessionLog.TravelTicket")),Integer.parseInt(json.getString("Date")),Integer.parseInt(json.getString("Time")),sessionID,stopsdifferences,stopOpen.getStopCode());
    		  DatabaseConnect db = new DatabaseConnect();
    	  	  db.ticketInsert(travelTicket.getDateTime(),travelTicket.getSessionId(),travelTicket.getReceiptNumber(),travelTicket.getProductCode(),travelTicket.getPersons(),travelTicket.getDistance(),travelTicket.getTotalPrice(),stopOpen.getStopCode(),travelTicket.getLastStop());
    	  }
    	  if(evtn.equals("POS.SessionLog.CardReloading")){ 
    		  cardReloading = new CardReloading(new JSONObject(events.getString("POS.SessionLog.CardReloading")),Integer.parseInt(json.getString("Date")),Integer.parseInt(json.getString("Time")),sessionID);
    		  DatabaseConnect db = new DatabaseConnect();
    		  int ticketid=db.ticketInsert(cardReloading.getDateTime(),cardReloading.getSessionId(),cardReloading.getReceiptNumber(),3,0,0,cardReloading.getTotalPrice(),stopOpen.getStopCode(),stopsdifferences.size()+1);
        	  db.cardReloadsInsert(cardReloading.getDateTime(),cardReloading.getSessionId(),ticketid,cardReloading.getReceiptNumber(),cardReloading.getCardNumber(),cardReloading.getTotalPrice(),stopOpen.getStopCode());
    	  }
    	  if(evtn.equals("POS.SessionLog.CardValidation")){ 
    		  cardValidation = new CardValidation(new JSONObject(events.getString("POS.SessionLog.CardValidation")),Integer.parseInt(json.getString("Date")),Integer.parseInt(json.getString("Time")),sessionID);
    		  DatabaseConnect db = new DatabaseConnect();
    		  int ticketid=db.ticketInsert(cardValidation.getDateTime(),cardValidation.getSessionId(),0,5,0,0,0,stopOpen.getStopCode(),stopsdifferences.size()+1);
        	  db.cardValidationInsert(cardValidation.getDateTime(),cardValidation.getSessionId(),ticketid,cardValidation.getCardNumber(),stopOpen.getStopCode());
    	  }
    	}
        br.close();
	}
}
