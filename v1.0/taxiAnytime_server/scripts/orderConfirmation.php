<?php
/*
@info
������ ��� ��� ��������� ��� ���������� ��� �����������.
��������� :
orderState = 1 : ������� ������ ����������
orderState = 2 : ��� ���� ��������� � ������
orderState = 3 : ��� ���� ��������� � �������, ��� ���� ����� �� ������ ������ ��� �����������.

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

	$result = mysql_query($sql);	 //mysql_query ���������� TRUE on �� �������� � FALSE �� �����		
	
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
