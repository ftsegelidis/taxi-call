<?php
/*
@info
Αρχείο για την ειδοποίηση των οδηγών

*/

header('Content-Type: text/html; charset=utf-8');
require('SAM/php_sam.php');
require('config.php');

//create a new connection object
$conn = new SAMConnection();

require_once '../scripts/db_connect.php';
$database = new DB_CONNECT();

//type:1 = notifyDrivers

$message = "1".":"."Νέος Πελάτης".":".$_REQUEST["lat"].":".$_REQUEST["lng"].":".$_REQUEST["customerid"];

//καθαρισμός για παλαιότερες μη ολοκληρωμένες παραγγελίες
					$devid = $_REQUEST["customerid"];
					mysql_query ("DELETE FROM orders WHERE 
									customerFK =(SELECT customerID FROM customer WHERE customerDeviceID = '".$devid."' LIMIT 1) 
									AND orderState <>3") ;



//$target = "/357777961700933";
$conn->connect(SAM_MQTT,$SAM_MQTTCONF);      
						   

foreach ($_REQUEST as $key => $target){	
		if($key != "lat" && $key !="lng" && $key !="customerid"){
			$conn->send('topic://'.$target,$message);
		}

}

$conn->disconnect();     



//for debug
function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}    



?>
