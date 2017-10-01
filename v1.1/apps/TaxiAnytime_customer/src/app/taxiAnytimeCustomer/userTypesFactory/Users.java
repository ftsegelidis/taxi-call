package app.taxiAnytimeCustomer.userTypesFactory;

/**
 * @info
 * Abstract ����� ��� ��� ������ �������� ������ ��� ������
 * ��������������� ��� �� factory pattern 
 *
 */

public abstract class Users {

	 public abstract void setLatitude(double lat);
	 public abstract void setLongitude(double lng);
	 public abstract void setDeviceID(String devid);
	 public abstract double getLatitude();
	 public abstract double getLongitude();
	 public abstract String getDeviceID();

	 
 
	 
}
