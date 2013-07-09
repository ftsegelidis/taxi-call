package app.taxiAnytimeDriver.Common;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.taxiAnytimeDriver.R;




public class EditProfileActivity extends Activity {

	private EditText txtCellphone,
    txtPassword,txtRepatPassword,txtCarPlate,txtTown,txtImageUrl;
	
	protected SharedPreferences prefs ;
    private static final String NAME_PATTERN      = "^[a-zA-Zα-ωΑ-Ω]+$";
    private static final String CELLPHONE_PATTERN = "^[0-9]+$";
    
    private static final String BOOKMARK_TITLE = "Taxi Anytime";
	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
    

	
	  /**
	  * @info
	  * Αρχικοποίηση menu buttons
	  * 
	  * @details
	  * Αρχικοποίηση menu buttons
	  */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu_items, menu);
		return true;
	}
		  
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
		
	
		txtCellphone = (EditText)findViewById(R.id.txtCellphone);
		txtTown = (EditText)findViewById(R.id.txtTown);
		txtCarPlate = (EditText)findViewById(R.id.txtCarPlate);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		txtRepatPassword = (EditText)findViewById(R.id.txtRepatPassword);
		txtImageUrl = (EditText)findViewById(R.id.txtImageUrl);
		
		
		prefs = getSharedPreferences("fromDriver", MODE_PRIVATE);
		
	
		new fetchDataToEditTask( prefs.getString("driverDeviceId","") ).execute();
	
	}
	
	@Override
	public void onBackPressed() {
		
		finish();
		
		super.onBackPressed();
	}
	
//======================================================================
protected void jsonToTextEdits(JSONObject jo) throws JSONException{
	
	if(jo != null){
		
		txtCellphone.setText(jo.getJSONArray("editDetails").getJSONObject(0).get("cellphone").toString() );
		txtTown.setText(jo.getJSONArray("editDetails").getJSONObject(0).get("town").toString() );
		txtCarPlate.setText(jo.getJSONArray("editDetails").getJSONObject(0).get("taxiPlateNumber").toString() );
		txtImageUrl.setText(jo.getJSONArray("editDetails").getJSONObject(0).get("driverImageUrl").toString() );
	}
	
}
//==============================================================================
	
	/**
	 * @info
	 * Έλεγχος ορθότητας δεδομένων για αλλαγή στοιχείων στην εφαρμογή
	 * 
	 * @details
	 * Με χρήση κανονικών εκφράσεων (regex) γίνετε ελεγχος ορθότητας δεδομένων.
	 * 
	 * -To όνομα πρέπει να ειναι πάνω απο 2 χαρακτήσες και να αποτελείτε μονο απο γράμματα.
	 *  κανονική έκφραση: ^[a-zA-Z]+$
	 *  
     * -To επώνυμο πρέπει να ειναι πάνω απο 2 χαρακτήσες και να αποτελείτε μονο απο γράμματα.
	 *  κανονική έκφραση: ^[a-zA-Z]+$
	 *  
	 *  -Το κινητό πρέπει να ειναι τουλάχιστον απο 10 αριθμους. Μόνο αριθμούς.
	 *  κανονική έκφραση: ^[0-9]+$
	 *  
     * -Η πόλη πρέπει να ειναι πάνω απο 2 χαρακτήσες και να αποτελείτε μονο απο γράμματα .
	 *  κανονική έκφραση: ^[a-zA-Z]+$
	 *  
     * 
	 *  
	 *  -O κωδικός πρέπει να ειναι τουλάχιστον 6 ψηφία
	 *
	 *
	 * 
	 * @return
	 * true : έχει δώσει σωστά στοιχεία.
	 * false: δεν έχει δώσει σωστά στοιχεία.
	 */
	private boolean isValidFormToEdit() {

		
		if (! txtCellphone.getText().toString().matches(CELLPHONE_PATTERN) || txtCellphone.getText().length() < 10)  {
			txtCellphone.setError("Το κινητό πρέπει να αποτελείτε απο αριθμούς");
			return false;
		}
		if (! txtTown.getText().toString().matches(NAME_PATTERN) || txtTown.getText().length() < 3) {
			txtTown.setError("Δώστε μια έγκυρη πόλη");
			return false;
		}

		if (txtPassword.getText().length() < 6) {
			txtPassword.setError("Ο κωδικός πρέπει να είναι τουλάχιστον 6 χαρακτήρες");
			return false;	
		}

		if (! txtPassword.getText().toString().equals(txtRepatPassword.getText().toString()) ) {
			txtRepatPassword.setError("Δεν ειναι ίδιοι οι κωδικοί πρόσβασης)");
			return false;
		}

		return true;

	}
//================================================================================================
	/**
	 * @param fields  Array με τα πεδία edit
	 * @info
	 * Ελέγχει αν όλα τα editboxes είναι κενά
	 * @return
	 * true : Αν έστω και ένα πεδίο είναι άδειο
	 * false : Αν δεν είναι κανένα άδειο
	 */
	private boolean AreEditBoxesEmpty(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return true;
            }
        }
        return false;
}
	
	
//========================================================================
	/**
	 * @param view H όψη στην οποία βρίσκεται το κουμπί
	 * 
	 * @info
	 * Αρχίζει την διαδικασία του edit
	 * 
	 * @details
	 * Κάνει έλεγχο αν έχουμε gps και σύνδεση στo internet ανοικτά και αν ναι, ξεκινάει την
	 * διαδικασία της αλλαγής στοιχείων. Αλλιώς ενημερώνει την χρήστη
	 * 
	 */
	public void onClkEdit(View view) {
		
		
		
		if(isValidFormToEdit() == true && 
				AreEditBoxesEmpty(new EditText[]{txtCellphone,
			    txtPassword,txtRepatPassword,txtTown,txtCarPlate}) == false ) {
			
			
			try{
				
				if ( new ConnectionDetector(this).execute().get().get("connectionState") ){
		       		new makeEditTask().execute();
				}
		       	
			}
			catch(Exception e){
				e.printStackTrace();
				new showAlertMessage(this ,"Ωχ! Κάτι πήγε στραβά!!!","Δοκιμάστε αργότερα");
			}	

		}
		else{
			new showAlertMessage(this ,"Προσοχή!!!","Ελέγξτε όλα τα πεδία");
		}
	
}
	
//========================================================================
	 /**
	 * @param view H όψη στην οποία βρίσκεται το κουμπί
	 * 
	 * @info
	 * Καθαρίζει όλα τα πεδία της φόρμας.
	 * 
	 * @details
	 * Καθαρίζει όλα τα πεδία της φόρμας.
	 */
	public void onClkClearFields(View view){

			
			txtCellphone.setText("");
			txtTown.setText("");
			txtCarPlate.setText("");
			txtPassword.setText("");
			txtRepatPassword.setText("");
			txtImageUrl.setText("");
	 }
	
//========================================================================

	/**
	 * @info
	 * Κλάση υπεύθυνη για την διαδικασία της εγγραφής χρηστών
	 */	
protected class fetchDataToEditTask extends AsyncTask<String, Void, String> {
		
		private ArrayList<NameValuePair> parameters ;	
		private String deviceID;
		private ProgressDialog pDialog;
		protected JSONObject myjobg ; 
		
			
		public fetchDataToEditTask(String drvID){
					
			deviceID = drvID;
			}

				/**
		  		 * @info
		  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη
		  		 * 
		  		 * @details
		  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη
		  		 * */
				
		 @Override
	        protected void onPreExecute() {
			 	   pDialog = new ProgressDialog(EditProfileActivity.this);
			 	   pDialog.setIndeterminate(false);
			 	   pDialog.setMessage("Αλλαγή στοιχείων...");
			 	   pDialog.setCancelable(true);
			 	   pDialog.show();
	       
	        }

		  		/**
		  		 * @info
		  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και στέλνουμε τα δεδομένα
		  		 * του χρήστη για εγγραφή στον server.
		  		 * 
		  		 * @details
		  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και στέλνουμε τα δεδομένα
		  		 * του χρήστη για εγγραφή στον server.
		  		 * 
		  		 * */
				
		 	@Override
	        protected String doInBackground(String... urls) {
		 		
		 			parameters = new ArrayList<NameValuePair>();
		 		
					try {
						
						parameters.add(new BasicNameValuePair("deviceid",deviceID) );
		    			parameters.add(new BasicNameValuePair("usertype","driver") );
		    		
		    		
		    			myjobg = null ;
		    			httpJSONParser json = new httpJSONParser();    
						myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/fetchDataToEdit.php",parameters);
		   
						//Log.d("editdetails",myjobg.toString() );
						if(Integer.parseInt(myjobg.get("success").toString()) == 1) {
					
							//do something
						
							
						}
						
					} catch (Exception e) {
						
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
		  		 * συνεχίζεται στο κύριο νήμα και μεταβαίνουμε στην login δραστηριότητα.
		  		 * 
		  		 * **/
		 	@Override
				protected void onPostExecute(String file_url) {
					// dismiss the dialog once done
					
					pDialog.dismiss();
					pDialog = null;
					
					try {
						jsonToTextEdits ( myjobg );
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
					

				}
		}

//========================================================================
/**
 * @info
 * Κλάση υπεύθυνη για την διαδικασία της εγγραφής χρηστών
 */
protected class makeEditTask extends AsyncTask<String, Void, String> {

		private ArrayList<NameValuePair> parameters ;	
		private ProgressDialog pDialog;
		private JSONObject myjobg ;
		private httpJSONParser json ;
		private boolean check;
		
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
					pDialog = new ProgressDialog(EditProfileActivity.this);
					pDialog.setMessage("Αλλαγή στοιχείων...");
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(true);
					pDialog.show();
				}

				
		  		/**
		  		 * @info
		  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και στέλνουμε τα δεδομένα
		  		 * του χρήστη για εγγραφή στον server.
		  		 * 
		  		 * @details
		  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και στέλνουμε τα δεδομένα
		  		 * του χρήστη για εγγραφή στον server.
		  		 * 
		  		 * */
				@Override
				protected String doInBackground(String... args) {

				try {	
					parameters = new ArrayList<NameValuePair>();
					
		    		parameters.add(new BasicNameValuePair("cellphone",String.valueOf(txtCellphone.getText())));
		    		parameters.add(new BasicNameValuePair("password",String.valueOf(txtPassword.getText())));
		    		parameters.add(new BasicNameValuePair("town",String.valueOf(txtTown.getText())));
		    		parameters.add(new BasicNameValuePair("taxiplate",String.valueOf(txtCarPlate.getText())));
		    		parameters.add(new BasicNameValuePair("deviceid", prefs.getString("driverDeviceId","") ) );
		    		parameters.add(new BasicNameValuePair("imageUrl", String.valueOf(txtImageUrl.getText())));
		    		parameters.add(new BasicNameValuePair("usertype","driver") );
		 
					myjobg = null; 
					json = new httpJSONParser();
		    	    myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeEdit.php",parameters);
		
						if(Integer.parseInt(myjobg.get("success").toString()) == 1) {
					
							check = true;
	
						}
						else{
							check = false;
						}
						
					} catch (NumberFormatException e) {
						
						e.printStackTrace();
					} catch (JSONException e) {
						
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
		  		 * συνεχίζεται στο κύριο νήμα και μεταβαίνουμε στην login δραστηριότητα.
		  		 * 
		  		 * **/
				@Override
				protected void onPostExecute(String file_url) {
					pDialog.dismiss();
					pDialog = null;
					
					if(this.check == true){
						Toast toast = Toast.makeText(getApplicationContext(),"Ολοκληρώθηκαν οι αλλαγές!", Toast.LENGTH_LONG);
						toast.show();
					}
					else {
						Toast toast = Toast.makeText(getApplicationContext(),"Μη επιτυχής αλλαγή στοιχείων!!!", Toast.LENGTH_LONG);
						toast.show();
					}
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
    	Toast.makeText(this, "Είστε ήδη εδώ!!", Toast.LENGTH_SHORT).show();
        return true;
    case R.id.menu_aboutTheApp:
    	aboutTheApp();
        return true;
    default:
        return super.onOptionsItemSelected(item);
    }
    
}



/**
* @info
* Μεταβαίνουμε στην δραστηριότητα της αναφοράς
*/
public void report(){
	  
	  Intent intent = new Intent(getApplicationContext(),ReportActivity.class);
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
	
	  Intent intent = new Intent(getApplicationContext(),About.class);
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
