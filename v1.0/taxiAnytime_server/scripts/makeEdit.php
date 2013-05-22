<?php

/*
@info
Κλάση για την αλλαγή στοιχείων των χρηστών

@details
Δίνουμε την δυνατότητα στον χρήστη να μπορεί να αλλάξει στοιχεία όπως όνομα,επίθετο,τηλ κτλ

*/

header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();

$editor = new Edit();


if($_REQUEST["usertype"] == "customer"){

$editor->makeEdit(mysql_real_escape_string( $_REQUEST["cellphone"] ),mysql_real_escape_string( $_REQUEST["password"]),
						mysql_real_escape_string( $_REQUEST["town"]),mysql_real_escape_string( $_REQUEST["deviceid"]),
						null,null, $_REQUEST["usertype"] );


}
else{
$editor->makeEdit(mysql_real_escape_string( $_REQUEST["cellphone"] ),mysql_real_escape_string( $_REQUEST["password"]),
						mysql_real_escape_string( $_REQUEST["town"]),mysql_real_escape_string( $_REQUEST["deviceid"]),
						mysql_real_escape_string( $_REQUEST["taxiplate"]),$_REQUEST["imageUrl"], $_REQUEST["usertype"] );

}


//for debugging
/*
$editor->makeEdit("695345345","newleme",
						"serres_new","/357777961700933",
						"ereNEWre-345234",null, "driver" );

*/


//====================================================================
class Edit
{

	private $jsonResponse;
	private $cellphone;
	private $password ;
	private $town;
	private $deviceid;
	private $imageurl;
	private $sqlCheckIfExists;
	
	private $taxiplate;

	
	private $sql ;
	
	function __construct()
	{
		$this->jsonResponse = array();
		$this->cellphone = NULL;
		$this->password = NULL;
		$this->town = NULL;
		$this->deviceid = NULL;
		$this->sqlCheckIfExists = NULL;
		
		$this->taxiplate = NULL;
		$this->imageurl = "";
		
		$this->sql = "";

	}

	public function makeEdit($cellphone,$password,$town,$deviceid,$taxiplate,$imageurl,$userType) {
								
		switch($userType)
		{
			case "customer":{
			
				if( isset($cellphone) && isset($password) && isset($deviceid) && isset($town)  ) {
					
					
					$this->cellphone = $cellphone;
					$this->password = $password;
					$this->deviceid = $deviceid;
					$this->town = $town;
					
					
				
						$this->sql = "UPDATE customer
									  SET 
									  cellphone = '".$this->cellphone."',
									  password = '".$this->password."',
									  town = '".$this->town."'
									  WHERE customerDeviceID = '".$this->deviceid."' ";
				
					
				}
				break;
			}
			case "driver":{

				$this->imageurl = $imageurl ;
				if( isset($cellphone)  && isset($password) && isset($deviceid) && isset($town) && isset($taxiplate) )  //Αν έχουν τιμή
				{
					$this->cellphone = $cellphone;
					$this->password = $password;
					$this->town = $town;
					$this->deviceid = $deviceid;
					$this->taxiplate = $taxiplate;
					
					if($this->imageurl == ""){
						$this->imageurl ="-" ; 
					}
					
					
					$this->sqlCheckIfExists = mysql_query("SELECT *
										FROM taxidriver
										WHERE taxiPlateNumber ='".$this->taxiplate."' ");
										
									
					if (mysql_num_rows($this->sqlCheckIfExists ) > 0) { //αν υπάρχουν ήδη
					
						$this->sql = " "; //δίνουμε άδειο sql άρα δεν θα εκτελεστεί τπτ
						
					
					}
					else{
		
						$this->sql = "UPDATE taxidriver
									  SET 
									  cellphone = '".$this->cellphone."',
									  password = '".$this->password."',
									  town = '".$this->town."',
									  driverImageUrl = '".$this->imageurl."',
									  taxiPlateNumber = '".$this->taxiplate."'
									  WHERE driverDeviceID = '".$this->deviceid."' ";
					}
		
				}
				break;
			}
	
		} //end switch

	
			mysql_query("SET NAMES UTF8");
			 mysql_query($this->sql);
			  
			if( mysql_affected_rows() > 0 ) // αν έγινε ενημέρωση
			{
				$this->jsonResponse["success"] = 1;
				$this->jsonResponse["message"] = "successfully updated";
        
				// echoing JSON response
				echo json_encode($this->jsonResponse);
			}
			else
			{
				$this->jsonResponse["success"] = 0;
				$this->jsonResponse["message"] = "Error";

				echo json_encode($this->jsonResponse);
			}
	
	}
	
}


//==========================================
//for debug
function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}


?>