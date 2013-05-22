<?php
/*
@info
Κλάση για την εμφάνιση σχολίων από άλλους πελάτες για τον συγκεκριμένο οδηγό


*/

//header('Content-Type: text/html; charset=utf-8');
header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();


$showCom = new ShowCustomerComments();
$showCom->showComments(  $_REQUEST["driverDevID"]  ) ;


//=============================================================================================================
class ShowCustomerComments
{
	private $jsonResponse;

	function __construct(){
	
		$this->jsonResponse = array();
	}
//=============================================================================================================
	public function showComments( $deviceID ){
	
		if( isset($deviceID) ){
	
				
			$sql ="SELECT com.comment,c.name,c.sirname 
				   FROM comments AS com INNER JOIN orders AS o 
				   ON(o.orderID =com.orderid) INNER JOIN taxidriver AS td 
				   ON (o.taxidriverFK = td.driverID) INNER JOIN customer AS c 
				   ON (o.customerFK = c.customerID)
				WHERE td.driverDeviceID = '".$deviceID."'";
	
			mysql_query("SET NAMES UTF8");
			$result = mysql_query($sql);
			
	
			if (mysql_num_rows($result) > 0 ){
			
				$this->jsonResponse["usercomments"] = array();
				$users = array();
					while ($row = mysql_fetch_array($result)){
						$users["comment"] = $row["comment"];
						$users["fullname"] = $row["name"]." ".$row["sirname"];
			
						array_push($this->jsonResponse["usercomments"],$users );
					}
			
				$this->jsonResponse["success"] = 1;
			
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