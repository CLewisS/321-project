/* This module handles user requests
 * 
 * Functions:
 *   
 */




var db = require("../dbInterface/userDB.js");
var checkData = require("./userInfoCheck.js");


module.exports.addUser = function (req, res) {
  // console.log("In service handler: add user");
  var user = req.body;

  try {
    checkData.checkUserInfo(user);
  } catch (err) {
    console.log(err);
    res.status(400).json({code: 400, message: err});
    return;
  }  

  if (!user.hasOwnProperty("deviceToken")) {
    user.deviceToken = "";
  }

  db.add(user, (username, err) => {
    if (err) {
      res.status(err.code).json({code: 403, message: err.message});
      return;
    } else {
      res.json(username);
      return;
    }
  });
};


module.exports.deleteUser = function (req, res) {
  // console.log("In service handler: delete user");
  const username = req.query;
  const keys = Object.keys(username);
  if(keys.length!==1 || keys[0]!=="username"){
    res.status(400).json({code: 400, message: "Expected a username, but didn't get one"});
    return;
  }

  
  db.delete(username.username, (user, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(user);
      return;
    }
  });
 
};


module.exports.updateUser = function (req, res) {
  // console.log("In service handler: update service");
  var updateUser = req.body;

  try {
    checkData.checkUserInfo(updateUser);
  } catch (err) {
    console.log(err);
    res.status(400).json({code: 400, message: err});
    return;
  }  

  if (!updateUser.hasOwnProperty("deviceToken")) {
    updateUser.deviceToken = "";
  }

  db.update(updateUser, (user, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(user);
      return;
    }
  });

};


module.exports.loginCheck = function (req, res) {

  // console.log("In service handler: check user login. " + JSON.stringify(req.body));
  
  var loginInfo = req.body;

  try {
    checkData.checkUserInfo(loginInfo);
  } catch (err) {
    console.log(err);
    res.status(400).json({code: 400, message: err});
    return;
  }  
  
  db.loginCheck(loginInfo , (result, err) => {
    if (err) {
      res.status(err.code).json(err);
      return;
    } else {
      res.json(result);
    }
  });

};
