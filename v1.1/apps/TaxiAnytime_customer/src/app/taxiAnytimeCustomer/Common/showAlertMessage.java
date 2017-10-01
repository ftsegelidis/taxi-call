package app.taxiAnytimeCustomer.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import app.taxiAnytimeCustomer.R;

/**
 * @info 
 * Κλάση για την διαχείριση των προηδοποιητικών μυνημάτων
 * Μπορεί να χρησιμοποιηθεί από όλες τις κλάσεις
 *
 */
public class showAlertMessage {
	
private Activity actContext ;
private String title ;
private String message ;
	
	public showAlertMessage() {
		actContext = null;
		title = null;
		message = null;
		
	}
	public showAlertMessage(Activity act,String tlt,String msg) {
		
		actContext = act;
		title = tlt;
		message = msg;
		
		showConnectionAlertMessage(actContext,title,message);
	}
	
		/**
		 * 
		 * 
		 * @param message - Το μήνυμα
		 * 
		 * @info
		 * Συνάρτηση εμφάνισης μυνήματος σφάλματος
		 * 
		 * @details
		 *Συνάρτηση εμφάνισης μυνήματος σφάλματος
		 * 
		 * 
		 *
		 * */
		
		 public void showConnectionAlertMessage (Activity actAcontext , String title,String message) {
			
			 AlertDialog alertDialog = new AlertDialog.Builder(
					 actAcontext ).create();

			 	alertDialog.setTitle(title);
			 	alertDialog.setMessage(message);
			 	alertDialog.setIcon(R.drawable.tick);

			 	// Setting OK Button
			 	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			 	public void onClick(DialogInterface dialog, int which) {
			 
	       
	         }
			 });

			 	// Showing Alert Message
			 	alertDialog.show();
		} 

}
