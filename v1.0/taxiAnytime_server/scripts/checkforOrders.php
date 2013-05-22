<?php
/*
@info
Αρχείο php για έλεγχο διαθέσιμων παραγγελιών  βάσει του πίνακα orders
@details
Με το sql  ερώτημα βλέπουμε το πλήθος αυτών που ενδιαφέρονται, έτσι αν υπάρχουν (count>0) 
η απόκριση json θα έχει success tag "1" αλλιώς "0"

*/

header('Content-type=application/json; charset=utf-8');

$jsonResponse = array();
$customerID = null;


require_once __DIR__ .'/db_connect.php';
$database = new DB_CONNECT();



if(isset($_REQUEST["customerdeviceid"]) )
{
	$customerID = $_REQUEST["customerdeviceid"];
}
else
	$customerID ="-";



	
mysql_query("SET NAMES UTF8");
$sql = "SELECT COUNT(*) 
		FROM orders o INNER JOIN customer c ON o.customerFK = c.customerID
		WHERE customerFK = (SELECT customerID FROM customer WHERE customerDeviceID = '".$customerID."') AND orderState = 1"; 
		
		      
$query  = mysql_query($sql) or die(mysql_error());
						


if (mysql_num_rows($query) > 0) //Αν βρέθηκαν εγγραφές
{

	$rows = mysql_fetch_array($query);
	
	if($rows[0] > 0) //το row[0] το result από το query
	{
		$jsonResponse["success"] = 1;
		
	}
	else
		$jsonResponse["success"] = 0;
	
    // echoing JSON response
   print json_encode($jsonResponse);
  
} 
else 
{
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
