package app.taxiAnytimeDriver.Common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;



/**
 * 
 * @info
 *����� �������� ��� �� ������� �� ������������� ��� 
 * ��� server ��� ����������� ��� �� ������������ � ��������
 * ����� ��� ��� �� �� ����� ������������ � ������� �� wifi/3g/gps
 */
public class ConnectionDetector  extends AsyncTask<String, Void , HashMap<String, Boolean> > {
	
	
	public Activity activityContext;
	private ProgressDialog pDialog;
	private int timeOut = 2000 ; //ms
	private int HTTP_GOOD_REQUEST = 200;
	private HashMap<String, Boolean> conState = new HashMap<String, Boolean>();

    public ConnectionDetector(Activity ActCon) {
    	activityContext = ActCon;
   
	}
    

    
    /**
		 * @info
		 * ���� ��������� � ���������� �������  ����������� ������ ���� ����� ������
		 * 
		 * @details
		 * ���� ��������� � ���������� �������   ����������� ������ ���� ����� ������
		 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(activityContext);
		pDialog.setMessage("����������..");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}



/**
	 * @return HashMap �� ��� ����������� ��������
	 * @info
	 * ��� ������� � ������� ��� �� �������
	 * @details
	 * 
	 * 
	 * */
	@Override
	protected HashMap<String, Boolean> doInBackground(String... args) {
			
		try {
			conState.put("serverState", ServersAreReachable(activityContext) );
			conState.put("serviceState", isConnectingToInternet(activityContext) );
		
			if( ServersAreReachable(activityContext) == true && 
				isConnectingToInternet(activityContext) == true){
				conState.put("connectionState", true) ;
			}
			else{
				conState.put("connectionState",false) ;
			}	
		}
		
		catch(Exception e){
			conState.put("serverState", false );
			conState.put("serviceState", false );
			conState.put("connectionState",false) ;
			
			e.printStackTrace();
		}

		return conState;
	}

	/**
		 * @info
		 *  ���� ����������� � ����������, ������������� �� dialog ��� � ��������
		 * ����������� ��� ����� ����
		 * 
		 * @details
		 * �� ������� ������ ������� ������ � ������� �������������,
		 * ����������� ����������� ������� ������ ��� ���������� � ��������.
		 * 
		 * **/
	@Override
	protected void onPostExecute(HashMap<String, Boolean> result) {
		// dismiss the dialog once done
		pDialog.dismiss();
		pDialog = null;

		//Log.d("servicestate - serverstate-con",String.valueOf(serviceState)+"-"+String.valueOf(serverState)+"-"+String.valueOf(connectionState));
		
		if(result.get("serviceState") == false) {
			new showAlertMessage(activityContext ,"��!�������� ��������!!!","������� �� GPS �/��� to wifi/3g");
		}
		else if(result.get("serverState") == false){
			new showAlertMessage(activityContext ,"��!�������� ��������!!!","�������� ������������ �� ���� servers! ��������� ��������.");
		}
		
	}
//=====================================================

/**
 * @info
 * ������� ��� ��� ������ �������������� ��� server
 * 
 * @details
 * ������ ��������� �� ����� ��� request ���� ip ��� server,
 * �������� ��� timeout 3 sec.
 * �� ����� �� ���������� � server �� ������� ��� ������ response (HTTP 200) ,
 * ����������� ��� ������� ������� 
 *
 * @return
 * true  : ���� ������� �������
 * false : ���� ��� ����� ������ � ������� ���� server
 * 
 */
private  boolean ServersAreReachable(Activity activContext) {
       
	
        final ConnectivityManager connMgr = (ConnectivityManager) activContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            //  Some sort of connection is open, check if server is reachable
            try {
                URL url = new URL("http://"+globalVariables.getInstance().getIP_ADDRESS());
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
              
                urlc.setConnectTimeout(timeOut); 
                urlc.connect();
                if (urlc.getResponseCode() == HTTP_GOOD_REQUEST) { 
                    return true;
                } else { // Anything else is unwanted
                    return false;
                }
            } catch (IOException e) {
            	
            }
        } 
        
            return true;  
    }
	
//===============================================================================
	/**
	 * @info
	 * ������� ������� ��� �� �� ������� ������� ��� �������� ��� �� �� internet(wifi � 3G),
	 * ����� ��� �� �� gps ����� �������.
	 * 
	 * @details
	 * �������������� ��� ������������� ������� ��� android (LocationManager,ConnectivityManager),
	 * ��� �� ��������� �� ������� ������ �������������
	 * 
	 * @return
	 * true  : �� ����� ������� �� GPS ��� ������ �������� ��� internet
	 * false : �� ��� ������ ���� ��� ��� ��� �� ��������
	 */
	private boolean isConnectingToInternet(Activity activContext)
	{
		 
		try {
			LocationManager GpsSservice = (LocationManager)activContext.getSystemService(Context.LOCATION_SERVICE);
			ConnectivityManager connManager =(ConnectivityManager)activContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			boolean GpsEnabled = GpsSservice.isProviderEnabled(LocationManager.GPS_PROVIDER);
			NetworkInfo info = connManager.getActiveNetworkInfo();
			
			
			if( !GpsEnabled) {
				
				return false;
			}
			else if(info.getState() != NetworkInfo.State.CONNECTED ) {  
				
				return false;
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	



}
