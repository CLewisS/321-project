
var db = require("../dbInterface/chatDB.js");


var admin = require("firebase-admin");

var serviceAccount = require("./fcm-key.json");
var check = require("./chatCheck.js")
var registrationToken = "do3dy_XfRbSg0oPjJXxKqO:APA91bEHsnPBlhVeAaxVTHKUGr7snS8NeB5CjrGTg4h412gdwINLyV9l6_k89PDfqH6J8Yed0VKe8--fA-xchn1GdDb3_-Iu6GtttFeESrl8XSG_LfcxJy96GDJ06DGSXBv8G6NWsZrK";

//Connect to firebase server to push new messages to recipient
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://communitylink-1d665.firebaseio.com"
});


module.exports.getMessages = function(req, res) {
  console.log("In chat server");
 
  var queryString = req.query;
  check.checkMessageQuery(queryString);
  console.log(queryString)
  db.get(queryString.user1, queryString.user2, queryString.newest, (messages) => {

    res.json(JSON.parse(messages));
    console.log(messages);
  });
};

module.exports.addMessage = function(req, res) {
  console.log("In chat server");
  
  var body = req.body;

  if (typeof(body) === "string") {
    body = JSON.parse(body);
  }
  check.checkMessage(body);
  message = body;
  var payload = {
      data: message
  };

  var options = {
    priority: "high",
    timeToLive: 60 * 60 *24
  };

  admin.messaging().sendToDevice(registrationToken, payload, options)
  .then(function(response) {
    console.log("Successfully sent message:");
  })
  .catch(function(error) {
    console.log("Error sending message:", error);
  });

  db.add(message, (id) => {
    res.json(id);
  });
};
