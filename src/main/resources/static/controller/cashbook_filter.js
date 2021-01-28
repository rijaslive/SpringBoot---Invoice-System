var table;
$(document).ready(function(){

    var elem = document.getElementById('range');
    var dateRangePicker = new DateRangePicker(elem, {
         defaultViewDate: undefined,
         todayHighlight: true,
         todayBtnMode: 0,
         autohide: true,
         clearBtn: true,
         format: 'M-dd-yyyy'
    });

    $("#start").val(formatDate(new Date()));
    $("#end").val(formatDate(new Date()));

    $("#summary").hide();

});


var formatDate = function (date) {
	// Create a list of names for the months
	var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct',	'Nov', 'Dec'];
	// return a formatted date
	return months[date.getMonth()] + '-' + date.getDate() + '-' + date.getFullYear();

};


var getCashBookEntries = function(){
    var start = $("#start").val();
    var end = $("#end").val();

    var transactionType = parseInt($('#transactionTypes').val(), 10);
    var transactionMode = parseInt($('#transactionModes').val(), 10);


     let requestJson = {
        "start": start,
        "end": end,
        "transactionType": transactionType,
        "transactionMode": transactionMode
     };

     $.ajax({
         url: 'cashbookfilter/list',
         data: JSON.stringify(requestJson),
         type: 'POST',
         dataType: "json",
         contentType: 'application/json',
         success: function(data) {
             if(data.response && data.response.cashbooks){
                 fillData(data.response.cashbooks,data.response.totalIncome,data.response.totalExpense,true);
             }else{
                fillData(null,0,0,false);
             }
         },
         error: function(result) {
             console.error(result);
             fillData(null,0,0,false);
         },
     });

}


var fillData = function(tableData, income, expense, status){
     if(status){
         $(".export-button").removeClass('hidden')
         fillTable(tableData);
         $("#income").val(income);
         $("#expense").val(expense);
         $("#summary").fadeIn();
     }else{
        $(".export-button").addClass('hidden')
         fillTable([]);
         $("#income").val(0);
         $("#expense").val(0);
         $("#summary").fadeOut();
     }

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
            ],
            rowClick:function(e, row){ //trigger an alert message when the row is clicked
                alert("Row " + row.getData().cashBookId + " Clicked!!!!");
            },
        });

}

var downloadExcel = function(){
    table.download("xlsx", "data.xlsx", {sheetName:"report"});
}