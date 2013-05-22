package app.taxiAnytimeCustomer.Customer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.taxiAnytimeCustomer.R;
import app.taxiAnytimeCustomer.Common.About;
import app.taxiAnytimeCustomer.Common.Contact;
import app.taxiAnytimeCustomer.Common.ConnectionDetectorTask;
import app.taxiAnytimeCustomer.Common.EditProfileActivity;
import app.taxiAnytimeCustomer.Common.ReportActivity;
import app.taxiAnytimeCustomer.Common.globalVariables;
import app.taxiAnytimeCustomer.Common.httpJSONParser;



/**
 * @info
 * Δραστηριότητα για την εκκίνηση και τερματισμό της διαδρομής
 *
 */
public class CustomerStartEndRide extends Activity{

	
	private static final String BOOKMARK_TITLE = "Taxi Anytime";
	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
	protected ImageButton btnStart;
	protected ImageButton btnEnd;
	private Bundle extras;
	
	 /**
     * @info
     * Αρχικοποίηση activity
     * 
     * @details
     * Αντιστοιχούμε τις όψεις του xml αρχείου σε αντικείμενα , ώστε να μπορούμε να τo διαχειριστούμε.
     * Εδώ ελέγχουμε αν υπάρχει εισερχόμενη ειδοποίηση από το service (για το αν έχει φτάσει ο οδηγός).
     * Εφόσον υπάρχει ενεργοποιείται το κουμπί για να μπορέσουμε να αρχίσουμε τη διαδρομή, διαφορετικά 
     * αναμένουμε
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//  Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_end_ride);
		
		btnStart = (ImageButton) findViewById(R.id.imageButtonStartRide);
		btnEnd = (ImageButton) findViewById(R.id.imageButtonEndRide);
		
	
		
		btnEnd.setEnabled(false);
		btnStart.setEnabled(false);
		

			//Αν δηλαδή λάβαμε ειδοποίηση ενεργοποιούμε τα widgets
				extras = getIntent().getExtras();
				if(extras != null ){
					if ( extras.getInt("driverHasArrived") == 1 ){
						btnStart.setEnabled(true);
					}
					
				}
				

	}
	
	
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
	     * @param v το View για το button
	     * 
	     * @throws ExecutionException 
	     * @throws InterruptedException
	     *  
	     * @info
	     * Πατώντας το κουμπί ξεκινά η διαδρομή (orderState = 3)
	     * 
	     * @details
	     * Αρχικά ελέγχουμε για το αν υπάρχει ενεργή σύνδεση στο internet και το gps αν είναι ανοικτό,
	     * έπειτα ανακτούμε από τις SharedPreferences τα στοιχεία της παραγγελίας που επιλέχθηκε καθώς και
	     * το id της συσκευής του πελάτη. Αυτά χρειάζονται παρακάτω στο AsynTask.
	     * 
	     */
	public void onClickStartRide(View v) throws InterruptedException, ExecutionException
	{
		
		if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){

			SharedPreferences preferencesOrders = getSharedPreferences("myOrder", MODE_PRIVATE);
			SharedPreferences preferencesCustomer = getSharedPreferences("fromCustomer", MODE_PRIVATE);

			new CustomerConfirmOrderTask( preferencesCustomer.getString("customerdevid", ""), preferencesOrders.getString("orderid", "")   ).execute();
		
			btnStart.setEnabled(false);
			btnEnd.setEnabled(true);
			
      	 }
 	 	
	
		
	}
	
	
	
	/**
	 * @param v το View για το button
	 * 
	 * @info
	 * Μεταβαίνουμε στην επόμενη δραστηριότητα (comments)
	 */
	public void onClickEndRide(View v)
	{
		
		finish();
		Intent intent = new Intent(getApplicationContext(), CommentsAndRating.class);
		startActivity(intent);

	}

//====================================================================================


 	/** 
     * @info 
     * Μέθοδος για την αποστολή επιβεβαίωσης του πελάτη 
     * 
     * @details 
     * Η κλάση "τρέχει " στο παρασκήνιο και επικοινωνεί με τη βάση δεδομένων για να θέσει
     * ότι η παραγγελία είναι αποδεκτή από τον πελάτη (orderState =3)
     * 
     * */
	protected class CustomerConfirmOrderTask extends AsyncTask<String, String, String> {

		private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		private ProgressDialog pDialog;	
		private String deviceid;
		private String orderid;
		
		public CustomerConfirmOrderTask(final String customerDeviceid,final String orderid) {
			//  Auto-generated constructor stub
			this.deviceid = customerDeviceid;
			this.orderid = orderid;
		}
		
		
		/**
  		 * @info
  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη.
  		 * 
  		 * @details
  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη.
  		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CustomerStartEndRide.this);
			pDialog.setMessage("Περιμένετε..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
  		 * @info
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * και θέτουμε την νέα κατάσταση παραγγελίας
  		 * 
  		 * @details
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * και θέτουμε την νέα κατάσταση παραγγελίας
  		 * 
  		 * */
		protected String doInBackground(String... args) {

			parameters.add(new BasicNameValuePair("customerid",this.deviceid));
			parameters.add(new BasicNameValuePair("orderid",this.orderid));
			parameters.add(new BasicNameValuePair("type","customer"));
		   
			try {
				// getting JSON Object
				JSONObject myjobg = null; 
				httpJSONParser json = new httpJSONParser();
				myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/orderConfirmation.php","POST",parameters);

				if (Integer.parseInt(myjobg.get("success").toString()) == 1) {
					
				}
			
			}
			catch (Exception e) {
				e.printStackTrace();

			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done

			pDialog.dismiss();
			pDialog = null;

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
	  public boolean onOptionsItemSelected(MenuItem item){
	  	// Single menu item is selected do something
	  	// Ex: launching new activity/screen or show alert message
	      switch (item.getItemId()){
	      
	      case R.id.menu_contact:{
	    	  	try {
					call();
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;	
	      }
	      case R.id.menu_report:{
	    	  report();
	      	
	      	return true;	
	      }
	      case R.id.menu_bookmark:{
	    	  saveBookmark(this,BOOKMARK_TITLE,BOOKMARK_URL);
	        return true;
	      }
	      case R.id.menu_share:{
	      		share();
	          return true;
	      }
	      case R.id.menu_profile:{
	    	  changeProfile();
	          return true;
	      }
	      case R.id.menu_aboutTheApp:{
	      	  aboutTheApp();
	          return true;
	      }
	      default:
	          return super.onOptionsItemSelected(item);
	      }
	      
	  }
	  
	  public void call() throws InterruptedException, ExecutionException{
		  Contact contact = new Contact(this);
		  contact.makeContact();
	  }
	  
	  public void report(){
		  Intent intent = new Intent(CustomerStartEndRide.this,ReportActivity.class);
		  startActivity(intent); 
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
	  	
	  	Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
	  	startActivity(intent);
	  }
		

	
	
	
	
}
