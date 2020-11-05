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
  console.log("service " + JSON.stringify(req.body));
  
  var service = reqData.getServiceFromReq(req.body);
  var  insertID ;

  
db.add(service, (id) => {
  db.adduserServices(service, id);
    res.json(id);
  });
  
  /*  } else {
  
    console.log("This is not a valid object for create a service.");
    res.status(400).json({error: "Service object is invalid"});

  }*/

};



module.exports.deleteService = function (req, res) {
  console.log("In service handler: delete service");
  const id = req.query;
  const keys = Object.keys(id);
  if(keys.length!==1 || keys[0]!=="id"){
    throw "The delete service id passed in was wrong.";
  }

  // still need to delete the service in the serviceUser table.

  var id_num = parseInt(id[keys[0]],10);
  
  db.delete(id_num, (id) => {
    res.json(id);
  });
 
};







module.exports.updateService = function (req, res) {
  console.log("In service handler: update service");
  var updateService = req.body;
  // console.log(updateService);
  const keys = Object.keys(req.query);
  if(keys.length!==1 || keys[0]!=="id"){
    throw "The delete service id passed in was wrong.";
  }

  var serviceID = parseInt(req.query["id"], 10);
  console.log(serviceID);

  if(!reqData.serviceIsValid(updateService)){
    throw "This is not a valid service!";
  }

  db.update(serviceID, updateService, (service) => {
    res.json(service);
  });

};




module.exports.receiveService = function (req, res) {
  console.log("In service handler: receive service");
  
  var receiveInfo = req.body;
  
  // console.log("Body " + JSON.stringify(receiveInfo));

  db.receive(receiveInfo.username, receiveInfo.serviceID , (id)=>{
    res.json(id);
  })
  

/*  } else {
  
    console.log("This is not a valid object for create a service.");
    res.status(400).json({error: "Service object is invalid"});

  }*/

};


