<?php
/*
@info
Κλάση για την προσκόμιση των στοιχείων εγγραφής χρήστη

@details
Επιστρέφονται τα στοιχεία του χρήστη που χρειάζονται για να γίνει το edit.
*/

header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();


$fetchData = new FetchData();
$fetchData->fetchDataToEdit($_REQUEST["deviceid"],$_REQUEST["usertype"]) ;

//$fetchData->fetchDataToEdit("/357777961700933","driver") ;

//=============================================================================================================
class FetchData
{
	private $jsonResponse;

	function __construct(){
	
	}
//=============================================================================================================
	public function fetchDataToEdit($deviceID,$userType){
		if( isset($deviceID) && isset($userType) ){
			switch($userType){
				case "customer":{
				
					$sql ="SELECT name,sirname,cellphone,town FROM customer
						   WHERE customerDeviceID ='".$deviceID."' LIMIT 1";
				
					break;
				}
				case "driver":{
					$sql ="SELECT name,sirname,cellphone,town,taxiPlateNumber,driverImageUrl FROM taxidriver
						   WHERE driverDeviceID ='".$deviceID."' LIMIT 1";
					break;
				}
				
			}

			mysql_query("SET NAMES UTF8");
			$result = mysql_query($sql);
			
	
			if (mysql_num_rows($result) >0 ){
				while( $row = mysql_fetch_assoc($result) ){
					$rows[] = $row ; //γέμισμα με τα αποτελέσματα
				}
				$this->jsonResponse["editDetails"] = $rows;
				$this->jsonResponse["success"] = 1;

				// echoing JSON response
				print json_encode($this->jsonResponse);
			} 
			else {
				$this->jsonResponse["success"] = 0;
				$this->jsonResponse["message"] = "not successful";
				print json_encode($this->jsonResponse);
			}
		
		}
		else{
			$this->jsonResponse["success"] = 0;
			$this->jsonResponse["message"] = "not successful";

			// echo no users JSON
			print json_encode($this->jsonResponse);
		}
		
	}//end function

}//end class


function debug($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}


?>