/* This module handles user requests
 * 
 * Functions:
 *   
 */




var db = require("../dbInterface/userDB.js");
var checkData = require("./userInfoCheck.js");

// module.exports.getServices = function (req, res) {
//   console.log("In service handler: get services");
//   console.log("query: " + JSON.stringify(req.query));

//   var conditions = reqData.getConditionsFromQuery(req.query);

//   console.log(conditions);

//   db.get(conditions, (services) => { 
//     res.json(services);
//   });
// }



module.exports.addUser = function (req, res) {
  console.log("In service handler: add user");
  
  var user = req.body;

  if(!checkData.checkUserInfo(user)){
     throw "This is not a valid User Object.";
  }
  

  db.add(user, (id) => {
    res.json(id);
  });

/*  } else {
  
    console.log("This is not a valid object for create a service.");
    res.status(400).json({error: "Service object is invalid"});

  }*/

};



module.exports.deleteUser = function (req, res) {
  console.log("In service handler: delete user");
  console.log(req.query);
  const id = req.query;
  const keys = Object.keys(id);
  if(keys.length!==1 || keys[0]!=="userID"){
    throw "The delete service id passed in was wrong.";
  }

  id_num = parseInt(id[keys[0]]);
  console.log(id_num);
  
  db.delete(id_num, (id) => {
    res.json(id);
  });
 
};







module.exports.updateUser = function (req, res) {
  console.log("In service handler: update service");
  var updateUser = req.body;

  const keys = Object.keys(req.query);
  if(keys.length!==1 || keys[0]!=="userID"){
    throw "The delete user id passed in was wrong.";
  }

  var userID = parseInt(req.query["userID"]);

  if(!checkData.checkUserInfo(updateUser)){
    throw "This is not a valid User Object.";
 }

  db.update(userID, updateUser, (user) => {
    res.json(user);
  });

};