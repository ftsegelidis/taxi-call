package app.taxiAnytimeCustomer.Common;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class checkGoogleServices {

	private Context context;
	
	public checkGoogleServices(Context con) {
		context = con;
		
	}
	
	public boolean isGoogleMapsInstalled()
	{
	    try
	    {
	        ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
	        return true;
	    } 
	    catch(PackageManager.NameNotFoundException e)
	    {
	        return false;
	    }
	}
	
	
	
	public boolean isGoogleServicesEnabled(){
		
		  int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable( context );
		  if (resultCode == ConnectionResult.SUCCESS){
		   return true;
		  }
		  else
			  return false;
		
	}
	
	
	
	
	
	
}
