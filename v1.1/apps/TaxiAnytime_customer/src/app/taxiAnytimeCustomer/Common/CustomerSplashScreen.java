package app.taxiAnytimeCustomer.Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import app.taxiAnytimeCustomer.R;


/**
 * @info
 * ���������� �����, ��������������� ����� ��� �� ����������� ��� ������ ���� ��������.
 */
public class CustomerSplashScreen extends Activity {
	
	private ImageView imageView;
	private int activityTimeOut = 2000; // ������ �� ms
	private checkGoogleServices chkServices;
	
	
	
	 /**
     * @info
     * ������������ activity
     * 
     * @details
     * ������������� ��� ��� ��� xml ������� �� �� ����������� imageview , ���� �� �������� �� �o ��������������.
     * ������ ��������� ��� �� �� ������� ������ ������� ��� internet(���� wifi ���� 3g), ����� ��� �� ����� 
     * ������� �� gps
     */
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		chkServices = new checkGoogleServices( getApplicationContext() );
		
		
		setImageView((ImageView)findViewById(R.id.splashImage));

			 try {
			  if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){
				  
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
	  
/**
 * @info 
 * ���� ��� ������ timeout (�� 3 sec) ������ � ������ ������������� login,
 *  ��� ������������ � ��������(splash screen)
 */
private void startMainActivity(){
	
	Handler handler = new Handler();
    // run a thread after N seconds to start the home screen
    handler.postDelayed(new Runnable() {

        @Override
        public void run() {

        	finish();
            // start the home screen
          	  Intent intent = new Intent(CustomerSplashScreen.this, LoginActivity.class);
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



