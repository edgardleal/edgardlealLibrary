var names = ["Mary", "Antony", "Jhon", "Bill"];

function getRandomName(){
   return names[Math.round(names.length * Math.random())] + " " +
     names[Math.round(names.length * Math.random())];
}

function getRandomPhone(){
   return "(55) 4458-9965";
}

function getRandomEmail(){

}

var genders = ["Male", "Female"];
function getRantomGender(){
  return genders[Math.round(genders.length * Math.random())];
}

function getHTMLTable(count){
   var table = $("<table><thread></thead><tbody></tbody></table>");
   var thead = table.find('thead');
   thead.append("<tr>" + 
      "<th>Code</th> " +
      "<th>Name</th> " +
      "<th>Phone</th> " +
      "<th>email</th> " +
      "<th>Gender</th> " +      
      "</tr>");
      
  var tbody = table.find('tbody');
  
  var _count =  count&&typeof count === "number"?count:100;
  for(var i = 0; i < _count; i++)
    tbody.append("<tr>" + 
    "<td>" + i + "</td>" +
    "<td>" + getRandomName() + "</td>" +
    "<td>" + getRandomPhone() + "</td>" +
    "<td>" + i + "</td>" +
    "<td>" + getRantomGender() + "</td>" +    
    "</tr>");


   return table;
}
