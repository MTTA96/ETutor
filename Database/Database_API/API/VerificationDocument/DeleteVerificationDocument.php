<?php

// Kết nối database
require "/storage/ssd4/981/5026981/APIs/DbConnection.php";
require "/storage/ssd4/981/5026981/APIs/Model.php";


// isset($_POST['submit']) ? $_POST['submit'] : null;
 $Uid=isset($_POST['Uid']) ? $_POST['Uid'] : null;
 $IdVdTypes=isset($_POST['IdVdTypes']) ? $_POST['IdVdTypes'] : null;
 
 $sql="DELETE FROM VerificationDocuments WHERE Uid='$Uid' AND IdVdTypes='$IdVdTypes'"; 

 if(mysqli_query($conn,$sql)){
    //thong báo thanh công
      $ms = json_encode(http_response_code());
      echo "{errorCode: ".$ms.", status: Success}"; 
   }else{
    //thông báo thất bại
      $ms = json_encode(http_response_code());
      echo "{errorCode: ".$ms.", status: Failed}"; 
   }
?>