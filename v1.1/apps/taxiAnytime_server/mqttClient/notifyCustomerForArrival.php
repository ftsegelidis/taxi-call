<?php
/*
@info
Αρχείο για την ειδοποίηση των πελατών ότι ο οδηγός που επέλεξαν έχει φτάσει

*/
header('Content-Type: text/html; charset=utf-8');
require('SAM/php_sam.php');
require('config.php');

//create a new connection object
$conn = new SAMConnection();

$conn->connect(SAM_MQTT,$SAM_MQTTCONF);    

require_once '../scripts/db_connect.php';
$database = new DB_CONNECT();


if( isset ( $_REQUEST["orderid"] )  ){
	$orderID = $_REQUEST["orderid"];
	
	 mysql_query("SET NAMES UTF8");
			$sql = "SELECT c.customerDeviceID
			FROM orders o INNER JOIN customer c ON c.customerID = o.customerFK 
			WHERE orderID ='".$orderID."' " ;
			$query = mysql_query($sql);
			
	if (mysql_num_rows($query) > 0) 
	{
		$row = mysql_fetch_array($query);
		$target = $row["customerDeviceID"];
		$message ="Ο οδηγός έχει φτάσει!!";
		$conn->send('topic://'.$target,$message);
	}
	$conn->disconnect(); 

}

//for debug

function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}

   

?>
