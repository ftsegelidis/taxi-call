<?php
/*
@info
Κλάση υπεύθυνη για την δημιουργία αναφοράς

*/
//header('Content-Type: text/html; charset=utf-8');
header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();

$rep = new report();
$rep->makeReport( $_REQUEST["getReasons"],$_REQUEST["orderid"],$_REQUEST["reportfrom"],$_REQUEST["reportTo"],$_REQUEST["userType"] );	


	
//========================================================================================
class report
{
	private $jsonResponse ;
	private $reportReason ;
	private $orderid;
	private $reportFrom;
	private $reportTo;
	private $sql;
	private $query;
	
	function __construct()
	{
		$this->jsonResponse = array() ;
		$this->reportReason = null ;
		$this->sql = "";
		$this->query = null;
		$this->orderid = null;
		$this->reportFrom = null;
		$this->reportTo = null;
		$this->userType = null;
	}
	
//==========================================================


	public function setQuery($subject)
	{
		$this->query = $subject;
	}
	
	public function setSql($subject)
	{
		$this->sql = $subject;
	}
	
	public function getSql()
	{
		return $this->sql;
	}

	public function getQuery()
	{
		return $this->query;
	}
//==========================================================
/*
@info
Μέθοδος για την αναφορά
@details
Δημιουργία αναφοράς με τα εξής στοιχεία :
-λόγοι
-orderid
-από
-σε ποιόν

Επιστρέφει σε μορφή json την απόκριση "success"= 1 σε επιτυχή αλλαγή και "success"= 0 σε μή επιτυχή

*/
	public function makeReport($reasons,$orderid,$repFrom,$repTo,$usrType)
	{
			if(isset ($reasons) && isset($orderid) && isset($repFrom) && isset($repTo) && isset($usrType) )
			{
				$this->reportReason = $reasons;
				$this->orderid = $orderid;
				$this->reportFrom = $repFrom;
				$this->reportTo = $repTo;
				$this->userType = $usrType;
				
				mysql_query("SET NAMES UTF8");
				
				switch( $this->userType )
				{
					case "customer":
					{
						$this->setSql( "INSERT INTO report 
								SET reportReason = '".$this->reportReason."' ,
								orderFK = '".$this->orderid."',
								byUser = 'customer',
								reportFrom = ( SELECT customerID from customer WHERE customerDeviceID = '".$this->reportFrom."' LIMIT 1),
								reportTo = ( SELECT driverID from taxidriver WHERE driverDeviceID = '".$this->reportTo."' LIMIT 1) " );
						break;
					}
					case "driver":
					{
						$this->setSql( "INSERT INTO report 
								SET reportReason = '".$this->reportReason."' ,
								orderFK = '".$this->orderid."',
								byUser = 'driver',
								reportFrom = ( SELECT driverID from taxidriver WHERE driverDeviceID = '".$this->reportFrom."' LIMIT 1),
								reportTo = ( SELECT customerID from customer WHERE customerDeviceID = '".$this->reportTo."' LIMIT 1) ") ;
						break;
					}

				}
			
				
				
				$this->setQuery ( mysql_query($this->getSql()) ) ;
				if( $this->getQuery() ) //Αν έγινε κανονικά εισαγωγή
				{
					// successfully updated
					$this->jsonResponse["success"] = 1;
					$this->jsonResponse["message"] = "successfully reported.";
        
					// echoing JSON response
					echo json_encode($this->jsonResponse);
				} 
				else
				{
					$this->jsonResponse["success"] = 0;
					$this->jsonResponse["message"] = "not reported";
					echo json_encode($this->jsonResponse);
				}
			}
			else  
			{
				$this->jsonResponse["success"] = 0;
				$this->jsonResponse["message"] = "not reported";
				echo json_encode($this->jsonResponse);
			}
			
	} //end function

		


} //end class



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