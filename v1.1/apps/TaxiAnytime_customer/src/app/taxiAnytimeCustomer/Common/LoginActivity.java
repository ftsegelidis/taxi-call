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
 * ������� ������
 * ��������� � ������� ��� ������ �� ������� login, 
 * ��� �� ����� �������� ����������� ���� ��������.  
 * 
 */
public class LoginActivity extends Activity {

    private EditText txtUsername,txtPassword;
    private CheckBox saveLoginCheckBox;
    private static final String BOOKMARK_TITLE = "Taxi Anytime";
   	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";


    private SharedPreferences loginPreferences;
    //� editor ����� ��� �� �������� �� �������������� �� �������� ��� ������ ������������.
    private SharedPreferences.Editor loginPrefsEditor;
    public static final String PREFERENCE_FILENAME = "LoginInfo";
    
    private Boolean saveLogin;
    private String username,password;

   

	
	  /**
	  * @info
	  * ������������ menu buttons
	  * 
	  * @details
	  * ������������ menu buttons
	  */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu_items, menu);
		return true;
	}
    
  
    /**
     * @info
     * ������������ activity
     * 
     * @details
     * ������������� ��� ����� ��� xml ������� �� ����������� , ���� �� �������� �� �� ��������������.
     * ������ �������������� ��� ��������� ��������� ��� android (SharedPreferences) ��� ��� ,
     * ���������� ���������(login) ��� ������ ���� �� ����� ���������� ���� ��������. 
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
        //������� ��� editor �� ������ �� ������ ��� �� ����� ����������� -�� ������������- preference
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtUsername.setText(loginPreferences.getString("username", ""));
            txtPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }

    
    
    /**
     * @param view H ��� ���� ����� ��������� �� ������
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * 
     * @info
     * ������� � ���� ���������� ��������� ������ ���� ��������
     * 
     * @details
     * ���� ������� �� ������ login � ������� ������ ������� ��� ��������� ��� �����
     * ��� �� ���� ����� ����� ��� ����� ���� ��������. �� ����������� ���������
     * ��������� ������ ������.������ ������� ������� ��� ��� �� ������� ������� ��� ��������
     * ���� �� �� internet ��� ��� �� ��� server ���� ����� ���������
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
            	new showAlertMessage(this ,"��! ���� ���� ������!!!","��������� ��������");
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
     * @param loginComplete ��� ������� �� �� Login ���� ����� ��������
     * 
     * @info ��������� �� ����� �� ����������� �� login ��� �� ����� ����� ������� �������� ���� ������� activity
     * 
     * @details
     * �� ���� ����������� �� Login ������� ��� � ������� ���� �������� ��� "remember me" �������
     * �������� �� �������� ��� ��� SharedPreferences ������ ���� �� �� ���������� ��� �������
     * ���� ��� �� ������ � ��������
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
        	new showAlertMessage(this ,"������ �������!!!","������ ����� �������� � ����� ��������������!");
         }
        
    
    }
    
    
    
  //=================================================================================
    

    /**
     * @param view H ��� ���� ����� ��������� �� ������
     * 
     * @info
     * ������ �������� ��� ������������� ��� register
     * 
     * @details
     * ������ �������� ��� ������������� ��� register
     */
    public void onClkGoToRegister(View view){	
    	finish();
    	Intent i = new Intent(getApplicationContext(),RegisterCustomerActivity.class);
		startActivity(i);
    	
    }
//==================================================================
    /**
	 * @info
	 * � ����� ���� ����� �������� ��� ��� ������ ��������� ��� ��������� ��� 
	 * ����� � �������.
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
  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������
  		 * 
  		 * @details
  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������
  		 */
        @Override
        protected void onPreExecute() {
               Dialog = new ProgressDialog(LoginActivity.this);
               Dialog.setIndeterminate(false);
               Dialog.setMessage("�������...");
               Dialog.setCancelable(true);
               Dialog.show();
       
        }
        
        /**
  		 * @info
  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� ��� 
  		 * ������������ true � false
  		 * 
  		 * @details
  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� ��� 
  		 * ������������ true � false.
  		 * 
  		 * @return 
  		 * true : � ������� ����� ����� ��������
  		 * false: � ������� ��� ����� ����� �������� 
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
  		 *  ���� ����������� � ����������, ������������� �� dialog ��� � ��������
  		 * ����������� ��� ����� ����
  		 * 
  		 * @details
  		 * ���� ����������� � ����������, ������������� �� dialog ��� � ��������
  		 * ����������� ��� ����� ����
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
  	 * @param item � ������� ��� ����� � ������� ��� �� �����
  	 * 
   * @info
   * ������� ������� �� �� hardware button menu
   * 
   * 
   * @details
   * ���� ������� ���� � ������� ��� ���� �������� �� ����� ������� ���� �����������
   * �� ������� ����� ����� ���� ���� ��� ���� ��������� � ����������.
   * ���� ��� ����������� bookmark, share & profile.
   * 
   * */
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
  	// Single menu item is selected do something
  	// Ex: launching new activity/screen or show alert message
      switch (item.getItemId()){
     
      case R.id.menu_contact:
      
      	Toast.makeText(this, "��� �������� ����!!", Toast.LENGTH_SHORT).show();
      	return true;
      case R.id.menu_report:
      	
      	Toast.makeText(this, "��� �������� ����!!", Toast.LENGTH_SHORT).show();
      	return true;	

      case R.id.menu_bookmark:
    	  saveBookmark(this,BOOKMARK_TITLE,BOOKMARK_URL);
          return true;
      case R.id.menu_share:
      	share();
          return true;
      case R.id.menu_profile:
      	Toast.makeText(this, "������ �� ������ login �����!!", Toast.LENGTH_SHORT).show();
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
   * ������������ ���� ������������� ��� ������/sms
   */
  public void call(){
  	  Intent intent = new Intent(getApplicationContext(),Contact.class);
  	  startActivity(intent); 
  }

  /**
  * @info
  * ������������ ���� ������������� ��� ��������
  */
  public void report(){
  	  
  	  Intent intent = new Intent(getApplicationContext(),ReportActivity.class);
  	  startActivity(intent); 
  }

  /**
   * @param c �� ����������� ��� ��������������.
   * @param title � ������ ��� ������������.
   * @param url �� site ��� ����� �� ����� ������������.
   * 
   * @info
   * ����� ������������ �� site ��� ���������.
   * 
   * @details
   * ����� ������������ �� site ��� ���������.
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
   * ����� ��� ������� ���� ������ �� ��������� �� ������ ����������� �������
   * �� ��� ��������
   * 
   * @details
   * ����� ��� ������� ���� ������ �� ��������� �� ������ ����������� �������
   * �� ��� ��������. �� �� ���� ����� �� �� ��������� ����� ������ ���� �� ���������
   * ��� ����������� ���� ��� smartphone �������������� (gmail, facebook, tweeter, sms ���)
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
   * ����������� ������� �� ��� ��������.
   * 
   * @details
   * ����������� ������� �� ��� ��������.
   */
  public void aboutTheApp() {
  	
	  Intent intent = new Intent(this.getApplicationContext(), About.class);
  	startActivity(intent);

  }


  /**
   * @info
   * ������ ������
   * 
   * @details
   * ������������ ���� ���������� ������������� ��� ��� ������ ������
   */
  public void changeProfile(){
  	
  	Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
  	startActivity(intent);
  }
    
    
}