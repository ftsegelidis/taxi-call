package app.taxiAnytimeCustomer.DriversList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import app.taxiAnytimeCustomer.R;
import app.taxiAnytimeCustomer.Common.ConnectionDetectorTask;
import app.taxiAnytimeCustomer.Common.globalVariables;
import app.taxiAnytimeCustomer.Common.httpJSONParser;
import app.taxiAnytimeCustomer.Customer.CustomerStartEndRide;


/**
 * @info
 * Κλάση υπεύθυνη για την διαχείρηση του listview που περιέχει 
 * την λίστα όλων των ενδιαφερόμενων οδηγών ταξί σχετικά με μια κλήση.
 */
public class ListViewActivity extends ListActivity {
	/** Called when the activity is first created. */
	

public Bundle extras;
protected String customerid;
protected ListView myListView ;
protected Activity thisActivity;
	

public Activity getThisActivity() {
	return thisActivity;
}

/**
 * @info
 * Αρχικοποίηση activity
 * 
 * @details
 * Γίνετε έλεγχος για το αν είμαστε συνδεδεμένοι στο internet και αν έχουμε gps ανοικτό
 * Στην συνέχεια εμφανίζει τα δεδομένα όλων των οδηγών που αποδέχτηκαν την συγκεκριμένη
 *  παραγγελία
 * 
 */	
    @Override
public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.drivers_listview);
        myListView = (ListView)findViewById(android.R.id.list);
        thisActivity = this;
        SharedPreferences prefs = getSharedPreferences("fromCustomer", MODE_PRIVATE);
	    customerid = prefs.getString("customerdevid","");

        ArrayList<DriverDetails> items_details = null;

				try {
					if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){
					
						items_details = new setListViewDataTask(customerid).execute().get();
					
						myListView.setAdapter(new ItemListBaseAdapter(this, items_details));

					}
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
       
}//onCreate
    
    
    @Override
    public ListView getListView() {
    	
    	return super.getListView();
    }
    
//===============================================================================   
	/** 
     * @info 
     * Μέθοδοι για να "ετοιμάσουν" το listview
     * 
     * @details 
     * Εδώ παίρνουμε τα δεδομένα των οδηγών τα τοποθετούμε σωστά στο listview.
     */
    
    
class setListViewDataTask extends AsyncTask<String,String,  ArrayList<DriverDetails>> {

    	private ArrayList<DriverDetails> results = new ArrayList<DriverDetails>();
    	
    	private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
    	private JSONObject myjobg = null;  
    	private httpJSONParser json = new httpJSONParser();
    	private String customerID;
    	
    	public setListViewDataTask (String cst) {
			//  Auto-generated constructor stub
    		customerID = cst;
		}

    	/**
  		 * @info
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * επιστρέφονται τα αποτελέσματα (οι πληροφορίες των οδηγών που ενδιαφέρθηκαν
  		 * για την συγκεκριμένη κλήση)
  		 * 
  		 * @details
  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
  		 * επιστρέφονται τα αποτελέσματα (οι πληροφορίες των οδηγών που ενδιαφέρθηκαν
  		 * για την συγκεκριμένη κλήση)
  		 * 
  		 * */
		protected  ArrayList<DriverDetails> doInBackground(String... params) {

			parameters.add(new BasicNameValuePair("customerDevID",customerID));
			myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/driversListview.php","POST",parameters);

			try 
			{
				for(int i=0;i<myjobg.getJSONArray("drivers").length();i++)
		    	{
					DriverDetails item_details = new DriverDetails();
					
					item_details.setOrderid( Integer.valueOf( myjobg.getJSONArray("drivers").getJSONObject(i).get("orderid").toString() )  );
					item_details.setName(myjobg.getJSONArray("drivers").getJSONObject(i).get("name").toString()   );
			    	item_details.setDistance( myjobg.getJSONArray("drivers").getJSONObject(i).get("distance").toString()); 
			    	item_details.setRate(Float.valueOf(myjobg.getJSONArray("drivers").getJSONObject(i).get("rate").toString())) ;
			    	item_details.setPlateNumber(myjobg.getJSONArray("drivers").getJSONObject(i).get("taxiPlateNumber").toString());
			    	item_details.setDriverDeviceId(myjobg.getJSONArray("drivers").getJSONObject(i).get("driverDeviceID").toString());
			    	item_details.setMyImage(  downloadImage(myjobg.getJSONArray("drivers").getJSONObject(i).get("driverImageUrl").toString() ) ) ; 

			    	results.add(item_details);
	
		    	}
				
			} 
			catch (JSONException e) 
			{
				//  Auto-generated catch block
				e.printStackTrace();
			}
			
	    	return results;
 
	}

		/**
		 * @param url το path της εικόνας
		 * 
		 * @info
		 * Κατεβάζει την εικόνα απο την βάση για να την βάλει στο listview
		 * 
		 * @details
		 * Κατεβάζει την εικόνα απο την βάση για να την βάλει στο listview
		 * 
		 * @return myImage η τρέχον εικόνα
		 */
		private Bitmap downloadImage (String url)
		{

			Bitmap myImage = null;
			try {
				InputStream in = new java.net.URL(url).openStream();
				myImage = BitmapFactory.decodeStream(in);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				
			}
			return myImage;
		}


}
//=====================================================
private static class ViewHolder {
	public TextView txt_DriverName;
	public TextView plateNumber;
	public TextView txt_distance;
	public ImageView itemImage;
	public RatingBar driversRate;
	public Button showComments,selectDriver;
}


//==============================================================================================

public class ItemListBaseAdapter extends BaseAdapter {
	

	private  ArrayList<DriverDetails> driverDetailsArrayList;
	
	private LayoutInflater l_Inflater;

	/**
	 * @param context Το περιεχόμενο της δραστηριότητας που καλεί το listview
	 * @param results Όλα τα δεδομένα του listview
	 * 
	 * @info
	 * Παράγει το listview με όλα τα δεδομένα μέσα για να μπορούμε να τα χρησιμοποιήσουμε
	 * 
	 * @details
	 * Παράγει το listview με όλα τα δεδομένα μέσα για να μπορούμε να τα χρησιμοποιήσουμε
	 */
	public ItemListBaseAdapter(Context context, ArrayList<DriverDetails> results) {
		driverDetailsArrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return driverDetailsArrayList.size();
	}

	public Object getItem(int position) {
		return driverDetailsArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * @param context position Η θέση του αντικειμένου μέσα στην λίστα
	 * @param convertView το κομμάτι της λίστας απο το οποίο θα πάρουμε τα δεδομένα
	 * @param parent όλο το Listview απο το οποίο θα πάρουμε δεδομένα
	 * 
	 * @info
	 * Βρήσκει και κρατάει τα δεδομένα μίας συγκεκριμένης όψης μέσα στο listview
	 * 
	 * @details
	 * Βρήσκει και κρατάει τα δεδομένα μίας συγκεκριμένης όψης μέσα στο listview
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_details_drivers_view, null);
			holder = new ViewHolder();
			holder.txt_DriverName = (TextView) convertView.findViewById(R.id.name);
			holder.txt_distance = (TextView) convertView.findViewById(R.id.distance);
			holder.driversRate = (RatingBar) convertView.findViewById(R.id.ratingBar);
			holder.plateNumber = (TextView) convertView.findViewById(R.id.plateNumber);
			holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);
			convertView.setTag(holder);
			
		
			((Button) convertView.findViewById(R.id.btnShowComments)).setOnClickListener(onShowCommentsListener);
			((Button) convertView.findViewById(R.id.btnSelectDriver)).setOnClickListener(onSelectDriverListener);
			
			
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.txt_DriverName.setText(driverDetailsArrayList.get(position).getName());
		holder.plateNumber.setText(driverDetailsArrayList.get(position).getPlateNumber());
		holder.txt_distance.setText(String.valueOf(driverDetailsArrayList.get(position).getDistance()));
		holder.driversRate.setRating(driverDetailsArrayList.get(position).getRate());
		holder.itemImage.setImageBitmap(driverDetailsArrayList.get(position).getMyImage() );

		return convertView;
	}
	
	
	private OnClickListener onShowCommentsListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        	final int position = getListView().getPositionForView(v);
        	if (position != ListView.INVALID_POSITION) {
        		
        		DriverDetails obj_DriverDetails = driverDetailsArrayList.get( position );
        		//Κρατάμε το deviceid  που επιλέχθηκε
     			SharedPreferences OrderPrefs = getThisActivity().getSharedPreferences(
     				  "myOrder", Context.MODE_PRIVATE);
     			SharedPreferences.Editor prefEditor = OrderPrefs.edit();
     			prefEditor.putString("selectedDriverDevId",obj_DriverDetails.getDriverDeviceId()  );
     			prefEditor.commit();
        	
     		
     			Intent intent = new Intent(getThisActivity(), ListViewShowComments.class);
     			getThisActivity().startActivity(intent);
        		//Log.d("name????",String.valueOf(driverDetailsArrayList.get( position ).getName() ));
        	}
      
       
        }
    };

    private OnClickListener onSelectDriverListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        	final int position = getListView().getPositionForView(v);
        	if (position != ListView.INVALID_POSITION) {
        		
        		DriverDetails obj_DriverDetails = driverDetailsArrayList.get( position );
                new AcceptDriversTask( Integer.valueOf(  obj_DriverDetails.getOrderid() ),getThisActivity() ).execute();
     			Toast toast = Toast.makeText(getThisActivity(),"Θα ειδοποιηθείτε όταν φτάσει ο οδηγός!",Toast.LENGTH_LONG);
     			toast.show();

     			//Κρατάμε το orderid  που επιλέχθηκε
     			SharedPreferences OrderPrefs = getThisActivity().getSharedPreferences(
     				  "myOrder", Context.MODE_PRIVATE);
        	
     			SharedPreferences.Editor prefEditor = OrderPrefs.edit();
     			prefEditor.putString("selectedDriverDevId",obj_DriverDetails.getDriverDeviceId()  );
     			prefEditor.putString("orderid",String.valueOf( obj_DriverDetails.getOrderid() ) );
     			prefEditor.commit();

     			//finish();
     			Intent intent = new Intent(getThisActivity(), CustomerStartEndRide.class);
     			getThisActivity().startActivity(intent);
               
        		
        	}
        	
         
        }
    };

}	

//===============================================================================


/**
 * Background Async Task για να ειδοποιήσει τους οδηγούς
 * */

/** 
 * @info 
 * Κλάση για να ειδοποιήσει τον οδηγό που επέλεξε ο πελάτης για την παραγγελία
 * 
 * @details 
 * ΕΚλάση για να ειδοποιήσει τον οδηγό που επέλεξε ο πελάτης για την παραγγελία
 */
protected class AcceptDriversTask extends AsyncTask<String, String, String> {

			private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			private ArrayList<NameValuePair> parameters2 = new ArrayList<NameValuePair>();
			private ProgressDialog pDialog;	
			private int orderID;
			private Activity myAct;
			
			public AcceptDriversTask(int orderid, Activity act) {
				//  Auto-generated constructor stub
				orderID = orderid;
				myAct = act;
			}
			public Activity getMyActivity() {
				return myAct;
			}
			/**
	  		 * @info
	  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη.
	  		 * 
	  		 * @details
	  		 * Πριν ξεκινήσει η διαδικασία προσκόμισης  εμφανίζεται μήνυμα στην οθόνη χρήστη.
	  		 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(getMyActivity());
				pDialog.setMessage("Περιμένετε..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			/**
	  		 * @info
	  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
	  		 * επιστρέφονται τα αποτελέσματα (γεωργαφικό μήκος & πλάτος του πελάτη και το id του 
	  		 * οδηγού που επιλέξαμε)
	  		 * 
	  		 * @details
	  		 * Εδώ γίνεται η επικοινωνία με τη βάση κάνοντας http request και μας 
	  		 * επιστρέφονται τα αποτελέσματα (γεωργαφικό μήκος & πλάτος του πελάτη και το id του 
	  		 * οδηγού που επιλέξαμε).
	  		 * Βάση το id του οδηγού του στέλνουμε ειδοποίηση ότι έχει επιλεχτεί για την πραγγελία.
	  		 * Επίσης του στέλνουμε και την τοποθεσία του πελάτη.
	  		 * 
	  		 * */
			protected String doInBackground(String... args) {

				parameters.add(new BasicNameValuePair("orderid",String.valueOf(orderID) ));
			 
			  	JSONObject myjobg = null  ; 
			  	httpJSONParser json = new httpJSONParser();
		
				try {

						SharedPreferences prefs = getMyActivity().getSharedPreferences("fromCustomer", 0);
						
						parameters.add(new BasicNameValuePair("customerid", prefs.getString("customerdevid",null) ) );
						parameters.add(new BasicNameValuePair("orderid", String.valueOf(orderID) ) );
						myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/"+"customerSelectDriver.php","POST",parameters);
						
					
					parameters2.add(new BasicNameValuePair("driverDevIdtoSend",myjobg.get("driverDevID").toString() ) );
					parameters2.add(new BasicNameValuePair("customerlat",myjobg.get("customerLat").toString() ) );
					parameters2.add(new BasicNameValuePair("customerlon",myjobg.get("customerLon").toString() ) );
					parameters2.add(new BasicNameValuePair("selectedOrderid", String.valueOf( orderID ) ) );
					
					json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/mqttClient/notifyDriverForAccept.php","POST",parameters2);
						
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
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done

				pDialog.dismiss();
				pDialog = null;
	
			}
	}




} //end of class file