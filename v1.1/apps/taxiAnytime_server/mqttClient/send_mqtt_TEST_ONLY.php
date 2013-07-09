<?php
/*
@info
Αρχείο για δοκιμές

*/
require('SAM/php_sam.php');
require('config.php');

//create a new connection object
$conn = new SAMConnection();


$target= "/357717577700933";

//$message = "1".":"."hey youuu what are you doing".":"."56.5647746447".":"."67.698695675858".":"."/35777796170093346";

$message = "hi there";

$conn->connect(SAM_MQTT,$SAM_MQTTCONF); 
$conn->send('topic://'.$target,$message);

       
$conn->disconnect();         



?>
