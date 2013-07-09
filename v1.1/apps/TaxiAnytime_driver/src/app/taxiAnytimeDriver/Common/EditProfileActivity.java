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
    private static final String NAME_PATTERN      = "^[a-zA-Z�-��-�]+$";
    private static final String CELLPHONE_PATTERN = "^[0-9]+$";
    
    private static final String BOOKMARK_TITLE = "Taxi Anytime";
	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
    

	
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
	 * ������� ��������� ��������� ��� ������ ��������� ���� ��������
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
     * 
	 *  
	 *  -O ������� ������ �� ����� ����������� 6 �����
	 *
	 *
	 * 
	 * @return
	 * true : ���� ����� ����� ��������.
	 * false: ��� ���� ����� ����� ��������.
	 */
	private boolean isValidFormToEdit() {

		
		if (! txtCellphone.getText().toString().matches(CELLPHONE_PATTERN) || txtCellphone.getText().length() < 10)  {
			txtCellphone.setError("�� ������ ������ �� ���������� ��� ��������");
			return false;
		}
		if (! txtTown.getText().toString().matches(NAME_PATTERN) || txtTown.getText().length() < 3) {
			txtTown.setError("����� ��� ������ ����");
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

		return true;

	}
//================================================================================================
	/**
	 * @param fields  Array �� �� ����� edit
	 * @info
	 * ������� �� ��� �� editboxes ����� ����
	 * @return
	 * true : �� ���� ��� ��� ����� ����� �����
	 * false : �� ��� ����� ������ �����
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
	 * @param view H ��� ���� ����� ��������� �� ������
	 * 
	 * @info
	 * ������� ��� ���������� ��� edit
	 * 
	 * @details
	 * ����� ������ �� ������ gps ��� ������� ��o internet ������� ��� �� ���, �������� ���
	 * ���������� ��� ������� ���������. ������ ���������� ��� ������
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
				new showAlertMessage(this ,"��! ���� ���� ������!!!","��������� ��������");
			}	

		}
		else{
			new showAlertMessage(this ,"�������!!!","������� ��� �� �����");
		}
	
}
	
//========================================================================
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
	 * ����� �������� ��� ��� ���������� ��� �������� �������
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
		  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������
		  		 * 
		  		 * @details
		  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������
		  		 * */
				
		 @Override
	        protected void onPreExecute() {
			 	   pDialog = new ProgressDialog(EditProfileActivity.this);
			 	   pDialog.setIndeterminate(false);
			 	   pDialog.setMessage("������ ���������...");
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
 * ����� �������� ��� ��� ���������� ��� �������� �������
 */
protected class makeEditTask extends AsyncTask<String, Void, String> {

		private ArrayList<NameValuePair> parameters ;	
		private ProgressDialog pDialog;
		private JSONObject myjobg ;
		private httpJSONParser json ;
		private boolean check;
		
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
					pDialog = new ProgressDialog(EditProfileActivity.this);
					pDialog.setMessage("������ ���������...");
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
					pDialog.dismiss();
					pDialog = null;
					
					if(this.check == true){
						Toast toast = Toast.makeText(getApplicationContext(),"������������� �� �������!", Toast.LENGTH_LONG);
						toast.show();
					}
					else {
						Toast toast = Toast.makeText(getApplicationContext(),"�� �������� ������ ���������!!!", Toast.LENGTH_LONG);
						toast.show();
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
public boolean onOptionsItemSelected(MenuItem item)
{
	// Single menu item is selected do something
	// Ex: launching new activity/screen or show alert message
    switch (item.getItemId())
    {
   
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
    	Toast.makeText(this, "����� ��� ���!!", Toast.LENGTH_SHORT).show();
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
	
	  Intent intent = new Intent(getApplicationContext(),About.class);
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
