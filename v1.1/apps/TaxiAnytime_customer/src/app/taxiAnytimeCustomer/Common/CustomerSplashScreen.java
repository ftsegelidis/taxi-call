package app.taxiAnytimeCustomer.Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import app.taxiAnytimeCustomer.R;


/**
 * @info
 * Εισαγωγική κλάση, χρησιμοποιείται πρώτη για να καλωσορίσει τον χρήστη στην εφαρμογή.
 */
public class CustomerSplashScreen extends Activity {
	
	private ImageView imageView;
	private int activityTimeOut = 2000; // Χρόνος σε ms
	private checkGoogleServices chkServices;
	
	
	
	 /**
     * @info
     * Αρχικοποίηση activity
     * 
     * @details
     * Αντιστοιχούμε την όψη του xml αρχείου με το αντικείμενο imageview , ώστε να μπορούμε να τo διαχειριστούμε.
     * Έπειτα ελέγχουμε για το αν υπάρχει ενεργή σύνδεση στο internet(είτε wifi είτε 3g), καθώς και αν είναι 
     * ανοικτό το gps
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
					  new showAlertMessage(this ,"Κάτι λέιπει!!","Ελέγξτε αν έχετε ενεργοποιημένα τα google maps & play");
			  }
			  
			} catch (Exception e) {
				e.printStackTrace();
				new showAlertMessage(this ,"Ωχ! Κάτι πήγε στραβά!!!","Δοκιμάστε αργότερα");
			}

    }

//=========================================================
	  
/**
 * @info 
 * Μετά από κάποιο timeout (πχ 3 sec) ξεκινά η βασική δραστηριότητα login,
 *  και τερματίζεται η τρέχουσα(splash screen)
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



