package app.taxiAnytimeCustomer.Common;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import app.taxiAnytimeCustomer.R;



/**
 * @info
 * ����� �������� ��� ��� ������� ������ ��� ��� ������.
 *
 */
public class ReportActivity extends Activity implements OnItemSelectedListener {
	
	 private Spinner spinner ;
     private EditText edtReason;
     private ImageButton imgBtnReport;

     private String RptReasons ;
     private String totalReasons;
     private ProgressDialog pDialog;
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
   * ������������ activity
   */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        
         spinner = (Spinner) findViewById(R.id.ReportReasons);
         edtReason=(EditText)findViewById(R.id.comment);
         imgBtnReport = (ImageButton)findViewById(R.id.imageButtonSubmitReport);
         imgBtnReport.setEnabled(true);
         edtReason.setText(" ");
        
       
     // Create an ArrayAdapter using the string array and a default spinner layout
         
     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
     R.array.reportReasons, android.R.layout.simple_spinner_item);
     // Specify the layout to use when the list of choices appears
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     // Apply the adapter to the spinner
     spinner.setAdapter(adapter);
     spinner.setOnItemSelectedListener(this);

    }
    
    
    /**
     * @param v �� View ��� �� button
     * @throws ExecutionException 
     * @throws InterruptedException 
     * 
     * @info
     * ����� ��� ������� ��� ������ ��� ��� ���������� ���� ����.
     * 
     * @details
     * ������ ����� ������ �� ������� ������� �� �� ������ ��� �� ������ �� �������� 
     * ���� server ��� ���������. 
     * �� ������� �������� �� ����� � ������� ��� �� ���������� ��� �� ���������� �����,
     * ��������� ���� �������
     * 
     */
    public void onClkReport(View v) throws InterruptedException, ExecutionException {
    	
    	if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ) {
      		 
      		 if(getRptReasons().equals("����,��������� ��������") && edtReason.getText().toString().equals(null)){
 
      			new showAlertMessage(this,"�������","������ �� ������ ��� ���� ��� ��������!!!") ;
      	      		
      		   }
      		 else{

      			 //�� ������ ����� �� ������� string ��������� �� ��� ���� �� "+" �������
      			 totalReasons = getRptReasons()+" "+edtReason.getText().toString();
      			new AlertDialog.Builder( this )
      			.setTitle( "����������� ��������" )
      			.setMessage( "����� �������� �� ���� : ' "+totalReasons +"'  ����� ������� ��� ������ �� ��������?")
      			.setOnCancelListener( new OnCancelListener() {

      				@Override
      				public void onCancel(DialogInterface dialog) {
      					
      				}
      			})
      			.setPositiveButton( "���", new DialogInterface.OnClickListener() {
		 			public void onClick(DialogInterface dialog, int which) {
		 				new CreateReportTask( totalReasons ).execute() ; 
		 
		 				}
      			})
		 		.setNegativeButton( "���", new DialogInterface.OnClickListener() {
		 		public void onClick(DialogInterface dialog, int which) {
		 			
		 			}
		 		} )
		 	.show();
    	
      		 imgBtnReport.setEnabled(false);
      	 }
      }
	
}

//========================================================================================================== 
    
    /**
     * @param v View ��� �� button
     * 
     * @info
     * (build-in)��������� ��� ��� ���������� ��� ��������� activity �� �� ������ ��� back (hardware button)
     */
    public void onClkBack(View v) {
    	
    	finish();
	  }

  //==========================================================================================================    
    /**
     * @info
     * ���������� ����� �������� ���� �������� ��� �� spinner � �������
     */
    public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {

    	setRptReasons(parent.getItemAtPosition(pos).toString());

    }

	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	public String getRptReasons() {
		return RptReasons;
	}

	public void setRptReasons(String rptReasons) {
		RptReasons = rptReasons;
	}

//=================================================================	


/** 
 * 
 * @info
 * �� ��� ����� ���� ������ � �������� report ��� ���� ���������
 */
	
protected class CreateReportTask extends AsyncTask<String, Void, String> {

	private ArrayList<NameValuePair> parameters =  new ArrayList<NameValuePair>() ;
	private String Repreasons;
	

public CreateReportTask(String repReason) {
	//  Auto-generated constructor stub
	
	this.Repreasons = repReason;

}
	

/**
	 * @info
	 * ���� ��������� � ���������� ��������� ����������� ������ ���� ����� ������
	 * 
	 * @details
	 * ���� ��������� � ���������� ��������� ����������� ������ ���� ����� ������
	 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(ReportActivity.this);
				pDialog.setMessage("���������� ��������..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			/**
	  		 * @info
	  		 * ������ � ���������� ��������� ����������� ������ ���� ����� ������
	  		 * 
	  		 * @details
	  		 * ������ � ���������� ��������� ����������� ������ ���� ����� ������.
	  		 * ��������� �������� ���� : ����� report , ��� ����� ����� ��� �� ���� ���������� ��� ���
	  		 * ���� ��� ��� ������������ ����������
	  		 * */
			@Override
			protected String doInBackground(String... args) {
				
			try {
				
				SharedPreferences orderPrefs = getSharedPreferences("myOrder", MODE_PRIVATE);
				SharedPreferences prefs = getSharedPreferences("fromCustomer", MODE_PRIVATE);
				
				parameters.add(new BasicNameValuePair("getReasons", this.Repreasons ));
				parameters.add(new BasicNameValuePair("orderid",String.valueOf(orderPrefs.getString("orderid", "")  ) ) );
				parameters.add(new BasicNameValuePair("reportfrom",prefs.getString("customerdevid","") ) );
				parameters.add(new BasicNameValuePair("reportTo",orderPrefs.getString("selectedDriverDevId", "") ) );
				parameters.add(new BasicNameValuePair("userType","customer" ) );
				// Building Parameters
			
				
					// getting JSON Object
					JSONObject myjobg = null; 
				    httpJSONParser json = new httpJSONParser();
			        myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeReport.php",parameters);

				// check for success tag
					if (Integer.parseInt(myjobg.get("success").toString()) == 1) {
						// successfully reported

					} else {
						// failed to make report

					}	
				}
				catch (JSONException e) {
					e.printStackTrace();

				}
				catch(Exception e)
				{
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
	  		 * ����������� ��� ����� ����.
	  		 * 
	  		 * **/
			@Override
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				pDialog.dismiss();
			}

		}	
//====================================================================================
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

	
  
}//end class






