package app.taxiAnytimeDriver.Driver;

import android.content.Context;
import android.graphics.drawable.Drawable;



/**
 *
 * @info  
 * ������� singleton ��� �� ������������� ��� ���� ��� ����������� overlay
 * �� ������� ��� ���� �� ���� �� ������������� �� markers
 * 
 */
public class OverlayInstance {
	
	static DriverPositionOverlay OverlayInstance = null ;

	
	
	/**
	 * @param mMarker
	 * @param mContext
	 * @return To ����������� DriverPositionOverlay, new �� ��� �������, ��� ���������� ����� ����
	 */
	//sync -> This guarantees that changes to the state of the object are visible to all threads.
	
	public static synchronized DriverPositionOverlay getOverlayInstance(Drawable mMarker,Context mContext){
	     if(OverlayInstance == null){
	    	 OverlayInstance = new DriverPositionOverlay(mMarker, mContext);
	     }
	     else
	     {
	    	 OverlayInstance.setDriverPositionOverlay(mMarker,mContext );
	     }
	     return OverlayInstance;
	   }
	
	

	
	
	
	
}
