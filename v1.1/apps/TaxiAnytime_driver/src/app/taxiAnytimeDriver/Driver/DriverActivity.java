package app.taxiAnytimeDriver.Driver;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import app.taxiAnytimeDriver.R;
import app.taxiAnytimeDriver.Common.About;
import app.taxiAnytimeDriver.Common.ConnectionDetector;
import app.taxiAnytimeDriver.Common.EditProfileActivity;
import app.taxiAnytimeDriver.Common.ReportActivity;
import app.taxiAnytimeDriver.Common.globalVariables;
import app.taxiAnytimeDriver.Common.httpJSONParser;
import app.taxiAnytimeDriver.Common.showAlertMessage;
import app.taxiAnytimeDriver.pushService.PushService;
import app.taxiAnytimeDriver.userTypesFactory.Users;
import app.taxiAnytimeDriver.userTypesFactory.UsersFactory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;




/**
 * @info
 *H ����� ������������� ��� ������.
 *����������� ���� ��� ����� ��� ��������� ���.
 *���������� ���� ����������� ������� ���� ����� ���.
 *������������ �� gps ��� �������� � ��������(Push Service) ��� ���������� �� ����� �
 * ������� ���,���� �� �������� �� ��������� ���� ��� �� ���������� ������������ .
 */

public class DriverActivity extends FragmentActivity 
{
	
	
	 protected Users driver;
	
	 public  GoogleMap mMap;
	 private LocationManager locationManager;
	 private  LocationListener loclistener;
	
	 public List<MarkerTag>  markerOptionsData;
	 public static LatLng currentLatLng;
	 public static Location myLocation;
	 public static CheckBox checkBoxAccept;
	 public static CheckBox ckeckReachCustomer;
	 public static  Context myContext;
	 public static Activity myActivity;
	 public static MarkerItems driverItems;
	 public static Bundle extraParameters;
	 
	 private final int SEND_NOTIFICATION = 1;
	 private final int SENDED_NOTIFICATION_ACK = 2;
	 private final int NEW_CUSTOMER = 1;
	 private final int ACCEPTED_CUSTOMER = 2;
	 private final int DRAW_CUSTOMER = 1;
	 private final int DRAW_DRIVER = -1;
	 private final int DRAW_ACCEPTED_CUSTOMER = 2;
	 
	 private final String BOOKMARK_TITLE = "Taxi Anytime";
	 private final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
	 
	
	

	  
  /**
  * @info
  * ������������ activity
  * 
  * @details
  * ������������� ��� ����� ��� xml ������� �� ����������� , ���� �� �������� �� �� ��������������.
  * ��������� �� id ��� �������� �� ������ SharedPreferences ��� �� ���������� �� �� ���������������� ��������
  * ���� �������� ��������������/service.
  * �������� �� service ���� ��� ��� ���� ���������� ��� ��������� gps �� ��� ������� ��� ���������� 
  * ������ (ConnectionManager) ��� ������������ �� ��������� ��� ������.
  * ������ ������������ ��� ��� ����� �� ���������� ��� ��������� ��� ������
  *
  */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    

    
    FragmentManager fragmentManager = getSupportFragmentManager();
    SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
            .findFragmentById(R.id.map);
    
    mMap = mapFragment.getMap();
    
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);     
    mMap.setMyLocationEnabled(true);
    

    checkBoxAccept = (CheckBox) findViewById(R.id.checkBoxAccept);
    checkBoxAccept.setChecked(false);
    ckeckReachCustomer = (CheckBox)findViewById(R.id.checkBoxReachCustomer);
    ckeckReachCustomer.setEnabled(false);

    myContext = getApplicationContext(); // ��� �� shared preferences ����
    myActivity = DriverActivity.this;   // ��� �� ������ �� �������������� ��� static
    //-----------------------
    //initialize driver
    driver = UsersFactory.createUser("driver");

    Editor editor = getSharedPreferences(PushService.TAG, MODE_PRIVATE).edit();
	editor.putString(PushService.PREF_DEVICE_ID, driver.getDeviceID());
	editor.commit();
 
	 //��� �� ������ �������� ��� id ��� �������� �� ���� ��� activities
 	SharedPreferences pref = getSharedPreferences("fromDriver", 0);
    SharedPreferences.Editor edit = pref.edit();
    edit.putString("driverDeviceId","/"+driver.getDeviceID());
    edit.commit();


	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
	 
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.NO_REQUIREMENT);  //NO_REQUIREMENT
    criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);         //NO_REQUIREMENT
    String provider = locationManager.getBestProvider(criteria, true);
    myLocation = locationManager.getLastKnownLocation(provider);
    loclistener = new mylocationlistener(myLocation);

    locationManager.requestLocationUpdates(provider,0,0,loclistener);
	  
   
    
   	currentLatLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
   
    driver.setLatitude(currentLatLng.latitude);
   	driver.setLongitude(currentLatLng.longitude);
    // o odhgos dhmiourgeitai katw ..sto  else    

   
 

   	try {
   	   	extraParameters = getIntent().getExtras();
		inComingNotifications( extraParameters );
	} 
   	catch (Exception e) {
		e.printStackTrace();
	}
 
 	
	       
  }//oncreate
  
  
  

  
  /**
	  * @info
	  * ������������ menu buttons
	  * 
	  * @details
	  * ������������ menu buttons
	  */
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.layout.menu_items, menu);
      return true;
  }
  
  
  
  
  
//========================================================================================== 
  
  /**
 * @info
 * ���������� ��� ������������ ���� ���� �����.
 */
private void clearMap()
  {

	if( mMap != null && extraParameters != null  ){
		
		mMap.clear();
		
		extraParameters = null;
	}
		
	
		
  }







//========================================================================================== 
/**
* @info
* build-in ��������� ��� ��� ���������� ����������� ��������������
* 
* @details
* ���� ����������� � ������������� ��� ������ ��������� �� push service
* ��� ����������� ��� �����
*/
@Override
protected void onDestroy() {
	//Auto-generated method stub
	//���������� ����� ��� �������� service
	
	PushService.actionStop(DriverActivity.this);
	clearMap();
	super.onDestroy();
}
  
//==================================================================================
  /**
   * @param view H ��� ���� ����� ��������� �� ������
 * @throws ExecutionException 
 * @throws InterruptedException 
   * 
   * @info
   * ���������� checkbox ������� �� �� �� ����� ���������� � ������
   * 
   * @details
   * �� ������� � ������ �� checkbox ���� ������ �� ������� �������.
   * ������ ���.
   */
 public void onCheckAcceptClicked(View view) throws InterruptedException, ExecutionException  {
	    // Is the view now checked?
	    
	 if ( new ConnectionDetector(this).execute().get().get("connectionState") ){

		 if (((CheckBox) view).isChecked()){
			 
			 new DriverAcception("1","/"+this.driver.getDeviceID()).execute();
		  
		 }
		 else{
			 new DriverAcception("0","/"+this.driver.getDeviceID()).execute();
		 
	  }
	  
    }
	 
} 
 
//================================================================================== 
 /**
 * @param view H ��� ���� ����� ��������� �� ������
 * @throws ExecutionException 
 * @throws InterruptedException 
 * 
 * @info
 * ���������� checkbox ������� �� �� �� ���� ������ � ������ ���� ������
 * 
 * @details
 * '���� � ������ ������ ���� ������ ����������� ���� �� checkbox
 * �� �������� ��� ���� �������������� � ������� ����������� (DriverConfirmOrder),
 * �� ��� ���� ����� �� checkbox �� ��������
 * 
 */
 public void onReachCustomer(View view) throws InterruptedException, ExecutionException {
	    // Is the view now checked?
	    
	 if ( new ConnectionDetector(this).execute().get().get("connectionState") ){
	 
		 if (((CheckBox) view).isChecked()){
			 
			 AlertDialog.Builder builder =  new AlertDialog.Builder(this );
		 
		  	builder.setTitle( "���������� ������" )
     		.setCancelable(false)
	    	.setMessage( "������� ��� ������??" )
	    	.setOnCancelListener( new OnCancelListener() {

	    	    @Override
	    	    public void onCancel(DialogInterface dialog) {
	    	    	
	    	    	
	    	        finish();   //to finish Activity on which dialog is displayed
	    	    }
	    	})
	    	.setPositiveButton( "NAI", new DialogInterface.OnClickListener() {
	    		
	    		public void onClick(DialogInterface dialog, int which) {

	    			//do something
	  
	    			//��������� ��� ����������(id) ��� ��� ������� �� ������ & ����������� ��� ������
					SharedPreferences incOrder = getSharedPreferences("fromPush", MODE_PRIVATE);
	    			new DriverConfirmOrder("/"+driver.getDeviceID() , incOrder.getString("selectedOrderId","") ).execute();
	    		
	    			clearMap();
	    			Intent i = new Intent(DriverActivity.this,DriverEndRideActivity.class);
	    			startActivity(i);
	    		
	    		}
	    	})
	    	.setNegativeButton( "OXI", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int which) {
	    			
	    			ckeckReachCustomer.setChecked(false);
	    			
	    			
	    		}
	    	} );
     
     
     	AlertDialog alert = builder.create();
     	alert.setOwnerActivity(DriverActivity.this);
         alert.show();

       }
   }

}
 

 //================================================================
 /**
 * @param extras ����������� ��� �������� ��� ����������� ��� ������
 * 
 * @info
 * ������������� ��� ������������ ��� ������ ��� ��� ������
 * 
 * @details
 * �� � ���������� ����� ��� ������� ���������� ������� ��� ��������� ���������� ��
 * ����������� ��� ������ �� ���� ��� ����������� ��� ��� ��� ���������� ���� �����
 * ��� �� ��� � ������ �� ����� �� ������� ���������� �� ���� ��� ����������.
 * 
 * �� � ���������� ����� ��� � ������� ���� ������ ��� ������������ ����� ��� ��� ����������
 * � ������ ��� ������ �� ������ ����� ������������ ��� ����������� (�� checkboxes �������� false)
 * 
 * 
 * 
 */
 private void inComingNotifications(Bundle extras) 
 {
	 
	 if(extras != null){
			  
	  	
			  SharedPreferences preferences = getSharedPreferences("fromPush", MODE_PRIVATE);
			  switch( Integer.valueOf( preferences.getString("type","") ))
			  {
			  	case SEND_NOTIFICATION:
			  	{
			  		
			  		
			  		new DriverAcception("0","/"+this.driver.getDeviceID());
			  				Users customer = UsersFactory.createUser("customer"); 
					
			  				
			  				customer.setDeviceID( preferences.getString("customerid", "") );
			  				customer.setLatitude( Double.valueOf(preferences.getString("customerLat", "") ) );
			  				customer.setLongitude( Double.valueOf(preferences.getString("customerLon", "") ) );
			  				
			  				
			  				//Log.d("accepted_customer", preferences.getString("customerAcceptedID", "") );
					  		SharedPreferences SendPrefs = this.getApplicationContext().getSharedPreferences(
									  "acceptedCustomer",Context.MODE_PRIVATE);
							SharedPreferences.Editor prefEditor = SendPrefs.edit();
							prefEditor.putString("acceptedCustomerDevid",customer.getDeviceID());
							prefEditor.commit();
			  				
			  				
			  				drawLocation("customer",customer,DRAW_CUSTOMER);
			  		
			  	
			  				break;
			  	}
			  	
			  	case SENDED_NOTIFICATION_ACK:
			  	{

			  		new DriverAcception("0","/"+this.driver.getDeviceID());
			  			Users customer = UsersFactory.createUser("customer"); 
			  
			  			
			  			customer.setDeviceID( preferences.getString("customerAcceptedID", "") );
			  			customer.setLatitude( Double.valueOf(preferences.getString("customerAcceptLat", "") ) );
				  		customer.setLongitude( Double.valueOf(preferences.getString("customerAcceptLon", "") ) );
				  	
				  		
				  		
				  		drawLocation("customer",customer,DRAW_ACCEPTED_CUSTOMER);
			
				  		ckeckReachCustomer.setEnabled(true);
				  		ckeckReachCustomer.setChecked(false);
				  		checkBoxAccept.setEnabled(false);

				  		break;
			  	}

			}
			  
		  }
		  else
		  { 

			  
			try {  
			//push service start
			PushService.actionStart(DriverActivity.this);
			  
			
			  }
	 		catch(Exception e){
		  new showAlertMessage(this ,"��! ���� ���� ������!!!","������������� ��� �������� ��� ��������� ��������");
		  e.printStackTrace();
	 	}
			
			new DriverAcception("0","/"+this.driver.getDeviceID());
			drawLocation("driver",this.driver,DRAW_DRIVER);

			Toast toast = Toast.makeText(getBaseContext(),"����� �������!",Toast.LENGTH_LONG);
	 	 	toast.show();
	  	

			
	 	
	 }
 
 }


 
 //=========================================================
 /**
* @param clientType(�� ����������� ��� ������ � ��� ������)
* @param userType(����� ������������ "customer" � "driver" �������)
* @param notificationType (1:��� ��� ������ , 2:��� ���� ������������ �������� , -1 : ����)
* 
* @info
* ���������� ���� ��� mapView ��� ��������� ��� ������ ����� ��� �.������/�������
* 
* @details
* ���������� ���� ��� mapView ��� ��������� ��� ������ ����� ��� �.������/�������
* 
*/
  protected void drawLocation(String userType,Users clientType,int notificationType)
	{

	    Marker marker = null ;
	    MarkerOptions mo = new MarkerOptions();

		if(userType == "customer") {
			
			switch(notificationType)
			{
				case NEW_CUSTOMER:
				{
					mo.icon( BitmapDescriptorFactory.fromResource( R.drawable.customer_icon2 ) );
					mo.title("customer");
					mo.snippet("new_customer");
					
					
					
					break;
				}
				case ACCEPTED_CUSTOMER:
				{
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.accepted_customer) );
					mo.title("customer");
					mo.snippet("accepted_customer");
					
					break;
				}
		
			}
			
			mo.position( new LatLng(clientType.getLatitude(),clientType.getLongitude()) );
		

			if(markerOptionsData instanceof List<?> ) {
				markerOptionsData.add( new MarkerTag(mo,clientType) );
				//Log.d("instanceof??","yes i am!!");
			}
			else{
				//Log.d("instanceof??","no, i am new!!");
				markerOptionsData = new ArrayList<MarkerTag>();
				markerOptionsData.add( new MarkerTag(mo,clientType) );
			}
			 mMap.addMarker( mo );
		
		
		}
	
		
		
		

		//   driverItems = OverlayInstance.getInstance(marker, this.getApplicationContext());
		
		// marker = mMap.addMarker(mo);
			
			
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(  new LatLng( clientType.getLatitude() , clientType.getLongitude())  , 15));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null); 
			
	    
	   
			  MarkerClickListener ml = new MarkerClickListener();
			  mMap.setOnMarkerClickListener(ml);
			
			
}
//===================================================================================  
  public class MarkerTag {
	  
	  
	  private Marker theMarker;
	  private Users theUser;

	public MarkerTag(MarkerOptions markerOpt,Users usr){
		  
		theMarker = mMap.addMarker( markerOpt );
		theUser = usr;
         
	}
	
	
	  public Marker getTheMarker() {
		return theMarker;
	}
	  
	  public Users getTheUser() {
		return theUser;
	}
  }
  

 //=================================================================================== 
  
  protected class MarkerClickListener implements OnMarkerClickListener {

	  /*
	  MarkerOptions myMopt;
	  public MapClickListener(MarkerOptions markOpt) {
		  myMopt = null;
		  myMopt = markOpt;
	}
	  */
	  


	@Override
	public boolean onMarkerClick(Marker marker) {
		
		Users customer ;
		
		if(markerOptionsData.size()>0){

			for(int i=0;i<markerOptionsData.size();i++){

				if(markerOptionsData.get(i).getTheMarker().getTitle().equals(marker.getTitle()) &&
					markerOptionsData.get(i).getTheMarker().getSnippet().equals("new_customer") )  {

						customer = markerOptionsData.get(i).getTheUser();
						new readyOrder(customer ,driver).execute();
						
					
						Toast toast = Toast.makeText(getApplicationContext(),"� ������� �� ����������� ��� �� ���������� ���",Toast.LENGTH_LONG);
	    				toast.show();
	
						markerOptionsData.remove(i);
						i = markerOptionsData.size()*2;
					
					}
				}

			}
			
		
		
		
		
		return false;
	}
	

}//end listener
  
  
  
//==================================================================================
  



//====================================================================================

/** 
* @info 
* ����� ��� �� ��������� � ������ ��� ���������� (���������� ��� ������ order orderState =2)
* 
* @details 
* � ����� "������ " ��� ���������� ��� ����������� �� �� ���� ��������� ��� ��� ����������
* ��� ���������� �����������
*/
protected class DriverConfirmOrder extends AsyncTask<String, String, String> {

	private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private ArrayList<NameValuePair> parameters2 = new ArrayList<NameValuePair>(1);
	private String deviceid;
	private ProgressDialog pDialog;
	private String orderid;
	
	public DriverConfirmOrder(final String driverDeviceid,final String orderid) {
		//  Auto-generated constructor stub
		this.deviceid = driverDeviceid;
		this.orderid = orderid;
	}
	
	/**
	 * @info
	 * ���� ��������� � ���������� ��������� ����������� ������ ���� ����� ������.
	 * 
	 * @details
	 * ���� ��������� � ���������� ��������� ����������� ������ ���� ����� ������.
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(DriverActivity.this);
		pDialog.setMessage("Loading..");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
	
	/**
	 * @info
	 * ��� ������� � ����������� �� �� ���� �������� http request 
	 * ��� �� ����������� � ��������� ����������
	 * 
	 * @details
	 * ��� ������� � ����������� �� �� ���� �������� http request 
	 * ��� �� ����������� � ��������� ����������
	 * 
	 * */
	@Override
	protected String doInBackground(String... args) {

		
		parameters.add(new BasicNameValuePair("driverid",this.deviceid));
		parameters.add(new BasicNameValuePair("orderid",this.orderid));
		parameters.add(new BasicNameValuePair("type","driver"));
	   
		parameters2.add(new BasicNameValuePair("orderid",this.orderid));
		try {
			// getting JSON Object
			JSONObject myjobg = null; 
			httpJSONParser json = new httpJSONParser();
			myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/orderConfirmation.php",parameters);

			
			//��������� ������ ��� ��� ����� ��� ������
			httpJSONParser json2 = new httpJSONParser();
			json2.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/mqttClient/notifyCustomerForArrival.php",parameters2);
			
			
		// check for success tag

			if (Integer.parseInt(myjobg.get("success").toString()) == 1) {
				// maybe something here

			} else {}
		
		}
		catch (Exception e) {
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



//=====================================================================================

/** 
* @info 
* ����� ��� �� ������� �� ������������� ��� ������
* 
* @details 
* � ����� "������ " ��� ���������� ��� ����������� �� �� ���� ��������� ��� ��� ����������
* ��� ���������� �������������� ��� ������
*/
protected class DriverAcception extends AsyncTask<String, Void, String> {

	private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private String accept;
	private ProgressDialog pDialog;
	private String driverid;
	
	public DriverAcception(String accept0,String driverid0) {
		//  Auto-generated constructor stub
		this.accept = accept0;
		this.driverid = driverid0;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(DriverActivity.this);
		pDialog.setMessage("Loading..");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
	/**
	 * @info
	 * ��� ������� � ����������� �� �� ���� �������� http request 
	 * ��� �� ����������� � ��������� �������������� ��� ������
	 * 
	 * @details
	 * ��� ������� � ����������� �� �� ���� �������� http request 
	 * ��� �� ����������� � ��������� �������������� ��� ������
	 * 
	 * */
	@Override
	protected String doInBackground(String... args) {

		
		parameters.add(new BasicNameValuePair("available",String.valueOf(this.accept)));
		parameters.add(new BasicNameValuePair("driverid",this.driverid));
	   
		try {
			// getting JSON Object
			JSONObject myjobg = null; 
			httpJSONParser json = new httpJSONParser();
			myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/setdriverAvailability.php",parameters);


		// check for success tag

			if (Integer.parseInt(myjobg.get("success").toString()) == 1) {
				// maybe something here

			} else {}
		
		}
		catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String file_url) {
		// dismiss the dialog once done
		pDialog.dismiss();
		pDialog = null;

	}

}

//=====================================================================================
/** 
* @info 
* ����� ��� �� ������������� � ���������� ��� ����
* 
* @details 
* � ����� "������ " ��� ���������� ��� ����������� �� �� ���� ��������� ��� ��� ����������
* ��� ���� �����������
*/			
protected class readyOrder extends AsyncTask<String, Void, String> {

	private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	private httpJSONParser json = new httpJSONParser();
	private JSONObject myjobg =null ;	
	private Users customers,driver;
	
    
	public readyOrder(Users Customers,Users Driver) {
		//  Auto-generated constructor stub
		this.customers = Customers;
		this.driver = Driver;
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(DriverActivity.this);
		pDialog.setMessage("Loading..");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
	
	/**
	 * @info
	 * ��� ������� � ����������� �� �� ���� �������� http request 
	 * ��� �� ����������� � ��� ����������
	 * 
	 * @details
	 *  ��� ������� � ����������� �� �� ���� �������� http request 
	 * ��� �� ����������� � ��� ����������
	 * 
	 * */
	@Override
	protected String doInBackground(String... args) {

		 float distance = getCalculatedDistance(customers.getLatitude(),customers.getLongitude(),driver.getLatitude(),driver.getLongitude());

		  
			try {

				parameters.add( new BasicNameValuePair("customerlat",String.valueOf( customers.getLatitude() ) ) );
				parameters.add( new BasicNameValuePair("customerlon",String.valueOf( customers.getLongitude() ) ) );
				parameters.add( new BasicNameValuePair("driverlat",String.valueOf(driver.getLatitude() ) ) );
				parameters.add( new BasicNameValuePair("driverlon",String.valueOf(driver.getLongitude() ) ) );
				parameters.add( new BasicNameValuePair("customerDevice",customers.getDeviceID()));
				parameters.add( new BasicNameValuePair("driverDevice","/"+driver.getDeviceID()));
				parameters.add( new BasicNameValuePair("distance",String.valueOf(distance) ));
				

				myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeOrder.php",parameters);
				
				
				if(Integer.parseInt(myjobg.get("success").toString()) == 1)
				{
				
					//kratame to orderid pou 8a steiloume otan etoimazetai mia paraggelia se shared preferences
	
					SharedPreferences OrderPrefs = myContext.getSharedPreferences(
							  "myOrder", Context.MODE_PRIVATE);
					SharedPreferences.Editor prefEditor = OrderPrefs.edit();
					prefEditor.putString("orderid",myjobg.get("orderid").toString());
					prefEditor.commit();
				
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();	
				
			}  

		return null;
	}
	
	/**
	 * @param lat_a
	 * @param lng_a
	 * @param lat_b
	 * @param lng_b
	 * 
	 * @info
	 * ������� ��� ��� ���������� ��� ��������� ������ ������ ���  ����������� ������
	 * 
	 * @details
	 * ������������ �����  �� ���������� ���� ��� �����
	 * @return
	 */
	private float getCalculatedDistance(double lat_a, double lng_a, double lat_b, double lng_b)
	 {
	  			Location locationA = new Location("point A");
	  			Location locationB = new Location("point B");


	  			try
	  			{
	  				locationA.setLatitude(lat_a);
	  				locationA.setLongitude(lng_a);

	  				locationB.setLatitude(lat_b);
	  				locationB.setLongitude(lng_b);

	  				
	  				return locationA.distanceTo(locationB);
	  			}
	  			catch(Exception e)
	  			{
	  				e.printStackTrace();
	  				return 0.0f;
	  			}	
	}
	
	
	@Override
	protected void onPostExecute(String file_url) {
		// dismiss the dialog once done
		pDialog.dismiss();
		pDialog = null;

	}
	
	

}


//======================================================================================
/**
* 
* 
* @info
* ��� ���������� �����, ���� ������� �� ������ ��� ��� ��������� �� ���������
* ����
* 
*����� �������������� ����� ��� SDK
* 
*/
private class mylocationlistener implements LocationListener {
	
	public mylocationlistener(Location myloc) {
		//  Auto-generated constructor stub
		
	}
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
        
        }
    }

	@Override
	public void onProviderDisabled(String provider) {
		// Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		//  Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Auto-generated method stub
		
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
	changeProfile();
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
share.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
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


}//end class



