package app.taxiAnytimeCustomer.userTypesFactory;

import android.os.Build;


/**
 *@info
 *Πληροφορίες σχετικά με τον οδηγό
 *
 */
public class Driver extends Users  {
	
	private  double Drvlat;
	private double Drvlng;
	private String driverDeviceID;
	
	
	public Driver() {
		// TODO Auto-generated constructor stub
		Drvlat = 0.0;
		Drvlng = 0.0;	
		driverDeviceID = generateUniqueID();

	}
	

	//Sets - gets
	
	public void setDeviceID(String devid) {
		driverDeviceID = devid;
		
	}
	
	public void setLatitude(double drvlat) {
		Drvlat = drvlat;
	}
	
	public void setLongitude(double dvrlng) {
		Drvlng = dvrlng;
	}
	
	public double getLatitude() {
		return Drvlat;
	}
	
	public double getLongitude() {
		return Drvlng;
	}
	
	
	public String getDeviceID() {
		return driverDeviceID;
	}
	
	
	/**
	 * @info
	 * Συνάρτηση για την εξαγωγή μοναδικού id συσκευής
	 * 
	 * @details
	 * Παίρνουμε πληροφορίες από το υλικό της συσκευής(cpu brand,board κτλ) ,
	 * τα συνθέτουμε και παράγουμε το τελικό id
	 * 
	 * 
	 * @return To μοναδικό id της συσκευής
	 */
	private String generateUniqueID()
	{
		String id;
		try
		{
			id = "35" +
	   				Build.BOARD.length()%10+ Build.BRAND.length()%10 + 
	   	        	Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + 
	   	        	Build.DISPLAY.length()%10 + Build.HOST.length()%10 + 
	   	        	Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + 
	   	        	Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + 
	   	        	Build.TAGS.length()%10 + Build.TYPE.length()%10 + 
	   	        	Build.USER.length()%10 ; //13 digits
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		return id;
	}











	
}
