package app.taxiAnytimeDriver.Common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;



/**
 * 
 * @info
 *Κλάση υπεύθυνη για να ελέγχει τη διαθεσιμότητα των 
 * δυο server που απαιτούνται για να λειτουργήσει η εφαρμογή
 * καθώς και για το αν είναι συνδεδεμένος ο χρήστης σε wifi/3g/gps
 */
public class ConnectionDetector  extends AsyncTask<String, Void , HashMap<String, Boolean> > {
	
	
	public Activity activityContext;
	private ProgressDialog pDialog;
	private int timeOut = 2000 ; //ms
	private int HTTP_GOOD_REQUEST = 200;
	private HashMap<String, Boolean> conState = new HashMap<String, Boolean>();

    public ConnectionDetector(Activity ActCon) {
    	activityContext = ActCon;
   
	}
    

    
    /**
		 * @info
		 * Πριν ξεκινήσει η διαδικασία ελέγχου  εμφανίζεται μήνυμα στην οθόνη χρήστη
		 * 
		 * @details
		 * Πριν ξεκινήσει η διαδικασία ελέγχου   εμφανίζεται μήνυμα στην οθόνη χρήστη
		 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(activityContext);
		pDialog.setMessage("Περιμένετε..");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}



/**
	 * @return HashMap με τις καταστάσεις σύνδεσης
	 * @info
	 * Εδώ γίνεται ο έλεγχος για τη σύνδεση
	 * @details
	 * 
	 * 
	 * */
	@Override
	protected HashMap<String, Boolean> doInBackground(String... args) {
			
		try {
			conState.put("serverState", ServersAreReachable(activityContext) );
			conState.put("serviceState", isConnectingToInternet(activityContext) );
		
			if( ServersAreReachable(activityContext) == true && 
				isConnectingToInternet(activityContext) == true){
				conState.put("connectionState", true) ;
			}
			else{
				conState.put("connectionState",false) ;
			}	
		}
		
		catch(Exception e){
			conState.put("serverState", false );
			conState.put("serviceState", false );
			conState.put("connectionState",false) ;
			
			e.printStackTrace();
		}

		return conState;
	}

	/**
		 * @info
		 *  Αφού ολοκληρωθεί η διαδικασία, αποδεσμεύουμε το dialog και η εκτέλεση
		 * συνεχίζεται στο κύριο νήμα
		 * 
		 * @details
		 * Αν τελικώς έχουμε σύνδεση ξεκινά η επόμενη δραστηριότητα,
		 * διαφορετικά εμφανίζεται ανάλογο μύνημα και τερματίζει η εφαρμογή.
		 * 
		 * **/
	@Override
	protected void onPostExecute(HashMap<String, Boolean> result) {
		// dismiss the dialog once done
		pDialog.dismiss();
		pDialog = null;

		//Log.d("servicestate - serverstate-con",String.valueOf(serviceState)+"-"+String.valueOf(serverState)+"-"+String.valueOf(connectionState));
		
		if(result.get("serviceState") == false) {
			new showAlertMessage(activityContext ,"Ωχ!Αδυναμία σύνδεσης!!!","Ανοίξτε το GPS ή/και to wifi/3g");
		}
		else if(result.get("serverState") == false){
			new showAlertMessage(activityContext ,"Ωχ!Αδυναμία σύνδεσης!!!","Αδύναμία επικοινωνίας με τους servers! Δοκιμάστε αργότερα.");
		}
		
	}
//=====================================================

/**
 * @info
 * Μέθοδος για τον έλεγχο διαθεσιμότητας του server
 * 
 * @details
 * Αρχικά προσπαθεί να κάνει ένα request στην ip του server,
 * θέτοντας ένα timeout 3 sec.
 * Αν είναι σε λειτουργία ο server θα πάρουμε ένα θετικό response (HTTP 200) ,
 * διαφορετικά δεν υπάρχει σύνδεση 
 *
 * @return
 * true  : Όταν υπάρχει σύνδεση
 * false : Όταν δεν είναι εφικτή η σύνδεση στον server
 * 
 */
private  boolean ServersAreReachable(Activity activContext) {
       
	
        final ConnectivityManager connMgr = (ConnectivityManager) activContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            //  Some sort of connection is open, check if server is reachable
            try {
                URL url = new URL("http://"+globalVariables.getInstance().getIP_ADDRESS());
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
              
                urlc.setConnectTimeout(timeOut); 
                urlc.connect();
                if (urlc.getResponseCode() == HTTP_GOOD_REQUEST) { 
                    return true;
                } else { // Anything else is unwanted
                    return false;
                }
            } catch (IOException e) {
            	
            }
        } 
        
            return true;  
    }
	
//===============================================================================
	/**
	 * @info
	 * Γίνεται έλεγχος για το αν υπάρχει σύνδεση της συσκευής μας με το internet(wifi ή 3G),
	 * καθώς και αν το gps είναι ανοικτό.
	 * 
	 * @details
	 * Χρησιμοποιούμε τις ενσωματωμένες κλάσεις του android (LocationManager,ConnectivityManager),
	 * για να ελέγξουμε αν υπάρχει ενεργή συνδεσιμότητα
	 * 
	 * @return
	 * true  : Αν είναι ανοικτό το GPS και έχουμε πρόσβαση στο internet
	 * false : Αν δεν ισχύει έστω και ένα από τα παραπάνω
	 */
	private boolean isConnectingToInternet(Activity activContext)
	{
		 
		try {
			LocationManager GpsSservice = (LocationManager)activContext.getSystemService(Context.LOCATION_SERVICE);
			ConnectivityManager connManager =(ConnectivityManager)activContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			boolean GpsEnabled = GpsSservice.isProviderEnabled(LocationManager.GPS_PROVIDER);
			NetworkInfo info = connManager.getActiveNetworkInfo();
			
			
			if( !GpsEnabled) {
				
				return false;
			}
			else if(info.getState() != NetworkInfo.State.CONNECTED ) {  
				
				return false;
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	



}
