<?php
/*
@info
Κλάση υπεύθυνη για την δημιουργία παραγγελίας.
Θέτουμε την κατάσταση της παραγγελίας σε 1.

*/

header('Content-type=application/json; charset=utf-8');
require_once __DIR__ . '/db_connect.php';
$database = new DB_CONNECT();


$neworder = new Orders();
$neworder->makeOrder( fixLatLon($_REQUEST["customerlat"]) ,fixLatLon($_REQUEST["customerlon"]), 
					  fixLatLon($_REQUEST["driverlat"]),fixLatLon($_REQUEST["driverlon"]),$_REQUEST["customerDevice"],
					  $_REQUEST["driverDevice"],$_REQUEST["distance"]); 


//san orismata vazoume ta requests



//================================================================================

class Orders{
	private $jsonResponse;
	
	private $CustomerLatitude;
	private $CustomerLongitude;
	private $DriverLatitude;
	private $DriverLongitude;
	private $customerid;
	private $driverid;
	private $orderid;
	private $distance;


	function __construct(){
		$this->jsonResponse = array();
		
		$this->CustomerLatitude = null;
		$this->CustomerLongitude= null;
		$this->DriverLatitude = null;
		$this->DriverLongitude = null;
		$this->driverid = null;
		$this->customerid = null;
		$this->orderid = null;
		$this->distance = null;

	}

	//set-get magic methods
	public function __get($property) {
            if (property_exists($this, $property)) {
                return $this->$property;
            }
    }

    public function __set($property, $value) {
        if (property_exists($this, $property)) {
            $this->$property = $value;
        }
	}
	
/*
@info
Μέθοδος για την δημιουργία παραγγελίας.

@details
Δέχεται ώς ορίσματα( requests) τα γ.μ/γ.π οδηγού και πελάτη , τα id των συσκευών και την απόσταση τους ,
τα οποία καταχωρούνται στη βάση στον πίνακα order .
Έχουμε δηλαδή μια νέα παραγγαλία με orderState = 1;

*/	
public function makeOrder($customerlat,$customerlon,$driverlat,$driverlon,$customerdevice,$driverdevice,$distance){
	if(isset($customerlat) && isset($customerlon) && isset($driverlat) && isset($driverlon) && isset($customerdevice) && isset($driverdevice) && isset($distance) ){
		$this->CustomerLatitude = $customerlat;
		$this->CustomerLongitude = $customerlon;
		$this->DriverLatitude = $driverlat;
		$this->DriverLongitude = $driverlon;
		$this->driverid = $driverdevice;
		$this->customerid =$customerdevice;
		$this->distance = $distance;
		
		

		$sql = "INSERT INTO orders
				SET
				customerLocation = '".$this->CustomerLatitude."+".$this->CustomerLongitude."',
				driversLocation =  '".$this->DriverLatitude."+".$this->DriverLongitude."',
				date = NOW(),
				taxidriverFK = (SELECT driverID FROM taxidriver WHERE driverDeviceID =  '".$this->driverid."' ),
				customerFK = (SELECT customerID FROM customer WHERE customerDeviceID =  '".$this->customerid."' ),
				distance = '".$this->distance."', 
				orderState = 1" ;
	
		$insert_query = mysql_query($sql);
	
		$this->orderid = mysql_insert_id();
	
	
		//Αν έγινε κανονικά εισαγωγή,ενημερώνουμε τον πίνακα με τις ανάλογες τιμές
		if($insert_query) {
			// successfully updated
			$this->jsonResponse["orderid"] = $this->orderid ;
			$this->jsonResponse["success"] = 1;
			$this->jsonResponse["message"] = "order successfully inserted.";
        
			// echoing JSON response
			echo json_encode($this->jsonResponse);
		}
		else{
			$this->jsonResponse["success"] = 0;
			$this->jsonResponse["message"] = "error";
			echo json_encode($this->jsonResponse);
		}

	}
	else{
    
		$this->jsonResponse["success"] = 0;
		$this->jsonResponse["message"] = "error";
		echo json_encode($this->$jsonResponse);
	}
	
 }//end function



}//end class	
	
/*	
@info
Συνάρτηση για να διορθώνει τα δεκαδικά από τα γεωγραφικά μήκη/πλάτη

@example
πχ αν το εισερχόμενο string είναι : 23.345678653434  (12 δεκαδικά)
θα αλλάξει σε 23.34567865 (8 δεκαδικά)
αν ήταν 23.345 (3 δεκαδικά)
θα αλλάξει σε 23.34500000 (8 δεκαδικά)

*/
function fixLatLon($string)
{
	$latpieces = explode(".",$string); //σπάμε το string
	$fixedSize = 8; // πλήθος δεκαδικών που θέλουμε

	if( (strlen($latpieces[1]) > $fixedSize) ) //αν το δεύτερο μέρος είναι μεγαλύτερο
	{
		$latpieces[1] = substr_replace($latpieces[1] ,"",-(strlen($latpieces[1])-$fixedSize)); //αφαιρούμε τα πλεονάζοντα δεδαδικά ώστε να είναι όσο και το fixed size
		//echo $latpieces[1];
		return $latpieces[0].".".$latpieces[1]; // επιστρέφουμε το επιθυμητό 
	}
	else if(strlen($latpieces[1]) < $fixedSize) //αν το δεύτερο μέρος είναι μικρότερο 
	{
		$zeros = null;
		for($i=0;$i<($fixedSize - strlen($latpieces[1]));$i++)  //γεμίζουμε όσα λείπουν με "0"
		{
			$zeros[$i] = "0";
	
		}
		return $latpieces[0].".".$latpieces[1].implode($zeros);
	}
	else
		return $latpieces[0].".".$latpieces[1]; // αν είναι ίδιο το μέθεθος των δεκαδικών
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