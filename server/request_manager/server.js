/* Server Request manager
 *
 * This is the interface between the server and the client.
 * Any requests from the client are routed to their respective Handler here.
 */

var express = require('express');
var app = express();
var service_handler = require('../service_handler/service_handler.js');
var chat_server = require('../chat_server/chat_server.js');


const PORT = 3000;

// Go to service handler
app.use('/service', service_handler);

// Go to Chat server
app.use('/chat', chat_server);

app.listen(PORT, () => console.log('Listening on port ' + PORT));
