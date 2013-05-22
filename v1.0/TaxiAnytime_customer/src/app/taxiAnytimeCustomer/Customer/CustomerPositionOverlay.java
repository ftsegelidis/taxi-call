package app.taxiAnytimeCustomer.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import app.taxiAnytimeCustomer.userTypesFactory.Users;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


/**
 * @info
 * ��������� ����� ��� ��� �������� ��������� ��� �����.
 * ��������������� �� ���������� �� ��� customerActivity ��� ��� ��������� �� ������������ ���� ���
 * �� ��������������� �������� items ���� ��� �����. 
 * �� items ���� ������������� �� arraylist
 * 
 *
 */
public class CustomerPositionOverlay extends ItemizedOverlay<OverlayItem>
{
	
	
   private ArrayList<OverlayItem> items ;
	
   protected static Location mlocation;
   protected static Context mContext;
   protected Users mCustomer;
   public static double currentLat,currentLon;

   
    
    
   public static void setLocation(Location location) {
	    mlocation = location;
	  }
   
   /**
 * @param defaultMarker �� ��������� ��� �� �����������
 * @param context �� ����������� ��� �������������� ��� �������
 * @param location � �������� ���������
 * @param customer �� ����������� ��� ������
 * 
 * @info
 * ����������� ��� �����������
 * 
 */
public CustomerPositionOverlay(Drawable defaultMarker,Context context,Location location,Users customer) 
   {
	   
       super(defaultMarker);
       
       boundCenterBottom(defaultMarker);
       items = new ArrayList<OverlayItem>();
       mContext = context;
       mlocation = location; 
       mCustomer = customer;
       
       currentLat = location.getLatitude();
       currentLon = location.getLongitude();
       
       populate();

   }
   
 
 
   
   
   /**
 * @param p �� ���������� ��������
 * @param title � ������ ��� ���������
 * @param snippet ��� ��������� ��� ���������
 * 
 * @info
 * ��������� ��� arraylist �� ����������
 * 
 * @details
 * ��������� ��� arraylist �� ���������� ��� �� ������������ ��� ����� (�� ���������)
 * 
 * 
 */
public void addItem(GeoPoint p, String title, String snippet)
   {
	   
	   OverlayItem newItem = new OverlayItem(p, title, snippet);
       items.add(newItem);
        populate();
        
    }
   
 
/**
* @info 
* �������������� ������� ��� �� �������� ��� ������������
*/
public void draw(Canvas canvas, MapView mapView, boolean shadow) 
   {
       
       super.draw(canvas, mapView, shadow);
   
   }
   

/**
 * @param p �� ���������� ����� ��� ������ tap ��� �����
 * @param mapView �� ���������� ����������� ��� �� �� ��������������
 * 
 * 
 * @info
 * ���������������/�������������� ���� ������� tap ���� ��� ����� 
 * 
 * @details
 * ������ ������� �� ������� ���� ��� ����������� , ���� ��� ������ ������� remove ��� �� list
 * ��� ����������� ��� , �� ��� ����������� ���������.
 * ������ ����������� ��� ����� ���� �� �������� �� ��� ������ ��� �������
 * 
 * 
 */
   @Override
   public boolean onTap(GeoPoint p, MapView mapView) 
   {

	this.items.removeAll(items);
   
   	this.addItem(p,null,null);
   	
   	mlocation.setLatitude(p.getLatitudeE6()/1E6);
   	mlocation.setLongitude(p.getLongitudeE6()/1E6);
   	updateAddress(p.getLatitudeE6()/1E6,p.getLongitudeE6()/1E6);
   	currentLat = p.getLatitudeE6()/1E6;
   	currentLon = p.getLongitudeE6()/1E6;

   	mapView.invalidate();  //refresh map
   	populate();
   	
   	return super.onTap(p,mapView);
   }
   

   /**
   * @info 
   * �������������� ������� ��� ���������� �� ����������� ��� �����
   */
    @Override
    protected OverlayItem createItem(int i) {
        
        return (items.get(i));
    }

    /**
    * @info 
    * �������������� ������� ��� ���������� �� ������� ��� ������  ��� ������������ 
    * ��� �������� ��� �����
    * 
    * @return 
    * size
    */
	@Override
	public int size(){
		return(items.size());
	}
    
	  

	 /**
	 * @param lat �� ���������� ����� ��� ������
	 * @param lon �� ���������� ������ ��� ������
	 * 
	 * @info
	 * ������� ��� �� ���������� ��� ��������� ������� �� ���������� ���� ��� ����� 
	 * 
	 * @details
	 * ��������������� � built-in �����  Geocoder ��� Address,
	 * ��� �� ������� ��� ��������� ����� �� ���������� ���� ��� �����.
	 * '������ ������� ��� ��������� ��� textedit
	 * 
	 */
	public static void updateAddress(double lat,double lon) {
			
		 Geocoder gc = new Geocoder(mContext, Locale.getDefault());
	      try {
	    	List<Address> addresses = gc.getFromLocation(lat,lon, 1);

	    	if(addresses != null) {
	    		Address address = addresses.get(0);
	    		StringBuilder sb = new StringBuilder("���������:\t");

	    		for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
	        	  sb.append(address.getAddressLine(i)).append("\n");

	            //setting text 
	            CustomerActivity.setTextAddress(sb.toString());
	        } 
	    	
	      } catch (IOException e) {}
      
}  	
	


}//end main class







