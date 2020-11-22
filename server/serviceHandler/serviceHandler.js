/* This module handles service requests
 * 
 * Functions:
 *   - getServices: Gets services requested in the HTTP request, and returns them in the response.
 *   - addService:  Adds the new service from the HTTP request. 
 */


var db = require("../dbInterface/serviceDB.js");
var reqData = require("./requestData.js");

module.exports.getServices = function (req, res) {

  try {
    var conditions = reqData.getConditionsFromQuery(req.query);
  } catch(err) {
    res.status(400).json({code: 400, message: err});
    return;
  }

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

  try {  
    var service = reqData.getServiceFromReq(req.body);
  } catch (err) {
    res.status(400).json({code: 400, message: err});
    return;
  }

  db.add(service, (id, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {

      db.adduserServices(service, id, (err) => {
        if (err) {
          res.status(err.code).json(err);
          return;
        } else {
          res.json(id);
          return;
        }
      });

    }

  });

};


module.exports.deleteService = function (req, res) {

  const id = req.query;
  const keys = Object.keys(id);
  if(keys.length!==1 || keys[0]!=="id"){
    //throw "The delete service id passed in was wrong.";
    res.status(400).json({code: 400, message: "The query for the service to be deleted is invalid"});
    return;
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

  var updateService = req.body;
  const keys = Object.keys(req.query);
  if(keys.length!==1 || keys[0]!=="id"){
    res.status(400).json({code: 400, message: "The id for the service to be update is invalid"});
    return;
  }

  var serviceID = Number(req.query["id"]);
  
  try {
    reqData.serviceIsValid(updateService)
  } catch (err) {
    console.log(err);
    res.status(400).json({code: 400, message: err});
    return;
  }

  db.update(serviceID, updateService, (service, err) => {

    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
     // service.date = JSON.stringify(service.date);
      //console.log(service);
      res.json(service);
      return;
    }

  });

};


module.exports.receiveService = function (req, res) {
  
  var receiveInfo = req.body;
  
  db.receive(receiveInfo.username, receiveInfo.serviceID , (result, err) => {

    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json({id: result.insertId});
      return;
    }

  });
  
};


module.exports.getReceivedServices = function (req, res) {

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
