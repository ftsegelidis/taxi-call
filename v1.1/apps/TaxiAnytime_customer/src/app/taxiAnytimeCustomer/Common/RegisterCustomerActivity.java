package app.taxiAnytimeCustomer.Common;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.taxiAnytimeCustomer.R;
import app.taxiAnytimeCustomer.userTypesFactory.Users;
import app.taxiAnytimeCustomer.userTypesFactory.UsersFactory;



/**
 * @info
 * ����� �������� ��� ��� ���� �� ����� �� �� register ���� �������.
 *
 */
public class RegisterCustomerActivity extends Activity {
	
    private EditText txtName,txtSirName,txtCellphone,txtBirthday,
                     txtUsername,txtPassword,txtRepatPassword,txtEmail,
                     txtTown;

    private static final String NAME_PATTERN      = "^[a-zA-Z�-��-�]+$";
    private static final String CELLPHONE_PATTERN = "^[0-9]+$";
    private static final String USERNAME_PATTERN  = "^[a-zA-Z][[a-zA-Z][0-9]]*$";
    private static final String EMAIL_PATTERN     = "^[a-z0-9_\\+-]+(\\.[a-z0-9_\\+-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,4})$";

    private final String BOOKMARK_TITLE = "Taxi Anytime";
	private final String BOOKMARK_URL ="www.taxianytime.hostzi.com";

	
	
	
	 /**
  * @info
  * ������������ menu buttons
  * 
  * @details
  * ������������ menu buttons
  */
 @Override
 public boolean onCreateOptionsMenu(Menu menu){
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
		txtUsername = (EditText)findViewById(R.id.txtUsername);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		txtRepatPassword = (EditText)findViewById(R.id.txtRepatPassword);
		txtEmail = (EditText)findViewById(R.id.txtEmail);

	}
	
    /**
     * @info
     * ���� ������� �� hardware back (������ ��������) ��� ���������� ��� login
     * 
     * @details
     * ���� ������� �� hardware back (������ ��������) ��� ���������� ��� login 
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
	 * ������� ��������� ��������� ��� ������� ���� ��������
	 * 
	 * @details
	 * �� ����� ��������� ��������� (regex) ������ ������� ��������� ���������.
	 * 
	 * -To ����� ������ �� ����� ���� ��� 2 ���������� ��� �� ���������� ���� ��� ��������.
	 *  �������� �������: ^[a-zA-Z]+$
	 *  
     * -To ������� ������ �� ����� ���� ��� 2 ���������� ��� �� ���������� ���� ��� ��������.
	 *  �������� �������: ^[a-zA-Z]+$
	 *  
	 *  -�� ������ ������ �� ����� ����������� ��� 10 ��������. ���� ��������.
	 *  �������� �������: ^[0-9]+$
	 *  
     * -� ���� ������ �� ����� ���� ��� 2 ���������� ��� �� ���������� ���� ��� �������� .
	 *  �������� �������: ^[a-zA-Z]+$
	 *  
     * -To username ������ �� ����� ���� ��� 2 ���������� ��� �� ������� �� ������ � ���� �����. 
	 *  �������� �������: ^[a-zA-Z][[a-zA-Z][0-9]]*$
	 *  
	 *  -O ������� ������ �� ����� ����������� 6 �����
	 *  
	 *  -�� email ������ �� ����� ����������� 4 ����������, �� ������� �� ������,������,_,+ � -, ������ �� �������
	 *  �� @ ��� � . ��� ���� ��� . �� ������ ��� 2 ��� 4 ����������
	 *  �������� �������: ^[a-z0-9_\\+-]+(\\.[a-z0-9_\\+-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,4})$
	 * 
	 * @return
	 * true : ���� ����� ����� ��������.
	 * false: ��� ���� ����� ����� ��������.
	 */
	private boolean isValidFormToRegister() {
		
		
		if (! txtName.getText().toString().matches(NAME_PATTERN) || txtName.getText().length() < 3)    {
			txtName.setError("�� ����� ������ �� ����������� ��� ��������");
			return false;
		}
			
		if (! txtSirName.getText().toString().matches(NAME_PATTERN) || txtSirName.getText().length() < 3 ) {
			txtSirName.setError("�� ������� ������ �� ����������� ��� ��������");
			return false;
			
			
		}
			
		if (! txtCellphone.getText().toString().matches(CELLPHONE_PATTERN) || txtCellphone.getText().length() < 10)  {
			txtCellphone.setError("�� ������ ������ �� ����������� ��� ��������");
			return false;
			
		}
		
		if (! txtTown.getText().toString().matches(NAME_PATTERN) || txtTown.getText().length() < 3) {
			txtTown.setError("����� ��� ������ ����");
			return false;
		}
			
		if (! txtUsername.getText().toString().matches(USERNAME_PATTERN) || txtUsername.getText().length() < 3) {
			txtUsername.setError("�� username ��� ����� ������");
			return false;
		}
		
		if (txtPassword.getText().length() < 6) {
			txtPassword.setError("� ������� ������ �� ����� ����������� 6 ����������");
			return false;	
		}

		
		if (! txtPassword.getText().toString().equals(txtRepatPassword.getText().toString()) ) {
			txtRepatPassword.setError("��� ����� ����� �� ������� ���������)");
			return false;
		}
		
		if(! txtEmail.getText().toString().matches(EMAIL_PATTERN) || txtEmail.getText().length() < 4)  {
			txtEmail.setError("����� ��� ������ email");
			return false;
		}
		
	
		return true;

		
	}
	

	/**
	 * @param view H ��� ���� ����� ��������� �� ������
	 * 
	 * @info
	 * ������� ��� ���������� ��� register
	 * 
	 * @details
	 * ����� ������ �� ������ gps ��� ������� ��o internet ������� ��� �� ���, �������� ���
	 * ���������� ��� �� register. ������ ���������� ��� ������
	 * 
	 */
	public void onClkRegister(View view) {
		
		if(isValidFormToRegister() == true) {
			
			try{
				 if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){
		       		 new makeRegisterTask().execute();
				 } 	 
			}
			catch(Exception e){
				e.printStackTrace();
				new showAlertMessage(RegisterCustomerActivity.this ,"��! ���� ���� ������!!!","��������� ��������");
			}	
		}
		
	}
	

	 /**
	 * @param view H ��� ���� ����� ��������� �� ������
	 * 
	 * @info
	 * ��������� ��� �� ����� ��� ������.
	 * 
	 * @details
	 * ��������� ��� �� ����� ��� ������.
	 */
	public void onClkClearFields(View view){
		 
			txtName.setText("");
			txtSirName.setText("");
			txtCellphone.setText("");
			txtBirthday.setText("");
			txtTown.setText("");
			txtUsername.setText("");
			txtPassword.setText("");
			txtRepatPassword.setText("");
			txtEmail.setText("");
	 }
	 
	 
//======================================================================================
	/**
	 * @info
	 * ����� �������� ��� ��� ���������� ��� �������� �������
	 */
	
	protected class makeRegisterTask extends AsyncTask<String, Void, String> {
			
	
	private ArrayList<NameValuePair> parameters ;	
	protected ProgressDialog pDialog;
	private JSONObject myjobg ;
	private httpJSONParser json ;
	private boolean check;
	
			public makeRegisterTask() {
				//Auto-generated constructor stub
				
				parameters = new ArrayList<NameValuePair>();
				myjobg = null; 
				json = new httpJSONParser();
			}

		
			
			/**
	  		 * @info
	  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������
	  		 * 
	  		 * @details
	  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������
	  		 * */
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(RegisterCustomerActivity.this);
				pDialog.setMessage("�������..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			
	  		/**
	  		 * @info
	  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� ��������� �� ��������
	  		 * ��� ������ ��� ������� ���� server.
	  		 * 
	  		 * @details
	  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� ��������� �� ��������
	  		 * ��� ������ ��� ������� ���� server.
	  		 * 
	  		 * */
			@Override
			protected String doInBackground(String... args) {
				Users customer = UsersFactory.createUser("customer");
				
	    		parameters.add(new BasicNameValuePair("name",String.valueOf(txtName.getText())));
	    		parameters.add(new BasicNameValuePair("sirname",String.valueOf(txtSirName.getText())));
	    		parameters.add(new BasicNameValuePair("cellphone",String.valueOf(txtCellphone.getText())));
	    		parameters.add(new BasicNameValuePair("birthday",String.valueOf(txtBirthday.getText())));
	    		parameters.add(new BasicNameValuePair("username",String.valueOf(txtUsername.getText())));
	    		parameters.add(new BasicNameValuePair("password",String.valueOf(txtPassword.getText())));
	    		parameters.add(new BasicNameValuePair("town",String.valueOf(txtTown.getText())));
	    		parameters.add(new BasicNameValuePair("mail",String.valueOf(txtEmail.getText())));
	    		
	    		parameters.add(new BasicNameValuePair("deviceid","/"+customer.getDeviceID()) );
	    		parameters.add(new BasicNameValuePair("usertype","customer") );
	    		
	
	    	    
	   
	    		try {
	    			
	    			myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeRegister.php",parameters);
	    			
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
	  		 *  ���� ����������� � ����������, ������������� �� dialog ��� � ��������
	  		 * ����������� ��� ����� ����
	  		 * 
	  		 * @details
	  		 * ���� ����������� � ����������, ������������� �� dialog ��� � ��������
	  		 * ����������� ��� ����� ���� ��� ������������ ���� login �������������.
	  		 * 
	  		 * **/
			@Override
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				
				pDialog.dismiss();
				pDialog = null;
				
				if(check == true){
					
					Toast toast = Toast.makeText(RegisterCustomerActivity.this,"�������� �������!!!", Toast.LENGTH_SHORT);
					toast.show();
					
					finish();
					Intent i = new Intent(getApplicationContext(),LoginActivity.class);
					startActivity(i);	
				}
				else{
					new showAlertMessage(RegisterCustomerActivity.this ,"��! ������ ����� �������� � ������� ���!!","��������� ��������");
				}
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
    	Toast.makeText(this, "��� �������� ����!!", Toast.LENGTH_SHORT).show();
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
