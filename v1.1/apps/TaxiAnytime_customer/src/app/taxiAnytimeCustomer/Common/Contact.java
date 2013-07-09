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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 *  @info  
 *  ����� �������� ��� �� ���������� �������/sms �� ��������� ��� ���������
 * 
 */

public class Contact  {

	protected Activity actContext;
	private String mNumber;
	
	public Contact(Activity theContext) {
		actContext = theContext;
		this.mNumber = null;
	}
	
	

    /**
     * 
     * 
     * @throws ExecutionException 
     * @throws InterruptedException
     *  
     * @info
     * �������� �� ������ ����������� ������ �� �������� sms/�����
     * 
     * @details
     * ������ ���������� ��� ������ ��� ������ ��� ����������� , ������ ����������� ��� dialog box 
     * ��� �� �������� � ������� ��� ����� ������������ (�����/ sms)
     * 
     */
  
    public void makeContact() throws InterruptedException, ExecutionException {
    	
    	 if ( new ConnectionDetectorTask(actContext).execute().get().get("connectionState") ) {

       		 SharedPreferences pref = actContext.getSharedPreferences("myOrder", Context.MODE_PRIVATE);
       		 setmNumber( new GetCellphoneTask ("customer", 
       					pref.getString("selectedDriverDevId", "").toString() )
       					.execute().get() 
       					);
    
       		 final CharSequence[] items = {"�����", "SMS", "���������"};
       		 AlertDialog.Builder builder = new AlertDialog.Builder(actContext);

       		 builder.setTitle("�������� ����� ������������");
       		 builder.setItems(items, new DialogInterface.OnClickListener() {

       			 public void onClick(DialogInterface dialog, int item) {
       				 if (items[item].equals("�����")) {
       					actContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+getmNumber())));
    			  
       				 }
       				 else if (items[item].equals("SMS")) {
       					Intent smsIntent = new Intent(Intent.ACTION_VIEW);
       			        smsIntent.putExtra("sms_body", "Taxi anytime!"); 
       			        smsIntent.putExtra("address", getmNumber());
       			        smsIntent.setType("vnd.android-dir/mms-sms");

       			     actContext.startActivity(smsIntent);
       					 //startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms:"+getmNumber(), null)));   
       				 }
       				 else
       					 Toast.makeText(actContext, items[item], Toast.LENGTH_SHORT).show();
       			 }

       		 	});

       		 	AlertDialog alert = builder.create();

       		 	alert.show();
       		 	
       	 }//end if
	
  }
   

    public void setmNumber(String mNumber) {
    	this.mNumber = mNumber;
    }  
    
    public String getmNumber() {
    	return mNumber;
	
    }

  //===========================================================================================

  	/** 
     * @info 
     * ������� ��� ��� ���������� ��� ������� ��������� ��� ����������� ������/������ 
     * 
     * @details 
     * � ����� "������ " ��� ���������� ��� ����������� �� �� ���� ��������� ��� ��� ����������
     * ��� ������� ������� ���������
     */

  
 protected class GetCellphoneTask extends AsyncTask<String, String, String> {
	 
	 	private ArrayList<NameValuePair> parameters = new  ArrayList<NameValuePair>() ;
		private JSONObject myjobg = null;;
		private httpJSONParser json = new httpJSONParser();  
		protected ProgressDialog pDialog;
  		private String usrType;
  		private String driverDevId;
  		
  		public GetCellphoneTask () {

  			this.usrType = null;
  			this.driverDevId = null;
  
		}
  		public GetCellphoneTask (String userType,String drvDevId) {
  			//  Auto-generated constructor stub
  			this.usrType = userType;
  			this.driverDevId = drvDevId;
  		}
  		
  		/**
  		 * @info
  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������.
  		 * 
  		 * @details
  		 * ���� ��������� � ���������� �����������  ����������� ������ ���� ����� ������.
  		 * */
  		@Override
  		protected void onPreExecute() {
  			super.onPreExecute();
  			pDialog = new ProgressDialog(actContext);
  			pDialog.setMessage("Loading..");
  			pDialog.setIndeterminate(false);
  			pDialog.setCancelable(true);
  			pDialog.show();
  		}

  		/**
  		 * @info
  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� ��� 
  		 * ������������� �� ������������ (� ������� ���������)
  		 * 
  		 * @details
  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� ��� 
  		 * ������������� �� ������������ (� ������� ���������)
  		 * 
  		 * */
  		@Override
  		protected String doInBackground(String... args) {
  	
  			parameters.add(new BasicNameValuePair("client",this.usrType)); 
			parameters.add(new BasicNameValuePair("Deviceid",this.driverDevId ));
  			
			try {
				myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH() +"/scripts/makeCall.php",parameters);
	
  				if (Integer.parseInt(myjobg.get("success").toString()) == 1){
					return (myjobg.get("cellphone").toString() );
  			
				}
				else{
					
					return ("");
				}
		
  			} catch (NumberFormatException e) {
  				// Auto-generated catch block
  				e.printStackTrace();
  			} catch (JSONException e) {
  				//  Auto-generated catch block
  				e.printStackTrace();
  			}
  			
			return ("");
  			
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
  		protected void onPostExecute(String file_url) {
  			// dismiss the dialog once done
    			pDialog.dismiss();
    			pDialog = null;

  		}

  }   


}//end class file




