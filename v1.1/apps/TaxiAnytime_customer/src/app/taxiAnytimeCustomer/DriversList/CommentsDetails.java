package app.taxiAnytimeCustomer.DriversList;

/**
 * @info
 * Κλάση που περιέχει όλες τις πληροφορίες σχετικά με τα σχόλια και τους πελάτες
 *
 */
public class CommentsDetails {
	
	private String name ;
	private String comment; 
	
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
public void setComment(String com) {
	this.comment = com;
}
public String getComment() {
	return comment;
}
	
}

