<?php

/*
@info
����� ��� ���  ������ ��� �������������� ��� ������.

*/


//header('Content-Type: text/html; charset=utf-8');
header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();


$available = new availability();
$available->makeAvailability($_REQUEST["available"],$_REQUEST["driverid"]) ;


//=============================================================================================================
class availability {

	private $jsonResponse;
	private $driverid;
	private $available;
	private $sql;
	private $query;


	function __construct(){
		$this->jsonResponse = array();
		$this->driverid = null;
		$this->available = null;
		$this->sql = "";
		$this->query = null;
	
	}

	//set ====================================================================
	public function setDriverid($subject)
	{
		$this->driverid = $subject;
	}

	public function setAvailable($subject)
	{
		$this->available = $subject;
	}
	
	public function setSql($subject)
	{
		$this->sql = $subject;
	}
	
	//get
	public function getAvailable()
	{
		return $this->available;
	}
	
	public function getSql()
	{
		return $this->sql;
	}
	
	public function getDriverid()
	{
		return $this->driverid ;
	}
	
	public function getQuery()
	{
		return $this->query ;
	}
	
//============================================================================	
/*
@info
������� ��� ������ ��� �������������� ��� ������.

@details
������� ��� ��������� ������������� ("1" �� ������� �� ����� ���������� ��� "0" �� ���), ����� ��� �� 
������������ id ��� ��������

���������� �� ����� json ��� �������� "success"= 1 �� ������� ������ ��� "success"= 0 �� �� �������
*/
	public function makeAvailability($available,$driverid)
	{
		if( isset($available) && isset($driverid) ){
			$this->setAvailable($available);
			$this->setDriverid($driverid) ; 
	
			mysql_query("SET NAMES UTF8");
			$this->setSql("UPDATE taxidriver SET isAvailable = '".$this->getAvailable()."' WHERE driverDeviceID = '".$this->getDriverid()."' " ) ; 
			$this->query = mysql_query( $this->getSql() );
			
			if ( $this->getQuery() ) {
				$this->jsonResponse["available"] = $this->getAvailable() ;
				$this->jsonResponse["success"] = 1;

				// echoing JSON response
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