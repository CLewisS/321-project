/* This module handles service requests
 * 
 * Functions:
 *   - getServices: Gets services requested in the HTTP request, and returns them in the response.
 *   - addService:  Adds the new service from the HTTP request. 
 */

//const { log, Console } = require("console");
//const { S_IFDIR } = require("constants");


var db = require("../dbInterface/serviceDB.js");
var reqData = require("./requestData.js");

module.exports.getServices = function (req, res) {
  console.log("In service handler: get services");
  console.log("query: " + JSON.stringify(req.query));

  var conditions = reqData.getConditionsFromQuery(req.query);

  console.log(conditions);

  db.get(conditions, (services) => { 
    res.json(services);
  });
}



module.exports.addService = function (req, res) {
  console.log("In service handler: add service");
  
  var service = reqData.getServiceFromReq(req.body);
  console.log(service);

  console.log(service);

  db.add(service, (id) => {
    res.json(id);
  });

/*  } else {
  
    console.log("This is not a valid object for create a service.");
    res.status(400).json({error: "Service object is invalid"});

  }*/

};



module.exports.deleteService = function (req, res) {
  console.log("In service handler: delete service");
  console.log(req.query);
  const id = req.query;
  const keys = Object.keys(id);
  if(keys.length!==1 || keys[0]!=="id"){
    throw "The delete service id passed in was wrong.";
  }

  id_num = parseInt(id[keys[0]]);
  console.log(id_num);
  
  db.delete(id_num, (id) => {
    res.json(id);
  });
 
};







module.exports.updateService = function (req, res) {
  console.log("In service handler: update service");
  var updateService = req.body;
  console.log(updateService);
  const keys = Object.keys(req.query);
  if(keys.length!==1 || keys[0]!=="id"){
    throw "The delete service id passed in was wrong.";
  }

  var serviceID = parseInt(req.query["id"]);
  console.log(serviceID);

  if(!reqData.serviceIsValid(updateService)){
    throw "This is not a valid service!";
  }

  db.update(serviceID, updateService, (service) => {
    res.json(service);
  });

};