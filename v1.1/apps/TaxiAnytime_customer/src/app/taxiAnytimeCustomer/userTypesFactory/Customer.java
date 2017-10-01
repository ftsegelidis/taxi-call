package app.taxiAnytimeCustomer.userTypesFactory;

import android.os.Build;


/**
 *@info
 *Πληροφορίες σχετικά με τον πελάτη
 *
 */

public class Customer extends Users {
	

	private double Cstlat;
	private double Cstlng;
	private String customerDeviceID;
	
	
	
	public Customer() {
		// Auto-generated constructor stub
		
		Cstlat = 0.0f;
		Cstlng = 0.0f;
		customerDeviceID = generateUniqueID();
		
	}
	

public void setLatitude(double cstlat) {
	Cstlat = cstlat;
}
public void setLongitude(double cstlng) {
	Cstlng = cstlng;
}


	public void setDeviceID(String devid) {
		customerDeviceID = devid;
	
}
	public double getLatitude() {
		return Cstlat;
	}
	public double getLongitude() {
		return Cstlng;
	}
	
	public String getDeviceID() { 
		
		return customerDeviceID;
	
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
