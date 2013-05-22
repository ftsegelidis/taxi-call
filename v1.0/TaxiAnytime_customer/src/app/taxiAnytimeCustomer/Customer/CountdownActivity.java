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
 * � ����� ���� ����� �������� ��� ���� timer � ������ ����� ��������� ��� ��� �����
 * ��� �� ��������� � ������� ����������� ���� ����� ��� ��� ���� ��� ���� �������.
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
* ������������ activity
* 
* @details
* ������������� ��� ����� ��� xml ������� �� ����������� , ���� �� �������� �� �� ��������������.
* ��������� �� device id ��� ��� ������ SharedPreferences, ������� ��������� ���������������� ���
* time, ��� ��� ��������
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
   * @param seconds ���� ������������ �� ����� �� �������
   * 
   * @info
   * ������� ��� ����� ��� ��� ����� �� ��������� � �������
   * 
   * @details
   * ������� ��� ����� ��� ��� ����� �� ��������� � �������
   * 
   */
  public void setWaitTime(int seconds) { 
    waitTime = seconds;
    lblTimeLeft.setText(String.valueOf(waitTime) + "s");
  }
  
  /**
   * @info
   * ������� ��� ���������� �������
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
			new showAlertMessage(CountdownActivity.this ,"��! ���� ���� ������!!!","��������� ��������");
			
			
			e.printStackTrace();
		
		} 
    	  
      }
      
    }.start();

  }
  

/**
 * @param v �� View ��� �� button
 * 
 * @info
 * ��������� ��� timer ���� ������� �� ������ � �������.
 * 
 * @details
 * ��������� ��� timer ���� ������� �� ������ � �������.
 */
  
  
public void onImgBtnTerminateTimer(View v){
	
	countDownTimer.cancel();
	countDownTimer.onFinish();
	
}



/**
 * 
 * @info
 * ��������� ��� timer ���� ������� �� hardware ������ back � �������.
 * 
 * @details
 * ��������� ��� timer ���� ������� �� hardware ������ back � �������.
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
 * ��������� ������� ��� �� ���������� �� timer ���� ����� � ������.
 * 
 * @details
 * ��������� ������� ��� �� ���������� �� timer ���� ����� � ������.
 * �� ���� ��� ��������� �������� ����� ������ ��������� ����� ����.
 * 
 * @param result �� ���������� ��� �������� ��� �� asyntask
 */
protected void onForceStopTimer(boolean result){
	
	if(result == true) {
		  new AlertDialog.Builder(CountdownActivity.this )
			.setTitle( "����!" )
			.setMessage( "��� �������� ���������� ������," +
					" ������ �� ���� ������������?" )
			.setPositiveButton( "���", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
		
					countDownTimer .cancel();
					countDownTimer = null;
					startCounting();
				
	
				}
			})
			.setNeutralButton( "���������", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					//������� ��� ��������� ���� ��� �������� ������.
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
		  //������� ��� �� ��������� ��� listview ��� ����� ��
		  //����������� �� ���������� ��� ������� ����������

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
	 * ������� ���� ���� �� ������� ��������� ���������� ��� ��� ������������ ������ (��� ����� ���
	 * ���������� ��� �� device id)
	 */
	protected class checkOrdersTask extends AsyncTask<String, String, String> {

		private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		private JSONObject myjobg = null;  
		private httpJSONParser json = new httpJSONParser();
		private ProgressDialog pDialogCheck;
		
  		/**
  		 * @info
  		 * ���� ��������� � ���������� ��� �������  ����������� ������ ���� ����� ������.
  		 * 
  		 * @details
  		 * ���� ��������� � ���������� ��� �������  ����������� ������ ���� ����� ������.
  		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialogCheck = new ProgressDialog(CountdownActivity.this);
			pDialogCheck.setMessage("����������...");
			pDialogCheck.setIndeterminate(false);
			pDialogCheck.setCancelable(true);
			pDialogCheck.show();
		}

		
  		/**
  		 * @info
  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� �������� �� ����� �������
  		 * ������ ���������� ��� ��� ������������ ����������
  		 * 
  		 * @details
  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� �������� �� ����� �������
  		 * ������ ���������� ��� ��� ������������ ����������
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
  		 *  ���� ����������� � ����������, ������������� �� dialog ��� � ��������
  		 * ����������� ��� ����� ���� ��� �� �������� � �������.
  		 * 
  		 * @details
  		 * ���� ����������� � ����������, ������������� �� dialog ��� � ��������
  		 * ����������� ��� ����� ���� ��� �� �������� � �������. ������ �� ��������� ��� ���
  		 * ���� ������������ � ���������� ��� ������ �� ���� ������� ���������� ����� �������
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