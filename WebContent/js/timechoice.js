function addTimeSelect() {
	addOptions(24, "hours");
	addOptions(60, "minutes");
}

function addOptions(max, containerID){
	for(var i = 0; i < max; ++i){
		var string = (i < 10) ? "0" + i : i;
		var option = document.createElement("option");
		option.value = string;
		option.text = string;
		document.getElementById(containerID).add(option);
	}
}

addTimeSelect();