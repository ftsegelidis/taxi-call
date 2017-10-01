<?php

/*
Βοηθητικό αρχείο για να λάβουμε τα στοιχεία των οδηγών οι οποίοι είναι διαθέσιμοι.(όνομα,rate,id συσκευής )

*/

header('Content-Type: application/json; charset=utf-8');
$jsonResponse = array();
require_once __DIR__ .'/db_connect.php';
$database = new DB_CONNECT();



mysql_query("SET NAMES UTF8");

$query  = mysql_query("SELECT sirname,totalRate,driverImageUrl,driverDeviceID
					FROM taxidriver where isAvailable = 1") or die(mysql_error());

if (mysql_num_rows($query) > 0) 
{

    $jsonResponse["drivers"] = array();
	$driver = array();
    while ($row = mysql_fetch_array($query))
	{
      	
		$driver["sirname"] = $row["sirname"];
		$driver["rate"] = $row["totalRate"];
		$driver["driverImageUrl"] = $row["driverImageUrl"];
		$driver["driverDeviceID"] = $row["driverDeviceID"];
		
		
		array_push($jsonResponse["drivers"],$driver );
    }
    
    $jsonResponse["success"] = 1;

	
    // echoing JSON response
	print json_encode($jsonResponse);
	
} 
else 
{
    $jsonResponse["success"] = 0;
    $jsonResponse["message"] = "No driver found";

    // echo no users JSON
    print json_encode($jsonResponse);
}







?>
