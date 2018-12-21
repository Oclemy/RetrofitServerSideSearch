<?php

class Constants
{
    //DATABASE DETAILS
    static $DB_SERVER="localhost";
    static $DB_NAME="spacecraftsDB";
    static $USERNAME="root";
    static $PASSWORD="";

    //STATEMENTS
    static $SQL_SELECT_ALL="SELECT * FROM spacecraftsTB";
}

class Spacecrafts
{
    /*******************************************************************************************************************************************/
    /*
       1.CONNECT TO DATABASE.
       2. RETURN CONNECTION OBJECT
    */
    public function connect()
    {
        $con=new mysqli(Constants::$DB_SERVER,Constants::$USERNAME,Constants::$PASSWORD,Constants::$DB_NAME);
        if($con->connect_error)
        {
            return null;
        }else
        {
            return $con;
        }
    }
    /*******************************************************************************************************************************************/
    /*
       1.SELECT FROM DATABASE.
    */
    public function search($query)
    {

        $sql="SELECT * FROM spacecraftsTB WHERE name LIKE '%$query%' OR propellant LIKE '%$query%' OR destination LIKE '%$query%' ";
		 //$sql="SELECT * FROM spacecraftsTB WHERE name LIKE '%$query%' ";

        $con=$this->connect();
        if($con != null)
        {
            $result=$con->query($sql);
            if($result->num_rows > 0)
            {
                $spacecrafts=array();
                while($row=$result->fetch_array())
                {
                    array_push($spacecrafts, array("id"=>$row['id'],"name"=>$row['name'],"propellant"=>$row['propellant'],"destination"=>$row['destination'],"image_url"=>$row['image_url'],"technology_exists"=>$row['technology_exists']));
                }
                print(json_encode(array_reverse($spacecrafts)));
            }else
            {
                print(json_encode(array("No item Found that matches the query: ".$query)));
            }
            $con->close();

        }else{
            print(json_encode(array("PHP EXCEPTION : CAN'T CONNECT TO MYSQL. NULL CONNECTION.")));
        }
    }
    public function handleRequest() {
		if($_SERVER['REQUEST_METHOD'] == 'POST'){
			$query=$_POST['query'];
            $this->search($query);
        } else{
            $this->search("");
        }
	
    }
}
$spacecrafts=new Spacecrafts();
$spacecrafts->handleRequest();
//end
