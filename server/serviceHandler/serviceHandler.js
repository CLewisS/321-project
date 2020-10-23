/* This module handles service requests
 * 
 * Functions:
 *   - getServices: Gets services requested in the HTTP request, and returns them in the response.
 *   - addService:  Adds the new service from the HTTP request. 
 */


var db = require("../dbInterface/serviceDB.js");
var serviceAttributes = ["date", "dow", "time", "lat", "longi", "type"];

var isValidCondition = function (condition) {
  var split = condition.split("-");
  if (split.length == 2 
      && serviceAttributes.includes(split[0])
      && (split[1] === "max" || split[1] == "min")) {
console.log("valid");
    return true;
  } 

  return false;
}; 

module.exports.getServices = function (req, res) {
  console.log("In service handler: get services");

  // This is just a placeholder for debugging
  // This is also a good example of what the conditions object should be like
  //*****************************************
  /*var conditions = {
    date:  {min:"2020-10-15", max:"2020-10-25"},
    time:  {max:"15:57:33"},
    lat:   {min: 49.56911},
    longi: {max: 130.456, min: 122.908}
  };*/
  //*****************************************

  //TODO: Get conditions from request (req) body, and check that it has all the information required
  // The conditions should be in a JSON object (Details in README), and assigned to the variable conditions.
  
  console.log("query: " + JSON.stringify(req.query));
  
  var conditions = [];
  var queryString = req.query;

  // THIS IS NOT CLEAN BUT IT WORKS
    // it goes through each part of the query string and adds it to the conditions array
  for (var key of Object.keys(queryString)) {
    var split = key.split("-");

    if (key === "type") {
      conditions.push("type='" + queryString[key] + "'");
    } else if (isValidCondition(key) && split[1] === "max") { 

        if (split[0] == "lat" || split[0] == "longi") {
          conditions.push(split[0] + " <= " + queryString[key]);
        } else {
          conditions.push(split[0] + " <=" + "'" + queryString[key] + "'");
        }

    } else if (isValidCondition(key) && split[1] === "min") { 

        if (split[0] == "lat" || split[0] == "longi") {
          conditions.push(split[0]+ " >= " + queryString[key]);
        } else {
          conditions.push(split[0] + " >=" + "'" + queryString[key] + "'");
        }

    }
  }

  console.log(conditions);

  db.get(conditions, (services) => { 
    res.json(services);
  });
}


module.exports.addService = function (req, res) {
  

  console.log("In service handler: add service");
 
  // This is just a placeholder for debugging
  // This is also a good example of what the service object should be like
  //*****************************************
  var service = {
    name: "A service",
    date: "2020-10-17",
    time: "12:57:33",
    lat:   49.56911,
    longi: 123.456,
    owner: "Jon",
    type:  "food",
    description: "Food will be provided"
  };
  //*****************************************

  //TODO: Get service from request (req) body, and check that it has all the information required
  // The service should be in a JSON object (Details in README), and assigned to the variable service.

  var body = req.body; // Now we have the JSON object that the client put in the request body

  // First we want to check if the parameters are valid (Here I only checked if it is type string, but we want to check more than that)
  if (typeof(body) === "string") {
    body = JSON.parse(body);
  }

  service = body; // Then we assign to the service variable so that it gets passed to db.add()

  console.log(service);

  //console.log(req.body);

  db.add(service, (id) => {
    res.json(id);
  });

};
