$(document).ready(function(){
  $(".loader").fadeOut(750);
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