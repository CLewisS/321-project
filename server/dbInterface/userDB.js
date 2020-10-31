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

  console.log("Adding User to DB");


  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Get Service Values  
    try {
      if (Object.values(user).length < 2) throw "User has too few [" + Object.values(user).length + "] values. user: " + JSON.stringify(user);
    } catch (err) {
      return console.error(err);
    }
    var values = [
      user.username,
      user.password,
      user.deviceToken,
    ];
    
    // Insert service into database
    var query = `INSERT INTO users (username, password, deviceToken )
                 VALUES(?, ?, ?)`;

    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log("Inserted");

      callback({
        username: user.username,
        password: user.password
      });
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
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
  console.log("Getting Services from DB");

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");
    
    // Build SQL query
    var query = `DELETE FROM users WHERE username = ` + `"` + username + `"`;
  
    console.log(query);
    // Get services
    dbConn.query(query, (err, result, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      callback(result);
    });

    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
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

  console.log("Adding Service to DB");


  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Get Service Values  
    try {
      if (Object.values(user).length != 3) throw "User has too few [" + Object.values(user).length + "] values. user: " + JSON.stringify(user);
    } catch (err) {
      return console.error(err);
    }
    var values = [
      user.username,
      user.password,
      user.deviceToken,
    ];
    
    // Insert service into database
    var query = `UPDATE users SET username = ?, password = ?, deviceToken = ? WHERE username = ` + `"`+ user.username + `"`;
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log("Updated");

      callback({
        username: user.username,
        password: user.password
      });
    });


    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
    });

  });

}; 










module.exports.loginCheck = function (loginInfo, callback) {

  console.log("login check " + JSON.stringify(loginInfo));


  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Get Service Values  
    try {
      if (Object.values(loginInfo).length != 3) throw "loginInfo has too few [" + Object.values(loginInfo).length + "] values. loginInfo: " + JSON.stringify(loginInfo);
    } catch (err) {
      return console.error(err);
    }
    var values = [
      loginInfo.deviceToken
    ];
    
    // Insert service into database
    var query1 = `Select password from users where username=`  + `"` + loginInfo.username + `"`;

    dbConn.query(query1, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
      console.log(JSON.stringify(loginInfo));
      console.log(results[0].password === loginInfo.password);
      if (results[0].password === loginInfo.password){
        callback({username: loginInfo.username, password: loginInfo.password});
      }else{
        callback(401);
      }
    });

    var query2 = `UPDATE users SET deviceToken = ? WHERE username = ` + `"` + loginInfo.username + `"`;
    console.log(query2);
    dbConn.query(query2,values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
    });
   

    // End connection
    dbConn.end(function (err) {
      if (err) {
        return console.error("error: " + err.message); 
      }
  
      console.log("Closed connection to MySQL server");
    });

  });

}; 
