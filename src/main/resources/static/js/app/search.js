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
	console.log(" getSearchResult pageNo >> "+pageNo);
	if(pageNo > 0){
		$("#pageNo").val(pageNo);
		$.ajax({
			  type: "GET",
			  url: ctxPath+"profiles/get",
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
}

function viewDetails(id){
	$('.nav-pills a[href="#profile_tab"]').tab('show');
	
	if(id != null && id > 0){
		$("#viewProfileId").val(id);
		$.ajax({
			type: "GET",
			cache: false,
			url: ctxPath+"profile/view?id="+id,
			contentType: 'application/x-www-form-urlencoded',
		    success: function (resp) {
		    	 console.log(resp);
				  if(resp && resp.success){
					  console.log("  vd: "+resp.success);
					  if(resp.data){
						  //console.log(resp.data);
						  $( "#ViewDetailsDivId" ).html(resp.data); 
					  }
				  }
		    },
		    error: function( xhr,status, error ){
				  console.log("xhr  ================>>>>>>>>>>>>  "+xhr);
			  }
		});
		
		/*$.ajax({
			  type: "GET",
			  url: ctxPath+"view-profile",
			  cache: false,
			  contentType: 'application/x-www-form-urlencoded',
		      data: $("#FilterFormId").serialize(),
			  success: function( resp ) {
				  console.log(resp);
				  if(resp && resp.success){
					  console.log("  vd: "+resp.success);
					  if(resp.data){
						  //console.log(resp.data);
						  $( "#ViewDetailsDivId" ).html(resp.data); 
					  }
				  }
			  },
			  error: function( xhr,status,error ){
				  console.log("xhr  ================>>>>>>>>>>>>  "+xhr);
			  }
		});*/
	}
	
}

function clearProfileFilter(){
	$("#gid").val('');
	$("#name").val('');
	$("#city").val('');
	$("#FilterFormId").submit();
}
