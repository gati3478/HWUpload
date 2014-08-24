var exts = ["docx", "doc", "xls", "xlsx", "ppt", "cpp", "java", "h", "pdf", "jpeg"];
var n = 0;

function addField()
{
   var container = document.getElementById("list");
   var node = document.createElement("LI");
   var regex = document.createElement('input');
   regex.type = 'text';
   regex.name = 'regex' + n;
   node.appendChild(regex);
   var choice = document.createElement("select");
   choice.name = "ext" + n;
   for(var i = 0; i < exts.length; ++i){
      var option = document.createElement("option");
      option.value = exts[i];
      option.text = exts[i];
      choice.add(option);
   }
   node.appendChild(choice);
   container.appendChild(node);
   n++;
};

addField();

function removeField()
{
   if(n == 1) return;
   var container = document.getElementById("list");
   container.removeChild(container.lastChild);
   n--;
};