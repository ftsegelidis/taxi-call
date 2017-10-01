<?php

/*
@info
Κλάση για την πραγματοποίηση κλήσης/sms σε περίπτωση ανάγκης

@details
Λαμβάνει από τη βάση το κινητό τηλέφωνο του συγκεκριμένου οδηγού ή πελάτη ανάλογα

*/

header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();

$caller = new call();
$caller->makecall( $_REQUEST["client"] ,$_REQUEST["Deviceid"]) ; 


//======================================================================
class call
{
	private $jsonResponse;
	private $client;
	private $deviceid;
	private $sql;
	private $query;
	private $row;

	function __construct()
	{
		$this->jsonResponse = array();
		$this->client = null;
		$this->deviceid = null;
		$this->sql = "";
		$this->query = null;
		$this->row = null;
	}

	//set ====================================================================
	public function setClient($subject)
	{
		$this->client = $subject;
	}

	public function setDeviceid($subject)
	{
		$this->deviceid = $subject;
	}
	
	public function setSql($subject)
	{
		$this->sql = $subject;
	}
	
	//get
	public function getClient()
	{
		return $this->client;
	}
	
	public function getSql()
	{
		return $this->sql;
	}
	
	public function getDeviceid()
	{
		return $this->deviceid ;
	}
	
	public function getQuery()
	{
		return $this->query;
	}
//=============================================================================

	public function makeCall($client,$devid)
	{
		if(isset( $client) && isset( $devid ) )
		{
	
			$this->setDeviceid($devid);
			mysql_query("SET NAMES UTF8");

			switch($client)
			{
				case "customer" :
				{	
					$this->setSql ( "SELECT cellphone FROM taxidriver where driverDeviceID = '".$this->getDeviceid()."' LIMIT 1" ) ; 
					break;
				}
				case "driver" :
				{
					$this->setSql ("SELECT cellphone FROM customer where customerDeviceID = '".$this->getDeviceid()."' LIMIT 1" ) ; 
					break;
				}
	
			}
			$this->query = mysql_query( $this->getSql() );
			
	
			if (mysql_num_rows( $this->getQuery() ) > 0) 
			{
   
				// $jsonResponse["coordinates"] = array(); //Σύνθετο array
				$this->row = mysql_fetch_array( $this->getQuery() );
				$this->jsonResponse["cellphone"] = $this->row["cellphone"];   
				$this->jsonResponse["success"] = 1;

				// echoing JSON response
				print json_encode($this->jsonResponse);
			} 
			else 
			{
				$this->jsonResponse["success"] = 0;
				$this->jsonResponse["message"] = "not successful";
				print json_encode($this->jsonResponse);
			}

		}
		else
		{
			$this->jsonResponse["success"] = 0;
			$this->jsonResponse["message"] = "not successful";

			// echo no users JSON
			print json_encode($this->jsonResponse);

		}

	}

}


function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}



?>