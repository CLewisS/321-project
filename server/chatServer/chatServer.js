
var db = require("../dbInterface/chatDB.js");
var userDB = require("../dbInterface/userDB.js");

var admin = require("firebase-admin");

var serviceAccount = require("./fcm-key.json");
var check = require("./chatCheck.js")

//Connect to firebase server to push new messages to recipient
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://communitylink-1d665.firebaseio.com"
});


module.exports.getMessages = function(req, res) {
  // console.log("In chat server " + JSON.stringify(req.query));
 
  var queryString = req.query;
  var newest;
  if (queryString.hasOwnProperty("timestamp")) {
    newest = queryString.timestamp;
  } else {
    newest = queryString.newest;
  }

  check.checkMessageQuery(queryString);
  // console.log(queryString);

  db.get(queryString.user1, queryString.user2, newest, (messages) => {
    res.json(JSON.parse(messages));
    // console.log(messages);
  });
};

module.exports.addMessage = function(req, res) {
  // console.log("In chat server " + JSON.stringify(req.body));
  
  var body = req.body;

  if (typeof(body) === "string") {
    body = JSON.parse(body);
  }

  check.checkMessage(body);
  var message = body;
  var payload = {
      data: message
  };

  userDB.get(message.recipient, (user) => {
    if (user.deviceToken !== "") {
      var payload = {
          data: message
      };
    
      var options = {
        priority: "high",
        timeToLive: 60 * 60 *24
      };

      // console.log("Push notification " + payload.data);
      admin.messaging().sendToDevice(user.deviceToken, payload, options)
      .then(function(response) {
        console.log("Successfully sent message:" + JSON.stringify(response)); 
      })
      .catch(function(error) {
        console.log("Error sending message:", error);
      });
    }
  });


  db.add(message, (id) => {
    res.json(id);
  });
};
