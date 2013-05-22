package app.taxiAnytimeDriver.Driver;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import app.taxiAnytimeDriver.userTypesFactory.Users;
import app.taxiAnytimeDriver.userTypesFactory.UsersFactory;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


/**
 * @info
 * Βοηθητική κλάση για την εισαγωγή στοιχείων στο χάρτη.
 * Χρησιμοποιείται σε συνεργασία με την DriverActivity και μας επιτρέπει να ζωγραφίζουμε αλλά και
 * να διαχειρίζομαστε πολλαπλά items πάνω στο χάρτη. 
 * Τα items αυτά αποθηκεύονται σε arraylist
 * 
 *
 */
class  DriverPositionOverlay extends ItemizedOverlay<OverlayItem>
{
	
	public List<OverlayItem> items ;
    protected Users driver;
    protected Users customer;
    private  Context mContext;

    /**
     * @param defaultMarker Το εικονίδιο που θα ζωγραφιστεί
     * @param context το περιεχόμενο της δραστηριότητας που καλούμε
     * 
     * @info
     * Αρχικοποιεί τις παραμέτρους
     * 
     */
	
	  public DriverPositionOverlay(Drawable defaultMarker,Context mycontext) 
	    {
		  super(boundCenterBottom(defaultMarker));
		  	
	    	items = new ArrayList<OverlayItem>();
	    	driver = UsersFactory.createUser("driver"); 
	    	customer = UsersFactory.createUser("customer");
	    	mContext = mycontext;
	    	
	    	populate();
	    

	    }
	  
	  public void setDriverPositionOverlay( Drawable defaultMarker,Context mycontext )
	  {
		  boundCenterBottom(defaultMarker); 
		  mContext = mycontext;
		  populate();
		
	  }


	 
    
   public Context getmContext() {
	return mContext;
}
    
  
    
    public Users getCustomer(){

    		return ( this.customer ); 

    }
   
    
    
    public void removeCustomer(){
    	
    		this.customer = null;

    }
    
   
    
    
    public void updateWithNewLocation(Location location)
    {
  	  
    }
    
    public void removeItem(int index) {
    	
    	if(index>0){
    		
    		items.remove(index);
    		
    		DriverActivity.mv.invalidate();
    
    	}
    	
  }
    
    @Override 
    protected OverlayItem createItem(int i) 
    {

        return items.get(i);
    }
    
   
   
    
    public void addOverlayItem(OverlayItem overlayItem) {
   	 items.add(overlayItem);
     populate();

       
    }

 


    public void addOverlayItem(GeoPoint point, String title,String info, Drawable altMarker,Users usr) {
        
    	
   	  try {
   		  
   		  if(title == "customer"){
   			customer = usr;
   			
   		  }
   		  else //if(title == "driver")
   		  {
   			driver = usr;
   			
   		  }
   		
   		  OverlayItem overlayItem = new OverlayItem(point, title, info);
   		  overlayItem.setMarker( boundCenterBottom(altMarker) );
   		  addOverlayItem(overlayItem);
  
      } catch (Exception e) {
          // : handle exception
          e.printStackTrace();
      }
   	 	
    }
    
  
    
    @Override
    public int size() 
    {
        //  Auto-generated method stub
        return items.size();
    }
    
    
    

    @Override
    protected boolean onTap(int index)
    {
    	 

    	/*
    	 * Toast toast = Toast.makeText(mContext,items.get(index).getSnippet(),Toast.LENGTH_SHORT);
    	toast.show();
    	Log.d("index-size",String.valueOf(index)+"-"+String.valueOf(items.size()));
    	Log.d("customerSize",String.valueOf(customers.size()));
    	 Log.d("index-size-max_index",String.valueOf(index)+"-"+String.valueOf(items.size()) + String.valueOf(maxIndex) );
    	 */

    	try {
    		OverlayItem item = items.get(index);
    		

    			if(getCustomer()!= null && size()>0 && index>0 && item.getSnippet().equals( "new_customer" ) ){

    				DriverActivity.makeOrder(getCustomer() ,driver);
    				removeCustomer();
    				//removeItem(index);
    			
    				Toast toast = Toast.makeText(mContext,"Ο πελάτης θα ειδοποιηθεί για το ενδιαφέρον σας",Toast.LENGTH_LONG);
    				toast.show();

    			}

    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}

    	
    	//Log.d("index-size-mvsize",String.valueOf(index)+"-"+String.valueOf(items.size())+"-"+String.valueOf(DriverActivity.mv.getOverlays().size()));
    	

    	return (true);
 }
    

    
    
//===================================================================================



  
    

    
    
     
}