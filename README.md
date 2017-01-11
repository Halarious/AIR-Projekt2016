# AIR-Projekt2016

SERVER SIDE FILES
--------------------------------
users database:

create database android_api 
use android_api 
 
create table users(
   id int(11) primary key auto_increment,
   unique_id varchar(23) not null unique,
   name varchar(50) not null,
   email varchar(100) not null unique,
   role varchar(20) not null,
   encrypted_password varchar(80) not null,
   salt varchar(10) not null,
   created_at datetime,
   updated_at datetime null
); 

Config.php (add your info instead of _BLANK_):
<?php
  define("DB_HOST", "localhost");
  define("DB_USER", "_BLANK_");
  define("DB_PASSWORD", "_BLANK_");
  define("DB_DATABASE", "android_api");
?>

db_connect.php:
<?php
class DB_Connect 
{
    private $conn;
 
    public function connect()
    {
        require_once 'include/Config.php';
         
        $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
         
        return $this->conn;
    }
}
?>

db_functions.php
<?php
class DB_Functions
{
    private $conn;
 
    function __construct() 
    {
        require_once 'db_connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    function __destruct() 
    {     
    }
 
    public function storeUser($name, $email, $password)
    {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        if ($result) 
        {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } 
        else 
        {
            return false;
        }
    }
 
    public function getUserByEmailAndPassword($email, $password) 
    {
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) 
        {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            if ($encrypted_password == $hash) {
                return $user;
            }
        }
        else 
        {
            return NULL;
        }
    }
 
    public function isUserExisted($email)
    {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
 public function hashSSHA($password) 
 {
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    public function checkhashSSHA($salt, $password) 
    {
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
}
?>

register.php:
<?php
require_once 'include/db_functions.php';
$db = new DB_Functions();
 
$response = array("error" => FALSE);
 
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password'])) 
{
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    if ($db->isUserExisted($email)) 
    {
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    }
    else
    {
        $user = $db->storeUser($name, $email, $password);
        if ($user) {
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} 
else 
{
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>

login.php:
<?php
require_once 'include/db_functions.php';
$db = new DB_Functions();
 
$response = array("error" => FALSE);
 
if (isset($_POST['email']) && isset($_POST['password'])) 
{
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    $user = $db->getUserByEmailAndPassword($email, $password);
 
    if ($user != false)
    {
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
        echo json_encode($response);
    }
    else
    {
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
}
else 
{
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>
