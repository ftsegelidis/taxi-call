package app.taxiAnytimeCustomer.DriversList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import app.taxiAnytimeCustomer.R;
import app.taxiAnytimeCustomer.Common.ConnectionDetectorTask;
import app.taxiAnytimeCustomer.Common.globalVariables;
import app.taxiAnytimeCustomer.Common.httpJSONParser;


/**
 * @info
 * ����� �������� ��� ��� ���������� ��� listview ��� �������� 
 * ��� ����� ���� ��� �������������� ������ ���� ������� �� ��� �����.
 */
public class ListViewShowComments extends ListActivity {
	/** Called when the activity is first created. */
	


protected String driverDevId;
protected ListView myListView ;
protected Activity thisActivity;
	

public Activity getThisActivity() {
	return thisActivity;
}

/**
 * @info
 * ������������ activity
 * 
 * @details
 * ������ ������� ��� �� �� ������� ������������ ��� internet ��� �� ������ gps �������
 * ���� �������� ��������� �� ������ ���� ��� ������� ��� ��� ������������ �����
 *  
 * 
 */	
    @Override
public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_listview_show_comments);
        myListView = (ListView)findViewById(android.R.id.list);
        thisActivity = this;

        ArrayList<CommentsDetails> items_details = null;
        
        SharedPreferences prefs = getSharedPreferences("myOrder", MODE_PRIVATE);
        driverDevId = prefs.getString("selectedDriverDevId",null);

				try {
					if ( new ConnectionDetectorTask(this).execute().get().get("connectionState") ){
					
						items_details = new setListViewDataTask (driverDevId ).execute().get();
					
						myListView.setAdapter(new ItemListBaseAdapter(this, items_details));

					}
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
       
}//onCreate
    
    
    @Override
    public ListView getListView() {
    	
    	return super.getListView();
    }
    
//===============================================================================   
	/** 
     * @info 
     * ������� ��� �� "����������" �� listview
     * 
     * @details 
     * ��� ��������� �� �������� (������ ��� ����� �� ������)  ��� �� ����������� ����� ��� listview.
     */
    
    
    protected class setListViewDataTask extends AsyncTask<String,Void,  ArrayList<CommentsDetails>> {

    	private ArrayList<CommentsDetails> results = new ArrayList<CommentsDetails>();
    	private ProgressDialog pDialog;	
    	private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
    	private JSONObject myjobg = null;  
    	private httpJSONParser json = new httpJSONParser();
    	private String driverID;
    	
    	public setListViewDataTask (String cst) {
			//  Auto-generated constructor stub
    		driverID = cst;
		}

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListViewShowComments.this);
			pDialog.setMessage("����������..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
    	/**
  		 * @info
  		 * ��� ������� � ����������� �� �� ���� �������� http request ��� ��� 
  		 * ������������� �� ������������ 
  		 * 
  		 * 
  		 * 
  		 * */
    	@Override
		protected  ArrayList<CommentsDetails> doInBackground(String... params) {

			parameters.add(new BasicNameValuePair("driverDevID",driverID));
			myjobg = json.makeHttpRequest(globalVariables.getInstance().getBASE_PATH()+"/scripts/showCustomerComments.php",parameters);

			try{
				for(int i=0;i<myjobg.getJSONArray("usercomments").length();i++){
					CommentsDetails item_details = new CommentsDetails();
			
					item_details.setName(myjobg.getJSONArray("usercomments").getJSONObject(i).get("fullname").toString() );
					item_details.setComment( myjobg.getJSONArray("usercomments").getJSONObject(i).get("comment").toString() ) ;
			    	
					results.add(item_details);
		    	}
				
			} 
			catch (JSONException e) 
			{
				//  Auto-generated catch block
				e.printStackTrace();
			}
			
	    	return results;
 
	}
    	
    @Override
    protected void onPostExecute(ArrayList<CommentsDetails> result) {
    	pDialog.dismiss();
		pDialog = null;
    }
    	

}
//=====================================================
private static class ViewHolder {
	public TextView txt_DriverName;
	public EditText comments;

	
}


//==============================================================================================

public class ItemListBaseAdapter extends BaseAdapter {
	

	private  ArrayList<CommentsDetails> commentsArrayList;
	
	private LayoutInflater l_Inflater;

	/**
	 * @param context �� ����������� ��� �������������� ��� ����� �� listview
	 * @param results ��� �� �������� ��� listview
	 * 
	 * @info
	 * ������� �� listview �� ��� �� �������� ���� ��� �� �������� �� �� ����������������
	 * 
	 * @details
	 * ������� �� listview �� ��� �� �������� ���� ��� �� �������� �� �� ����������������
	 */
	public ItemListBaseAdapter(Context context, ArrayList<CommentsDetails> results) {
		commentsArrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return commentsArrayList.size();
	}

	public Object getItem(int position) {
		return commentsArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * @param context position � ���� ��� ������������ ���� ���� �����
	 * @param convertView �� ������� ��� ������ ��� �� ����� �� ������� �� ��������
	 * @param parent ��� �� Listview ��� �� ����� �� ������� ��������
	 * 
	 * @info
	 * ������� ��� ������� �� �������� ���� ������������� ���� ���� ��� listview
	 * 
	 * @details
	 * ������� ��� ������� �� �������� ���� ������������� ���� ���� ��� listview
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_details_show_comments, null);
			holder = new ViewHolder();
			holder.txt_DriverName = (TextView) convertView.findViewById(R.id.name);
			holder.comments = (EditText)convertView.findViewById(R.id.editTextComments);
			convertView.setTag(holder);
	
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.txt_DriverName.setText(commentsArrayList.get(position).getName());
		holder.comments.setText( commentsArrayList.get(position).getComment());
		

		return convertView;
	}
	
	
}	

//===============================================================================

@Override
public void onBackPressed() {
	finish();
	super.onBackPressed();
}


} //end of class file