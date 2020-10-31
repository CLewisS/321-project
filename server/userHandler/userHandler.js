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
  

  db.add(user, (username) => {
    res.json(username);
  });

/*  } else {
  
    console.log("This is not a valid object for create a service.");
    res.status(400).json({error: "Service object is invalid"});

  }*/

};



module.exports.deleteUser = function (req, res) {
  console.log("In service handler: delete user");
  console.log(req.query);
  const username = req.query;
  const keys = Object.keys(username);
  if(keys.length!==1 || keys[0]!=="username"){
    throw "The delete username passed in was wrong.";
  }

  
  db.delete(username.username, (user) => {
    res.json(user);
  });
 
};







module.exports.updateUser = function (req, res) {
  console.log("In service handler: update service");
  var updateUser = req.body;

  if(!checkData.checkUserInfo(updateUser)){
    throw "This is not a valid User Object.";
 }

  db.update(updateUser, (user) => {
    res.json(user);
  });

};




module.exports.loginCheck = function (req, res) {

  console.log("In service handler: check user login. " + JSON.stringify(req.body));
  
  var loginInfo = req.body;
  
  console.log(loginInfo);
  db.loginCheck(loginInfo , (result)=>{
    res.json(result);
  })

}
