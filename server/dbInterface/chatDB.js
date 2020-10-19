/* The interface for the chat Database
 */

var mysql = require('mysql');
var dbConfig = require('./dbConfig');


/* Adds a new entry to the chat database
 *
 * Parameters:
 *   - message: The values for the message to be added. 
 *              Must be a JSON object.
 *
 *   - callback: A callback function that is called once the message has been inserted.
 *               The message number of the inserted message is passed as an argument.
 */

module.exports.add = function (message, callback) {

  console.log('Adding message to DB');


  var dbConn = mysql.createConnection(dbConfig.chatDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error('error: ' + err.message);
    }

    console.log('Connected to MySQL server');

    var thread = message.user1 + ':' + message.user2;
    
    // Increment number of messages in thread
    var query = 'INSERT INTO threads (thread, numMess) VALUES (\'' + thread + '\', 1) ON DUPLICATE KEY UPDATE numMess = numMess + 1';
    dbConn.query(query, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log('Updated thread message count');

      // Get number of messages in thread
      query = 'SELECT numMess FROM threads WHERE thread = \'' + thread + '\'';
      dbConn.query(query, (err, results, fields) => {
        if (err) {
          return console.error(err.message);
        }

        // Insert message into table
        query = 'INSERT INTO messages (id, time, content) VALUES(\'' + thread + ':' + results[0].numMess + '\', \'' + message.time + '\', \'' + message.content + '\')';
        dbConn.query(query, (err, results, fields) => {
          if (err) {
            return console.error(err.message);
          }
        });
          
        // End connection
        dbConn.end(function (err) {
          if (err) {
            return console.error('error: ' + err.message); 
          }
      
          console.log('Closed connection to MySQL server');
        });
      });

    });

  });

}; 
