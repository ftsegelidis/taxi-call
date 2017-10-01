package app.taxiAnytimeDriver.userTypesFactory;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @info
 * Abstract ����� ��� ��� ������ �������� ������ ��� ������
 * ��������������� ��� �� factory pattern 
 *
 */
public abstract class Users implements Parcelable {

	 public abstract void setLatitude(double lat);
	 public abstract void setLongitude(double lng);
	 public abstract void setDeviceID(String devid);
	 public abstract double getLatitude();
	 public abstract double getLongitude();
	 public abstract String getDeviceID();

	 
	 
}
