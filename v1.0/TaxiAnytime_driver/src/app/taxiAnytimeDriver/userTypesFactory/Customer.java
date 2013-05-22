package app.taxiAnytimeDriver.userTypesFactory;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/**
 *@info
 *����������� ������� �� ��� ������
 *
 */
public class Customer extends Users implements Parcelable{
	

	private double Cstlat;
	private double Cstlng;
	private String customerDeviceID;
	
	
	
	public Customer() {
		// Auto-generated constructor stub
		
		Cstlat = 0.0f;
		Cstlng = 0.0f;
		customerDeviceID = generateUniqueID();
		
	}
	
	
	
	private Customer(Parcel in){
		Cstlat = in.readDouble();
		Cstlng = in.readDouble();
		customerDeviceID = in.readString();
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
	 * ��������� ��� ��� ������� ��������� id ��������
	 * 
	 * @details
	 * ��������� ����������� ��� �� ����� ��� ��������(cpu brand,board ���) ,
	 * �� ���������� ��� ��������� �� ������ id
	 * 
	 * 
	 * @return To �������� id ��� ��������
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


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//  Auto-generated method stub
		 dest.writeDouble(Cstlat);
		 dest.writeDouble(Cstlng);
	     dest.writeString(customerDeviceID);
	    
		
	}

	public static final Customer.Creator<Customer> CREATOR = new Customer.Creator<Customer>() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
	};



	



 
	
	

}
