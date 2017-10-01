<?php

/*
@info
Κλάση υπεύθυνη για την καταχώρηση νέου χρήστη στο σύστημα (τόσο πελάτη όσο και οδηγού)

*/


header('Content-type=application/json; charset=utf-8');

require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();

$register = new Register();


if($_REQUEST["usertype"] == "customer")
{
						//αποτροπή απλών sql injection
$register->makeRegister(mysql_real_escape_string( $_REQUEST["name"] ),mysql_real_escape_string( $_REQUEST["sirname"]),
						mysql_real_escape_string( $_REQUEST["cellphone"] ),mysql_real_escape_string( $_REQUEST["birthday"] ),
						mysql_real_escape_string( $_REQUEST["username"]),mysql_real_escape_string( $_REQUEST["password"]),
						mysql_real_escape_string( $_REQUEST["mail"]),mysql_real_escape_string( $_REQUEST["town"]),
						mysql_real_escape_string( $_REQUEST["deviceid"]),null, $_REQUEST["usertype"] );


}
else
{
$register->makeRegister(mysql_real_escape_string( $_REQUEST["name"] ),mysql_real_escape_string( $_REQUEST["sirname"]),
						mysql_real_escape_string( $_REQUEST["cellphone"] ),mysql_real_escape_string( $_REQUEST["birthday"] ),
						mysql_real_escape_string( $_REQUEST["username"]),mysql_real_escape_string( $_REQUEST["password"]),
						mysql_real_escape_string( $_REQUEST["mail"]),mysql_real_escape_string( $_REQUEST["town"]),
						mysql_real_escape_string( $_REQUEST["deviceid"]),mysql_real_escape_string( $_REQUEST["taxiplate"]), $_REQUEST["usertype"] );

}





//for debugging

/*
$register->makeRegister("sfwgerg","sdsbbbh",
						"693434535","12/02/1987",
						"customernew","sggrhtr",
						"mailwdfgf@mail.gr","town","devid",
						"ere-4564", "driver" );


*/


//====================================================================
class Register
{

	private $jsonResponse;
	private $name;
	private $sirname;
	private $cellphone;
	private $birthday;
	private $username ;
	private $password ;
	private $mail;
	private $town;
	private $deviceid;
	private $sqlCheckIfExists;
	
	private $taxiplate;

	
	private $sql ;
	
	function __construct(){
		$this->jsonResponse = array();
		$this->name = NULL;
		$this->sirname = NULL;
		$this->cellphone = NULL;
		$this->birthday = NULL;
		$this->username = NULL;
		$this->password = NULL;
		$this->mail = NULL;
		$this->town = NULL;
		$this->taxiplate = NULL;
		$this->deviceid = NULL;
		$this->sqlCheckIfExists = NULL;

		$this->sql = "";

	}


/*
@info
Μέθοδος για την καταχώρηση νέου χρήστη στο σύστημα (τόσο πελάτη όσο και οδηγού)

Επιστρέφει σε μορφή json την απόκριση "success"= 1 σε επιτυχή αλλαγή και "success"= 0 σε μή επιτυχή
*/
	public function makeRegister($name,$sirname,$cellphone,$birthday,$username,
								$password,$mail,$town,$deviceid,$taxiplate,$userType) {
								
		switch($userType){
		
			case "customer":{
			
				if( isset($name) && isset($sirname) && isset($cellphone) && isset($birthday) 
					&& isset($username) && isset($password) && isset($deviceid) && isset($mail) && isset($town)  ) {
					
					$this->name = $name;
					$this->sirname = $sirname;
					$this->cellphone = $cellphone;
					$this->birthday = $birthday;
					$this->username = $username;
					$this->password = $password;
					$this->mail = $mail;
					$this->deviceid = $deviceid;
					$this->town = $town;
					
					
					
					$this->sqlCheckIfExists = mysql_query("SELECT *
										FROM customer
										WHERE username ='".$this->username."' OR email= '".$this->mail."' ");
										
									
					if (mysql_num_rows($this->sqlCheckIfExists ) > 0) { //αν υπάρχουν ήδη
					
						$this->sql = " "; //δίνουμε άδειο sql άρα δεν θα εκτελεστεί τπτ
						
					
					}
					else {
					
				
						$this->sql = "INSERT INTO customer
								SET 	name ='".$this->name."' ,
								sirname = '".$this->sirname."',
								cellphone = '".$this->cellphone."',
								birthdate = DATE_FORMAT ('".$this->birthday."','%Y-%m-%d'),
								username = '".$this->username."',
								password = '".$this->password."',
								customerDeviceID = '".$this->deviceid."',
								town = '".$this->town."',
								email = '".$this->mail."' " ;
								
					}
				}
				break;
			}
			case "driver":{

				if( isset($name) && isset($sirname) && isset($cellphone) && isset($birthday) && isset($username) && isset($password) && isset($deviceid) && isset($mail) && isset($town) && isset($taxiplate) )  //Αν έχουν τιμή
				{
					$this->name = $name;
					$this->sirname = $sirname;
					$this->cellphone = $cellphone;
					$this->birthday = $birthday;
					$this->username = $username;
					$this->password = $password;
					$this->mail = $mail;
					$this->town = $town;
					$this->deviceid = $deviceid;
					$this->taxiplate = $taxiplate;
		
		
				
					//Έλεγχος για το αν υπάρχουν ήδη από άλλο χρήστη τα στοιχεία
					$this->sqlCheckIfExists = mysql_query("SELECT * 
										FROM taxidriver
										WHERE username ='".$this->username."' OR email= '".$this->mail."' ");
										
					if (mysql_num_rows($this->sqlCheckIfExists ) > 0) { //αν υπάρχουν ήδη
					
						$this->sql = " "; //δίνουμε άδειο sql άρα δεν θα εκτελεστεί τπτ
					
					}
				
					else {
			
						$this->sql = "INSERT INTO taxidriver
						SET 	name ='".$this->name."' ,
								sirname = '".$this->sirname."',
								cellphone = '".$this->cellphone."',
								birthdate = DATE_FORMAT ('".$this->birthday."','%Y-%m-%d'),
								username = '".$this->username."',
								password = '".$this->password."',
								town = '".$this->town."',
								totalRate = 0,
								isAvailable = 0,
								driverDeviceID = '".$this->deviceid."',
								taxiPlateNumber = '".$this->taxiplate."',
								driverImageUrl = 'http://192.168.2.100/taxiAnytime_server/androidBot.jpg',
								email = '".$this->mail."' " ;
					}
					
				}

				break;
			}
	
		} //end switch
	
	
			mysql_query("SET NAMES UTF8");
			if( mysql_query($this->sql) > 0 ) // αν έγινε εισαγωγή
			{
       
				$this->jsonResponse["success"] = 1;
				$this->jsonResponse["message"] = "successfully registered";
				
        
				// echoing JSON response
				echo json_encode($this->jsonResponse);
				
			}
			else
			{
				$this->jsonResponse["success"] = 0;
				$this->jsonResponse["message"] = "error";

				echo json_encode($this->jsonResponse);
			}
		
		
	}
	
	
}

/*
@info
Ανεξάρτηση μέθοδος για αποσφαλμάτωση

@details
Γράφει σε αρχείο τα αποτελέσματα

*/
function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}


?>