package app.taxiAnytimeDriver.Driver;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.Marker;


/**
 * @info
 * ��������� ����� ��� ��� �������� ��������� ��� �����.
 * ��������������� �� ���������� �� ��� DriverActivity ��� ��� ��������� �� ������������ ���� ���
 * �� ��������������� �������� items ���� ��� �����. 
 * �� items ���� ������������� �� arraylist
 * 
 *
 */
class  MarkerItems 
{
	
	public List<Marker> markerItems ;
    private  Context mContext;

    /**
     * @param defaultMarker �� ��������� ��� �� �����������
     * @param context �� ����������� ��� �������������� ��� �������
     * 
     * @info
     * ����������� ��� �����������
     * 
     */
	
	  public MarkerItems(Drawable defaultMarker,Context mycontext) 
	    {
		 
		  	
		  markerItems= new ArrayList<Marker>();
	      mContext = mycontext;


	    }
	  
	  
	  public void setDriverPositionOverlay( Drawable defaultMarker,Context mycontext )
	  {
		  
		  mContext = mycontext;
		 
		
	  }


	 
    
   public Context getmContext() {
	return mContext;
}
    
  
    
   
    
 
   

    
    
//on tap
    	 

    	/*
    	 * Toast toast = Toast.makeText(mContext,items.get(index).getSnippet(),Toast.LENGTH_SHORT);
    	toast.show();
    	Log.d("index-size",String.valueOf(index)+"-"+String.valueOf(items.size()));
    	Log.d("customerSize",String.valueOf(customers.size()));
    	 Log.d("index-size-max_index",String.valueOf(index)+"-"+String.valueOf(items.size()) + String.valueOf(maxIndex) );
    	
    	try {
    		OverlayItem item = items.get(index);
    		

    			if(getCustomer()!= null && size()>0 && index>0 && item.getSnippet().equals( "new_customer" ) ){

    				DriverActivity.makeOrder(getCustomer() ,driver);
    				removeCustomer();
    				//removeItem(index);
    			
    				Toast toast = Toast.makeText(mContext,"� ������� �� ����������� ��� �� ���������� ���",Toast.LENGTH_LONG);
    				toast.show();

    			}

    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}

    	
    	//Log.d("index-size-mvsize",String.valueOf(index)+"-"+String.valueOf(items.size())+"-"+String.valueOf(DriverActivity.mv.getOverlays().size()));
    	
 */


    
    
//===================================================================================



  
    

    
    
     
}