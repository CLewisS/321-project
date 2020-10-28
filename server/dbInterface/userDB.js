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
      if (Object.values(user).length != 5) throw "User has too few [" + Object.values(user).length + "] values. user: " + JSON.stringify(user);
    } catch (err) {
      return console.error(err);
    }
    var values = [
      user.username,
      user.password,
      user.deviceToken,
      // user.servicesPosted,
      // user.servicesUsed
    ];
    
    // Insert service into database
    var query = `INSERT INTO users (username, password, deviceToken )
                 VALUES(?, ?, ?)`;

                //  create table users (
                //    userID int unsigned not null auto_increment,
                //    username varchar(150) not null,
                //    passward varchar(100) not null unique,
                //    deviceToken varchar(150) not null,
                //    servicesPosted 
                //    primary key (userID)
                //  );

    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log("Inserted");

      callback({id: results.insertId});
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
module.exports.delete = function(userID, callback) {
  console.log("Getting Services from DB");

  var dbConn = mysql.createConnection(dbConfig.userDB);

  // Start database connection  
  dbConn.connect(function (err) {
    if (err) {
      return console.error("error: " + err.message);
    }

    console.log("Connected to MySQL server");

    // Build SQL query
    var query = `DELETE FROM users WHERE userID = ` + userID;

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

module.exports.update = function (userID, user, callback) {

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
      if (Object.values(user).length != 5) throw "User has too few [" + Object.values(user).length + "] values. user: " + JSON.stringify(user);
    } catch (err) {
      return console.error(err);
    }
    var values = [
      user.username,
      user.password,
      user.deviceToken,
      // user.servicesPosted,
      // user.servicesUsed
    ];
    
    // Insert service into database
    var query = `UPDATE users SET username = ?, password = ?, deviceToken = ? WHERE userID = ` + userID;
  
    dbConn.query(query, values, (err, results, fields) => {
      if (err) {
        return console.error(err.message);
      }
  
      console.log("Updated");

      callback(results);
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