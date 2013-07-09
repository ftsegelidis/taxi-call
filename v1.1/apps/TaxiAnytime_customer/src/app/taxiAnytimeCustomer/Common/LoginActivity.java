package app.taxiAnytimeCustomer.Common;



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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import app.taxiAnytimeCustomer.R;
import app.taxiAnytimeCustomer.Customer.CustomerActivity;
import app.taxiAnytimeCustomer.userTypesFactory.Users;
import app.taxiAnytimeCustomer.userTypesFactory.UsersFactory;






/**
 * @info
 * Είσοδος Χρήστη
 * Ελέγχεται η είσοδος του χρήστη με σύστημα login, 
 * και αν είναι επιτυχής συνεχίζουμε στην εφαρμογή.  
 * 
 */
public class LoginActivity extends Activity {

    private EditText txtUsername,txtPassword;
    private CheckBox saveLoginCheckBox;
    private static final String BOOKMARK_TITLE = "Taxi Anytime";
   	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";


    private SharedPreferences loginPreferences;
    //Ο editor ειναι για να μπορουμε να τροποποιησουμε τα δεδομενα που εχουμε αποθηκευμενα.
    private SharedPreferences.Editor loginPrefsEditor;
    public static final String PREFERENCE_FILENAME = "LoginInfo";
    
    private Boolean saveLogin;
    private String username,password;

   

	
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
     * Έπειτα χρησιμοποιούμε τον εσωτερικό μηχανισμό του android (SharedPreferences) για την ,
     * αποθήκευση στοιχείων(login) στο αρχείο ώστε να είναι προσβάσιμο στην εφαρμογή. 
     * 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        txtUsername = (EditText)findViewById(R.id.editTextUsername);
        txtPassword = (EditText)findViewById(R.id.editTextPassword);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);

        
        
        loginPreferences = getSharedPreferences(PREFERENCE_FILENAME, MODE_PRIVATE);
        //Βαζουμε τον editor να μπορει να βλεπει για να κανει επεξεργασια -το συγκεκριμενο- preference
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtUsername.setText(loginPreferences.getString("username", ""));
            txtPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }

    
    
    /**
     * @param view H όψη στην οποία βρίσκεται το κουμπί
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * 
     * @info
     * Γίνεται η ορθή διαδικασία εισαγωγής χρήστη στην εφαρμογή
     * 
     * @details
     * Οταν πατησει το κουμπι login ο χρηστης γινετε ελεγχος των στοιχειων που εδωσε
     * και αν αυτα ειναι σωστα τον βαζει στην εφαρμογη. Σε διαφορετικη περιπτωση
     * εμφανιζει μηνυμα λαθους.Επίσης γίνεται έλεγχος για τον αν υπάρχει σύνδεση της συσκευής
     * τόσο με το internet όσο και με τον server πριν γίνει οτιδήποτε
     * 
     */
    public void onClkLogin(View view) throws InterruptedException, ExecutionException  {
    		
            username = txtUsername.getText().toString();
            password = txtPassword.getText().toString();
            
           if( !fieldsAreEmpty(username,password) ){
 
            try{
            	 if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){
            		 new checkLoginTask(username,password).execute();
   			  	 }
            		
            }
            catch(Exception e){
            	new showAlertMessage(this ,"Ωχ! Κάτι πήγε στραβά!!!","Δοκιμάστε αργότερα");
            	e.printStackTrace();
            }

         }  
           
    }
    
    private boolean fieldsAreEmpty(String username,String password){
    	
    	if(username == null || password == null){
    		return true;
    	}
    	else{
    		return false;
    	}
    		
    
    	
    }
    

  //================================================================================

    /**
     * @param loginComplete μας δείχνει αν το Login έχει γίνει επιτυχώς
     * 
     * @info Ελέγχουμε αν θέλει να αποθηκευτεί το login και αν είναι σωστό γίνεται μετάβαση στην επόμενη activity
     * 
     * @details
     * Αν έχει ολοκληρωθεί το Login επιυχώς και ο χρήστης έχει επιλέξει την "remember me" επιλογή
     * γράφουμε τα στοιχεία του στο SharedPreferences αρχείο ώστε να τα τραβήξουμε την επόμενη
     * φορά που θα τρέξει η εφαρμογή
     */
    public void saveLoginData(boolean loginComplete){

    	if(loginComplete == true) {
		     
           	if (saveLoginCheckBox.isChecked()) {
           		loginPrefsEditor.putBoolean("saveLogin", true);
           		loginPrefsEditor.putString("username", username);
           		loginPrefsEditor.putString("password", password);
           		loginPrefsEditor.commit();
           	} 
           	else{
           		loginPrefsEditor.clear();
           		loginPrefsEditor.commit();
           	}
           	
           	finish();
           	Intent i = new Intent(getApplicationContext(),CustomerActivity.class);
   			startActivity(i);
            }
        else { //(loginComplete == false)
        	new showAlertMessage(this ,"Σφάλμα εισόδου!!!","Δώσατε λάθος στοιχεία ή είστε μπλοκαρισμένος!");
         }
        
    
    }
    
    
    
  //=================================================================================
    

    /**
     * @param view H όψη στην οποία βρίσκεται το κουμπί
     * 
     * @info
     * Γίνετε μετάβαση την δραστηριότητα του register
     * 
     * @details
     * Γίνετε μετάβαση την δραστηριότητα του register
     */
    public void onClkGoToRegister(View view){	
    	finish();
    	Intent i = new Intent(getApplicationContext(),RegisterCustomerActivity.class);
		startActivity(i);
    	
    }
//==================================================================
    /**
	 * @info
	 * Η κλάση αυτή είναι υπεύθυνη για τον έλεγχο ορθότητας των δεδομένων που 
	 * έδωσε ο χρήστης.
	 *
	 */
protected class checkLoginTask extends AsyncTask<String, Void, String> {
        
        private ProgressDialog Dialog;
  
		private String Username;
		private String Password;
		private boolean loginState;

        
        public checkLoginTask() {
        	Username = null;
			Password = null;
			loginState = false;

		}
        
        public checkLoginTask(String usrname,String pass) {
			// Auto-generated constructor stub
			Username = usrname;
			Password = pass;
					
		}
        
    	/**
  		 * @info
  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη
  		 * 
  		 * @details
  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη
  		 */
        @Override
        protected void onPreExecute() {
               Dialog = new ProgressDialog(LoginActivity.this);
               Dialog.setIndeterminate(false);
               Dialog.setMessage("Είσοδος...");
               Dialog.setCancelable(true);
               Dialog.show();
       
        }
        
        /**
  		 * @info
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * επιστρέφεται true ή false
  		 * 
  		 * @details
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * επιστρέφεται true ή false.
  		 * 
  		 * @return 
  		 * true : ο χρήστης έδωσε σωστά δεδομένα
  		 * false: ο χρήστης δεν έδωσε σωστά δεδομένα 
  		 */
        @Override
        protected String doInBackground(String... urls) {
        	
        	ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>(4);

        	Users customer = UsersFactory.createUser("customer");
        	
    		parameters.add(new BasicNameValuePair("username",Username ));
    		parameters.add(new BasicNameValuePair("password",Password ));
    		parameters.add(new BasicNameValuePair("deviceid","/"+customer.getDeviceID() ));
    		parameters.add(new BasicNameValuePair("type","customer"));
    	
        	JSONObject myjobg = null; 
    		httpJSONParser json = new httpJSONParser();
    	    
             try { 
            	myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeLogin.php",parameters );
       
            	
     				 if(Integer.parseInt(myjobg.get("success").toString() ) == 1) {
     					loginState = true;
     				}
     				else {
     					loginState = false;	
     				}
     			}
             	catch (Exception e) {
     				e.printStackTrace();
     				loginState = false;	
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
  		 * συνεχίζεται στο κύριο νήμα
  		 * 
  		 * **/
        @Override
        protected void onPostExecute(String result) {
          Dialog.dismiss();
          Dialog = null;
          saveLoginData(loginState);       
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
      	Toast.makeText(this, "Πρέπει να κάνετε login πρώτα!!", Toast.LENGTH_SHORT).show();
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
   * Μεταβαίνουμε στην δραστηριότητα της κλήσης/sms
   */
  public void call(){
  	  Intent intent = new Intent(getApplicationContext(),Contact.class);
  	  startActivity(intent); 
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