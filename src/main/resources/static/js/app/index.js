var password = document.getElementById("signupPassword"), confirm_password = document
		.getElementById("signupConfirmPassword");

function validatePassword() {
	if (password.value != confirm_password.value) {
		confirm_password.setCustomValidity("Passwords Don't Match");
	} else {
		confirm_password.setCustomValidity('');
	}
}
password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;

$(document).ready(function() {
	/*$("#signupForm").submit(function(event) {
		console.log(" signup form ... ");
	});*/
});
