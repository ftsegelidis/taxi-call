<?php
/*
@info
Κλάση για την αποθήκευση σχολίων και rating

@details
Εισάγεται στην βάση το συνολικό rating και τα σχόλια

*/

header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();


$commentRating = new CommentsRating();

$commentRating->makeCommentsAndRate($_REQUEST["rate"],$_REQUEST["comment"],$_REQUEST["driverDevId"],$_REQUEST["orderid"],$_REQUEST["customerDevID"]); 


//=============================================================================================
class CommentsRating
{

	private $jsonResponse;
	private $comments;
	private $rate;
	private $orderid;
	private $driverDeviceId;
	private $totalRating;
	private $customerDevID;


	function __construct()
	{
		$this->jsonResponse = array();
		$this->comments = null;
		$this->rate = null;
		$this->orderid = null;
		$this->driverDeviceId = null;
		$this->totalRating = null;
		$this->customerDevID = null;

	}
	

	public function makeCommentsAndRate($rate,$comment,$driverdevid,$orderid,$customerDevid)
	{
		if(isset($rate) && isset($comment) && isset ($driverdevid) && isset($orderid) && isset($customerDevid)  )  //Αν έχουν τιμή
		{
			$this->comments = $comment;
			$this->rate = $rate;
			$this->driverDeviceId = $driverdevid;
			$this->orderid = $orderid;
			$this->customerDevID = $customerDevid;
			
			 mysql_query("SET NAMES UTF8");
			 $query_make_comment = mysql_query(" INSERT INTO comments
												SET comment ='".$this->comments."',
												rating = '".$this->rate."',
												orderid = '".$this->orderid."' ")   ;
			//Υπολογίζει το συνολικό rating άθροισμα/πλήθος από τον πίνακα comments										
			 $results = mysql_query("
			 SELECT SUM(c.rating)/COUNT(*)
			 FROM taxidriver td INNER JOIN orders o ON o.taxidriverFK = td.driverID INNER JOIN comments c ON o.orderID = c.orderid 
			 WHERE td.driverDeviceID = '".$this->driverDeviceId."'
			 AND o.orderState = 3 LIMIT 1 " );
			 
			
			if( $query_make_comment == true && mysql_num_rows($results) > 0) //Αν έγινε κανονικά εισαγωγή
			{
			
				$this->totalRating = $this->calculateRating( mysql_fetch_array($results) ) ;
				
				
				 mysql_query("UPDATE taxidriver 
							SET totalRate = '".$this->totalRating."' 
							WHERE driverDeviceID = '".$this->driverDeviceId."' LIMIT 1 " );
	
				// successfully updated
				$this->jsonResponse["success"] = 1;
				$this->jsonResponse["message"] = "successfully inserted.";
        
				// echoing JSON response
				echo json_encode($this->jsonResponse);
			} 
			else
			{
				$this->jsonResponse["success"] = 0;
				$this->jsonResponse["message"] = "Required field(s) is missing";
				echo json_encode($this->$jsonResponse);
			}
	
		}

		else  
		{
			$this->jsonResponse["success"] = 0;
			$this->jsonResponse["message"] = "Required field(s) is missing";
			echo json_encode($this->$jsonResponse);
		}
		
		//Καθαρίζουμε πρώτα τις μη ολοκληρωμένες παραγγελίες , ώστε να μας εμφανίζονται μόνο οι καινούργιες
		mysql_query("DELETE FROM orders WHERE orderState<>3 AND customerFK =(SELECT customerID FROM customer WHERE customerDeviceID = '".$this->customerDevID."' LIMIT 1) ");

	}

	
/*
@info
Συνάρτηση για τον υπολογισμό του τελικου rating

@details
Στρογγυλοποιεί το τελικό rating στην πλησιέστερη μισάδα (βλ παράδειγμα κάτω)

@source 
To round to the nearest half number:

1. Multiply by 2
2. Add 0.5.
3. Erase the decimal part of the number (truncate).
4. Divide by 2

Examples: 
7.2 -> 14.4 -> 14.9 -> 14 -> 7
7.4 -> 14.8 -> 15.3 -> 15 -> 7.5
7.6 -> 15.2 -> 15.7 -> 15 -> 7.5
7.8 -> 15.6 -> 16.1 -> 16 -> 8
link: http://answers.yahoo.com/question/index?qid=20061212113854AABP8Gj

*/


//το αποτέλεσμα του fetching είναι το τελικό rating 
private function calculateRating($parameter){
		$piece = explode(".", ( $parameter[0]*2 + 0.5 ) );
		return ( $piece[0]/2 ) ;
	}

}//end class






function write2file($string)
{
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}



?>