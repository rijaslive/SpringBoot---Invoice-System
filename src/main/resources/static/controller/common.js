$(document).ready(function(){
  $(".loader").fadeOut(750);

  setInterval(function() {
   healthCheck()
  }, 300000);

});

var showLoader = function(){
   $(".loader").fadeIn();
}

var hideLoader = function(time){
  if(time){
    $(".loader").fadeOut(time);
  }else{
     $(".loader").fadeOut(750);
  }
}


var showNotification = function(message,type){
    var toast = SnackBar({
        message: message,
        timeout: 5000,
        dismissible: true,
        status: type
    });
}

var healthCheck = function(){

         $.ajax({
             url: '/__health',
             type: 'GET',
             dataType: "json",
             success: function(data) {
             },
             error: function(result) {
                 console.error(result);
             },
         });


 }