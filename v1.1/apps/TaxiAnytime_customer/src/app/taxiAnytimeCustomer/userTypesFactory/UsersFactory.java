package app.taxiAnytimeCustomer.userTypesFactory;
/**
 * @info
 * Κλάση για την δημιουργία χρηστών με τη μέθοδο factory pattern
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

//Παράδειγμα κλήσης : Users customer = UsersFactory.createUser("customer");