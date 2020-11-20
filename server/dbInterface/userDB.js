/* The interface for the user database
 *
 * Functions:
 *   - add: Adds a new user to the user database.
 */


var mysql = require("mysql");
var dbConfig = require("./dbConfig");


/* Adds a new user to the database.
 *
 * Parameters:
 *   - user: The values for the user to be added. 
 *              Must be a JSON object.
 *
 *   - callback: A callback function that is called once the user  has been inserted.
 *               The unique identifier of the inserted user is passed as an argument.
 */

module.exports.add = function (user, callback) {

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return; 
    }

    var values = [
      user.username,
      user.password,
      user.deviceToken,
    ];
    
    // Insert service into database
    var query = "INSERT INTO users (username, password, deviceToken) VALUES(?, ?, ?)";

    dbConn.query(query, values, (err, results, fields) => {
      if (err && err.code === "ER_DUP_ENTRY") {
        callback({}, {code: 403, message: "USER_ALREADY_EXISTS"});
        return;
      } else if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
  
      callback({
        username: user.username,
        password: user.password
      });
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
  
    });

  });

}; 





/* Delete the user from the users database. 
 *
 * Parameters:
 *   - userID: The ID represented the user user want to delete.
 *
 *   - callback: A callback function that is called once the user have been deleted.
 *               The retrieved user are passed as an argument.
 */
module.exports.delete = function(username, callback) {

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return; 
    }

    // Build SQL query
    var query = "DELETE FROM users WHERE username = '" + username + "'";
  
    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
    });

  });

};


/* Update a service in the services database
 *
 * Parameters:
 *   -serviceID: The ID of the service is gona be updated.
 *   - service: The updated services. 
 *              Must be a JSON object.
 *
 *   - callback: A callback function that is called once the service  has been updated.
 *               The unique identifier of the inserted service is passed as an argument.
 */

module.exports.update = function ( user, callback) {

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return; 
    }

    var values = [
      user.username,
      user.password,
      user.deviceToken,
    ];
    
    // Insert service into database
    var query = "UPDATE users SET username = ?, password = ?, deviceToken = ? WHERE username = '" + user.username + "'";
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
  
      callback({
        username: user.username,
        password: user.password
      });
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
    });

  });

}; 


module.exports.loginCheck = function (loginInfo, callback) {

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return; 
    }

    // Insert service into database
    var query1 = "Select password from users where username='" + loginInfo.username + "'";
    dbConn.query(query1, (err, results, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }

      if (results[0] && results[0].password === loginInfo.password){
        callback({username: loginInfo.username, password: loginInfo.password});
      }else{
        callback({}, {code: 401, message: "Username and password aren't a valid pair"});
      }
    });

    var query2 = "UPDATE users SET deviceToken ='" + loginInfo.deviceToken + "' WHERE username = '" + loginInfo.username + "'";
    dbConn.query(query2, (err, results, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
    });
   

    // End connection
    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
    });

  });

};


/* Retrieve a users information 
 *
 * Parameters:
 *   -username: The username of the user beign retrieved.
 *
 *   - callback: A callback function that is called once the user has been retrieved.
 *               The function must have one argument which will be the user object.
 */
module.exports.get = function(username, callback) {

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      callback({}, {code: 500, message: err.message});
      return; 
    }

    // console.log("Connected to MySQL server");

    var query1 = "SELECT * FROM users where username='" + username  + "'";

    dbConn.query(query1, (err, results, fields) => {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      } else{
        callback(results[0]);
      }
    });

    dbConn.end(function (err) {
      if (err) {
        callback({}, {code: 500, message: err.message});
        return; 
      }
    });

  });
};
