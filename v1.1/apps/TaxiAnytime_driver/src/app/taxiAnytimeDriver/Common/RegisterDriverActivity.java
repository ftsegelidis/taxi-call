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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.taxiAnytimeDriver.R;
import app.taxiAnytimeDriver.userTypesFactory.Users;
import app.taxiAnytimeDriver.userTypesFactory.UsersFactory;

/**
 * @info
 * Κλάση υπεύθυνη για ότι έχει να κάνει με το register νέων χρηστών.
 *
 */
public class RegisterDriverActivity extends Activity {
	
    private EditText txtName,txtSirName,txtCellphone,txtBirthday,
                     txtTown,txtCarPlate,txtUsername,txtPassword,
                     txtRepatPassword,txtEmail;
  
    
    private static final String BOOKMARK_TITLE = "Taxi Anytime";
   	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
    private static final String NAME_PATTERN      = "^[a-zA-Zα-ωΑ-Ω]+$";
    private static final String CELLPHONE_PATTERN = "^[0-9]+$";
    private static final String USERNAME_PATTERN  = "^[a-zA-Z][[a-zA-Z][0-9]]*$";
    private static final String EMAIL_PATTERN     = "^[a-z0-9_\\+-]+(\\.[a-z0-9_\\+-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,4})$";

    
    
    
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
    
    
    
    
    
    
    /**
     * @info
     * Αρχικοποίηση activity
     * 
     * @details
     * Αντιστοιχούμε τις όψεις των xml αρχείων με αντικείμενα , ώστε να μπορούμε να τα διαχειριστούμε. 
     * 
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		
		txtName = (EditText)findViewById(R.id.txtName);
		txtSirName = (EditText)findViewById(R.id.txtSirName);
		txtCellphone = (EditText)findViewById(R.id.txtCellphone);
		txtBirthday = (EditText)findViewById(R.id.txtBirthday);
		txtTown = (EditText)findViewById(R.id.txtTown);
		txtCarPlate = (EditText)findViewById(R.id.txtCarPlate);
		txtUsername = (EditText)findViewById(R.id.txtUsername);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		txtRepatPassword = (EditText)findViewById(R.id.txtRepatPassword);
		txtEmail = (EditText)findViewById(R.id.txtEmail);

	}
	
	 /**
     * @info
     * Οταν πατήσει το hardware back (κουμπί συσκευής) τον επιστρέφει στο login
     * 
     * @details
     * Οταν πατήσει το hardware back (κουμπί συσκευής) τον επιστρέφει στο login 
     * 
     */
	@Override
	public void onBackPressed() {

		finish();
		Intent i = new Intent(getApplicationContext(),LoginActivity.class);
		startActivity(i);
		
		
		super.onBackPressed();
	}
	
	
	
	/**
	 * @info
	 * Έλεγχος ορθότητας δεδομένων για εγγραφή στην εφαρμογή
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
     * -To username πρέπει να ειναι πάνω απο 2 χαρακτήσες και να αρχίζει με γράμμα ή κάτω παύλα. 
	 *  κανονική έκφραση: ^[a-zA-Z][[a-zA-Z][0-9]]*$
	 *  
	 *  -O κωδικός πρέπει να ειναι τουλάχιστον 6 ψηφία
	 *  
	 *  -Το email πρεπει να ειναι τουλάχιστον 4 χαρακτήρες, να αρχίζει με γράμμα,αριθμό,_,+ ή -, πρέπει να υπάρχει
	 *  το @ και η . και μετά την . να έχουμε απο 2 έως 4 χαρακτήρες
	 *  κανονική έκφραση: ^[a-z0-9_\\+-]+(\\.[a-z0-9_\\+-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,4})$
	 * 
	 * @return
	 * true : έχει δώσει σωστά στοιχεία.
	 * false: δεν έχει δώσει σωστά στοιχεία.
	 */
	private boolean isValidFormToRegister() {
		
		
		if (! txtName.getText().toString().matches(NAME_PATTERN) || txtName.getText().length() < 3)    {
			txtName.setError("Το όνομα πρέπει να αποτελείται απο γράμματα");
			return false;
		}
			
		else if (! txtSirName.getText().toString().matches(NAME_PATTERN) || txtSirName.getText().length() < 3 ) {
			txtSirName.setError("Το επώνυμο πρέπει να αποτελείται απο γράμματα");
			return false;
		}
			
		else if (! txtCellphone.getText().toString().matches(CELLPHONE_PATTERN) || txtCellphone.getText().length() != 10)  {
			txtCellphone.setError("Το κινητό πρέπει να αποτελείται απο 10 αριθμούς");
			return false;
		}
		
		else if (! txtTown.getText().toString().matches(NAME_PATTERN) || txtTown.getText().length() < 3) {
			txtTown.setError("Δώστε μια έγκυρη πόλη");
			return false;
		}
			
		else if (! txtUsername.getText().toString().matches(USERNAME_PATTERN) || txtUsername.getText().length() < 3) {
			txtUsername.setError("Το username δεν είναι έγκυρο");
			
			Log.d("username?","here!!!");
			
			return false;
		}
		
		else if (txtPassword.getText().length() < 6) {
			txtPassword.setError("Ο κωδικός πρέπει να είναι τουλάχιστον 6 χαρακτήρες");
			return false;	
		}

		
		else if (!txtPassword.getText().toString().equals( txtRepatPassword.getText().toString() ) ) {
			txtRepatPassword.setError("Δεν ειναι ίδιοι οι κωδικοί πρόσβασης)");
			return false;
		}
		
		else if(! txtEmail.getText().toString().matches(EMAIL_PATTERN) || txtEmail.getText().length() < 4)  {
			txtEmail.setError("Δώστε ενα έγκυρο email");
			return false;
		}
		else {
			
			return true;
		}
		
	}
	
	/**
	 * @param view H όψη στην οποία βρίσκεται το κουμπί
	 * 
	 * @info
	 * Αρχίζει την διαδικασία του register
	 * 
	 * @details
	 * Κάνει έλεγχο αν έχουμε gps και σύνδεση στo internet ανοικτά και αν ναι, ξεκινάει την
	 * διαδικασία για το register. Αλλιώς ενημερώνει την χρήστη
	 * 
	 */
public void register(View view) {
		
		if(isValidFormToRegister() == true) {
			
		
			try{
				 if ( new ConnectionDetector(this).execute().get().get("connectionState") ){
		       		 new makeRegister().execute();
				 } 	 
			}
			catch(Exception e){
				e.printStackTrace();
				new showAlertMessage(this ,"Ωχ! Κάτι πήγε στραβά!!!","Δοκιμάστε αργότερα");
			}	
		}
		
}
	
//===============================================================================		
/**
	 * @param view H όψη στην οποία βρίσκεται το κουμπί
	 * 
	 * @info
	 * Καθαρίζει όλα τα πεδία της φόρμας.
	 * 
	 * @details
	 * Καθαρίζει όλα τα πεδία της φόρμας.
	 */
	 public void clearFields(View view){
		 
			txtName.setText("");
			txtSirName.setText("");
			txtCellphone.setText("");
			txtBirthday.setText("");
			txtTown.setText("");
			txtCarPlate.setText("");
			txtUsername.setText("");
			txtPassword.setText("");
			txtRepatPassword.setText("");
			txtEmail.setText("");
	 }
	 
//======================================================================================	 
	 
	 /**
		 * @info
		 * Κλάση υπεύθυνη για την διαδικασία της εγγραφής χρηστών
		 */
	 protected class makeRegister extends AsyncTask<String,Void, String> {
				
		private ArrayList<NameValuePair> parameters ;	
		private JSONObject myjobg ;
		private httpJSONParser json ;
		private boolean check;
		private ProgressDialog pDialog;
		
				public makeRegister() {
					//Auto-generated constructor stub
					
					parameters = new ArrayList<NameValuePair>();
					myjobg = null; 
					json = new httpJSONParser();
			
					
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
					super.onPreExecute();
					pDialog = new ProgressDialog(RegisterDriverActivity.this);
					pDialog.setMessage("Εγγραφή..");
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

					
					Users driver = UsersFactory.createUser("driver");
					
					
		    		parameters.add(new BasicNameValuePair("name",String.valueOf(txtName.getText())));
		    		parameters.add(new BasicNameValuePair("sirname",String.valueOf(txtSirName.getText())));
		    		parameters.add(new BasicNameValuePair("cellphone",String.valueOf(txtCellphone.getText())));
		    		parameters.add(new BasicNameValuePair("birthday",String.valueOf(txtBirthday.getText())));
		    		parameters.add(new BasicNameValuePair("username",String.valueOf(txtUsername.getText())));
		    		parameters.add(new BasicNameValuePair("password",String.valueOf(txtPassword.getText())));
		    		parameters.add(new BasicNameValuePair("town",String.valueOf(txtTown.getText())));
		    		parameters.add(new BasicNameValuePair("taxiplate",String.valueOf(txtCarPlate.getText())));
		    		parameters.add(new BasicNameValuePair("mail",String.valueOf(txtEmail.getText())));
		    		
		    		parameters.add(new BasicNameValuePair("deviceid","/"+driver.getDeviceID()) );
		    		parameters.add(new BasicNameValuePair("usertype","driver") );
		    		
		    
		    	    myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeRegister.php",parameters);
		   
		    		try {
						if(Integer.parseInt(myjobg.get("success").toString()) == 1) {
							
							//do something
							check = true;
						}
						else {
							check = false;
						}
					} catch (NumberFormatException e) {
						//  Auto-generated catch block
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
		  		 * συνεχίζεται στο κύριο νήμα και μεταβαίνουμε στην login δραστηριότητα.
		  		 * 
		  		 * **/
				@Override
				protected void onPostExecute(String file_url) {
					// dismiss the dialog once done
					pDialog.dismiss();
					pDialog = null;
					
					if(check == true){
						
						Toast toast = Toast.makeText(RegisterDriverActivity.this,"Επιτυχής Εγγραφή!!!", Toast.LENGTH_SHORT);
						toast.show();
						
						finish();
						Intent i = new Intent(getApplicationContext(),LoginActivity.class);
						startActivity(i);
						
					}
					else{
						new showAlertMessage(RegisterDriverActivity.this ,"Ωχ! Δώσατε λάθος στοιχεία ή υπάρχει ήδη!!","Δοκιμάστε αργότερα");
					}
				
				}

			}
	
//===================================================================================
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
	    	Toast.makeText(this, "Δεν μπορείτε τώρα!!", Toast.LENGTH_SHORT).show();
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
