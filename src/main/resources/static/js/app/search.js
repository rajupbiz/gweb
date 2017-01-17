var ctxPath = $("#ctxPath").val();
$(document).ready(function() {
	
    $("#education").selectpicker({
        style: "btn-default btn-sm"
    });
   
});

$("#FilterFormId").submit(function( event ) {
	console.log(" filter form submit ");
	event.preventDefault();
	getSearchResult(1);
});

function getSearchResult(pageNo){
	$("#pageNo").val(pageNo);
	$.ajax({
		  type: "GET",
		  url: ctxPath+"get-profiles",
		  cache: false,
		  async: false,
		  contentType: 'application/x-www-form-urlencoded',
	      data: $("#FilterFormId").serialize(),
		  success: function( resp ) {
			  console.log(resp);
			  if(resp && resp.success){
				  console.log(resp.success);
				  if(resp.data){
					  //console.log(resp.data);
					  $( "#SearchResultDivId" ).html(resp.data); 
				  }
			  }
		  },
		  error: function( xhr,status,error ){
			  console.log("xhr  ================>>>>>>>>>>>>  "+xhr);
		  }
	});
}

function clearProfileFilter(){
	
}