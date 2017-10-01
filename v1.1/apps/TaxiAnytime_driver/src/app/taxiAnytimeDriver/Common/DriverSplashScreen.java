package app.taxiAnytimeDriver.Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import app.taxiAnytimeDriver.R;




public class DriverSplashScreen extends Activity {
	
	private ImageView imageView;
	private int activityTimeOut = 3000; // ������ �� ms
	private checkGoogleServices chkServices;

	
	

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		setImageView((ImageView)findViewById(R.id.splashImage));
		
		chkServices = new checkGoogleServices( getApplicationContext() );
		 try {
			  if ( new ConnectionDetector(this).execute().get().get("connectionState") ){
				  
				  if(chkServices.isGoogleMapsInstalled() && chkServices.isGoogleServicesEnabled())
					  startMainActivity();
				  else
					  new showAlertMessage(this ,"���� ������!!","������� �� ����� �������������� �� google maps & play");
			  }
  
			} catch (Exception e) {
				e.printStackTrace();
				new showAlertMessage(this ,"��! ���� ���� ������!!!","��������� ��������");
			}
		
		
	
    }

//=========================================================
	  
private void startMainActivity()
{
	Handler handler = new Handler();
	 
    // run a thread after N seconds to start the home screen
    handler.postDelayed(new Runnable() {

        @Override
        public void run() {

        	finish();
            // start the home screen

          	  Intent intent = new Intent(DriverSplashScreen.this, LoginActivity.class);
          	  startActivity(intent);
     		
         
        }

    }, activityTimeOut); 
}

public ImageView getImageView() {
	return imageView;
}

public void setImageView(ImageView imageView) {
	this.imageView = imageView;
}
	  

}
