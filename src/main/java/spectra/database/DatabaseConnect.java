package spectra.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseConnect
{
   public Connection dbConnect()
   {
      try {
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://LAPTOP-SFEMMCH3", "sa","sudha");
         return conn;
      } catch (Exception e) {
         e.printStackTrace();
      }
	return null;
	
   }
   public int sessionInsert(String date,String DeviceNumber,int DriverCode,int VehicleCode,int LineCode,int JourneyDistance,String lineopentime,String lineclosetime) throws SQLException{
	   Connection conn=dbConnect();
	   String queryString = "insert into [ReportDB].[dbo].[session] VALUES ('"+date+"','"+DeviceNumber+"',"+VehicleCode+","+DriverCode+","+LineCode+","+JourneyDistance+",'"+lineopentime+"','"+lineclosetime+"')";
       
       PreparedStatement pstmt;
       int key = 0;
       try {
       pstmt = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
         
       pstmt.executeUpdate();
       ResultSet keys = pstmt.getGeneratedKeys();
        
       keys.next();
       key = keys.getInt(1);
       keys.close();
       pstmt.close();
       conn.close();
       } catch (Exception e) { e.printStackTrace(); }

       return key;
	}
   public ArrayList<Integer> getStopDistances(int lineCode) throws SQLException{
	   Connection conn=dbConnect();
	   String queryString = "EXEC [ReportDB].[dbo].[sp_web_stopDistances] "+lineCode;
	   ArrayList<Integer> distances = new ArrayList<Integer>();
       PreparedStatement pstmt;
       try {
       pstmt = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
       ResultSet rs = null;
       rs=pstmt.executeQuery();
       
       while(rs.next()){
    	   distances.add(Integer.parseInt(rs.getString(1)));
       }
       pstmt.close();
       conn.close();
       } catch (Exception e) { e.printStackTrace(); }

       return distances;
	}
   public int ticketInsert(String dateTime,int sessionId,int receiptNumber,int productCode,int persons,int distance,int totalPrice,int stopCode,int lastStop) throws SQLException{
	   Connection conn=dbConnect();
	   String queryString = "insert into [ReportDB].[dbo].[ticket] VALUES ('"+dateTime+"',"+sessionId+","+receiptNumber+","+productCode+","+persons+","+distance+","+totalPrice+","+stopCode+","+lastStop+")";
       PreparedStatement pstmt;
       int key = 0;
       try {
        pstmt = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
        ResultSet keys = pstmt.getGeneratedKeys();
        keys.next();
        key = keys.getInt(1);
        keys.close();
        pstmt.close();
        conn.close();
       } catch (Exception e) { e.printStackTrace(); }
       return key;
	}
   public void sessionUpdate(int journeyDistance,String closingTime,int sessionID) throws SQLException{
	   Connection conn=dbConnect();
	   String queryString = "update [ReportDB].[dbo].[session] set journeyDistance="+journeyDistance+" ,closingDatetime='"+closingTime+"' where id="+sessionID;
       PreparedStatement pstmt;
       try {
        pstmt = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
       } catch (Exception e) { e.printStackTrace(); }
	}
   public void cardReloadsInsert(String dateTime,int sessionId,int ticketid,int receiptNumber,String cardNumber,int totalPrice,int stopCode) throws SQLException{
	   Connection conn=dbConnect();
	   String queryString = "insert into [ReportDB].[dbo].[cardReloads] VALUES ('"+dateTime+"',"+sessionId+","+ticketid+","+receiptNumber+",'"+cardNumber+"',"+totalPrice+","+stopCode+")";
       PreparedStatement pstmt;
       try {
        pstmt = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
       } catch (Exception e) { e.printStackTrace(); }
	}
   public void cardValidationInsert(String dateTime,int sessionId,int ticketid,String cardNumber,int stopCode) throws SQLException{
	   Connection conn=dbConnect();
	   String queryString = "insert into [ReportDB].[dbo].[cardValidations] VALUES ('"+dateTime+"',"+sessionId+","+ticketid+",'"+cardNumber+"',"+stopCode+")";
       PreparedStatement pstmt;
       try {
        pstmt = conn.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
       } catch (Exception e) { e.printStackTrace(); }
	}
   
}