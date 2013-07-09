<?php

header('Content-type=application/json; charset=utf-8');
require_once __DIR__ .'/db_connect.php';


$co = new checkForOrders( $_REQUEST["customerdeviceid"]);
$co->checkOrders();



 class checkForOrders{
    
    private $customerdeviceid;
    private $jsonResponse;
    private $databaseConnection;
    private $sqlQuery;
   
    
     public function __construct($custID){
    
        $this->customerdeviceid = null;
        $this->jsonResponse = array();
        $this->databaseConnection = new DB_CONNECT();
         
         
         if(isset($custID) ){
            $this->customerdeviceid = $custID;
         }
         else
            $this->customerdeviceid ="-";
     }
     
  public function checkOrders(){
         
         mysql_query("SET NAMES UTF8");
         $sql = "SELECT COUNT(*) 
		FROM orders o INNER JOIN customer c ON o.customerFK = c.customerID
		WHERE customerFK = (SELECT customerID FROM customer WHERE customerDeviceID = '".$this->customerdeviceid."') AND orderState = 1"; 
				      
          $query  = mysql_query($sql) or die(mysql_error());

        if (mysql_num_rows($query) > 0) 
        {

            $rows = mysql_fetch_array($query);
	
            if($rows[0] > 0) 
            {
		$this->jsonResponse["success"] = 1;
		
            }
            else
		$this->jsonResponse["success"] = 0;
	
            // echoing JSON response
            print json_encode($this->jsonResponse);
  
         } 
        else 
        {
            $this->jsonResponse["success"] = 0;
            $this->jsonResponse["message"] = "some error";

            // echo no users JSON
            print json_encode($this->jsonResponse);
        }   
     
     }
     //==============================================================
     private function write2file($string) {
	$file = fopen("test.txt","a");
	fwrite($file,$string);
	fclose($file);

}
     
  
}


?>
