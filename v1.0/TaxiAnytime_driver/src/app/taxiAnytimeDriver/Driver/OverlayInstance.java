package app.taxiAnytimeDriver.Driver;

import android.content.Context;
import android.graphics.drawable.Drawable;



/**
 *
 * @info  
 * Τεχνική singleton για να εξασφαλίσουμε ότι μόνο ένα αντικείμενο overlay
 * θα υπάρχει και πάνω σε αυτό θα ζωγραφίζονται τα markers
 * 
 */
public class OverlayInstance {
	
	static DriverPositionOverlay OverlayInstance = null ;

	
	
	/**
	 * @param mMarker
	 * @param mContext
	 * @return To στιγμιότυπο DriverPositionOverlay, new αν δεν υπάρχει, δηλ εκτελείται πρώτη φορά
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
