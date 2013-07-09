<?php
/*
@info
Αρχείο για την προβολή του listview με τους οδηγούς

@details
Εμφανίζονται στον πελάτη όλοι οι οδηγοί οι οποίοι έδειξαν ενδιαφέρον.
Τα στοιχεία που φαίνονται είναι όνομα,rate,distance,taxiplatenumber

*/
header('Content-type=application/json; charset=utf-8');
$jsonResponse = array();


require_once __DIR__ .'/db_connect.php';
$database = new DB_CONNECT();

$customerDevice = null;
if(isset($_REQUEST["customerDevID"]) ){
	$customerDevice = $_REQUEST["customerDevID"];
}
else
	$customerDevice ="-";


mysql_query("SET NAMES UTF8");

$query  = mysql_query("SELECT orderID,name,sirname,town,totalRate,driverImageUrl,taxiPlateNumber,driverDeviceID,distance
						FROM orders  INNER JOIN taxidriver  ON (driverID = taxidriverFK)
						WHERE customerFK = (SELECT customerID FROM customer WHERE customerDeviceID = '".$customerDevice."') AND orderState = 1 ")
					   
					or die(mysql_error());

if (mysql_num_rows($query) > 0){

    $jsonResponse["drivers"] = array();
	$driver = array();
    while ($row = mysql_fetch_array($query)){
      	$driver["orderid"] = $row["orderID"];
		$driver["name"] = $row["sirname"]." ".$row["name"];
		$driver["rate"] = $row["totalRate"];
		$driver["distance"] = $row ["distance"];
		$driver["driverImageUrl"] = $row["driverImageUrl"];
		$driver["taxiPlateNumber"] = $row["taxiPlateNumber"];
		$driver["driverDeviceID"] = $row["driverDeviceID"];

		array_push($jsonResponse["drivers"],$driver );
    }
    
    $jsonResponse["success"] = 1;

	
    // echoing JSON response
    print json_encode($jsonResponse);
} 
else {
    $jsonResponse["success"] = 0;
    $jsonResponse["message"] = "No drivers found";

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
