/* The interface for the chat Database
 */

var mysql = require("mysql");
var dbConfig = require("./dbConfig");


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

  var dbConn = mysql.createConnection(dbConfig.chatDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    var users = [message.sender, message.recipient];
    users.sort();

    var thread = users[0] + ":" + users[1];
    
    // Increment number of messages in thread
    var query = "INSERT INTO threads (thread, numMess) VALUES ('" + thread + "', 1) ON DUPLICATE KEY UPDATE numMess = numMess + 1";
    dbConn.query(query, (err, results, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }
  
      // Get number of messages in thread
      query = "SELECT numMess FROM threads WHERE thread = '" + thread + "'";
      dbConn.query(query, (err, results, fields) => {
        if (err) {
          callback({}, {code: 500, message: err.message});
          return;
        }


        var values = [thread + ":" + results[0].numMess, 
                      message.sender, 
                      message.recipient, 
                      message.timestamp, 
                      message.content];
        // Insert message into table
        query = "INSERT INTO messages (id, sender, recipient, time, content)" + 
                " VALUES(?, ?, ?, ?, ?)"; 

        dbConn.query(query, values, (err, results, fields) => {
          if (err) {
            callback({}, {code: 500, message: err.message});
            return;
          }

          callback({id: values[0]});
        });
          
        // End connection
        dbConn.end(function (err) {
          if (err) {
            callback({}, {code: 500, message: err.message});
            return;
          }
      
        });
      });

    });

  });

}; 

/* Gets messages that meet the given conditions from the chat database. 
 *
 * Parameters:
 *   - user1: The user whose ID comes first lexicographically
 *   - user2: The user whose ID comes lexicographically second
 *   - newest: The newest message number that the requesting user has from the thread
 *
 *   - callback: A callback function that is called once the messages have been retrieved.
 *               The retrieved messages are passed as an argument.
 */
module.exports.get = function(user1, user2, newest, callback) {

  var dbConn = mysql.createConnection(dbConfig.chatDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return;
    }

    var users = [user1, user2];
    users.sort();

    var thread = users[0] + ":" + users[1];

    var query = "SELECT numMess FROM threads WHERE thread='" + thread + "'";

    // Get total number of thread messages
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return;
      }

      if (result[0]) {
        // Get messages newer than newest
        var numMess = result[0].numMess;

        query = "SELECT JSON_ARRAYAGG(JSON_OBJECT('sender', sender, 'recipient', recipient, 'timestamp', time, 'content', content)) FROM messages" + 
                " WHERE (sender='" + user1 + "' AND recipient='" + user2 + "') OR (sender='" + user2 + "' AND recipient='" + user1 + "') AND time>='" + newest + "'";

        dbConn.query(query, (err, results, fields) => {
          if (err) {
            callback({}, {code: 500, message: err.message});
            return;
          }
          callback(Object.values(results[0])[0]);
        });
      }

      // End connection
      dbConn.end(function (err) {
        if (err) {
          callback({}, {code: 500, message: err.message});
          return;
        }
      });
    });


  });

};
