<?php

/*
@info
Κλάση για τη διαχείριση της σύνδεσης με τη βάση

@details
Συγκεκριμένα επιστρέφεται η τοποθεσία του πελάτη,στον συγκεκριμένο οδηγό που επέλεξε από το listview, ώστε
στην οθόνη του οδηγού να ζωγραφιστεί στην ανάλογη τοποθεσία ο πελάτης

*/
class DB_CONNECT {

    function __construct() {

        $this->connect();
    }

    function __destruct(){
        // closing db connection
        $this->close();
    }

   
    function connect(){
      
        require_once __DIR__ .'/db_config.php';
        $con = mysql_pconnect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());
        $db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());
        return $con;
    }

    function close(){
        mysql_close();
    }

}

?>