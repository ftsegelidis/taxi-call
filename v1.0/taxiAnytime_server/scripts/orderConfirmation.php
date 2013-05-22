<?php
/*
@info
Αρχείο για την ενημέρωση της κατάστασης της παραγγελίας.
Θυμίζουμε :
orderState = 1 : Υπάρχει έτοιμη παραγγελία
orderState = 2 : Την έχει αποδεχθεί ο οδηγός
orderState = 3 : Την έχει αποδεχθεί ο πελάτης, και αυτό είναι το τελικό στάδιο της παραγγελίας.

*/

//header('Content-Type: text/html; charset=utf-8');
header('Content-type=application/json; charset=utf-8');
$jsonResponse = array();
require_once __DIR__ .'/db_connect.php';
$database = new DB_CONNECT();


$deviceID = null;
$orderID = null;

/*
$_REQUEST["type"] = "customer";
$_REQUEST["orderid"] ="1";
$_REQUEST["customerid"] = "/356778781788245";
//$_REQUEST["driverid"] = "/356778781788245";
*/

if( isset($_REQUEST["type"]) ){

	switch( $_REQUEST["type"] ){
		case "driver";{
			if(isset($_REQUEST["driverid"]) && isset($_REQUEST["orderid"])){
				$deviceID = $_REQUEST["driverid"];
				$orderID = $_REQUEST["orderid"];
			}
			else{
				$deviceID = "-";
				$orderID = "-";
			}
			
			mysql_query("SET NAMES UTF8");
			$sql = "UPDATE orders
			SET orderState = 2
			WHERE taxidriverFK = (SELECT driverID FROM taxidriver WHERE driverDeviceID = '".$deviceID."' ) AND orderID ='".$orderID."' " ;
			break;
		}
		case "customer":{
			if(isset($_REQUEST["customerid"]) && isset($_REQUEST["orderid"])){
				$deviceID = $_REQUEST["customerid"];
				$orderID = $_REQUEST["orderid"];
			}
			else{
				$deviceID = "-";
				$orderID = "-";
			}
			mysql_query("SET NAMES UTF8");
			$sql = "UPDATE orders
			SET orderState = 3
			WHERE customerFK = (SELECT customerID FROM customer WHERE customerDeviceID ='".$deviceID."') AND orderID='".$orderID."'  " ;
			break;
		}
	}

	$result = mysql_query($sql);	 //mysql_query επιστρέφει TRUE on σε επιτυχία ή FALSE σε λάθος		
	
	if ( mysql_affected_rows() > 0  ) {
		$jsonResponse["message"] = "updated";
		$jsonResponse["success"] = 1;

		// echoing JSON response
		print json_encode($jsonResponse);
	} 
	else {
		$jsonResponse["success"] = 0;
		$jsonResponse["message"] = "some error";

		// echo no users JSON
		print json_encode($jsonResponse);
	}
}	
else{
	$jsonResponse["success"] = 0;
	$jsonResponse["message"] = "some error";

	// echo no users JSON
	print json_encode($jsonResponse);

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
