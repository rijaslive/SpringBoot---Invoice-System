var _ctx = $("meta[name='_ctx']").attr("content");
var table;
$(document).ready(function(){

    setTimeout(function(){
        $( ".alert" ).fadeOut( "slow" );
    }, 3000);

/*    $("#form").validate({
        submitHandler: function (form) { // for demo
           saveCashBook();
        }
    });*/

    $('#submit').on('click', function () {
         var valid = $('#form').valid();
         if(valid){
             saveCashBook();
         }
    });

     table = new Tabulator("#cashbook-table", {
        pagination:"remote", //enable remote pagination
        ajaxURL:"/cashbook/load", //set url for ajax request
        paginationSize:3,//optional parameter to request a certain number of rows per page
//      ajaxParams:{token:"ABC123"}, //set any standard parameters to pass with the request
//      paginationInitialPage:1, //optional parameter to set the initial page to load
//      height:250, // set height of table (in CSS or here), this enables the Virtual DOM and improves render speed dramatically (can be any valid css height value)
        layout:"fitColumns", //fit columns to width of table (optional)
        index:"cashBookId",
        paginationAddRow:"table",
        selectable:1, //make rows selectable
        columns:[ //Define Table Columns
            {title:"No.", field:"index",  maxWidth:70},
            {title:"Item", field:"item", width:150},
            {title:"Amount", field:"amount", hozAlign:"right"},
            {title:"Type", field:"type"},
            {title:"Mode", field:"mode"},
            {title:"Date", field:"date", sorter:"date", hozAlign:"center"},
            {title:"Edit", field:"cashBookId",maxWidth:100,hozAlign:"center",cellClick:function(e, cell){editCashbook(cell._cell.row, cell._cell.row.data)},  formatter:function(cell, formatterParams, onRendered){
              //cell - the cell component
              //formatterParams - parameters set for the column
              //onRendered - function to call when the formatter has been rendered

               return '<span class="btn btn-info btn-xs"><i class="fas fa-edit"></i></span>'; //return the contents of the cell;
            }},
            {title:"Remove", field:"cashBookId",maxWidth:100,hozAlign:"center", cellClick:function(e, cell){deleteCashBookById(cell._cell.row, cell._cell.row.data)},  formatter:function(cell, formatterParams, onRendered){
              //cell - the cell component
              //formatterParams - parameters set for the column
              //onRendered - function to call when the formatter has been rendered

               return '<span class="btn btn-danger btn-xs"><i class="fas fa-minus-circle"></i></span>'; //return the contents of the cell;
            }}
        ],
        rowClick:function(e, row){ //trigger an alert message when the row is clicked
            console.log(row);
//            alert("Row " + row.getData().cashBookId + " Clicked!!!!");
//            table.selectRow();
        },


    });



});

function saveCashBook(){
         showLoader();
         let requestJson = {
            "cashBookId": $("#cashBookId").val(),
            "item": $("#particular").val(),
            "amount": parseFloat($("#amount").val()),
            "type": $("#transactionTypes").val(),
            "typeString": $( "#transactionTypes option:selected" ).text(),
            "mode": $("#transactionModes").val(),
            "modeString": $( "#transactionModes option:selected" ).text()
         };
        $.ajax({
            url: '/cashbook',
            data: JSON.stringify(requestJson),
            type: 'POST',
            dataType: "json",
            contentType: 'application/json',
            success: function(result) {
                console.log(result);
                addRow(result.response, $("#cashBookId").val());
                resetForm();
                hideLoader();
                if(result.code==="200"){
                    showNotification("Successfully Saved Cashbook Entry","green")
                }else if(result.code==="201"){
                    showNotification("Successfully Updated Cashbook Entry","green")
                }
            },
            error: function(result) {
               showNotification("Error while saving/updating cashbook entry","error")
               resetForm();
               hideLoader();
            },
        });

}

 function editCashbook(row, data){
            showLoader();
            var rowPosition = table.getRowPosition(row, true);
            table.selectRow(rowPosition);
            console.log("cashBookId:"+data.cashBookId);
            $("#cashBookId").val(data.cashBookId);
            $("#submit").prop('value', 'Update');
            $("#particular").val(data.item);
            $("#amount").val(data.amount);
            $("#transactionTypes").val($('#transactionTypes option').filter(function () { return $(this).html() === data.type; }).val());
            $("#transactionModes").val($('#transactionModes option').filter(function () { return $(this).html() === data.mode; }).val());
            hideLoader();
 }


function deleteCashBookById(row, data){
         showLoader();
         console.log("cashBookId:"+data.cashBookId);

         let requestJson = {
            "cashbookId": parseInt(data.cashBookId, 10)
         };
        $.ajax({
            url: '/cashbook/delete',
            data: JSON.stringify(requestJson),
            type: 'POST',
            dataType: "json",
            contentType: 'application/json',
            success: function(result) {
                console.log(result);
                table.setPage(table.getPage());
                showNotification("Deleted entry from cashbook","danger")
                hideLoader();
            },
            error: function(result) {
               console.error(result);
               hideLoader();
            },
        });

    }


 var fillTable = function fillTable(data){

         table = new Tabulator("#cashbook-table", {
             height:250, // set height of table (in CSS or here), this enables the Virtual DOM and improves render speed dramatically (can be any valid css height value)
             data:data, //assign data to table
             layout:"fitColumns", //fit columns to width of table (optional)
             index:"cashBookId",
             columns:[ //Define Table Columns
                 {title:"No.", field:"cashBookId", formatter:"rownum",  maxWidth:70},
                 {title:"Item", field:"item", width:150},
                 {title:"Amount", field:"amount", hozAlign:"right"},
                 {title:"Type", field:"type"},
                 {title:"Mode", field:"mode"},
                 {title:"Date", field:"date", sorter:"date", hozAlign:"center"},
                 {title:"Edit", field:"cashBookId",  formatter:function(cell, formatterParams, onRendered){
                      //cell - the cell component
                      //formatterParams - parameters set for the column
                      //onRendered - function to call when the formatter has been rendered

                      return '<span class="btn btn-info btn-xs">Edit</span>'; //return the contents of the cell;
                 }}
             ],
             rowClick:function(e, row){ //trigger an alert message when the row is clicked
                 alert("Row " + row.getData().cashBookId + " Clicked!!!!");
             },
         });

 }

 var submitForm = function(){
    saveCashBook();
 }


 var addRow = function(cashbook,cashBookId){
     if(!cashBookId){
             /*table.addRow(
                 {
                    cashBookId: cashbook.cashBookId,
                    item: cashbook.item,
                    amount:cashbook.amount,
                    type:cashbook.type,
                    mode:cashbook.mode,
                    date:cashbook.date
                },true).then(function(row){
                 //row - the row component for the row updated or added
                 for(var i=1; i<=table.getRows().length;i++){
                    if(i==table.getRows().length && i>3){
                       table.getRows()[i-1].delete();
                    }
                 }
             })
             .catch(function(error){
                 //handle error updating data
             });*/
            table.setPage(1);
     }else{
         //row - the row component for the row updated or added
          var rowIndex = 0;
          for(var i=1; i<=table.getRows().length;i++){
             if(cashBookId==table.getRows()[i-1]._row.data.cashBookId){
               rowIndex = table.getRows()[i-1];
             }
          }

         table.updateRow(
                rowIndex,{
                    cashBookId: cashbook.cashBookId,
                    item: cashbook.item,
                    amount:cashbook.amount,
                    type:cashbook.type,
                    mode:cashbook.mode,
                    date:cashbook.date
                }
         ).then(function(row){

         })
         .catch(function(error){
             //handle error updating data
         });
     }
 }

 var resetForm = function(){
    $("[id$=-error]").text("");
    $("[id$=-error]").css("display", "none");
    $("#cashBookId").val(null);
    $("#particular").val("");
    $("#amount").val("");
    $("#transactionModes").val(1);
    $("#transactionTypes").val(1);
    $("#submit").prop('value', 'Save');
    table.deselectRow();
 }