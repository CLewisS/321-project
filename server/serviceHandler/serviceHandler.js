/* This module handles service requests
 * 
 * Functions:
 *   - getServices: Gets services requested in the HTTP request, and returns them in the response.
 *   - addService:  Adds the new service from the HTTP request. 
 */


var db = require("../dbInterface/serviceDB.js");
var reqData = require("./requestData.js");

module.exports.getServices = function (req, res) {
  // console.log("query: " + JSON.stringify(req.query));

  var conditions = reqData.getConditionsFromQuery(req.query);

  db.get(conditions, (services, err) => { 
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(services);
      return;
    }
  });
};



module.exports.addService = function (req, res) {
  // console.log("service " + JSON.stringify(req.body));
  
  var service = reqData.getServiceFromReq(req.body);
  var  insertID ;

  db.add(service, (id, err) => {
    db.adduserServices(service, id, (err) => {
      if (err) {
        res.status(err.code).json(err);
	return;
      }
    });

    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(id);
      return;
    }
  });

};


module.exports.deleteService = function (req, res) {
  // console.log("In service handler: delete service");
  const id = req.query;
  const keys = Object.keys(id);
  if(keys.length!==1 || keys[0]!=="id"){
    throw "The delete service id passed in was wrong.";
  }

  // still need to delete the service in the serviceUser table.

  var idNum = parseInt(id[keys[0]],10);
  
  db.delete(idNum, (id, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(id);
      return;
    }
  });
 
};


module.exports.updateService = function (req, res) {
  // console.log("In service handler: update service");
  var updateService = req.body;
  const keys = Object.keys(req.query);
  if(keys.length!==1 || keys[0]!=="id"){
    throw "The delete service id passed in was wrong.";
  }

  var serviceID = parseInt(req.query["id"], 10);

  if(!reqData.serviceIsValid(updateService)){
    throw "This is not a valid service!";
  }

  db.update(serviceID, updateService, (service, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(service);
      return;
    }
  });

};


module.exports.receiveService = function (req, res) {
  // console.log("In service handler: receive service");
  
  var receiveInfo = req.body;
  
  db.receive(receiveInfo.username, receiveInfo.serviceID , (id, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(id);
      return;
    }
  });
  
};


module.exports.getReceivedServices = function (req, res) {
  // console.log("In service handler: get received services");

  var conditions = ["username='" + req.query.username + "'", "status='" + req.query.status + "'"];

  db.getReceivedIDs(conditions, (services, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      var conditions = [];
      for(var service of services) {
        conditions.push("id=" + service.serviceID);
      }

      db.getReceivedServices(conditions, (services, err) => { 
        if (err) {
          res.status(err.code).json(err);
          return;
        } else {
          res.json(services);
          return;
        }
      });
    }
  });
};
