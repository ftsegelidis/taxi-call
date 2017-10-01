<?php
/*
@info
Κλάση υπεύθυνη για την διαδικασία ορθής εισαγωγής χρήστη.

*/

//header('Content-Type: text/html; charset=utf-8');
header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();


$login = new login();

$login->makeLogin( mysql_real_escape_string( $_REQUEST["username"] ),mysql_real_escape_string( $_REQUEST["password"] ),$_REQUEST["deviceid"],$_REQUEST["type"]);

//====================================================================
class login{
	private $jsonResponse;
	private $username ;
	private $password ;
	private $deviceid;
	private $usertype ;
	private $queryLogin;
	private $sql ;
	private $report_value ;
	
	function __construct(){
		$this->jsonResponse = array();
		$this->username = null;
		$this->password = null;
		$this->usertype = null;
		$this->deviceid = null;
		$this->queryLogin = "";
		$this->sql = "";
		$this->report_value = 5;
	}

//set
	public function setUsername($subject){
		$this->username = $subject;
	}

	
	public function setPassword($subject){
		$this->password = $subject;
	}
	
	public function setUserType($subject){
		$this->usertype  = $subject;
	}
	
	
	//get
	public function getUserName(){
		return $this->username;
	}
	
	public function getSql(){
		return  $this->sql ;
	}
	
	public function getPassWord(){
		return $this->password ;
	}
	public function getQueryLogin(){
		return $this->queryLogin;
	}
	
	public function GetUserType(){
		return $this->usertype;
	}
	
	
/*
@info
Μέθοδος για την διαδικασία ορθής εισαγωγής χρήστη.
Σε περίπτωση σωστής εισαγωγής έχουμε success "1" διαφορετικά "0"
*/
	
	public function makeLogin($user,$pass,$devid,$type)
	{
		if( isset($user) && isset($pass) && isset($devid) && isset($type) )  //Αν έχουν τιμή
		{
			$this->setUsername($user);
			$this->setPassword($pass);
			$this->setUserType($type);
			$this->deviceid = $devid;
		
			switch($this->GetUserType())
			{
				case "customer" :
				{
				
				
					$sqlCheckForBan = mysql_query("SELECT COUNT(*) 
											  FROM report 
											  WHERE reportTo = (SELECT customerID FROM customer WHERE customerDeviceID = '".$this->deviceid."' LIMIT 1) 
											  AND byUser = 'driver' LIMIT 1"); //driver γιατι έγινε από οδηγό το report
							
					$row = mysql_fetch_array($sqlCheckForBan);
					if ($row[0] >= $this->report_value ) //Αν έχει πάνω από 5 reports, στο row[0] είναι το αποτέλεσμα
					{
					
							$this->sql = ""; //δίνουμε άδειο sql άρα δεν θα εκτελεστεί τπτ
					}
					else
					{
					
							$this->sql = "SELECT username,password
									FROM  customer
									WHERE username = '".$this->getUserName()."' AND password = '".$this->getPassWord()."' LIMIT 1 "  ;
					}
					
					break;
				}
				case "driver" :
				{
				
					$sqlCheckForBan = mysql_query("SELECT COUNT(*) 
											  FROM report 
											  WHERE reportTo = (SELECT driverID FROM taxidriver WHERE driverDeviceID = '".$this->deviceid."' LIMIT 1) 
											  AND byUser = 'customer' LIMIT 1");// customer γιατί έχει από πελάτη το report
											  
					$row = mysql_fetch_array($sqlCheckForBan);
					if ($row[0] >= $this->report_value ) //Αν έχει πάνω από 5 reports, στο row[0] είναι το αποτέλεσμα από το query
					{
					
							$this->sql = ""; //δίνουμε άδειο sql άρα δεν θα εκτελεστεί τπτ
					}
					else
					{
				
						$this->sql ="SELECT username,password
						FROM  taxidriver
						WHERE username = '".$this->getUserName()."' AND password = '".$this->getPassWord()."' LIMIT 1 ";
						
					}
					break;
				}
				default:
					$this->sql ="";
					break;		
			}

			$this->queryLogin = mysql_query($this->getSql());
	
			if(mysql_num_rows( $this->getQueryLogin() ) == 1){ // αν βρέθηκε 
			
	
					$this->jsonResponse["success"] = 1;
					$this->jsonResponse["message"] = "successfully logged in.";
				
				echo json_encode($this->jsonResponse);
			}
			else{
				$this->jsonResponse["success"] = 0;
				$this->jsonResponse["message"] = "Wrong login";

				echo json_encode($this->jsonResponse);
			}
		}
		else
		{
			$this->jsonResponse["success"] = 0;
			$this->jsonResponse["message"] = "wrong login or reported ";

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