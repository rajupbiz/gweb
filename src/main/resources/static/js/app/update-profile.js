var ctxPath = $("#ctxPath").val();
$(document).ready(function() {
	
	
	
	/* activate sidebar */
	$('#sidebar').affix({
		offset : {
			top : 100
		}
	});

	/* activate scrollspy menu */
	var $body = $(document.body);
	var navHeight = $('.navbar').outerHeight(true) + 10;

	$body.scrollspy({
		target : '#leftCol',
		offset : navHeight
	});

	/* smooth scrolling sections */
	$('a[href*=\\#]:not([href=\\#])').click(function() {
		if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
			var target = $(this.hash);
			target = target.length ? target	: $('[name='+ this.hash.slice(1) + ']');
			if (target.length) {
				$('html,body').animate({
					scrollTop : target.offset().top - 60
				}, 700);
				return false;
			}
		}
	});
	
	 $('#datetimepicker8').datetimepicker({
        	
    	/* defaultDate: "1990-01-01", */
    	minDate: "1950-01-01 00:00:00",
    	/* maxDate: "2000-12-31 00:00:00", */
    	format: "DD/MM/YYYY",
        icons: {
            /* time: "fa fa-clock-o", */
            date: "fa fa-birthday-cake",	/* fa fa-calendar */
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down"
        }
    });
    
    $("#datetimepicker8").on("dp.change", function (e) {
    	var day = getDayOfWeekFromDay(new Date(e.date).getDay());
    	$("#birthDay").val(day);
    	$("#birthDayVisible").val(day);
    });
	
    $('#datetimepicker3').datetimepicker({
        format: 'LT'
    });
    
    $("#maritalStatusId").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#birthDayId").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#bloodGroupId").selectpicker({
        style: "btn-default btn-sm"
    });

    /*$("#contact-relation1").selectpicker({
        style: "btn-default btn-sm"
    });*/
    
    /*$("#contact-relation2").selectpicker({
        style: "btn-default btn-sm"
    });*/
    
    $("#address-state").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#address-country").selectpicker({
        style: "btn-default btn-sm"
    });

    /*$("#education-degree-1").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#education-degree-2").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#education-degree-3").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#education-specialization-1").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#education-specialization-2").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#education-specialization-3").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#occ-1").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#occ-2").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#occ-desg-1").selectpicker({
        style: "btn-default btn-sm"
    });
    
    $("#occ-desg-2").selectpicker({
        style: "btn-default btn-sm"
    });
    */
    
    $('#first-name').bind('keypress keyup blur', function() {
        $('#birth-name').val($(this).val());
    });
    
    $('#middle-name').bind('keypress keyup blur', function() {
        $('#father-first-name').val($(this).val());
    });
    
    $('#last-name').bind('keypress keyup blur', function() {
        $('#father-last-name').val($(this).val());
    });
    
    $('#address-state').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-state'){
    		$('#address-state-other').prop('disabled', false);
    	}else{
    		$('#address-state-other').prop('disabled', true);
    	}
    });
    
    $('#address-country').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-country'){
    		$('#addess-country-other').prop('disabled', false);
    	}else{
    		$('#addess-country-other').prop('disabled', true);
    	}
    });
    
   /* $('#education-degree-1').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-degree'){
    		$('#education-other-degree-1').prop('disabled', false);
    	}else{
    		$('#education-other-degree-1').prop('disabled', true);
    	}
    });
    
    $('#education-degree-2').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-degree'){
    		$('#education-other-degree-2').prop('disabled', false);
    	}else{
    		$('#education-other-degree-2').prop('disabled', true);
    	}
    });
    
    $('#education-degree-3').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-degree'){
    		$('#education-other-degree-3').prop('disabled', false);
    	}else{
    		$('#education-other-degree-3').prop('disabled', true);
    	}
    });
    
    $('#education-specialization-1').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-specialization'){
    		$('#education-other-specialization-1').prop('disabled', false);
    	}else{
    		$('#education-other-specialization-1').prop('disabled', true);
    	}
    });
    
    $('#education-specialization-2').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-specialization'){
    		$('#education-other-specialization-2').prop('disabled', false);
    	}else{
    		$('#education-other-specialization-2').prop('disabled', true);
    	}
    });
    
    $('#education-specialization-3').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-specialization'){
    		$('#education-other-specialization-3').prop('disabled', false);
    	}else{
    		$('#education-other-specialization-3').prop('disabled', true);
    	}
    });
    
   
	$("#edu-1-collapse").on("hide.bs.collapse", function(){
		$("#edu-1-collapse-btn").html('<span class="fa fa-chevron-circle-down"></span>');
	});
	$("#edu-1-collapse").on("show.bs.collapse", function(){
		$("#edu-1-collapse-btn").html('<span class="fa fa-chevron-circle-up"></span>');
	});
	
	$("#edu-2-collapse").on("hide.bs.collapse", function(){
		$("#edu-2-collapse-btn").html('<span class="fa fa-chevron-circle-down"></span>');
	});
	$("#edu-2-collapse").on("show.bs.collapse", function(){
		$("#edu-2-collapse-btn").html('<span class="fa fa-chevron-circle-up"></span>');
	});
	
	$("#edu-3-collapse").on("hide.bs.collapse", function(){
		$("#edu-3-collapse-btn").html('<span class="fa fa-chevron-circle-down"></span>');
	});
	$("#edu-3-collapse").on("show.bs.collapse", function(){
		$("#edu-3-collapse-btn").html('<span class="fa fa-chevron-circle-up"></span>');
	});
	
	
	$("#edu-1-clear-btn").click(function(){
		$("#education-degree-1").val("");
		$("#education-degree-1").selectpicker('refresh');
		$("#education-other-degree-1").val("");
		$("#education-other-degree-1").prop('disabled', true);
		
		$("#education-specialization-1").val("");
		$("#education-specialization-1").selectpicker('refresh');
		$("#education-other-specialization-1").val("");
		$("#education-other-specialization-1").prop('disabled', true);
	});
	
	$("#edu-2-clear-btn").click(function(){
		$("#education-degree-2").val("");
		$("#education-degree-2").selectpicker('refresh');
		$("#education-other-degree-2").val("");
		$("#education-other-degree-2").prop('disabled', true);
		
		$("#education-specialization-2").val("");
		$("#education-specialization-2").selectpicker('refresh');
		$("#education-other-specialization-2").val("");
		$("#education-other-specialization-2").prop('disabled', true);
	});
	
	$("#edu-3-clear-btn").click(function(){
		$("#education-degree-3").val("");
		$("#education-degree-3").selectpicker('refresh');
		$("#education-other-degree-3").val("");
		$("#education-other-degree-3").prop('disabled', true);
		
		$("#education-specialization-3").val("");
		$("#education-specialization-3").selectpicker('refresh');
		$("#education-other-specialization-3").val("");
		$("#education-other-specialization-3").prop('disabled', true);
	});
	
	$('#occ-1').change(function(){
    	if($(this).find("option:selected").val() == 'occ-specify-other'){
    		$('#occ-other-1').prop('disabled', false);
    	}else{
    		$('#occ-other-1').val("");
    		$('#occ-other-1').prop('disabled', true);
    	}
    });
    
	$('#occ-2').change(function(){
    	if($(this).find("option:selected").val() == 'occ-specify-other'){
    		$('#occ-other-2').prop('disabled', false);
    	}else{
    		$('#occ-other-2').val("");
    		$('#occ-other-2').prop('disabled', true);
    	}
    });
	
	$('#occ-desg-1').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-desg'){
    		$('#occ-other-desg-1').prop('disabled', false);
    	}else{
    		$('#occ-other-desg-1').val("");
    		$('#occ-other-desg-1').prop('disabled', true);
    	}
    });
	
	$('#occ-desg-2').change(function(){
    	if($(this).find("option:selected").val() == 'specify-other-desg'){
    		$('#occ-other-desg-2').prop('disabled', false);
    	}else{
    		$('#occ-other-desg-2').val("");
    		$('#occ-other-desg-2').prop('disabled', true);
    	}
    });*/
	
	/*$("#photo-upload").fileinput({
        initialPreview: [
        ],
        initialPreviewAsData: true,
        initialPreviewConfig: [
        ],
        deleteUrl: "/site/file-delete",
        overwriteInitial: false,
        maxFileSize: 100,
        initialCaption: "No file selected"
    });*/
});

function editInfo(section) {
	switch (section) {
	case 'Personal':
		$("#personalInfoSectionBody").load(ctxPath+"ePersonalInfo");
		break;
	case 'Family':
		$("#familyInfoSectionBody").load(ctxPath+"eFamilyInfo");
		break;
	case 'Contact':
		$("#contactInfoSectionBody").load(ctxPath+"eContactInfo");
		break;
	case 'Education':
		$("#educationInfoSectionBody").load(ctxPath+"eEducationInfo");
		break;
	case 'Photo':
		$("#photoInfoSectionBody").load(ctxPath+"ePhotoInfo");
		break;
	default:
		break;
	}
}

function cancelEdit(section) {
	switch (section) {
	case 'Personal':
		$("#personalInfoSectionBody").load(ctxPath+"vPersonalInfo");
		break;
	case 'Family':
		$("#familyInfoSectionBody").load(ctxPath+"vFamilyInfo");
		break;
	case 'Contact':
		$("#contactInfoSectionBody").load(ctxPath+"vContactInfo");
		break;
	case 'Education':
		$("#educationInfoSectionBody").load(ctxPath+"vEducationInfo");
		break;
	case 'Photo':
		$("#photoInfoSectionBody").load(ctxPath+"vPhotoInfo");
		break;
	default:
		break;
	}
}

function saveInfo(section) {
	
	switch (section) {
	case 'Personal':
		var $editPersonalInfoForm = $('#editPersonalInfoForm');
		$.ajax({
			url: $editPersonalInfoForm.attr('action'),
			type: 'post',
			data: $editPersonalInfoForm.serialize(),
			success: function(response) {
				// if the response contains any errors, replace the form
				//if ($(response).find('.has-error').length) {
				if($("#isNewCandidate")){
					location.reload();
				}else{
					$("#personalInfoSectionBody").replaceWith(response);
				}
				//} else {
				// in this case we can actually replace the form
				// with the response as well, unless we want to 
				// show the success message a different way
				//}
				
			}
		});
		
		break;
		
	case 'Family':
		var $editFamilyInfoForm = $('#editFamilyInfoForm');
		$.ajax({
			url: $editFamilyInfoForm.attr('action'),
			type: 'post',
			data: $editFamilyInfoForm.serialize(),
			success: function(response) {
				$("#familyInfoSectionBody").replaceWith(response);
			}
		});
		
		break;
	
	case 'Contact':
		var $editContactInfoForm = $('#editContactInfoForm');
		$.ajax({
			url: $editContactInfoForm.attr('action'),
			type: 'post',
			data: $editContactInfoForm.serialize(),
			success: function(response) {
				$("#contactInfoSectionBody").replaceWith(response);
			}
		});
	
	case 'Education':
		var $editEducationInfoForm = $('#editEducationInfoForm');
		$.ajax({
			url: $editEducationInfoForm.attr('action'),
			type: 'post',
			data: $editEducationInfoForm.serialize(),
			success: function(response) {
				$("#educationInfoSectionBody").replaceWith(response);
			}
		});
		
		break;
		
	case 'Photo':
		var $editPhotoInfoForm = $('#editPhotoInfoForm');
		$.ajax({
			url: $editPhotoInfoForm.attr('action'),
			type: 'post',
			data: $editPhotoInfoForm.serialize(),
			success: function(response) {
				$("#photoInfoSectionBody").replaceWith(response);
			}
		});
		
		break;
		
	default:
		break;
	}
}



function onChangeDropdown(dropdown){
	
	console.log(" dd "+dropdown);
	
	switch (dropdown) {
	
	// TODO: handling specify other options for state, country, desg, occupation, degree, specialization
	case 'address-state':
		if($("#address-state").val() != '' || $("#address-state option:selected").text() != 'Specify Other'){
			$("#address-country option").each(function() {
				if($(this).text() == 'India') {
					$(this).prop('selected', 'selected');            
				}
				$("#address-country").selectpicker('refresh');
			});0
		}else if($("#address-state option:selected").text() == 'Specify Other'){
			
			$("#address-state-other").prop("disabled", false);
			
			$("#address-country option").each(function() {
				if($(this).text() == 'India') {
					$(this).prop('disabled', 'disabled');
					$("#address-country").selectpicker('refresh');
				}
				
				if($(this).text() == 'USA') {
					$(this).prop('selected', 'selected');
					$("#address-country").selectpicker('refresh');
				}
				
			});
		}
		break;

	default:
		break;
	}
}

