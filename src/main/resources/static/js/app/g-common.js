function getDayOfWeekFromDay(day){
	//day++;
	var d = "";
	switch (day) {
		case 0:
			d = "Sunday";
			break;
		case 1:
			d = "Monday";
			break;
		case 2:
			d = "Tuesday";
			break;
		case 3:
			d = "Wednesday";
			break;
		case 4:
			d = "Thursday";
			break;
		case 5:
			d = "Friday";
			break;
		case 6:
			d = "Saturday";
			break;
		default:
			break;
	}
	return d;
}