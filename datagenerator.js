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

function getRantomGender(){

}

fuction getHTMLTable(){
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
  for(var i = 0; i < 100; i++)
    tbody.append("<tr>" + 
    "<td>" + i + "</td>" +
    "<td>" + getRandomName() + "</td>" +
    "<td>" + getRandomPhone() + "</td>" +
    "<td>" + i + "</td>" +
    "<td>" + i + "</td>" +    
    "</tr>");


   return table;
}
