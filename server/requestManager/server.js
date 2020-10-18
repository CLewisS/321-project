/* Server Request manager
 *
 * This is the interface between the server and the client.
 * Any requests from the client are routed to their respective Handler here.
 */

var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var serviceHandler = require('../serviceHandler/serviceHandler.js');
var chatServer = require('../chatServer/chatServer.js');


const PORT = 3000;

//*******************
// JUST FOR DEBUGGING
app.get('/', (req, res) => {
  res.sendFile(__dirname + '/test.html');
});
//*******************

app.use(bodyParser.json());

app.get('/service', serviceHandler.getServices);

app.post('/service', serviceHandler.addService);

app.get('/chat', chatServer.getNewMessages);


app.listen(PORT, () => console.log('Listening on port ' + PORT));
