package app.taxiAnytimeCustomer.DriversList;

import android.graphics.Bitmap;
import android.util.Log;
/**
 * @info
 * Κλάση που περιέχει όλες τις πληροφορίες σχετικά με τον οδηγό ταξί
 *
 */
public class DriverDetails {
	
	private String name ;
	private String distance; 
	private float rate;  
	private String plateNumber;
	private String driverDeviceId;
	private int orderid;
	private Bitmap myImage;
	
	
	public void setMyImage(Bitmap myImage) {
		this.myImage = myImage;
	}
	public Bitmap getMyImage() {
		return myImage;
	}
	public String getDriverDeviceId() {
		return driverDeviceId;
	}
	public void setDriverDeviceId(String driverDeviceId) {
		this.driverDeviceId = driverDeviceId;
	}
	
	public String getPlateNumber() {
		return plateNumber;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String dist) {
		
		this.distance = distance2dec(dist);
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rating) {
		this.rate = rating;
	}
	
	
	/**
	 * @param distance Η απόσταση μεταξύ πελάτη και οδηγού.
	 * 
	 * @info 
	 * Mετατρέπει την απόσταση απο μορφή xxx.xxxxx σε xxx.x
	 * 
	 * @details
	 * Δέχεται την απόσταση ως string, πχ 456.34
	 * Το χωρίζει σε δυο υπο-string με βάση την ' . ' 
	 * και επιστρέφει το πρώτο υπομέρος μαζί με τον πρώτο χαρακτήρα του δεύτερου,
	 * ως αποτέλεσμα να έχουμε 1 δεκαδικό ακρίβεια
	 * 
	 * 
	 * 
	 * @return calculatedDistance η απόσταση μεταξύ οδηγού και πελάτη σε μορφή 400.5m
	 * 
	 *
	 */
	private String distance2dec(String distance)
	{
		
		String []split = distance.split("\\.");
		
		String returned;
		try
		{
			returned = split[0]+"."+split[1].charAt(0)+"m";
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			returned = distance+"m";
		}
		
		
		return returned;
		
	}

	
}

