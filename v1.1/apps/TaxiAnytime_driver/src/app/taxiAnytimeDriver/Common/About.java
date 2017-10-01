package app.taxiAnytimeDriver.Common;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;
import app.taxiAnytimeDriver.R;


public class About extends Activity {
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu_about);
		
		TextView aboutTextView = (TextView)findViewById(R.id.about);
		aboutTextView.setText(Html.fromHtml("<h3>Taxi anytime</h3> \n" +
				"Έκδοση 1.0<br> \n" +
				"Copyright 2013<br>  \n" +
				"<b>www.taxianytime.hostzi.com</b><br><br> \n \n" + 
				"Η εφαρμογή σχεδιάστηκε απο τους \n" +
				"Χατζημιχαήλ Γεώργιος <br> \n" +
				"Τσεγγελίδης Φίλανδρος <br>"));

		aboutTextView.setLinkTextColor(Color.WHITE);
		Linkify.addLinks(aboutTextView, Linkify.ALL);
	}
	
	
	
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
	
	
	
	
	
	
	
	
	
	
	

}
