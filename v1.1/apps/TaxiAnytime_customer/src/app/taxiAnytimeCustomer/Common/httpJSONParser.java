package app.taxiAnytimeCustomer.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



/**
 *@info 
 *  ����� ��� �� ���������� http post/get ��� ���� mysql (�� ����� json)
 * 
 * ���� ����� ���������� ��� �������� json ��� ��� http request ��� ������� , 
 * �� ����������� JSON �� java ���� �� �������� �� �� ��������������.
 *
 *@external_source
 *���� � ����� ������������� ������ ��� ����� ����� ������� ��������� android
 *��� ����� ��� ��� ����������� �� ��� ���� ���������
 *
 *����: www.androidhive.info
 */
public class httpJSONParser {

	InputStream is ;
	JSONObject jObj ;
	String json ;
	final int BUFFER_SIZE = 8192;

	
	public httpJSONParser() {
		is = null;
		jObj = null;
		json = null;
		
	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	/**
	 * @param url � ��������� ��� php scripts ���� ���� server ���, �� ����� ����� �������.
	 * @param method ������� ������������ �� ��� server (post � get)
	 * @param params �� ������� ��� php scripts ���, �� ���������� ����.
	 * 
	 * @info
	 * ������ ��� �������� ��� ��� ���� ���������
	 * 
	 * @details
	 * ���� ����� � ������� �� ��� ���� ������ ������� �� ������� �� �������.
	 * �� � ������� ����� post, ��������� ��� ������������ �������� ���� ����.
	 * �� � ������� ����� get, ���������� ������������ ��� ��� ���� ��� �� ������������.
	 * 
	 * @return
	 * JSONObject : �������� ����� �� ���������� ��� php scripts �� ����� JSON
	 */
	public JSONObject makeHttpRequest(String url,List<NameValuePair> params) {  

		// Making HTTP request
		try {
			
		
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				
			
			

		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			
		} 
		catch (ClientProtocolException e){
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),BUFFER_SIZE);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} 
		catch (Exception e){
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try{
			jObj = new JSONObject(json);
		} 
		catch (JSONException e){
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}
