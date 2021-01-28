var _ctx = $("meta[name='_ctx']").attr("content");
$(document).ready(function(){
    setTimeout(function(){
        $( ".alert" ).fadeOut( "slow" );
    }, 3000);

});

function deleteCashBookById(cashbookId){
         showLoader();
         console.log("cashbookId:"+cashbookId);
         let itemCount = parseInt($('#itemCount').val(), 10);
         let currPage =  parseInt($('#currPage').val(), 10);
         console.log("itemCount:" +itemCount);
         console.log("currPage:" +currPage);

         let requestJson = {
            "itemCount": itemCount,
            "currPage": currPage,
            "cashbookId": parseInt(cashbookId, 10)
         };
        $.ajax({
            url: '/cashbook/delete',
            data: JSON.stringify(requestJson),
            type: 'POST',
            dataType: "json",
            contentType: 'application/json',
            success: function(result) {
                console.log(result);
                if(itemCount==1){
                    currPage = currPage-2;
                }else{
                    currPage = currPage-1;
                }
                location.href = "/cashbook?page="+currPage;
                hideLoader();
            },
            error: function(result) {
               console.error(result);
               hideLoader();
            },
        });

    }

 var submitForm = function(){
    showLoader();
    $("#form").submit();
 }