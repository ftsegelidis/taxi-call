<?php

/*
@info
Αρχείο για την ειδοποίηση των οδηγών ότι τον επέλεξε ο πελάτης και είναι έτοιμος για παραλαβή

*/

header('Content-Type: text/html; charset=utf-8');
require('SAM/php_sam.php');
require('config.php');

$conn = new SAMConnection();

//type:1 = notifyDrivers
//type:2 = message for acception

if( isset($_REQUEST["customerlat"]) && isset($_REQUEST["customerlon"]) && isset($_REQUEST["driverDevIdtoSend"]) && isset ($_REQUEST["selectedOrderid"]) ){

	$message = "2".":"."Πελάτης για παραλαβή".":".$_REQUEST["customerlat"].":".$_REQUEST["customerlon"].":".$_REQUEST["driverDevIdtoSend"].":".$_REQUEST["selectedOrderid"] ;
	$target = $_REQUEST["driverDevIdtoSend"];
}
else{
	$message = "";
	$target = "";
}
	

$conn->connect(SAM_MQTT,$SAM_MQTTCONF);      
							   
$conn->send('topic://'.$target,$message);
$conn->disconnect(); 




//for debug

function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}

   

?>
