package app.taxiAnytimeCustomer.Customer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import app.taxiAnytimeCustomer.R;

import app.taxiAnytimeCustomer.Common.Contact;

import app.taxiAnytimeCustomer.Common.About;
import app.taxiAnytimeCustomer.Common.ConnectionDetectorTask;
import app.taxiAnytimeCustomer.Common.EditProfileActivity;
import app.taxiAnytimeCustomer.Common.ReportActivity;
import app.taxiAnytimeCustomer.Common.globalVariables;
import app.taxiAnytimeCustomer.Common.httpJSONParser;


/**
 * @info
 *����� ������� �� ��� ����������� ��� ��������� ��� ������ ��� �����������.
 */
public class CommentsAndRating extends Activity {

	protected RatingBar ratingBar;
	protected TextView txtRatingValue;
	protected EditText txtComments;
	protected Button btnMakeReport;
	protected CheckBox checkConfirmOrder;
	protected Button btnSubmit;
	
	
	
	private static final String BOOKMARK_TITLE = "Taxi Anytime";
	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
	
	
  /**
  * @info
  * ������������ menu buttons
  * 
  * @details
  * ������������ menu buttons
  */
	
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater menuInflater = getMenuInflater();
	      menuInflater.inflate(R.layout.menu_items, menu);
	      return true;
	  }
	  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating);
		
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		
		txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
		txtComments = (EditText) findViewById(R.id.comments);
	
	
		addListenerOnRatingBar();
		
	
	
	}

	
	/**
	 * @info
	 * �� ������� � ������� �� rating �� ��� ��������� �� ���� ��������.
	 * 
	 */
	public void addListenerOnRatingBar() {

		
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				txtRatingValue.setText(String.valueOf(rating));

			}
		});
	}


	 /**
	 * @param v  �� View ��� �� button
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * 
	 * @info
	 *  �������� �� ������ ������� ������� ��� �������
	 * @details
	 * ������ ������� ������� ��� �� �� ������� ������� �� �� internet ����� ��� �� ���� server,
	 * ��� ��� �������� ���������� �� �������� �� ��������� ����� ��� �� ������� ���� server
	 * �� �� ����������� ���� ����.
	 *   
	 * 
	 */
	public void onClkSubmit(View v) throws InterruptedException, ExecutionException {

		
      		 if(fieldsAreOk() == true ) {
      			 if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){
      				 new CreateCommentTask().execute();

      				 finish();
      	      		 Intent toListview = new Intent(CommentsAndRating.this,CustomerActivity.class);
      	      		 startActivity(toListview );
      			 }

      		 }
 		
}

private boolean fieldsAreOk(){
		
		if(this.txtComments.getText().toString().length() == 0 || (this.ratingBar.getRating() == 0.0)){
			Toast toast = Toast.makeText(CommentsAndRating.this.getApplicationContext(),"����������� ������� �/��� ������ ������.", Toast.LENGTH_LONG);
			toast.show();
	
			return false;
		}
		else
			return true;
	
		}

//=====================================================
	
	
	/** 
	 * 
	 * @info
	 * �� ��� ����� ���� ������ � �������� �������
	 */
protected class CreateCommentTask extends AsyncTask<String, String, String> {

	
	private ProgressDialog pDialog;
	private boolean successCheck;
	/**
		 * @info
		 * ���� ��������� � ����������  ����������� ������ ���� ����� ������
		 * 
		 * @details
		 * ���� ��������� � ���������� ����������� ������ ���� ����� ������ ��� �� ���
		 * ���������� ��� ���� �������
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CommentsAndRating.this);
			pDialog.setMessage("���������� �������..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

	
		protected String doInBackground(String... args) {


			SharedPreferences orderPrefs = getSharedPreferences("myOrder", MODE_PRIVATE);
			SharedPreferences fromCustomer = getSharedPreferences("fromCustomer", MODE_PRIVATE);
					
			// Building Parameters
			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("rate",String.valueOf( ratingBar.getRating())) );
			parameters.add(new BasicNameValuePair("comment",String.valueOf( txtComments.getText())) );
			parameters.add(new BasicNameValuePair("driverDevId",String.valueOf( orderPrefs.getString("selectedDriverDevId", "") ) ) );
			parameters.add(new BasicNameValuePair("orderid",String.valueOf(orderPrefs.getString("orderid", "")  ) ) );
			parameters.add(new BasicNameValuePair("customerDevID",String.valueOf(fromCustomer.getString("customerdevid", "")  ) ) );
			
			
			try {
				// getting JSON Object
				JSONObject myjobg = null; 
				httpJSONParser json = new httpJSONParser();
				myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/makeCommentsRatings.php","POST",parameters);


			// check for success tag
			
			
				if (Integer.parseInt(myjobg.get("success").toString()) == 1) {
					// successfully commented
					successCheck = true;

				} else {
					// failed to make a comment
					successCheck = false;

				}
				
			}
			catch (JSONException e) {
				e.printStackTrace();

			}
			return null;
		}

	
		protected void onPostExecute(String file_url) {
			
			pDialog.dismiss();
			pDialog = null;
			
			if(successCheck){
				Toast.makeText(getApplicationContext(),"�� ������ ������������!!", Toast.LENGTH_LONG).show();
			}
			else{
				
				Toast.makeText(getApplicationContext(),"������ !!! ��� ������������!!", Toast.LENGTH_LONG).show();
			}
			
			
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
      
      	try {
			call();
		} catch (Exception e) {
			e.printStackTrace();
		}
      	return true;
      case R.id.menu_report:
      	
      	report();
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
   * @throws ExecutionException 
 * @throws InterruptedException 
 * @info
   * ������������ ���� ������������� ��� ������/sms
   */
  public void call() throws InterruptedException, ExecutionException{
	  Contact contact = new Contact(this);
	  contact.makeContact();
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
  	share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
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
  	
	  Intent intent = new Intent(this.getApplicationContext(), About.class);
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
	
	
	
	
}//end of class