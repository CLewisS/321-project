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
  
    console.log("This is not a valid json object for create a service.");
    res.status(400).json({error: "Service object is invalid"});

  }*/

};
