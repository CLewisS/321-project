/* Server Request manager
 *
 * This is the interface between the server and the client.
 * Any requests from the client are routed to their respective Handler here.
 */

var express = require("express");
var bodyParser = require("body-parser");
var app = express();
var serviceHandler = require("../serviceHandler/serviceHandler.js");
var userHandler = require("../userHandler/userHandler.js");
var chatServer = require("../chatServer/chatServer.js");


const PORT = 5150;

//*******************
// JUST FOR DEBUGGING
app.get("/", (req, res) => {
  res.sendFile(__dirname + "/test.html");
});
//*******************

var jsonParser = bodyParser.json();

app.use(bodyParser.json());

app.route("/service")
  .get(serviceHandler.getServices)
  .post(serviceHandler.addService)
  .put(serviceHandler.updateService)
  .delete(serviceHandler.deleteService);

app.route("/service/use")
  .post(serviceHandler.receiveService)
  .get(serviceHandler.getReceivedServices);

app.route("/chat")
  .post(chatServer.addMessage)
  .get(chatServer.getMessages);

app.route("/user")
  .post(userHandler.addUser)
  .put(userHandler.updateUser)
  .delete(userHandler.deleteUser);

app.put("/user/login", userHandler.loginCheck);



app.listen(PORT, () => console.log("Listening on port " + PORT));
