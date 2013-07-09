<?php
/*
@info
������ ��� ���������� �� �������� ��� ������ ��� ������� ���� ������������ ����� ��� �� listview.

@details
������������ ������������ � ��������� ��� ������,���� ������������ ����� ��� ������� ��� �� listview, ����
���� ����� ��� ������ �� ����������� ���� ������� ��������� � �������

*/

header('Content-type=application/json; charset=utf-8');
$jsonResponse = array();


require_once __DIR__ .'/db_connect.php';
$database = new DB_CONNECT();

$customerID = null;
$orderID = null;
if(isset($_REQUEST["customerid"]) && isset($_REQUEST["orderid"]) ){
	$customerID = $_REQUEST["customerid"];
	$orderID = $_REQUEST["orderid"];
}
else{
	$customerID = "-";
	$orderID = "-";
	
}

mysql_query("SET NAMES UTF8");
$sql = "SELECT td.driverDeviceID,o.customerLocation
						FROM taxidriver td INNER JOIN orders o ON (td.driverID = o.taxidriverFK)
						WHERE o.customerFK = (SELECT customerID FROM customer WHERE customerDeviceID ='".$customerID."') AND o.orderState = 1 
						AND o.orderID = '".$orderID."' " ;
				

$query  = mysql_query($sql) or die(mysql_error());

if (mysql_num_rows($query) > 0) {

	$rows = mysql_fetch_array($query);
	
	$location = splitCustomerLocation( $rows["customerLocation"] );	
	$jsonResponse["customerLat"] = $location[0];
	$jsonResponse["customerLon"] = $location[1];
	$jsonResponse["driverDevID"] = $rows["driverDeviceID"];
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

/*
@info
��������� ��� ��� ���������� ��� ����������

@details
� ��������� ��������� ��� ���� �� ����� lat+lon, ����� �� ������������
��� ������������ �� array �� ����� �������� �� lat ��� ������� lon

@return array �� �� ������������ ��������

*/
function splitCustomerLocation($location){
	if($location != null){
		$loc = explode("+",$location);
		return $loc;
	}
	else
		return null;
}



/*
@info
���������� ������� ��� �������������

@details
������ �� ������ �� ������������

*/
function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}

?>
