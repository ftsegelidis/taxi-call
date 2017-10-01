package app.taxiAnytimeDriver.Common;

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
 * @author fil/george
 * @details  Κλάση υπεύθυνη για τη διαχείριση κλήσεως/sms σε περίπτωση που χρειαστεί
 * 
 *
 *
 */

public class Contact  {
	

	private String mNumber;
	protected Activity actContext;
	protected ProgressDialog pDialog;
	

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
     * Πατώντας το κουμπί επικοινωνία μπορεί να επιλεξει sms/κλήση
     * 
     * @details
     * Αρχικά λαμβάνουμε τον αριθμό του οδηγού της παραγγελίας , έπειτα εμφανίζεται ένα dialog box 
     * για να επιλέξει ο χρήστης τον τρόπο επικοινωνίας (κλήση/ sms)
     * 
     */
    public void makeContact() throws InterruptedException, ExecutionException{
    	
    	if ( new ConnectionDetector(actContext).execute().get().get("connectionState") ) {
    	
    	SharedPreferences pref = actContext.getSharedPreferences("acceptedCustomer", Context.MODE_PRIVATE);
    	
    	setmNumber ( new GetCellphone("driver",
    				pref.getString("acceptedCustomerDevid","").toString() )
    				.execute().get()  
    				);
    	 
    	
    	final CharSequence[] items = {"Κλήση", "SMS", "Επιστροφή"};
    	AlertDialog.Builder builder = new AlertDialog.Builder(actContext);

    	builder.setTitle("Επιλέξτε τρόπο επικοινωνίας");
    	builder.setItems(items, new DialogInterface.OnClickListener() {

    	   public void onClick(DialogInterface dialog, int item) {
    		   if (items[item].equals("Κλήση")) {
    			   actContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+getmNumber())));
    			  
    		   }
    		   else if (items[item].equals("SMS")) {
    			   
    				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
   			        smsIntent.putExtra("sms_body", "Taxi anytime!"); 
   			        smsIntent.putExtra("address", getmNumber());
   			        smsIntent.setType("vnd.android-dir/mms-sms");

   			     actContext.startActivity(smsIntent);
    			   
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
     * Μέθοδος για την προσκόμιση του κινητού τηλεφώνου τον συγκεριμένο οδηγού/πελάτη 
     * 
     * @details 
     * Η κλάση "τρέχει " στο παρασκήνιο και επικοινωνεί με τη βάση δεδομένων για την προσκόμιση
     * του αριθμού κινητου τηλεφώνου
     */
  
    protected class GetCellphone extends AsyncTask<String, Void, String> {
   	 
	 	private ArrayList<NameValuePair> parameters = new  ArrayList<NameValuePair>() ;
		private JSONObject myjobg = null;;
		private httpJSONParser json = new httpJSONParser();  
  		
  		private String usrType;
  		private String customerDevId;
  		
  		public GetCellphone() {
  	
  
  			this.usrType = null;
  			this.customerDevId = null;
  
		}
  		public GetCellphone(String userType,String drvDevId) {
  			//  Auto-generated constructor stub
  			this.usrType = userType;
  			this.customerDevId = drvDevId;
  		}
  		
  		/**
  		 * Before starting background thread Show Progress Dialog
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
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * επιστρέφονται τα αποτελέσματα (ο αριθμός τηλεφώνου)
  		 * 
  		 * @details
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * επιστρέφονται τα αποτελέσματα (ο αριθμός τηλεφώνου)
  		 * 
  		 * */
  		@Override
  		protected String doInBackground(String... args) {
  	
  			parameters.add(new BasicNameValuePair("client",this.usrType)); 
			parameters.add(new BasicNameValuePair("Deviceid",this.customerDevId ));
  			
  	
			myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeCall.php",parameters);
	
  			try 
  			{
  				if (Integer.parseInt(myjobg.get("success").toString()) == 1)
				{
					return ( myjobg.get("cellphone").toString() );
  			
				}
				else 
				{
					
					return "";
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
  		 * συνεχίζεται στο κύριο νήμα
  		 * 
  		 * @details
  		 * Αφού ολοκληρωθεί η διαδικασία, αποδεσμεύουμε το dialog και η εκτέλεση
  		 * συνεχίζεται στο κύριο νήμα
  		 * 
  		 * **/
  		@Override
  		protected void onPostExecute(String file_url) {
  			// dismiss the dialog once done
    			pDialog.dismiss();

  		}

  }
       
    
    
    
}//end class file




