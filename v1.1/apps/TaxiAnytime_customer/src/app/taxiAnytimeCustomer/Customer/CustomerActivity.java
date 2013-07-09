package app.taxiAnytimeCustomer.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import app.taxiAnytimeCustomer.R;
import app.taxiAnytimeCustomer.Common.About;
import app.taxiAnytimeCustomer.Common.ConnectionDetectorTask;
import app.taxiAnytimeCustomer.Common.EditProfileActivity;
import app.taxiAnytimeCustomer.Common.globalVariables;
import app.taxiAnytimeCustomer.Common.httpJSONParser;
import app.taxiAnytimeCustomer.Common.showAlertMessage;
import app.taxiAnytimeCustomer.PushService.PushService;
import app.taxiAnytimeCustomer.userTypesFactory.Users;
import app.taxiAnytimeCustomer.userTypesFactory.UsersFactory;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;




/**
 * @info
 * H βασικότερη κλάση της εφαρμογής.
 * Παράγει τον χάρτη , χρησιμοποιεί το gps και ξεκινάει η υπηρεσία(Push Service) και εγγράφεται σε αυτήν η
 * συσκευή μας,ώστε να μπορούμε να στέλνουμε αλλά και να λαμβάνουμε ειδοποιήσεις .
 * 
 *
 */

public class CustomerActivity extends FragmentActivity 
{
	
	

	public  GoogleMap mMap;
	public static LatLng currentLatLng;
	private LocationManager locationManager;
	private  LocationListener loclistener;
	public Location myLocation;
	public static TextView txtAddress;
	
	private final String BOOKMARK_TITLE = "Taxi Anytime";
	private final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
	
	
	public  Users customer;
	
	
	
  
	 /**
   * @info
   * Αρχικοποίηση menu buttons
   * 
   * @details
   * Αρχικοποίηση menu buttons
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.layout.menu_items, menu);
      return true;
  }
  
	
	 /**
   * @info
   * Αρχικοποίηση activity
   * 
   * @details
   * Αντιστοιχούμε τις όψεις του xml αρχείου με αντικείμενα , ώστε να μπορούμε να τα διαχειριστούμε.
   * Εισάγουμε το id της συσκευής σε αρχείο SharedPreferences για να μπορέσουμε να το χρησιμοποιήσουμε αργότερα
   * στις επόμενες δραστηριότητες/service.
   * Ξεκινάει το service μετά από όλα αυτά λαμβάνουμε την τοποθεσία gps με την βοήθεια της κατάλληλης 
   * κλασης (ConnectionManager) και ζωγραφίζουμε το εικονίδιο του χρήστη.
   *   * 
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
   
    
    txtAddress = (TextView)findViewById(R.id.txtAddress);
    customer = UsersFactory.createUser("customer");
    
    
   Editor editor = getSharedPreferences(PushService.TAG, MODE_PRIVATE).edit();
   editor.putString(PushService.PREF_DEVICE_ID, customer.getDeviceID());
   editor.commit();
    
   
 //Για να έχουμε πρόσβαση στο id της συσκευής σε όλες τις activities
 	SharedPreferences pref = getSharedPreferences("fromCustomer", MODE_PRIVATE);
    SharedPreferences.Editor edit = pref.edit();
    edit.putString("customerdevid","/"+customer.getDeviceID());
    edit.commit();
    
 try{   
    
   //start the service  
    PushService.actionStart(getApplicationContext());
    
 }
 catch(Exception e){
	  new showAlertMessage(this ,"Ωχ! Κάτι πήγε στραβά με το service!!!","Δοκιμάστε αργότερα");
	  e.printStackTrace();
	 
 }

    FragmentManager fragmentManager = getSupportFragmentManager();
    SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
            .findFragmentById(R.id.map);
    
    mMap = mapFragment.getMap();
    
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);     
    mMap.setMyLocationEnabled(true);
    
    
    
    MapClickListener ml = new MapClickListener(mMap);
    mMap.setOnMapClickListener(ml);
 
  


    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.NO_REQUIREMENT);  //NO_REQUIREMENT
    criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);  //NO_REQUIREMENT
    String provider = locationManager.getBestProvider(criteria, true);
    
	
    
    myLocation = locationManager.getLastKnownLocation(provider);
    loclistener = new mylocationlistener(myLocation);
    locationManager.requestLocationUpdates(provider,0,0,loclistener);
    
    
    
    customer.setLatitude(myLocation.getLatitude()*1E6) ;
	customer.setLongitude(myLocation.getLongitude()*1E6);

	currentLatLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
	updateAddress(currentLatLng.latitude,currentLatLng.longitude);
   
	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
	mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null); 
 
	

  
  
}//oncreate
  
  

 //======================================================================= 
 
  protected class MapClickListener implements OnMapClickListener {

	  GoogleMap mygm;
	  public MapClickListener(GoogleMap gm) {
		
		  mygm = gm;
	}
	  
	  
	@Override
	public void onMapClick(LatLng latLng) {
		
		// Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        mygm.clear();

        // Animating to the touched position
        mygm.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        mygm.addMarker(markerOptions);
		
        updateAddress(latLng.latitude,latLng.longitude) ;
        
        currentLatLng = latLng;
		
	}

}
//=============================================================================  
  
	protected void updateAddress(double lat,double lon) {
		
		 Geocoder gc = new Geocoder( getApplicationContext() , Locale.getDefault());
	      try {
	    	List<Address> addresses = gc.getFromLocation(lat,lon, 1);

	    	if(addresses != null) {
	    		Address address = addresses.get(0);
	    		StringBuilder sb = new StringBuilder("Διεύθυνση:\t");

	    		for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
	        	  sb.append(address.getAddressLine(i)).append("\n");

	            //setting text 
	          txtAddress.setText ( sb.toString());
	      	  txtAddress.refreshDrawableState();
	        } 
	    	
	      } catch (IOException e) {}
    
	}  	

  
  

//============================================================================== 
/**
* @info
* Τερματισμός του service για αποδέσμευση πόρων
*/
@Override
protected void onDestroy() {
	// Auto-generated method stub
	 PushService.actionStop(getApplicationContext());
	super.onDestroy();
}


//=====================================================================================
/**
* 
* 
* @info
* Για μελλοντική χρήση, όταν θέλουμε να έχουμε την νέα τοποθεσία αν αλλάξουμε
* θέση
* 
*Είναι αυτοπαραγώμενη κλάση του SDK
* 
*/ 

  private class mylocationlistener implements LocationListener {
	  
	  public mylocationlistener(Location myloc) {
		//  Auto-generated constructor stub
		  
		
	}
	    @Override
	    public void onLocationChanged(Location location) {
	        if (location != null) {
	        	
	        	
	        	
	        }
	    }

		@Override
		public void onProviderDisabled(String provider) {
			//  Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			//  Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//  Auto-generated method stub
			
		}
  }
 //=======================================================================================
	  
	 
 /**
 * @param v  το View για το button
 * @throws ExecutionException 
 * @throws InterruptedException 
 * 
 * @info
 *  Πατώντας το κουμπί γίνεται αναζήτηση για τους διαθέσιμους οδηγούς
 * @details
 * Αρχικά γίνεται έλεγχος για το αν υπάρχει σύνδεση με το internet καθώς και με τους server,
 * και στη συνέχεια πραγματοποιείται η ειδοποίηση στο παρασκήνιο
 *   
 * 
 */
public void OnimgBtnClickSearch(View v) throws InterruptedException, ExecutionException
 {
	
	if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){
		
		new NotifyDriversTask().execute();
	  }

 }
  

//===========================================================================================
	
	/**
	 * Background Async Task to notify drivers
	 * */


/** 
 * 
 * @info
 * Με την κλάση αυτή γίνετε ενημέρωση των διαθέσιμων οδηγών για νέο πελάτη
 */

private class NotifyDriversTask extends AsyncTask<String, String, String> {

		private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		private ArrayList<NameValuePair> parameters2 = new ArrayList<NameValuePair>();
		private ProgressDialog pDialog;
		
  		/**
  		 * @info
  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη
  		 * 
  		 * @details
  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη
  		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CustomerActivity.this);
			pDialog.setMessage("Ειδοποίηση οδηγών..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

  		/**
  		 * @info
  		 * Γίνετε ενημέρωση των διαθέσιμων οδηγών για νέο πελάτη.
  		 * 
  		 * @details
  		 * Παίρνουμε την τρέχουσα τοποθεσία του πελάτη (γεωργαφικό μήκος & πλάτος), και το id της
  		 * συσκευης και τα κραταμε σε ενα Arraylist, μέσω ενος php script βλέπουμε ποιοί οδηγοί
  		 * είναι διαθέσιμοι, και στέλνουμε σε αυτούς τα δεδομένα.
  		 * */
		@Override
		protected String doInBackground(String... args) {

	
			parameters.add(new BasicNameValuePair("lat",String.valueOf(currentLatLng.latitude)));
			parameters.add(new BasicNameValuePair("lng",String.valueOf(currentLatLng.longitude)));
			parameters.add(new BasicNameValuePair("customerid","/"+customer.getDeviceID()));
		  	  
		  	JSONObject myjobg = null  ; 
		  	httpJSONParser json = new httpJSONParser();
		  	httpJSONParser json2 = new httpJSONParser();
	
			try {

					//String orderid = myjobg.get("orderid").toString();
					myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/"+"getdriverinfo.php",parameters2);
					
					if(Integer.valueOf( myjobg.get("success").toString()) == 1)
					{
						for(int i=0;i<myjobg.getJSONArray("drivers").length();i++)
						{
							parameters.add(new BasicNameValuePair("deviceid"+i,myjobg.getJSONArray("drivers").getJSONObject(i).get("driverDeviceID").toString()));
							
						}

						json2.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/mqttClient/notifyDrivers.php",parameters);
					
					}

			} catch (NumberFormatException e) {
				// Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

  		/**
  		 * @info
  		 *  Αφού ολοκληρωθεί η διαδικασία, αποδεσμεύουμε το dialog και η εκτέλεση
  		 * συνεχίζεται στο κύριο νήμα
  		 * 
  		 * @details
  		 * Αφού ολοκληρωθεί η διαδικασία, αποδεσμεύουμε το dialog και η εκτέλεση
  		 * συνεχίζεται στο κύριο νήμα, και πηγαίνουμε σε οθόνη αναμονής έως ότου
  		 * ανταποκριθούν οι οδηγοί.
  		 * 
  		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			
			this.parameters.remove(parameters);
			this.parameters2.remove(parameters2);
			
			
			pDialog.dismiss();
			
			
			Intent intent = new Intent(CustomerActivity.this,CountdownActivity.class);
			startActivity(intent);
		
			
		}

	}

//=====================================================================
//menus
	
	/**
	 * @param item η επιλογή που έκανε ο χρήστης απο το μενού
	 * 
     * @info
     * Κώδικας σχετικά με το hardware button menu
     * 
     * 
     * @details
     * Στην παρούσα φάση ο χρήστης δεν εχει δικαίωμα να κανει αναφορά ούτε επικοινωνία
     * με κάποιον οδηγό διότι πολυ απλά δεν έχει ξεκινήσει η παραγγελία.
     * Έχει τις δυνατότητες bookmark, share & profile.
     * 
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	// Single menu item is selected do something
    	// Ex: launching new activity/screen or show alert message
        switch (item.getItemId())
        {
       
        case R.id.menu_contact:
        
        	Toast.makeText(this, "Δεν μπορείτε τώρα!!", Toast.LENGTH_SHORT).show();
        	return true;
        case R.id.menu_report:
        	
        	Toast.makeText(this, "Δεν μπορείτε τώρα!!", Toast.LENGTH_SHORT).show();
        	return true;	

        case R.id.menu_bookmark:
        	saveBookmark(this,BOOKMARK_TITLE,BOOKMARK_URL);
            return true;
        case R.id.menu_share:
        	share();
            return true;
        case R.id.menu_profile:
        	changeProfile();
            return true;
        case R.id.menu_aboutTheApp:
        	aboutTheApp();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
        
    }
    
    
    /**
     * @param c το περιεχόμενο της δραστηριότητας.
     * @param title ο τίτλος του σελιδοδείκτη.
     * @param url το site στο οποίο θα κάνει σελιδοδείκτη.
     * 
     * @info
     * Κάνει σελιδοδείκτη το site της εφαρμογής.
     * 
     * @details
     * Κάνει σελιδοδείκτη το site της εφαρμογής.
     */
    public static final void saveBookmark(Context c, String title, String url) {
        Intent i = new Intent(Intent.ACTION_INSERT,
                      android.provider.Browser.BOOKMARKS_URI);
        i.putExtra("title", title);
        i.putExtra("url", url);
        c.startActivity(i);
    }
    
    /**
     * @info
     * Δίνει την επιλογή στον χρήστη να μοιραστεί με άλλους πληροφορίες σχετικά
     * με την εφαρμογή
     * 
     * @details
     * Δίνει την επιλογή στον χρήστη να μοιραστεί με άλλους πληροφορίες σχετικά
     * με την εφαρμογή. Το με ποιό τρόπο θα το μοιραστεί ειναι καθαρά θέμα τι εφαρμογές
     * για διαμοιρασμό εχει στο smartphone εγκατεστημένες (gmail, facebook, tweeter, sms κλπ)
     */
    public void share () {
    	Intent share = new Intent(Intent.ACTION_SEND);
    	String shareBody = "Taxi Anytime";
    	share.setType("text/plain");
    	share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
    	share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
    	startActivity(Intent.createChooser(share, "Send via"));
    	
    }
    
    /**
     * @info
     * Πληροφορίες σχετικά με την εφαρμογή.
     * 
     * @details
     * Πληροφορίες σχετικά με την εφαρμογή.
     */
    public void aboutTheApp() {
    	
    	Intent intent = new Intent(this.getApplicationContext(), About.class);
    	startActivity(intent);

    }
    
    
    /**
     * @info
     * Αλλαγή προφίλ
     * 
     * @details
     * Μεταβαίνουμε στην αντίστοιχη δραστηριότητα για την αλλαγή προφίλ
     */
    public void changeProfile(){
    	
    	Intent intent = new Intent(this.getApplicationContext(), EditProfileActivity.class);
    	startActivity(intent);
    }
//============================================================


  
  
}//end class