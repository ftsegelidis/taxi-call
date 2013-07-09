package app.taxiAnytimeDriver.Driver;




/**
 *
 * @info  
 * Τεχνική singleton για να εξασφαλίσουμε ότι μόνο ένα αντικείμενο overlay
 * θα υπάρχει και πάνω σε αυτό θα ζωγραφίζονται τα markers
 * 
 */
public class OverlayInstance {
	
	//static Marker OverlayInstance = null ;

	
	
	/**
	 * @param mMarker
	 * @param mContext
	 * @return To στιγμιότυπο DriverPositionOverlay, new αν δεν υπάρχει, δηλ εκτελείται πρώτη φορά
	 */
	//sync -> This guarantees that changes to the state of the object are visible to all threads.
	
	/*
	public static synchronized MarkerItems getInstance(Drawable mMarker,Context mContext){
	     if(OverlayInstance == null){
	    	 OverlayInstance = new MarkerItems(mMarker, mContext);
	     }
	     else
	     {
	    	 OverlayInstance.setDriverPositionOverlay(mMarker,mContext );
	     }
	     return OverlayInstance;
	   }
	*/
	

	
	
	
	
}
