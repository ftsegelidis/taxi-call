package app.taxiAnytimeDriver.Driver;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import app.taxiAnytimeDriver.R;
import app.taxiAnytimeDriver.Common.About;
import app.taxiAnytimeDriver.Common.Contact;
import app.taxiAnytimeDriver.Common.EditProfileActivity;
import app.taxiAnytimeDriver.Common.ReportActivity;


/**
 * @info
 * ������������� ��� ���������� ��� ���������
 *
 */

public class DriverEndRideActivity extends Activity {
	
	private static final String BOOKMARK_TITLE = "Taxi Anytime";
	private static final String BOOKMARK_URL ="www.taxianytime.hostzi.com";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.endride_activity);
	
	}

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
	
	
	
	public void ImgBtnEndRide(View v)
	{
		
		finish();
		Intent i = new Intent(getApplicationContext(),DriverActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		
		
	}
	
	
	@Override
	protected void onDestroy() {
		//  Auto-generated method stub
		
		
		
		super.onDestroy();
	}
	
	
	//=====================================================================
	//menus
		
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
	    	Toast.makeText(this, "��� �������� ����!!", Toast.LENGTH_SHORT).show();
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
}

