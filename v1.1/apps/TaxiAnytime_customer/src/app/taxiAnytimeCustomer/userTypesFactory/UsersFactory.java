package app.taxiAnytimeCustomer.userTypesFactory;
/**
 * @info
 * ����� ��� ��� ���������� ������� �� �� ������ factory pattern
 * 
 *
 */
public class UsersFactory {
	
	public static Users createUser(String type)
	{
		 if (type.equals("customer")){
	            return new Customer();
	        }
		 else if (type.equals("driver")){
	            return new Driver();
	        }
	        return null;
	}

}

//���������� ������ : Users customer = UsersFactory.createUser("customer");