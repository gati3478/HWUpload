var n = 0;

function addField()
{
	var container = document.getElementById("list");
	var node = document.createElement("LI");
	var input = document.createElement("input");
	input.type = "text";
	input.name = "lecturer" + n;
	input.required = true;
	node.appendChild(input);
	node.appendChild(document.createTextNode("@freeuni.edu.ge"));
	container.appendChild(node);
	n++;
};

function removeField()
{
	if(n == 0) return;
	var container = document.getElementById("list");
	container.removeChild(container.lastChild);
	n--;
};