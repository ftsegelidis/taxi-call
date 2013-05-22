package app.taxiAnytimeCustomer.Customer;




import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.taxiAnytimeCustomer.R;
import app.taxiAnytimeCustomer.Common.ConnectionDetectorTask;
import app.taxiAnytimeCustomer.Common.globalVariables;
import app.taxiAnytimeCustomer.Common.httpJSONParser;
import app.taxiAnytimeCustomer.Common.showAlertMessage;
import app.taxiAnytimeCustomer.DriversList.ListViewActivity;



/**
 * @info
 * Η κλάση αυτή ειναι υπεύθυνη για έναν timer ο οποίος είναι υπεύθυνος για τον χρόνο
 * που θα περιμένει ο χρήστης ανταπόκριση στην κλήση του για ταξί απο τους οδηγούς.
 *
 */

public class CountdownActivity extends Activity {
  /** Properties **/


  protected Button btnStartTimer;
  protected TextView lblTimeLeft;

  protected int waitTime = 20;
  public boolean noResults ;
  protected CountDownTimer countDownTimer;
  protected Context context ;

  protected SharedPreferences myPrefs ;
  protected String customerid;

  
/**
* @info
* Αρχικοποίηση activity
* 
* @details
* Αντιστοιχούμε τις όψεις του xml αρχείου με αντικείμενα , ώστε να μπορούμε να τα διαχειριστούμε.
* Ανακτούμε το device id απο ένα αρχείο SharedPreferences, καλούμε συνάρτηση παραμετροποίησης του
* time, και τον ξεκινάμε
*   
*/
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.countdown);
   
    lblTimeLeft = (TextView) findViewById(R.id.txtWaitTime);
    
    context = this;
    
    SharedPreferences prefs = getSharedPreferences("fromCustomer", MODE_PRIVATE);
    customerid = prefs.getString("customerdevid",null);

    setWaitTime(waitTime); 
    startCounting();

	
	  
  }
  
  /** Methods **/
  
  /**
   * @param seconds Ποσα δευτερολεπτα θα ειναι σε αναμονη
   * 
   * @info
   * Δινουμε τον χρονο για τον οποιο θα περιμενει ο πελατης
   * 
   * @details
   * Δινουμε τον χρονο για τον οποιο θα περιμενει ο πελατης
   * 
   */
  public void setWaitTime(int seconds) { 
    waitTime = seconds;
    lblTimeLeft.setText(String.valueOf(waitTime) + "s");
  }
  
  /**
   * @info
   * Αρχιζει την αντιστροφη μετρηση
   */
  public void startCounting() {
	  
	  countDownTimer = new CountDownTimer(waitTime  * 1000, 1000) {
		  
      @Override
      public void onTick(long millisUntilFinished) {
    	  lblTimeLeft.setText(String.valueOf(millisUntilFinished / 1000) + "s");
      }
      
      @Override
      public void onFinish() {
    	  try {
			if ( new ConnectionDetectorTask(CountdownActivity.this).execute().get().get("connectionState") ){
				  new checkOrdersTask().execute();
				 
			  }
		} catch (Exception e) {
			new showAlertMessage(CountdownActivity.this ,"Ωχ! Κάτι πήγε στραβά!!!","Δοκιμάστε αργότερα");
			
			
			e.printStackTrace();
		
		} 
    	  
      }
      
    }.start();

  }
  

/**
 * @param v το View για το button
 * 
 * @info
 * Σταματάμε τον timer όταν πατήσει το κουμπί ο χρήστης.
 * 
 * @details
 * Σταματάμε τον timer όταν πατήσει το κουμπί ο χρήστης.
 */
  
  
public void onImgBtnTerminateTimer(View v){
	
	countDownTimer.cancel();
	countDownTimer.onFinish();
	
}



/**
 * 
 * @info
 * Σταματάμε τον timer όταν πατήσει το hardware κουμπί back ο χρήστης.
 * 
 * @details
 * Σταματάμε τον timer όταν πατήσει το hardware κουμπί back ο χρήστης.
 */

@Override
public void onBackPressed() {

	countDownTimer.cancel();
	finish();
	
	super.onBackPressed();
	}

//===========================================================================================
/**
 * @info
 * Βοηθητική μέθοδος για να τερματίσει το timer πριν λήξει ο χρόνος.
 * 
 * @details
 * Βοηθητική μέθοδος για να τερματίσει το timer πριν λήξει ο χρόνος.
 * Σε αυτή την περίπτωση βλέπουμε πόσοι οδηγοί απάντησαν μέχρι τώρα.
 * 
 * @param result το αποτέλεσμα που λαμβάνει από το asyntask
 */
protected void onForceStopTimer(boolean result){
	
	if(result == true) {
		  new AlertDialog.Builder(CountdownActivity.this )
			.setTitle( "Ουπς!" )
			.setMessage( "Δεν υπάρχουν διαθέσιμοι οδηγοί," +
					" Θελετε να ξανα προσπαθήσετε?" )
			.setPositiveButton( "Ναι", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
		
					countDownTimer .cancel();
					countDownTimer = null;
					startCounting();
				
	
				}
			})
			.setNeutralButton( "Επιστροφή", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					//κωδικας για επιστροφη εκει που διαλεγει σημειο.
					countDownTimer .cancel();
					countDownTimer = null;
					
					finish();
					Intent intent = new Intent(CountdownActivity.this,CustomerActivity.class);
					startActivity(intent);
					
					
				}
			}).show();
	  }
	  else { //if(noResults == false)
	  {
		  //κωδικας για να μεταβουμε στο listview στο οποιο θα
		  //εμφανιστουν οι ταξιτζιδες που εδειξαν ενδιαφερον

		  finish();
		  Intent toListview = new Intent(CountdownActivity.this,ListViewActivity.class);
		  startActivity(toListview );
	  
	  }
	 }
}
  
//===========================================================================================
	
	/**
	 * Background Async Task to check for orders
	 * */



	/**
	 * @info
	 * Ελέγχει στην βάση αν υπάρχει διαθέσιμη παραγγελία απο τον συγκεκριμένο πελάτη (εδώ είναι που
	 * χρειάζεται και το device id)
	 */
	protected class checkOrdersTask extends AsyncTask<String, String, String> {

		private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		private JSONObject myjobg = null;  
		private httpJSONParser json = new httpJSONParser();
		private ProgressDialog pDialogCheck;
		
  		/**
  		 * @info
  		 * Πριν ξεκινήσει η διαδικασία του ελέγχου  εμφανίζεται μήνυμα στην οθόνη χρήστη.
  		 * 
  		 * @details
  		 * Πριν ξεκινήσει η διαδικασία του ελέγχου  εμφανίζεται μήνυμα στην οθόνη χρήστη.
  		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialogCheck = new ProgressDialog(CountdownActivity.this);
			pDialogCheck.setMessage("Περιμένετε...");
			pDialogCheck.setIndeterminate(false);
			pDialogCheck.setCancelable(true);
			pDialogCheck.show();
		}

		
  		/**
  		 * @info
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και βλέπουμε αν έχουν δηλώσει
  		 * οδηγοί ενδιαφέρον για την συγκεκριμένη παραγγελία
  		 * 
  		 * @details
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και βλέπουμε αν έχουν δηλώσει
  		 * οδηγοί ενδιαφέρον για την συγκεκριμένη παραγγελία
  		 * 
  		 * */
		
		protected String doInBackground(String... args) {

			try {
				
					parameters.add(new BasicNameValuePair("customerdeviceid",customerid));
					myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/checkforOrders.php","POST",parameters);
		
					if(Integer.parseInt(myjobg.get("success").toString()) == 1){
						noResults = false;	
					}
					else{
						noResults = true;
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
  		 * συνεχίζεται στο κύριο νήμα εάν το επιθυμεί ο χρήστης.
  		 * 
  		 * @details
  		 * Αφού ολοκληρωθεί η διαδικασία, αποδεσμεύουμε το dialog και η εκτέλεση
  		 * συνεχίζεται στο κύριο νήμα εάν το επιθυμεί ο χρήστης. Επίσης σε περίπτωση που δέν
  		 * έχει αποτελέσματα η παραγγελία του μπορεί να ξανά στείλει ειδοποίηση στους οδηγούς
  		 * 
  		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			
			//this.parameters.remove(parameters);
			pDialogCheck.dismiss();
			pDialogCheck = null;
			
			onForceStopTimer(noResults);
		}

	}

//========================================================

}