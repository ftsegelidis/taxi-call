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
 * Βοηθητική κλάση για την εισαγωγή στοιχείων στο χάρτη.
 * Χρησιμοποιείται σε συνεργασία με την customerActivity και μας επιτρέπει να ζωγραφίζουμε αλλά και
 * να διαχειρίζομαστε πολλαπλά items πάνω στο χάρτη. 
 * Τα items αυτά αποθηκεύονται σε arraylist
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
 * @param defaultMarker Το εικονίδιο που θα ζωγραφιστεί
 * @param context το περιεχόμενο της δραστηριότητας που καλούμε
 * @param location η τρέχουσα τοποθεσία
 * @param customer το αντικείμενο του πελάτη
 * 
 * @info
 * Αρχικοποιεί τις παραμέτρους
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
 * @param p τα γεωγραφικά στοιχεία
 * @param title ο τίτλος του στοιχείου
 * @param snippet μια περιγραφή του στοιχειου
 * 
 * @info
 * Προσθέτει στο arraylist τα αντκείμενα
 * 
 * @details
 * Προσθέτει στο arraylist τα αντκείμενα και να δημοσιοποιεί στο χάρτη (τα εμφανίζει)
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
* Αυτοπαραγόμενη μέθοδος για τη ζωγραφιά των αντικειμώνων
*/
public void draw(Canvas canvas, MapView mapView, boolean shadow) 
   {
       
       super.draw(canvas, mapView, shadow);
   
   }
   

/**
 * @param p το γεωγραφικό μήκος που κάναμε tap στο χάρτη
 * @param mapView το αντιστοιχο αντικείμενο για να το διαχειριστούμε
 * 
 * 
 * @info
 * Χρησιμοποιείται/ενεργοποιείται όταν κάνουμε tap πάνω στο χάρτη 
 * 
 * @details
 * Επειδή θέλουμε να υπάρχει μόνο ένα αντικείμενο , αυτό του πελάτη κάνουμε remove όλο το list
 * και προσθέτουμε νέο , με την ενημερωμένη τοποθεσία.
 * Έπειτα ανανεώνουμε τον χάρτη ώστε να φαίνεται το νέο σημείο που είμαστε
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
   * Αυτοπαραγόμενη μέθοδος που δημιουργεί τα αντικείμενα του χάρτη
   */
    @Override
    protected OverlayItem createItem(int i) {
        
        return (items.get(i));
    }

    /**
    * @info 
    * Αυτοπαραγόμενη μέθοδος που επιστρέφει το μέγεθος της λίστας  των αντικειμένων 
    * που υπάρχουν στο χάρτη
    * 
    * @return 
    * size
    */
	@Override
	public int size(){
		return(items.size());
	}
    
	  

	 /**
	 * @param lat Το γεωγραφικό μήκος του χρήστη
	 * @param lon Το γεωγραφικό πλάτος του χρήστη
	 * 
	 * @info
	 * Μέθοδος για να λαμβάνουμε την διεύθυνση έχοντας τα γεωγραφικά μήκη και πλάτη 
	 * 
	 * @details
	 * Χρησιμοποιείται η built-in κλάση  Geocoder και Address,
	 * για να πάρουμε την διεύθυνση βάσει τα γεωγραφικά μήκη και πλάτη.
	 * 'Επειτα περνάμε την διεύθυνση στο textedit
	 * 
	 */
	public static void updateAddress(double lat,double lon) {
			
		 Geocoder gc = new Geocoder(mContext, Locale.getDefault());
	      try {
	    	List<Address> addresses = gc.getFromLocation(lat,lon, 1);

	    	if(addresses != null) {
	    		Address address = addresses.get(0);
	    		StringBuilder sb = new StringBuilder("Διεύθυνση:\t");

	    		for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
	        	  sb.append(address.getAddressLine(i)).append("\n");

	            //setting text 
	            CustomerActivity.setTextAddress(sb.toString());
	        } 
	    	
	      } catch (IOException e) {}
      
}  	
	


}//end main class







